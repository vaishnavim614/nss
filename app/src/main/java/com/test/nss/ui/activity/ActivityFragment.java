package com.test.nss.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.test.nss.R;

import static com.test.nss.Helper.blackishy;
import static com.test.nss.Helper.isFirst;
import static com.test.nss.Helper.isSec;
import static com.test.nss.Helper.primaryColDark;

public class ActivityFragment extends Fragment {

    View root;
    Toolbar toolbar;
    TextView firstButton;
    TextView secButton;

    FragmentManager fm;
    TextView toolbarTitle;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_activity, container, false);
        toolbarTitle = requireActivity().findViewById(R.id.titleTool);
        toolbarTitle.setText(getString(R.string.menu_activity));

        toolbar = requireActivity().findViewById(R.id.toolbar);
        fm = getChildFragmentManager();
        /*if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.popBackStack(this.toString(), 0);
        }*/

        firstButton = root.findViewById(R.id.firstButton);
        secButton = root.findViewById(R.id.secButton);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setVisibility(View.VISIBLE);
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        if (fm.findFragmentById(R.id.act_list) == null)
            ft.replace(R.id.act_list, isFirst ? FirstHalfFrag.newInstance() : SecHalfFrag.newInstance()).commit();

        if (isFirst) {
            secButton.setTextColor(blackishy);
            firstButton.setTextColor(primaryColDark);

            firstButton.setOnClickListener(v -> {

                if (!(fm.findFragmentById(R.id.act_list) instanceof FirstHalfFrag)) {
                    firstButton.setTextColor(primaryColDark);
                    secButton.setTextColor(blackishy);

                    fm.beginTransaction().replace(R.id.act_list, FirstHalfFrag.newInstance()).commit();
                }
            });
            secButton.setOnClickListener(v -> {
                Snackbar.make(v, "Please complete First Year", Snackbar.LENGTH_SHORT).show();
            });
        } else if (isSec) {
            secButton.setTextColor(primaryColDark);
            firstButton.setTextColor(blackishy);

            secButton.setOnClickListener(v -> {
                //Log.d("AAA", "onViewCreated: " + (fm.findFragmentById(R.id.act_list) instanceof FirstHalfFrag));

                if (fm.findFragmentById(R.id.act_list) instanceof FirstHalfFrag) {
                    secButton.setTextColor(primaryColDark);
                    firstButton.setTextColor(blackishy);

                    fm.beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, R.anim.fragment_fade_enter).replace(R.id.act_list, SecHalfFrag.newInstance()).commit();
                }
            });

            firstButton.setOnClickListener(view1 -> {

                //Log.d("AAA", "onViewCreated: " + getChildFragmentManager().findFragmentById(R.id.act_list));

                if (!((fm.findFragmentById(R.id.act_list)) instanceof FirstHalfFrag)) {
                    firstButton.setTextColor(primaryColDark);
                    secButton.setTextColor(blackishy);

                    fm.beginTransaction().setCustomAnimations(android.R.anim.slide_out_right, R.anim.fragment_fade_exit).replace(R.id.act_list, FirstHalfFrag.newInstance()).commit();
                }
            });
        } else {
            firstButton.setOnClickListener(v -> {

                firstButton.setTextColor(primaryColDark);

                fm.beginTransaction().replace(R.id.act_list, FirstHalfFrag.newInstance()).commit();
            });
        }
    }
}