package com.test.nss.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.test.nss.DatabaseAdapter;
import com.test.nss.Password;
import com.test.nss.R;
import com.test.nss.api.RetrofitClient;
import com.test.nss.ui.onClickInterface2;

import org.apache.commons.collections4.ListUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static com.test.nss.Helper.AUTH_TOKEN;
import static com.test.nss.Helper.VEC;
import static com.test.nss.Helper.blackishy;
import static com.test.nss.Helper.green;
import static com.test.nss.Helper.isLeader;
import static com.test.nss.Helper.isSec;
import static com.test.nss.Helper.kesar;
import static com.test.nss.Helper.primaryColDark;
import static com.test.nss.Helper.red;
import static com.test.nss.Helper.sbColorText;
import static com.test.nss.Helper.transparent;

public class syAct extends Fragment {

    private static final String TAG = "syAct";
    View root;
    Button univ;
    Button area;
    Button clg;
    //LinearLayout mainSy;
    Context mContext;
    RecyclerView univRecSy;
    RecyclerView areaRecSy;
    RecyclerView clgRecSy;
    CardView cardViewMain;
    List<AdapterDataMain> univListDataSy;
    List<AdapterDataMain> areaDataMainSy;
    List<AdapterDataMain> clgListDataSy;
    FloatingActionButton add;
    int whichAct;
    int act;
    int newHours = 0;
    LinearLayout actHeaderInput;
    LinearLayout fragSy;
    onClickInterface2 onClickInterface;
    LottieAnimationView lottieAnimationView;

    public syAct() {
    }

    public static syAct newInstance() {
        return new syAct();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_sy_act, container, false);
        mContext = requireContext();

        cardViewMain = root.findViewById(R.id.details_main_card2);
        actHeaderInput = requireActivity().findViewById(R.id.actHeaderInput);
        actHeaderInput.setVisibility(View.GONE);

        univ = root.findViewById(R.id.main_univ_sy);
        area = root.findViewById(R.id.main_area_sy);
        clg = root.findViewById(R.id.main_clg_sy);

        //mainSy = root.findViewById(R.id.main_sy);
        add = root.findViewById(R.id.add2);
        lottieAnimationView = root.findViewById(R.id.mainLottie);

        univRecSy = root.findViewById(R.id.univRecSy);
        areaRecSy = root.findViewById(R.id.areaRecSy);
        clgRecSy = root.findViewById(R.id.hoursRecSy);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragSy = root.findViewById(R.id.frag_sy);

        univ.setOnClickListener(v -> {
            Snackbar.make(add, "Swipe left on activity to modify", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

            whichAct = 23;
            univRecSy.setVisibility(View.VISIBLE);
            areaRecSy.setVisibility(View.GONE);
            clgRecSy.setVisibility(View.GONE);
            add.setVisibility(View.VISIBLE);

            univ.setTextColor(primaryColDark);
            area.setTextColor(blackishy);
            clg.setTextColor(blackishy);
        });

        area.setOnClickListener(v -> {
            whichAct = 22;
            //mainSy.setVisibility(View.VISIBLE);
            add.setVisibility(View.VISIBLE);

            univRecSy.setVisibility(View.GONE);
            areaRecSy.setVisibility(View.VISIBLE);
            clgRecSy.setVisibility(View.GONE);

            area.setTextColor(primaryColDark);
            univ.setTextColor(blackishy);
            clg.setTextColor(blackishy);
        });

        clg.setOnClickListener(v -> {
            whichAct = 21;
            add.setVisibility(View.VISIBLE);
            univRecSy.setVisibility(View.GONE);
            areaRecSy.setVisibility(View.GONE);
            clgRecSy.setVisibility(View.VISIBLE);

            clg.setTextColor(primaryColDark);
            univ.setTextColor(blackishy);
            area.setTextColor(blackishy);
        });

        clgListDataSy = addAct("Second Year College");
        areaDataMainSy = ListUtils.union(addAct("Second Year Area Based One"), addAct("Second Year Area Based Two"));

        univListDataSy = addAct("Second Year University");

        // UNIV
        onClickInterface = abc -> {
            TextView t;
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext, R.style.BottomSheetStyleTheme);
            View bottomSheetView = LayoutInflater.from(mContext).inflate(R.layout.bottom_sheet, root.findViewById(R.id.bottomSheetContainer));
            bottomSheetDialog.setContentView(bottomSheetView);
            t = bottomSheetView.findViewById(R.id.actTitle);
            t.setText(univListDataSy.get(abc).getAct());
            t = bottomSheetView.findViewById(R.id.actDesc);
            t.setText(univListDataSy.get(abc).getDesc());
            t = bottomSheetView.findViewById(R.id.actHours);
            t.setText(String.format("Worked for %sh on ", univListDataSy.get(abc).getHours()));
            t = bottomSheetView.findViewById(R.id.actDate);
            t.setText(univListDataSy.get(abc).getDate());
            t = bottomSheetView.findViewById(R.id.actState);

            switch (univListDataSy.get(abc).getState()) {
                case "LeaderDelete":
                case "PoDelete":
                    t.setTextColor(red);
                    break;
                case "LeaderModified":
                case "PoModified":
                    t.setTextColor(kesar);
                    break;
                case "Approved":
                    t.setTextColor(green);
                    break;
                case "Submitted":
                    t.setTextColor(primaryColDark);
                default:
                    break;
            }
            t.setText(univListDataSy.get(abc).getState());

            bottomSheetDialog.show();
        };
        RecyclerView recyclerViewUniv = root.findViewById(R.id.univRecSy);
        MyListAdapter adapterUniv = new MyListAdapter(univListDataSy, mContext, onClickInterface);

        ItemTouchHelper.SimpleCallback simpleCallbackUniv = new ItemTouchHelper.SimpleCallback
                (0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int p;
                if (viewHolder.getAdapterPosition() == -1)
                    p = viewHolder.getAdapterPosition() + 1;

                else if (viewHolder.getAdapterPosition() == adapterUniv.list.size())
                    p = viewHolder.getAdapterPosition() - 1;
                else
                    p = viewHolder.getAdapterPosition();

                if (isSec && adapterUniv.list.get(p).getState().equals("Modified") ||
                        isSec && adapterUniv.list.get(p).getState().equals("Submitted")) {

                    if (direction == ItemTouchHelper.RIGHT) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext, R.style.delDialog);
                        builder2.setMessage("Are you sure you want to delete?");
                        builder2.setCancelable(false);
                        builder2.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        adapterUniv.notifyItemChanged(p);
                                    }
                                }
                        );

                        builder2.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                int actID = Integer.parseInt(adapterUniv.list.get(p).getId());
                                String actName = adapterUniv.list.get(p).getAct();

                                DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                                mdb.createDatabase();
                                mdb.open();

                                Cursor c2 = mdb.getActAssigActNameAdmin(actName);
                                c2.moveToFirst();

                                Call<ResponseBody> putHours = RetrofitClient.getInstance().getApi().putHour(
                                        "Token " + AUTH_TOKEN,
                                        Integer.parseInt(adapterUniv.list.get(p).getHours()),
                                        VEC,
                                        Integer.parseInt(c2.getString(c2.getColumnIndex("activityType"))),
                                        Integer.parseInt(c2.getString(c2.getColumnIndex("id"))),
                                        4,
                                        Password.PASS,
                                        actID
                                );

                                mdb.dropDetails(actID);
                                adapterUniv.list.remove(p);
                                adapterUniv.notifyItemRemoved(p);

                                putHours.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    @EverythingIsNonNull
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    }

                                    @Override
                                    @EverythingIsNonNull
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                                mdb.close();
                            }
                        });
                        builder2.show();
                    } else if (direction == ItemTouchHelper.LEFT) {
                        int actID = Integer.parseInt(adapterUniv.list.get(p).getId());
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.inputDialog);
                        View viewInflated = LayoutInflater.from(mContext).inflate(R.layout.hours_input_layout, (ViewGroup) view, false);

                        EditText input = viewInflated.findViewById(R.id.input);
                        builder.setView(viewInflated);

                        builder.setCancelable(false);
                        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                    dialog.cancel();
                                    adapterUniv.notifyItemChanged(p);
                                }
                        );

                        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            dialog.dismiss();

                            if (!input.getText().toString().trim().equals("")) {
                                try {
                                    newHours = Integer.parseInt(input.getText().toString());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    Toast.makeText(mContext, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    newHours = 0;
                                }

                                String actName = adapterUniv.list.get(p).getAct();

                                DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                                mdb.createDatabase();
                                mdb.open();
                                Cursor m = mdb.getActAssigActNameAdmin(actName);
                                m.moveToFirst();
                                int maxH = m.getInt(m.getColumnIndex("HoursAssigned"));
                                mdb.close();

                                if (newHours >= 1 && newHours <= maxH) {
                                    mdb = new DatabaseAdapter(mContext);
                                    mdb.createDatabase();
                                    mdb.open();
                                    mdb.setDetails(newHours, "Modified", actID);

                                    Cursor c = mdb.getActAssigActNameAdmin(actName);
                                    c.moveToFirst();

                                    adapterUniv.list.add(p, new AdapterDataMain(
                                                    adapterUniv.list.get(p).getDate(),
                                                    adapterUniv.list.get(p).getAct(),
                                                    String.valueOf(newHours),
                                                    adapterUniv.list.get(p).getId(),
                                                    adapterUniv.list.get(p).isApproved(),
                                                    "Modified",
                                                    adapterUniv.list.get(p).getDesc()
                                            )
                                    );

                                    adapterUniv.notifyDataSetChanged();
                                    adapterUniv.list.remove(p + 1);
                                    adapterUniv.notifyItemInserted(p);

                                    //TODO: Offline mode needs to be checked
                                    //mdb.setSyncActDetails(0, actID);

                                    Call<ResponseBody> putHours = RetrofitClient.getInstance().getApi().putHour(
                                            "Token " + AUTH_TOKEN,
                                            newHours,
                                            VEC,
                                            Integer.parseInt(c.getString(c.getColumnIndex("activityType"))),
                                            Integer.parseInt(c.getString(c.getColumnIndex("id"))),
                                            3,
                                            Password.PASS,

                                            actID
                                    );
                                    c.close();
                                    mdb.close();
                                    putHours.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        @EverythingIsNonNull
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful())
                                                Snackbar.make(view, "Edited to " + newHours + "hours", Snackbar.LENGTH_SHORT);
                                            else if (response.errorBody() != null) {
                                                try {
                                                    Toast.makeText(requireContext(), "onResponse: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @Override
                                        @EverythingIsNonNull
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Log.e("Error:onFailure", t.toString());
                                        }
                                    });
                                } else {
                                    Toast.makeText(requireContext(), "Enter upto: " + maxH + "hour", Toast.LENGTH_SHORT).show();
                                    adapterUniv.notifyItemChanged(p);
                                }
                            } else
                                adapterUniv.notifyItemChanged(p);
                        });

                        if (viewInflated.getParent() != null)
                            ((ViewGroup) viewInflated.getParent()).removeView(viewInflated);
                        builder.show();

                    }
                } else if (adapterUniv.list.get(p).getState().equals("Approved")
                        || adapterUniv.list.get(p).getState().equals("LeaderModified") || adapterUniv.list.get(p).getState().equals("LeaderDelete")) {
                    if (isLeader == 1) {
                        String x = "";
                        if (adapterUniv.list.get(p).getState().equals("Approved"))
                            x = "Approved By: ";
                        else if (adapterUniv.list.get(p).getState().equals("LeaderDelete"))
                            x = "Deleted By: ";
                        else
                            x = "Modified By: ";

                        DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                        mdb.createDatabase();
                        mdb.open();
                        Cursor c = mdb.getActLeaderId(Integer.parseInt(adapterUniv.list.get(p).getId()));
                        c.moveToFirst();
                        int leadId = c.getInt(c.getColumnIndex("Approved_by"));

                        x = mdb.getLeaderName(leadId) != null ? x + mdb.getLeaderName(leadId) : x + "PO";
                        Snackbar sb = Snackbar.make(add, x, Snackbar.LENGTH_LONG)
                                .setTextColor(sbColorText);
                        //sb.getView().setBackgroundColor(transparent);
                        mdb.close();
                        sb.show();
                        adapterUniv.notifyItemChanged(p);
                    } else {
                        String x;
                        DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                        mdb.createDatabase();
                        mdb.open();
                        Cursor c = mdb.getActLeaderId(Integer.parseInt(adapterUniv.list.get(p).getId()));
                        c.moveToFirst();

                        if (c.getString(c.getColumnIndex("Approved_by")).equals("null")) {
                            Snackbar sb = Snackbar.make(add, "Approved By: PO", Snackbar.LENGTH_LONG)
                                    .setTextColor(sbColorText);
                            //sb.getView().setBackgroundColor(transparent);
                            sb.show();
                        } else {
                            if (adapterUniv.list.get(p).getState().equals("Approved"))
                                x = "Approved By: ";
                            else if (adapterUniv.list.get(p).getState().equals("LeaderDelete"))
                                x = "Deleted By: ";
                            else
                                x = "Modified By: ";

                            int leadId = c.getInt(c.getColumnIndex("Approved_by"));
                            x = mdb.getLeaderName(leadId) != null ? x + mdb.getLeaderName(leadId) : x + "PO";
                            Snackbar sb = Snackbar.make(add, x, Snackbar.LENGTH_LONG)
                                    .setTextColor(sbColorText);
                            //sb.getView().setBackgroundColor(transparent);
                            sb.show();
                        }
                        mdb.close();
                        adapterUniv.notifyItemChanged(p);
                    }
                }
                adapterUniv.notifyDataSetChanged();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                int p;
                if (viewHolder.getAdapterPosition() == -1)
                    p = viewHolder.getAdapterPosition() + 1;
                else if (viewHolder.getAdapterPosition() == adapterUniv.list.size())
                    p = viewHolder.getAdapterPosition() - 1;
                else
                    p = viewHolder.getAdapterPosition();

                Log.e("AAA", "" + p);
                if (isSec && adapterUniv.list.get(p).getState().equals("Modified") ||
                        isSec && adapterUniv.list.get(p).getState().equals("Submitted")) {
                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addSwipeRightActionIcon(R.drawable.ic_del_24)
                            .addSwipeLeftActionIcon(R.drawable.ic_edit_24)
                            .addBackgroundColor(transparent)
                            .create()
                            .decorate();
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                } else if (adapterUniv.list.get(p).getState().equals("Approved") || adapterUniv.list.get(p).getState().equals("LeaderModified") || adapterUniv.list.get(p).getState().equals("LeaderDelete")) {
                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addSwipeRightActionIcon(R.drawable.ic_eye_24)
                            .addSwipeLeftActionIcon(R.drawable.ic_eye_24)
                            .addBackgroundColor(transparent)
                            .create()
                            .decorate();
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };

        recyclerViewUniv.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewUniv.setAdapter(adapterUniv);

        // Recycler View Area
        onClickInterface = abc -> {
            TextView t;
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext, R.style.BottomSheetStyleTheme);
            View bottomSheetView = LayoutInflater.from(mContext).inflate(R.layout.bottom_sheet, root.findViewById(R.id.bottomSheetContainer));
            bottomSheetDialog.setContentView(bottomSheetView);
            t = bottomSheetView.findViewById(R.id.actTitle);
            t.setText(areaDataMainSy.get(abc).getAct());
            t = bottomSheetView.findViewById(R.id.actDesc);
            t.setText(areaDataMainSy.get(abc).getDesc());
            t = bottomSheetView.findViewById(R.id.actHours);
            t.setText(String.format("Worked for %sh on ", areaDataMainSy.get(abc).getHours()));
            t = bottomSheetView.findViewById(R.id.actDate);
            t.setText(areaDataMainSy.get(abc).getDate());
            t = bottomSheetView.findViewById(R.id.actState);

            switch (areaDataMainSy.get(abc).getState()) {
                case "LeaderDelete":
                case "PoDelete":
                    t.setTextColor(red);
                    break;
                case "LeaderModified":
                case "PoModified":
                    t.setTextColor(kesar);
                    break;
                case "Approved":
                    t.setTextColor(green);
                    break;
                case "Submitted":
                    t.setTextColor(primaryColDark);
                default:
                    break;
            }
            t.setText(areaDataMainSy.get(abc).getState());

            bottomSheetDialog.show();
        };

        RecyclerView recyclerViewArea = root.findViewById(R.id.areaRecSy);
        MyListAdapter adapterArea = new MyListAdapter(areaDataMainSy, mContext, onClickInterface);
        //adapterArea.notifyDataSetChanged();

        ItemTouchHelper.SimpleCallback simpleCallbackArea = new ItemTouchHelper.SimpleCallback
                (0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int p;
                if (viewHolder.getAdapterPosition() == -1)
                    p = viewHolder.getAdapterPosition() + 1;

                else if (viewHolder.getAdapterPosition() == adapterArea.list.size())
                    p = viewHolder.getAdapterPosition() - 1;
                else
                    p = viewHolder.getAdapterPosition();

                if (isSec && adapterArea.list.get(p).getState().equals("Modified") ||
                        isSec && adapterArea.list.get(p).getState().equals("Submitted")) {

                    if (direction == ItemTouchHelper.RIGHT) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext, R.style.delDialog);
                        builder2.setMessage("Are you sure you want to delete?");
                        builder2.setCancelable(false);
                        builder2.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        adapterArea.notifyItemChanged(p);
                                    }
                                }
                        );

                        builder2.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                int actID = Integer.parseInt(adapterArea.list.get(p).getId());
                                String actName = adapterArea.list.get(p).getAct();

                                DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                                mdb.createDatabase();
                                mdb.open();

                                Cursor c2 = mdb.getActAssigActNameAdmin(actName);

                                c2.moveToFirst();

                                Call<ResponseBody> putHours = RetrofitClient.getInstance().getApi().putHour(
                                        "Token " + AUTH_TOKEN,
                                        Integer.parseInt(adapterArea.list.get(p).getHours()),
                                        VEC,
                                        Integer.parseInt(c2.getString(c2.getColumnIndex("activityType"))),
                                        Integer.parseInt(c2.getString(c2.getColumnIndex("id"))),
                                        4,
                                        Password.PASS,
                                        actID
                                );

                                mdb.dropDetails(actID);
                                adapterArea.list.remove(p);
                                adapterArea.notifyItemRemoved(p);

                                putHours.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    @EverythingIsNonNull
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    }

                                    @Override
                                    @EverythingIsNonNull
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                                mdb.close();
                            }
                        });
                        builder2.show();
                    } else if (direction == ItemTouchHelper.LEFT) {
                        int actID = Integer.parseInt(adapterArea.list.get(p).getId());
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.inputDialog);
                        View viewInflated = LayoutInflater.from(mContext).inflate(R.layout.hours_input_layout, (ViewGroup) view, false);

                        EditText input = viewInflated.findViewById(R.id.input);
                        builder.setView(viewInflated);

                        builder.setCancelable(false);
                        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                    dialog.cancel();
                                    adapterArea.notifyItemChanged(p);
                                }
                        );

                        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            dialog.dismiss();

                            //Log.e("Yes this", adapterArea.list.get(p).getAct());
                            if (!input.getText().toString().trim().equals("")) {
                                try {
                                    newHours = Integer.parseInt(input.getText().toString());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    Toast.makeText(mContext, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    newHours = 0;
                                }

                                String actName = adapterArea.list.get(p).getAct();

                                DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                                mdb.createDatabase();
                                mdb.open();
                                Cursor m = mdb.getActAssigActNameAdmin(actName);
                                m.moveToFirst();
                                int maxH = m.getInt(m.getColumnIndex("HoursAssigned"));
                                mdb.close();

                                if (newHours >= 1 && newHours <= maxH) {

                                    mdb = new DatabaseAdapter(mContext);
                                    mdb.createDatabase();
                                    mdb.open();

                                    Cursor c = mdb.getActAssigActNameAdmin(actName);
                                    c.moveToFirst();

                                    if (c.getCount() > 0) {
                                        mdb.setDetails(newHours, "Modified", actID);
                                        adapterArea.list.add(p, new AdapterDataMain(
                                                        adapterArea.list.get(p).getDate(),
                                                        adapterArea.list.get(p).getAct(),
                                                        String.valueOf(newHours),
                                                        adapterArea.list.get(p).getId(),
                                                        adapterArea.list.get(p).isApproved(),
                                                        "Modified",
                                                        adapterArea.list.get(p).getDesc()
                                                )
                                        );

                                        adapterArea.notifyDataSetChanged();
                                        adapterArea.list.remove(p + 1);
                                        adapterArea.notifyItemInserted(p);

                                        //TODO: Offline mode needs to be checked
                                        //mdb.setSyncActDetails(0, actID);

                                        Call<ResponseBody> putHours = RetrofitClient.getInstance().getApi().putHour(
                                                "Token " + AUTH_TOKEN,
                                                newHours,
                                                VEC,
                                                Integer.parseInt(c.getString(c.getColumnIndex("activityType"))),
                                                Integer.parseInt(c.getString(c.getColumnIndex("id"))),
                                                3,
                                                Password.PASS,

                                                actID
                                        );
                                        c.close();
                                        mdb.close();
                                        putHours.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            @EverythingIsNonNull
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.isSuccessful())
                                                    Snackbar.make(view, "Edited to " + newHours + "hours", Snackbar.LENGTH_SHORT);
                                                else if (response.errorBody() != null) {
                                                    try {
                                                        Toast.makeText(requireContext(), "onResponse: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }

                                            @Override
                                            @EverythingIsNonNull
                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                Log.e("Error:onFailure", t.toString());
                                            }
                                        });
                                    } else
                                        Toast.makeText(mContext, "No activity found", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(requireContext(), "Enter upto: " + maxH + "hour", Toast.LENGTH_SHORT).show();
                                    adapterArea.notifyItemChanged(p);
                                }
                            } else
                                adapterArea.notifyItemChanged(p);
                        });

                        if (viewInflated.getParent() != null)
                            ((ViewGroup) viewInflated.getParent()).removeView(viewInflated);
                        builder.show();

                    }
                } else if (adapterArea.list.get(p).getState().equals("Approved")
                        || adapterArea.list.get(p).getState().equals("LeaderModified")) {
                    if (isLeader == 1) {
                        String x = "";
                        if (adapterArea.list.get(p).getState().equals("Approved"))
                            x = "Approved By: ";
                        else if (adapterArea.list.get(p).getState().equals("LeaderDelete"))
                            x = "Deleted By: ";
                        else
                            x = "Modified By: ";


                        DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                        mdb.createDatabase();
                        mdb.open();
                        Cursor c = mdb.getActLeaderId(Integer.parseInt(adapterArea.list.get(p).getId()));
                        c.moveToFirst();
                        int leadId = c.getInt(c.getColumnIndex("Approved_by"));

                        x = mdb.getLeaderName(leadId) != null ? x + mdb.getLeaderName(leadId) : x + "PO";
                        Snackbar sb = Snackbar.make(add, x, Snackbar.LENGTH_LONG)
                                .setTextColor(sbColorText);
                        //sb.getView().setBackgroundColor(transparent);
                        mdb.close();
                        sb.show();
                        adapterArea.notifyItemChanged(p);
                    } else {
                        String x;
                        DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                        mdb.createDatabase();
                        mdb.open();
                        Cursor c = mdb.getActLeaderId(Integer.parseInt(adapterArea.list.get(p).getId()));
                        c.moveToFirst();

                        if (c.getString(c.getColumnIndex("Approved_by")).equals("null")) {
                            Snackbar sb = Snackbar.make(add, "Approved By: PO", Snackbar.LENGTH_LONG)
                                    .setTextColor(sbColorText);
                            //sb.getView().setBackgroundColor(transparent);
                            sb.show();
                        } else {
                            if (adapterArea.list.get(p).getState().equals("Approved"))
                                x = "Approved By: ";
                            else if (adapterArea.list.get(p).getState().equals("LeaderDelete"))
                                x = "Deleted By: ";
                            else
                                x = "Modified By: ";

                            int leadId = c.getInt(c.getColumnIndex("Approved_by"));
                            x = mdb.getLeaderName(leadId) != null ? x + mdb.getLeaderName(leadId) : x + "PO";
                            Snackbar sb = Snackbar.make(add, x, Snackbar.LENGTH_LONG)
                                    .setTextColor(sbColorText);
                            //sb.getView().setBackgroundColor(transparent);
                            sb.show();

                        }
                        adapterArea.notifyItemChanged(p);
                        mdb.close();
                    }
                }

                adapterArea.notifyDataSetChanged();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                int p;
                if (viewHolder.getAdapterPosition() == -1)
                    p = viewHolder.getAdapterPosition() + 1;
                else if (viewHolder.getAdapterPosition() == adapterArea.list.size())
                    p = viewHolder.getAdapterPosition() - 1;
                else
                    p = viewHolder.getAdapterPosition();
                if (isSec && adapterArea.list.get(p).getState().equals("Modified") ||
                        isSec && adapterArea.list.get(p).getState().equals("Submitted")) {
                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addSwipeRightActionIcon(R.drawable.ic_del_24)
                            .addSwipeLeftActionIcon(R.drawable.ic_edit_24)
                            .addBackgroundColor(transparent)
                            .create()
                            .decorate();
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                } else if (adapterArea.list.get(p).getState().equals("Approved") || adapterArea.list.get(p).getState().equals("LeaderModified") || adapterArea.list.get(p).getState().equals("LeaderDelete")) {
                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addSwipeRightActionIcon(R.drawable.ic_eye_24)
                            .addSwipeLeftActionIcon(R.drawable.ic_eye_24)
                            .addBackgroundColor(transparent)
                            .create()
                            .decorate();
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };

        recyclerViewArea.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewArea.setAdapter(adapterArea);

        // Recycler View College
        onClickInterface = abc -> {
            TextView t;
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext, R.style.BottomSheetStyleTheme);
            View bottomSheetView = LayoutInflater.from(mContext).inflate(R.layout.bottom_sheet, root.findViewById(R.id.bottomSheetContainer));
            bottomSheetDialog.setContentView(bottomSheetView);
            t = bottomSheetView.findViewById(R.id.actTitle);
            t.setText(clgListDataSy.get(abc).getAct());
            t = bottomSheetView.findViewById(R.id.actDesc);
            t.setText(clgListDataSy.get(abc).getDesc());
            t = bottomSheetView.findViewById(R.id.actHours);
            t.setText(String.format("Worked for %sh on ", clgListDataSy.get(abc).getHours()));
            t = bottomSheetView.findViewById(R.id.actDate);
            t.setText(clgListDataSy.get(abc).getDate());
            t = bottomSheetView.findViewById(R.id.actState);

            switch (clgListDataSy.get(abc).getState()) {
                case "LeaderDelete":
                case "PoDelete":
                    t.setTextColor(red);
                    break;
                case "LeaderModified":
                case "PoModified":
                    t.setTextColor(kesar);
                    break;
                case "Approved":
                    t.setTextColor(green);
                    break;
                case "Submitted":
                    t.setTextColor(primaryColDark);
                default:
                    break;
            }
            t.setText(clgListDataSy.get(abc).getState());

            bottomSheetDialog.show();
        };

        RecyclerView recyclerViewHours = root.findViewById(R.id.hoursRecSy);
        MyListAdapter adapterClg = new MyListAdapter(clgListDataSy, mContext, onClickInterface);
        //adapterClg.notifyDataSetChanged();

        ItemTouchHelper.SimpleCallback simpleCallbackHours = new ItemTouchHelper.SimpleCallback
                (0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int p;
                if (viewHolder.getAdapterPosition() == -1)
                    p = viewHolder.getAdapterPosition() + 1;

                else if (viewHolder.getAdapterPosition() == adapterClg.list.size())
                    p = viewHolder.getAdapterPosition() - 1;
                else
                    p = viewHolder.getAdapterPosition();

                if (isSec && adapterClg.list.get(p).getState().equals("Modified") ||
                        isSec && adapterClg.list.get(p).getState().equals("Submitted")) {

                    if (direction == ItemTouchHelper.RIGHT) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext, R.style.delDialog);
                        builder2.setMessage("Are you sure you want to delete?");
                        builder2.setCancelable(false);
                        builder2.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        adapterClg.notifyItemChanged(p);
                                    }
                                }
                        );

                        builder2.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                int actID = Integer.parseInt(adapterClg.list.get(p).getId());
                                String actName = adapterClg.list.get(p).getAct();

                                DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                                mdb.createDatabase();
                                mdb.open();

                                Cursor c2 = mdb.getActAssigActNameAdmin(actName);
                                c2.moveToFirst();

                                Call<ResponseBody> putHours = RetrofitClient.getInstance().getApi().putHour(
                                        "Token " + AUTH_TOKEN,
                                        Integer.parseInt(adapterClg.list.get(p).getHours()),
                                        VEC,
                                        Integer.parseInt(c2.getString(c2.getColumnIndex("activityType"))),
                                        Integer.parseInt(c2.getString(c2.getColumnIndex("id"))),
                                        4,
                                        Password.PASS,
                                        actID
                                );

                                mdb.dropDetails(actID);
                                adapterClg.list.remove(p);
                                adapterClg.notifyItemRemoved(p);

                                putHours.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    @EverythingIsNonNull
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    }

                                    @Override
                                    @EverythingIsNonNull
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                                mdb.close();
                            }
                        });
                        builder2.show();
                    } else if (direction == ItemTouchHelper.LEFT) {
                        int actID = Integer.parseInt(adapterClg.list.get(p).getId());
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.inputDialog);
                        View viewInflated = LayoutInflater.from(mContext).inflate(R.layout.hours_input_layout, (ViewGroup) view, false);

                        EditText input = viewInflated.findViewById(R.id.input);
                        builder.setView(viewInflated);

                        builder.setCancelable(false);
                        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                                    dialog.cancel();
                                    adapterClg.notifyItemChanged(p);
                                }
                        );

                        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                            dialog.dismiss();

                            //Log.e("Yes this", adapterArea.list.get(p).getAct());
                            if (!input.getText().toString().trim().equals("")) {
                                try {
                                    newHours = Integer.parseInt(input.getText().toString());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    Toast.makeText(mContext, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    newHours = 0;
                                }

                                String actName = adapterClg.list.get(p).getAct();

                                DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                                mdb.createDatabase();
                                mdb.open();
                                Cursor m = mdb.getActAssigActNameAdmin(actName);
                                m.moveToFirst();
                                int maxH = m.getInt(m.getColumnIndex("HoursAssigned"));
                                mdb.close();

                                if (newHours >= 1 && newHours <= maxH) {
                                    mdb = new DatabaseAdapter(mContext);
                                    mdb.createDatabase();
                                    mdb.open();
                                    mdb.setDetails(newHours, "Modified", actID);

                                    Cursor c = mdb.getActAssigActNameAdmin(actName);
                                    c.moveToFirst();

                                    adapterClg.list.add(p, new AdapterDataMain(
                                                    adapterClg.list.get(p).getDate(),
                                                    adapterClg.list.get(p).getAct(),
                                                    String.valueOf(newHours),
                                                    adapterClg.list.get(p).getId(),
                                                    adapterClg.list.get(p).isApproved(),
                                                    "Modified",
                                                    adapterClg.list.get(p).getDesc()
                                            )
                                    );

                                    adapterClg.notifyDataSetChanged();
                                    adapterClg.list.remove(p + 1);
                                    adapterClg.notifyItemInserted(p);

                                    //TODO: Offline mode needs to be checked
                                    //mdb.setSyncActDetails(0, actID);

                                    Call<ResponseBody> putHours = RetrofitClient.getInstance().getApi().putHour(
                                            "Token " + AUTH_TOKEN,
                                            newHours,
                                            VEC,
                                            Integer.parseInt(c.getString(c.getColumnIndex("activityType"))),
                                            Integer.parseInt(c.getString(c.getColumnIndex("id"))),
                                            3,
                                            Password.PASS,

                                            actID
                                    );
                                    c.close();
                                    mdb.close();
                                    putHours.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        @EverythingIsNonNull
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful())
                                                Snackbar.make(add, "Edited to " + newHours + "hours", Snackbar.LENGTH_SHORT);
                                            else if (response.errorBody() != null) {
                                                try {
                                                    Toast.makeText(requireContext(), "onResponse: " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @Override
                                        @EverythingIsNonNull
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Log.e("Error:onFailure", t.toString());
                                        }
                                    });
                                } else {
                                    Toast.makeText(requireContext(), "Enter upto: " + maxH + "hour", Toast.LENGTH_SHORT).show();
                                    adapterClg.notifyItemChanged(p);
                                }
                            } else
                                adapterClg.notifyItemChanged(p);
                        });

                        if (viewInflated.getParent() != null)
                            ((ViewGroup) viewInflated.getParent()).removeView(viewInflated);
                        builder.show();

                    }
                } else if (adapterClg.list.get(p).getState().equals("Approved")
                        || adapterClg.list.get(p).getState().equals("LeaderModified")) {
                    if (isLeader == 1) {
                        String x = "";
                        if (adapterClg.list.get(p).getState().equals("Approved"))
                            x = "Approved By: ";
                        else if (adapterClg.list.get(p).getState().equals("LeaderDelete"))
                            x = "Deleted By: ";
                        else
                            x = "Modified By: ";

                        DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                        mdb.createDatabase();
                        mdb.open();
                        Cursor c = mdb.getActLeaderId(Integer.parseInt(adapterClg.list.get(p).getId()));
                        c.moveToFirst();
                        int leadId = c.getInt(c.getColumnIndex("Approved_by"));

                        x = mdb.getLeaderName(leadId) != null ? x + mdb.getLeaderName(leadId) : x + "PO";
                        Snackbar sb = Snackbar.make(add, x, Snackbar.LENGTH_LONG)
                                .setTextColor(sbColorText);
                        //sb.getView().setBackgroundColor(transparent);
                        mdb.close();
                        sb.show();
                        adapterClg.notifyItemChanged(p);
                    } else {
                        String x;
                        DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                        mdb.createDatabase();
                        mdb.open();
                        Cursor c = mdb.getActLeaderId(Integer.parseInt(adapterClg.list.get(p).getId()));
                        c.moveToFirst();

                        if (c.getString(c.getColumnIndex("Approved_by")).equals("null")) {
                            Snackbar sb = Snackbar.make(add, "Approved By: PO", Snackbar.LENGTH_LONG)
                                    .setTextColor(sbColorText);
                            //sb.getView().setBackgroundColor(transparent);
                            sb.show();
                            //adapterUniv.notifyDataSetChanged();
                        } else {
                            if (adapterClg.list.get(p).getState().equals("Approved"))
                                x = "Approved By: ";
                            else if (adapterClg.list.get(p).getState().equals("LeaderDelete"))
                                x = "Deleted By: ";
                            else
                                x = "Modified By: ";

                            int leadId = c.getInt(c.getColumnIndex("Approved_by"));
                            x = mdb.getLeaderName(leadId) != null ? x + mdb.getLeaderName(leadId) : x + "PO";
                            Snackbar sb = Snackbar.make(add, x, Snackbar.LENGTH_LONG)
                                    .setTextColor(sbColorText);
                            //sb.getView().setBackgroundColor(transparent);
                            sb.show();
                        }
                        adapterClg.notifyItemChanged(p);
                        mdb.close();
                    }
                }
                adapterClg.notifyDataSetChanged();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                int p;
                if (viewHolder.getAdapterPosition() == -1)
                    p = viewHolder.getAdapterPosition() + 1;
                else if (viewHolder.getAdapterPosition() == adapterClg.list.size())
                    p = viewHolder.getAdapterPosition() - 1;
                else
                    p = viewHolder.getAdapterPosition();
                if (isSec && adapterClg.list.get(p).getState().equals("Modified") ||
                        isSec && adapterClg.list.get(p).getState().equals("Submitted")) {
                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addSwipeRightActionIcon(R.drawable.ic_del_24)
                            .addSwipeLeftActionIcon(R.drawable.ic_edit_24)
                            .addBackgroundColor(transparent)
                            .create()
                            .decorate();
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                } else if (adapterClg.list.get(p).getState().equals("Approved") || adapterClg.list.get(p).getState().equals("LeaderModified") || adapterClg.list.get(p).getState().equals("LeaderDelete")) {
                    new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                            .addSwipeRightActionIcon(R.drawable.ic_eye_24)
                            .addSwipeLeftActionIcon(R.drawable.ic_eye_24)
                            .addBackgroundColor(transparent)
                            .create()
                            .decorate();
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };

        recyclerViewHours.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewHours.setAdapter(adapterClg);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallbackHours);
        itemTouchHelper.attachToRecyclerView(recyclerViewHours);

        ItemTouchHelper itemTouchHelper2 = new ItemTouchHelper(simpleCallbackArea);
        itemTouchHelper2.attachToRecyclerView(recyclerViewArea);

        ItemTouchHelper itemTouchHelper3 = new ItemTouchHelper(simpleCallbackUniv);
        itemTouchHelper3.attachToRecyclerView(recyclerViewUniv);

        add.setOnClickListener(view1 -> {
            DatabaseAdapter mdb = new DatabaseAdapter(mContext);
            mdb.createDatabase();
            mdb.open();
            int c = mdb.getCountAssignAct();
            mdb.close();
            if (c > 0) {

                AddDetailsActivity detailsActivity;
                switch (whichAct) {
                    case 21:
                        detailsActivity = new AddDetailsActivity(adapterClg, lottieAnimationView);
                        break;
                    case 22:
                        detailsActivity = new AddDetailsActivity(adapterArea, lottieAnimationView);
                        break;
                    default:
                    case 23:
                        detailsActivity = new AddDetailsActivity(adapterUniv, lottieAnimationView);
                        break;

                }
            /*fragSy.setVisibility(View.GONE);
            univRecSy.setVisibility(View.GONE);
            areaRecSy.setVisibility(View.GONE);
            clgRecSy.setVisibility(View.GONE);
            cardViewMain.setVisibility(View.GONE);*/

                Bundle args = new Bundle();
                args.putInt("whichAct", whichAct);
                args.putInt("act", act);
                detailsActivity.setArguments(args);

                FragmentManager fm = requireActivity().getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.halves_frame, detailsActivity, "AddDetailsActivity").commit();
                /*adapterArea.notifyDataSetChanged();
                adapterClg.notifyDataSetChanged();
                adapterUniv.notifyDataSetChanged();*/
            } else
                Toast.makeText(mContext, "No activity exist", Toast.LENGTH_SHORT).show();
        });
    }

    public List<AdapterDataMain> addAct(String whichAct) {

        ArrayList<AdapterDataMain> data = new ArrayList<>();

        DatabaseAdapter mDbHelper = new DatabaseAdapter(mContext);
        mDbHelper.createDatabase();
        mDbHelper.open();

        Cursor c = mDbHelper.getActList(whichAct);
        //Log.e("SSSHHH", "" + c.getCount());
        while (c.moveToNext()) {
            //Log.e("This", c.getString(c.getColumnIndex("ActivityCode")));

            data.add(new AdapterDataMain(
                            c.getString(c.getColumnIndex("Date")),
                            c.getString(c.getColumnIndex("ActivityName")),
                            c.getString(c.getColumnIndex("HoursWorked")),
                            c.getString(c.getColumnIndex("actID")),
                            c.getInt(c.getColumnIndex("If_Approved")),
                            c.getString(c.getColumnIndex("State")),
                            c.getString(c.getColumnIndex("Descr"))
                    )
            );
        }
        mDbHelper.close();
        return data;
    }

}