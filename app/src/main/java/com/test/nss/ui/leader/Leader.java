package com.test.nss.ui.leader;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.test.nss.DataBaseHelper;
import com.test.nss.DatabaseAdapter;
import com.test.nss.R;
import com.test.nss.api.RetrofitClient;
import com.test.nss.ediary;
import com.test.nss.ui.BottomDialogFragment;
import com.test.nss.ui.onClickInterface2;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static com.test.nss.Helper.AUTH_TOKEN;
import static com.test.nss.Helper.color1;
import static com.test.nss.Helper.color3;
import static com.test.nss.Helper.color5;
import static com.test.nss.Helper.isNight;
import static com.test.nss.Helper.primaryCol;
import static com.test.nss.Helper.white;

public class Leader extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    MyListAdapterLeader leaderActDataAdapter, leaderActAllDataAdapter;
    TextView toolbarTitle;

    List<AdapterDataLeader> dataLeaderList, dataLeaderListAll;
    onClickInterface2 modVol, viewVol;
    View root;
    RecyclerView recViewLeader, recViewLeaderAll;
    CardView leader, leaderAll;
    SwipeRefreshLayout swipeRefresh;

    TextView isEmpty;

    public Leader() {
    }

    public static Leader newInstance() {
        return new Leader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_leader, container, false);
        dataLeaderList = addActData();
        dataLeaderListAll = addActAllData();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbarTitle = requireActivity().findViewById(R.id.titleTool);
        toolbarTitle.setText(getString(R.string.menu_leader));
        isEmpty = root.findViewById(R.id.isEmpty);

        leader = root.findViewById(R.id.volAct);
        leaderAll = root.findViewById(R.id.volActAll);

        recViewLeader = root.findViewById(R.id.vecLeaderList);
        recViewLeaderAll = root.findViewById(R.id.vecLeaderListAll);

        swipeRefresh = root.findViewById(R.id.swipeRefresh);

        swipeRefresh.setColorSchemeColors(color1, color5, color3);
        swipeRefresh.setProgressBackgroundColorSchemeColor(white);

        if (dataLeaderList.isEmpty())
            isEmpty.setText(R.string.not_pend_desc);
        //swipeRefresh.setEnabled(false);
        swipeRefresh.setOnRefreshListener(this);

        leader.setOnClickListener(view1 -> {
            if (dataLeaderList.isEmpty()) {
                isEmpty.setVisibility(View.VISIBLE);
                isEmpty.setText(R.string.not_pend_desc);
            } else
                isEmpty.setVisibility(View.GONE);
            recViewLeaderAll.setVisibility(View.GONE);
            recViewLeader.setVisibility(View.VISIBLE);
        });

        if (isNight) {
            TextPaint paint = isEmpty.getPaint();
            float width = paint.measureText(getString(R.string.not_pend_desc));
            Shader textShader = new LinearGradient(0, 0, width, isEmpty.getTextSize(),
                    new int[]{
                            Color.parseColor("#00FFEA"),
                            Color.parseColor("#3882FF"),
                            Color.parseColor("#4B9EF6"),
                    }, null, Shader.TileMode.CLAMP);
            isEmpty.getPaint().setShader(textShader);
        } else
            isEmpty.setTextColor(primaryCol);

        leaderAll.setOnClickListener(view1 -> {
            isEmpty.setVisibility(View.GONE);
            recViewLeaderAll.setVisibility(View.VISIBLE);
            recViewLeader.setVisibility(View.GONE);
        });

        BottomDialogFragment commonSheet = new BottomDialogFragment();
        commonSheet.setStyle(BottomDialogFragment.STYLE_NORMAL, R.style.BottomSheetStyleTheme);

        modVol = abc -> {
            if (!swipeRefresh.isRefreshing()) {
                if (!checkApproved(dataLeaderList.get(abc).getvolVec())) {

                    leaderAll.setVisibility(View.GONE);
                    leader.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), "" + dataLeaderList.get(abc).getVolName(), Toast.LENGTH_SHORT).show();
                    ModifyVolunteer modifyVolunteer = ModifyVolunteer.newInstance();
                    Bundle args = new Bundle();
                    args.putString("thisVec", dataLeaderList.get(abc).getvolVec());
                    modifyVolunteer.setArguments(args);
                    FragmentManager fragmentManager = getChildFragmentManager();
                    Fragment simpleFragment = fragmentManager.findFragmentById(R.id.detailsModify);

                    //Log.e("Leader", "onViewCreated: " + fragmentManager + fragmentManager.getBackStackEntryCount());
                    int c = fragmentManager.getBackStackEntryCount();
                    if (simpleFragment instanceof ModifyVolunteer)
                        while (c >= 0) {
                            fragmentManager.popBackStack();
                            c--;
                        }
                    fragmentManager.beginTransaction().replace(R.id.detailsModify, modifyVolunteer, "ModifyVolunteer").addToBackStack(null).commit();
                } else
                    Toast.makeText(requireContext(), "It is already modified by leader, please refresh", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(requireContext(), "Refreshing", Toast.LENGTH_SHORT).show();
        };

        viewVol = abc1 -> {
            Call<ResponseBody> getVecDet = RetrofitClient.getInstance().getApi().volActVec("Token " + AUTH_TOKEN, dataLeaderListAll.get(abc1).getvolVec());
            getVecDet.enqueue(new Callback<ResponseBody>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            JSONArray j = new JSONArray(response.body().string());

                            Log.e("VolVecActAll", j.length() + "");
                            if (j.length() > 0) {
                                DatabaseAdapter mDbHelper = new DatabaseAdapter(requireContext());
                                mDbHelper.createDatabase();
                                mDbHelper.open();
                                deleteData("VolVecActAll");

                                for (int i = 0; i < j.length(); i++) {
                                    mDbHelper.insertVolVecAllAct(
                                            j.getJSONObject(i).getInt("id"),
                                            "",
                                            j.getJSONObject(i).getString("Date"),
                                            j.getJSONObject(i).getInt("Hours"),
                                            j.getJSONObject(i).getString("VEC"),
                                            j.getJSONObject(i).getString("ActivityName"),
                                            j.getJSONObject(i).getString("AssignedActivityName"),
                                            j.getJSONObject(i).getString("State")
                                    );
                                }
                                mDbHelper.setApproved("VolVecActAll");
                                mDbHelper.close();
                            }
                        } catch (JSONException | IOException e) {
                            Log.e("Failed", e.toString());
                            e.printStackTrace();
                        }

                        if (!swipeRefresh.isRefreshing()) {
                            Bundle args = new Bundle();
                            args.putString("thisVec2", dataLeaderListAll.get(abc1).getvolVec());
                            commonSheet.fragment = new ViewVolunteer();
                            commonSheet.setArguments(args);
                            commonSheet.show(getChildFragmentManager(), "view");

                            //fragmentManager.beginTransaction().replace(R.id.detailsModify, viewVolunteer, "ViewVolunteer").addToBackStack(null).commit();

                            /*Log.e("AAA", abc1 + "");
                            Toast.makeText(requireContext(), dataLeaderListAll.get(abc1).getVolName(), Toast.LENGTH_SHORT).show();
                            leaderAll.setVisibility(View.GONE);
                            leader.setVisibility(View.GONE);
                            ViewVolunteer viewVolunteer = ViewVolunteer.newInstance();
                            Bundle args = new Bundle();
                            args.putString("thisVec2", dataLeaderListAll.get(abc1).getvolVec());
                            viewVolunteer.setArguments(args);
                            recViewLeaderAll.setVisibility(View.GONE);
                            FragmentManager fragmentManager = getChildFragmentManager();
                            Fragment simpleFragment = fragmentManager.findFragmentById(R.id.detailsModify);

                            //Log.e("Leader", "onViewCreated: " + fragmentManager + fragmentManager.getBackStackEntryCount());

                            int c = fragmentManager.getBackStackEntryCount();
                            if (simpleFragment instanceof ViewVolunteer) {
                                while (c >= 0) {
                                    fragmentManager.popBackStack();
                                    c--;
                                }
                            }
                            fragmentManager.beginTransaction().replace(R.id.detailsModify, viewVolunteer, "ViewVolunteer").addToBackStack(null).commit();*/
                        } else
                            Toast.makeText(requireContext(), "Refreshing", Toast.LENGTH_SHORT).show();
                        //Log.e("here", "onViewCreated: " + abc1);
                    } else if (response.errorBody() != null) {
                        try {
                            Log.e("Here", "onResponse: " + response.errorBody().string());
                            Toast.makeText(getContext(), "Error in adding activity", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @EverythingIsNonNull
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(requireContext(), "Failed to add activity, device offline", Toast.LENGTH_SHORT).show();
                }
            });
        };

        leaderActDataAdapter = new MyListAdapterLeader(dataLeaderList, requireContext(), modVol);
        leaderActAllDataAdapter = new MyListAdapterLeader(dataLeaderListAll, requireContext(), viewVol);

        recViewLeader.setLayoutManager(new LinearLayoutManager(requireContext()));
        recViewLeader.setAdapter(leaderActDataAdapter);

        recViewLeaderAll.setLayoutManager(new LinearLayoutManager(requireContext()));
        recViewLeaderAll.setAdapter(leaderActAllDataAdapter);

        //Log.e("Here", "onViewCreated: "+dataLeaderListAll.get(0).getvolVec() + dataLeaderListAll.get(0).getVolName() + dataLeaderListAll.get(0).getvolVec());
    }

    public List<AdapterDataLeader> addActData() {
        ArrayList<AdapterDataLeader> data3 = new ArrayList<>();

        DatabaseAdapter mDbHelper = new DatabaseAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        Cursor c3 = mDbHelper.getVec();

        while (c3.moveToNext()) {
            Cursor c = mDbHelper.getVol(c3.getString(c3.getColumnIndex("VEC")));
            c.moveToFirst();
            data3.add(new AdapterDataLeader(
                    c3.getString(c3.getColumnIndex("VEC")),
                    c.getString(c.getColumnIndex("First_name"))
            ));
        }
        mDbHelper.close();
        return data3;
    }

    public List<AdapterDataLeader> addActAllData() {
        ArrayList<AdapterDataLeader> data3 = new ArrayList<>();

        DatabaseAdapter mDbHelper = new DatabaseAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        Cursor c3 = mDbHelper.getVecAll();

        while (c3.moveToNext()) {
            Cursor c = mDbHelper.getVolAll(c3.getString(c3.getColumnIndex("VEC")));
            c.moveToFirst();
            data3.add(new AdapterDataLeader(
                    c3.getString(c3.getColumnIndex("VEC")),
                    c.getString(c.getColumnIndex("First_name"))
            ));
        }
        mDbHelper.close();
        return data3;
    }

    private void deleteData(String table) {
        DatabaseAdapter mDbHelper2 = new DatabaseAdapter(requireContext());
        mDbHelper2.createDatabase();
        mDbHelper2.open();
        DataBaseHelper mDb2 = new DataBaseHelper(requireContext());
        SQLiteDatabase m = mDb2.getWritableDatabase();
        m.execSQL("DELETE FROM " + table);
        mDbHelper2.close();
        m.close();
        mDb2.close();
    }

    @Override
    public void onRefresh() {
        if (swipeRefresh.isRefreshing()) {
            Toast.makeText(requireContext(), "Refreshing", Toast.LENGTH_SHORT).show();
            ((ediary) requireActivity()).leaderRef();
            leaderActDataAdapter.isShimmer = true;
            leaderActDataAdapter.notifyDataSetChanged();
            if (!dataLeaderList.isEmpty())
                swipeRefresh.setEnabled(false);
            new Handler().postDelayed(() -> {
                leaderActDataAdapter.isShimmer = false;
                leaderActDataAdapter.notifyDataSetChanged();
                FragmentTransaction tr = getParentFragmentManager().beginTransaction();
                tr.replace(R.id.nav_host_fragment, Leader.newInstance());
                tr.commit();
            }, 3500);
        }
    }

    public boolean checkApproved(String vec) {
        boolean isApp;
        DatabaseAdapter mDbHelper = new DatabaseAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        isApp = mDbHelper.isApproved(vec);
        mDbHelper.close();
        return isApp;
    }
}