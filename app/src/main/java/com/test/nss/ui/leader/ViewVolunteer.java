package com.test.nss.ui.leader;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.test.nss.DatabaseAdapter;
import com.test.nss.R;

import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;

import static com.test.nss.Helper.black;
import static com.test.nss.Helper.primaryColDark;

public class ViewVolunteer extends Fragment {

    LinearLayout linearL;
    RecyclerView recViewLeader, recViewVolUniv, recViewVolArea, recViewVolClg;
    List<AdapterDataVolunteer> dataVolListUniv, dataVolListArea, dataVolListClg;
    Button univ, area, clg;
    //FloatingActionButton back;
    LinearLayout detailsVol;
    CardView cardModify, leader, leaderAll;
    Context context;
    View root;

    public ViewVolunteer() {
    }

    public static ViewVolunteer newInstance() {
        return new ViewVolunteer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_modify_details, container, false);
        root.findViewById(R.id.back).findViewById(R.id.back).setVisibility(View.GONE);
        recViewLeader = requireActivity().findViewById(R.id.vecLeaderListAll);
        linearL = root.findViewById(R.id.linearL);
        dataVolListUniv = ListUtils.union(addVolActData("First Year University"), addVolActData("Second Year University"));
        dataVolListArea = ListUtils.union(ListUtils.union(addVolActData("First Year Area Based One"), addVolActData("First Year Area Based Two")),
                ListUtils.union(addVolActData("Second Year Area Based One"), addVolActData("Second Year Area Based Two")));
        dataVolListClg = ListUtils.union(addVolActData("First Year College"), addVolActData("Second Year College"));
        context = requireContext();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //root.post(this::revealFab);
        //revealFab();
        //back = root.findViewById(R.id.back);
        detailsVol = root.findViewById(R.id.detailsVol);
        cardModify = root.findViewById(R.id.cardModify);

        leader = requireActivity().findViewById(R.id.volAct);
        leaderAll = requireActivity().findViewById(R.id.volActAll);

        univ = root.findViewById(R.id.univ);
        area = root.findViewById(R.id.area);
        clg = root.findViewById(R.id.clg);

        recViewVolUniv = root.findViewById(R.id.recVecDetailUniv);
        MyListAdapterVolunteer adapterVolUniv = new MyListAdapterVolunteer(dataVolListUniv, context);

        recViewVolArea = root.findViewById(R.id.recVecDetailArea);
        MyListAdapterVolunteer adapterVolArea = new MyListAdapterVolunteer(dataVolListArea, context);

        recViewVolClg = root.findViewById(R.id.recVecDetailClg);
        MyListAdapterVolunteer adapterVolClg = new MyListAdapterVolunteer(dataVolListClg, context);

        univ.setOnClickListener(view1 -> {
            detailsVol.setVisibility(View.VISIBLE);
            univ.setTextColor(primaryColDark);
            area.setTextColor(black);
            clg.setTextColor(black);
            recViewVolUniv.setVisibility(View.VISIBLE);
            recViewVolArea.setVisibility(View.GONE);
            recViewVolClg.setVisibility(View.GONE);
        });

        area.setOnClickListener(view1 -> {
            detailsVol.setVisibility(View.VISIBLE);
            area.setTextColor(primaryColDark);
            univ.setTextColor(black);
            clg.setTextColor(black);
            recViewVolArea.setVisibility(View.VISIBLE);
            recViewVolUniv.setVisibility(View.GONE);
            recViewVolClg.setVisibility(View.GONE);
        });

        clg.setOnClickListener(view1 -> {
            detailsVol.setVisibility(View.VISIBLE);
            clg.setTextColor(primaryColDark);
            univ.setTextColor(black);
            area.setTextColor(black);
            recViewVolClg.setVisibility(View.VISIBLE);
            recViewVolUniv.setVisibility(View.GONE);
            recViewVolArea.setVisibility(View.GONE);
        });
        //back.setVisibility(View.GONE);

        /*back.setOnClickListener(view12 -> {
            hideFab();
            new Handler().postDelayed(this::onDetach, 250);
        });*/

        univ.performClick();
        recViewVolUniv.setLayoutManager(new LinearLayoutManager(context));
        recViewVolUniv.setAdapter(adapterVolUniv);

        recViewVolArea.setLayoutManager(new LinearLayoutManager(context));
        recViewVolArea.setAdapter(adapterVolArea);

        recViewVolClg.setLayoutManager(new LinearLayoutManager(context));
        recViewVolClg.setAdapter(adapterVolClg);
    }

    public List<AdapterDataVolunteer> addVolActData(String actName) {
        ArrayList<AdapterDataVolunteer> data3 = new ArrayList<>();

        DatabaseAdapter mDbHelper = new DatabaseAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        String thisVec = null;
        if (getArguments() != null) {
            thisVec = getArguments().getString("thisVec2");
        }

        Cursor c0 = mDbHelper.getVolAllDetails(actName, thisVec);
        int m = c0.getCount();

        c0.moveToFirst();
        while (m > 0) {
            data3.add(new AdapterDataVolunteer(
                    c0.getString(c0.getColumnIndex("Date")),
                    c0.getString(c0.getColumnIndex("AssignedActivityName")),
                    c0.getString(c0.getColumnIndex("Hours")),
                    c0.getString(c0.getColumnIndex("id")),
                    c0.getString(c0.getColumnIndex("State")),
                    0, 0
            ));
            c0.moveToNext();
            m = m - 1;
        }
        mDbHelper.close();
        return data3;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //hideFab();
        leaderAll.setVisibility(View.VISIBLE);
        leader.setVisibility(View.VISIBLE);
        recViewLeader.setVisibility(View.VISIBLE);
        //((ediary)requireActivity()).closeFragment(this);
        FragmentManager fm = getParentFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.e("ViewVol", "onDetache: " + fm.getBackStackEntryCount());
            linearL.setVisibility(View.GONE);
        }
    }

    private void revealFab() {
        View v = root.findViewById(R.id.back);
        int x = v.getWidth() / 2;
        int y = v.getHeight() / 2;

        float finalRad = (float) Math.hypot(x, y);
        Animator animator = ViewAnimationUtils.createCircularReveal(v, x, y, 0, finalRad);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setVisibility(View.VISIBLE);
            }
        });
        animator.start();
    }

    private void hideFab() {
        View v = root.findViewById(R.id.back);
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
}