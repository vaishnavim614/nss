package com.test.nss.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;
import com.test.nss.R;

import static com.test.nss.Helper.blackishy;
import static com.test.nss.Helper.isFirst;
import static com.test.nss.Helper.isSec;
import static com.test.nss.Helper.primaryColDark;

public class MainFragment extends Fragment {

    public MainFragment() {
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    View root;

    TextView firstButton;
    TextView secButton;
    Toolbar toolbar;
    FragmentManager fm;
    FrameLayout halvesFrame;
    Context mContext;
    TextView toolbarTitle;
    TextView maLHay;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_main, container, false);

        mContext = requireContext();

        toolbarTitle = requireActivity().findViewById(R.id.titleTool);
        toolbarTitle.setText(getString(R.string.menu_main_frag));

        fm = getChildFragmentManager();
        //Log.e("MainFrag", "onCreate: " + fm.getBackStackEntryCount());

        halvesFrame = root.findViewById(R.id.halves_frame);

        firstButton = root.findViewById(R.id.firstButton);
        secButton = root.findViewById(R.id.secButton);

        toolbar = root.findViewById(R.id.toolbar);
        maLHay = root.findViewById(R.id.malHay);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = requireActivity().findViewById(R.id.toolbar);
        maLHay.setVisibility(View.VISIBLE);

        toolbar.setVisibility(View.VISIBLE);

        if (isFirst) {
            secButton.setTextColor(mContext.getColor(R.color.grey));
            firstButton.setOnClickListener(v -> {
                clear();
                firstButton.setTextColor(primaryColDark);
                maLHay.setVisibility(View.GONE);

                toolbar.setVisibility(View.VISIBLE);
                if (halvesFrame.getVisibility() == View.GONE)
                    halvesFrame.setVisibility(View.VISIBLE);

                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.halves_frame, fyAct.newInstance(), "fyAct").commit();
            });
            secButton.setOnClickListener(v -> {
                if (isFirst)
                    Snackbar.make(v, "Please complete First Year", Snackbar.LENGTH_SHORT).show();
            });
        } else if (isSec) {
            secButton.setOnClickListener(v -> {
                clear();
                secButton.setTextColor(primaryColDark);
                firstButton.setTextColor(blackishy);
                maLHay.setVisibility(View.GONE);

                toolbar.setVisibility(View.VISIBLE);
                if (halvesFrame.getVisibility() == View.GONE)
                    halvesFrame.setVisibility(View.VISIBLE);

                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.halves_frame, syAct.newInstance(), "syAct").commit();
            });

            firstButton.setOnClickListener(view1 -> {
                clear();
                secButton.setTextColor(blackishy);
                firstButton.setTextColor(primaryColDark);
                maLHay.setVisibility(View.GONE);

                toolbar.setVisibility(View.VISIBLE);
                if (halvesFrame.getVisibility() == View.GONE)
                    halvesFrame.setVisibility(View.VISIBLE);

                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.halves_frame, fyAct.newInstance(), "fyAct").commit();
            });
        } else {
            firstButton.setOnClickListener(v -> {
                clear();
                firstButton.setTextColor(primaryColDark);
                maLHay.setVisibility(View.GONE);

                toolbar.setVisibility(View.VISIBLE);
                if (halvesFrame.getVisibility() == View.GONE)
                    halvesFrame.setVisibility(View.VISIBLE);

                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.halves_frame, fyAct.newInstance(), "fyAct").commit();
            });
        }
    }

    public void clear() {
        /*Fragment simpleFragment = getChildFragmentManager().findFragmentById(R.id.halves_frame);

        Log.e("AAA", "clear() called MainFrag " + simpleFragment + fm.getBackStackEntryCount());
        if (simpleFragment instanceof fyAct || simpleFragment instanceof syAct) {
            //((ediary) requireActivity()).closeFragment(simpleFragment);
            fm.popBackStack();
            //simpleFragment.clear();
        }*/
    }
}