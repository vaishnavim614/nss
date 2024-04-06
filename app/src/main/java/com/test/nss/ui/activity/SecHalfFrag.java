package com.test.nss.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.test.nss.DatabaseAdapter;
import com.test.nss.Helper;
import com.test.nss.Password;
import com.test.nss.R;
import com.test.nss.api.RetrofitClient;
import com.test.nss.ui.onClickInterface2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class SecHalfFrag extends Fragment {


    View root;
    Context mContext;
    List<AdapterDataAct> areaOneListDataAct;
    List<AdapterDataAct> areaTwoListDataAct;
    List<AdapterDataAct> univListDataAct;
    List<AdapterDataAct> clgListDataAct;
    RecyclerView recyclerViewAreaOneAct;
    RecyclerView recyclerViewAreaTwoAct;
    RecyclerView recyclerViewUnivAct;
    RecyclerView recyclerViewClgAct;
    TextView areaActOne;
    TextView areaActTwo;
    TextView clgAct;
    TextView univAct;
    TextView noActDesc;
    onClickInterface2 onClickInterface2;
    FloatingActionButton backAct;
    LinearLayout actDetails;
    int act = -1;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    Date calobj = new Date();

    public SecHalfFrag() {
    }

    public static SecHalfFrag newInstance() {
        return new SecHalfFrag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_sec_half_act, container, false);
        mContext = requireContext();
        backAct = root.findViewById(R.id.backActBtn);

        clgListDataAct = addActData(21);
        areaOneListDataAct = addActData(221);
        areaTwoListDataAct = addActData(222);
        univListDataAct = addActData(23);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toast.makeText(requireContext(), "These are the assigned activities by PO", Toast.LENGTH_SHORT).show();

        root.post(this::hideFab);

        recyclerViewAreaOneAct = root.findViewById(R.id.areasyListOne);
        recyclerViewAreaTwoAct = root.findViewById(R.id.areasyListTwo);
        recyclerViewClgAct = root.findViewById(R.id.clgsyList);
        recyclerViewUnivAct = root.findViewById(R.id.univsyList);

        actDetails = root.findViewById(R.id.act_details2);

        areaActOne = root.findViewById(R.id.area_act1);
        areaActTwo = root.findViewById(R.id.area_act2);
        clgAct = root.findViewById(R.id.clg_act2);
        univAct = root.findViewById(R.id.univ_act2);
        noActDesc = root.findViewById(R.id.noActDesc);

        Cursor z;
        DatabaseAdapter md = new DatabaseAdapter(mContext);
        md.createDatabase();
        md.open();
        z = md.getArea("1%");
        if (z != null && z.getCount() > 0)
            areaActOne.setText(z.getString(z.getColumnIndex("ABProjectName")));
        z = md.getArea("2%");
        if (z != null && z.getCount() > 0) {
            areaActTwo.setText(z.getString(z.getColumnIndex("ABProjectName")));
        }
        md.close();

        areaActOne.setOnClickListener(view14 -> {
            if (areaOneListDataAct.isEmpty())
                noActDesc.setVisibility(View.VISIBLE);
            act = 5;
            new Handler().postDelayed(() -> {
                revealFab();
                recyclerViewAreaOneAct.setVisibility(View.VISIBLE);
                backAct.setVisibility(View.VISIBLE);
                actDetails.setVisibility(View.GONE);
                recyclerViewAreaTwoAct.setVisibility(View.GONE);
                recyclerViewClgAct.setVisibility(View.GONE);
                recyclerViewUnivAct.setVisibility(View.GONE);
            }, 250);
        });

        areaActTwo.setOnClickListener(view14 -> {
            if (areaTwoListDataAct.isEmpty())
                noActDesc.setVisibility(View.VISIBLE);
            act = 6;
            new Handler().postDelayed(() -> {
                revealFab();
                recyclerViewAreaTwoAct.setVisibility(View.VISIBLE);
                backAct.setVisibility(View.VISIBLE);
                actDetails.setVisibility(View.GONE);
                recyclerViewAreaOneAct.setVisibility(View.GONE);
                recyclerViewClgAct.setVisibility(View.GONE);
                recyclerViewUnivAct.setVisibility(View.GONE);
            }, 250);
        });

        clgAct.setOnClickListener(view13 -> {
            if (clgListDataAct.isEmpty())
                noActDesc.setVisibility(View.VISIBLE);
            act = 7;
            new Handler().postDelayed(() -> {
                revealFab();
                backAct.setVisibility(View.VISIBLE);
                actDetails.setVisibility(View.GONE);
                recyclerViewClgAct.setVisibility(View.VISIBLE);
                recyclerViewAreaOneAct.setVisibility(View.GONE);
                recyclerViewAreaTwoAct.setVisibility(View.GONE);
                recyclerViewUnivAct.setVisibility(View.GONE);
            }, 250);
        });

        univAct.setOnClickListener(view12 -> {
            if (univListDataAct.isEmpty())
                noActDesc.setVisibility(View.VISIBLE);
            act = 4;
            new Handler().postDelayed(() -> {
                revealFab();
                backAct.setVisibility(View.VISIBLE);
                actDetails.setVisibility(View.GONE);
                recyclerViewUnivAct.setVisibility(View.VISIBLE);
                recyclerViewClgAct.setVisibility(View.GONE);
                recyclerViewAreaOneAct.setVisibility(View.GONE);
                recyclerViewAreaTwoAct.setVisibility(View.GONE);
            }, 250);
        });

        DatabaseAdapter mdb2 = new DatabaseAdapter(requireContext());
        mdb2.createDatabase();
        mdb2.open();
        int c = mdb2.getSumHoursSubmitted(df.format(calobj.getTime()), "Second Year%");
        mdb2.close();

        onClickInterface2 = abc -> {
            if (c <= 10) {
                Toast.makeText(mContext, "Entering today's date", Toast.LENGTH_SHORT).show();

                String hours = "";
                String actName = "";

                String date = "";

                switch (act) {
                    case 4:
                        if (!univListDataAct.isEmpty()) {
                            actName = univListDataAct.get(abc).getAct();
                            hours = univListDataAct.get(abc).getHours();
                            date = univListDataAct.get(abc).getEndDate();
                        }
                    case 5:
                        if (!areaOneListDataAct.isEmpty()) {
                            actName = areaOneListDataAct.get(abc).getAct();
                            hours = areaOneListDataAct.get(abc).getHours();
                            date = areaOneListDataAct.get(abc).getEndDate();
                        }
                        break;
                    case 6:
                        if (!areaTwoListDataAct.isEmpty()) {
                            actName = areaTwoListDataAct.get(abc).getAct();
                            hours = areaTwoListDataAct.get(abc).getHours();
                            date = areaTwoListDataAct.get(abc).getEndDate();
                        }
                        break;
                    case 7:
                        if (!clgListDataAct.isEmpty()) {
                            actName = clgListDataAct.get(abc).getAct();
                            hours = clgListDataAct.get(abc).getHours();
                            date = clgListDataAct.get(abc).getEndDate();
                        }
                        break;
                }
                if (!actName.equals("") && !hours.equals("") && act != -1) {
                    String actCode = getResources().getStringArray(R.array.valOfActNames)[act];
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.inputDialog);
                    View viewInflated = LayoutInflater.from(mContext).inflate(R.layout.act_mini_input_layout, (ViewGroup) view, false);

                    EditText input = viewInflated.findViewById(R.id.input);
                    builder.setView(viewInflated);

                    builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                        dialog.cancel();
                    });

                    EditText descr = viewInflated.findViewById(R.id.desc);

                    String finalHours = hours;
                    String finalActName = actName;
                    String finalDate = date;
                    Calendar cal = Calendar.getInstance();
                    String nowDate = df.format(cal.getTime());
                    builder.setPositiveButton(android.R.string.ok, (dialog, i) -> {
                        int h = Integer.parseInt(finalHours);
                        int j = Integer.parseInt(input.getText().toString());
                        String desc = descr.getText().toString();
                        if (j > 0 && j <= h && !desc.equals("") && nowDate.compareTo(finalDate) <= 0) {
                            dialog.dismiss();

                            DatabaseAdapter mdb = new DatabaseAdapter(mContext);
                            mdb.createDatabase();
                            mdb.open();
                            mdb.insertActOff(
                                    Helper.VEC,
                                    actCode,
                                    df.format(cal.getTime()),
                                    finalActName,
                                    //actId.getText().toString(),
                                    String.valueOf(j),
                                    desc,
                                    0
                            );

                            Cursor m = mdb.getActAssigActNameAdmin(finalActName);
                            m.moveToFirst();
                            Call<ResponseBody> pushActList = RetrofitClient.getInstance().getApi().sendActList(
                                    "Token " + Helper.AUTH_TOKEN,
                                    Helper.VEC,
                                    m.getInt(m.getColumnIndex("id")),// AAA
                                    j,
                                    df.format(cal.getTime()),
                                    m.getInt(m.getColumnIndex("activityType")),
                                    Password.PASS,
                                    desc,
                                    1
                            );
                            mdb.close();
                            pushActList.enqueue(new Callback<ResponseBody>() {
                                @Override
                                @EverythingIsNonNull
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(mContext, "Entered", Toast.LENGTH_SHORT).show();
                                        DatabaseAdapter m = new DatabaseAdapter(mContext);
                                        m.createDatabase();
                                        m.open();
                                        m.setSync("DailyActivity", 1);
                                        m.close();
                                    } else
                                        Toast.makeText(mContext, "Entered locally", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                        } else
                            Toast.makeText(mContext, "Enter correct worked hours", Toast.LENGTH_SHORT).show();
                    });

                    builder.show();
                }
            }
        };

        MyListAdapterAct adapterAreaActOne = new MyListAdapterAct(areaOneListDataAct, mContext, onClickInterface2);
        recyclerViewAreaOneAct.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewAreaOneAct.setAdapter(adapterAreaActOne);

        MyListAdapterAct adapterAreaActTwo = new MyListAdapterAct(areaTwoListDataAct, mContext, onClickInterface2);
        recyclerViewAreaTwoAct.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewAreaTwoAct.setAdapter(adapterAreaActTwo);

        MyListAdapterAct adapterUnivAct = new MyListAdapterAct(univListDataAct, mContext, onClickInterface2);
        recyclerViewUnivAct.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewUnivAct.setAdapter(adapterUnivAct);

        MyListAdapterAct adapterClgAct = new MyListAdapterAct(clgListDataAct, mContext, onClickInterface2);
        recyclerViewClgAct.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerViewClgAct.setAdapter(adapterClgAct);

        backAct.setOnClickListener(view1 -> {
            hideFab();
            noActDesc.setVisibility(View.GONE);
            actDetails.setVisibility(View.VISIBLE);
            recyclerViewUnivAct.setVisibility(View.GONE);
            recyclerViewAreaOneAct.setVisibility(View.GONE);
            recyclerViewAreaTwoAct.setVisibility(View.GONE);
            recyclerViewClgAct.setVisibility(View.GONE);
        });
    }

    public List<AdapterDataAct> addActData(int yr) {
        //Log.e("opening db", "now for yr:" + yr);
        ArrayList<AdapterDataAct> data3 = new ArrayList<>();

        DatabaseAdapter mDbHelper = new DatabaseAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();

        Cursor c3 = mDbHelper.getActAllAdmin(yr);
        //Log.e("SSS", "" + c3.getCount());

        while (c3.moveToNext()) {
            data3.add(new AdapterDataAct(
                            c3.getString(c3.getColumnIndex("ActivityName")),
                            c3.getString(c3.getColumnIndex("HoursAssigned")),
                            c3.getString(c3.getColumnIndex("finalDate"))
                    )
            );
        }
        mDbHelper.close();
        return data3;
    }

    private void revealFab() {
        View v = root.findViewById(R.id.backActBtn);
        int x = v.getWidth() / 2;
        int y = v.getHeight() / 2;

        float finalRad = (float) Math.hypot(x, y);
        Animator animator = ViewAnimationUtils.createCircularReveal(v, x, y, 0, finalRad);
        v.setVisibility(View.VISIBLE);
        animator.start();
    }

    private void hideFab() {
        View v = root.findViewById(R.id.backActBtn);
        int x = v.getWidth() / 2;
        int y = v.getHeight() / 2;

        float inRad = (float) Math.hypot(x, y);
        Animator animator = ViewAnimationUtils.createCircularReveal(v, x, y, inRad, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setVisibility(View.GONE);
            }
        });
        animator.start();
    }

   /* @Override
    public void onDetach() {
        super.onDetach();
        FragmentManager fm = getChildFragmentManager();
        *//*if (fm.getBackStackEntryCount() > 0) {
            Log.e("SecHalfFrag", "onDetach: " + fm.getBackStackEntryCount());
            fm.popBackStack("SecHalfFrag", 0);
        }*//*
        noActDesc.setVisibility(View.GONE);
        actDetails.setVisibility(View.VISIBLE);
        recyclerViewUnivAct.setVisibility(View.GONE);

        recyclerViewAreaOneAct.setVisibility(View.GONE);
        recyclerViewAreaTwoAct.setVisibility(View.GONE);
        recyclerViewClgAct.setVisibility(View.GONE);
    }*/
}