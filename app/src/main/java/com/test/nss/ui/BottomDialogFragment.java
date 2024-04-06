package com.test.nss.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.test.nss.R;
import com.test.nss.ui.leader.ModifyVolunteer;
import com.test.nss.ui.leader.ViewVolunteer;

public class BottomDialogFragment extends BottomSheetDialogFragment {

    public Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        view.findViewById(R.id.container_details).setVisibility(View.GONE);
        view.findViewById(R.id.sheet_frame).setVisibility(View.VISIBLE);
        setShowsDialog(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewVolunteer viewVolunteer = new ViewVolunteer();

        Bundle bundle = new Bundle();
        if (fragment instanceof ViewVolunteer && getArguments() != null) {
            bundle.putString("thisVec2", getArguments().getString("thisVec2"));
            viewVolunteer.setArguments(bundle);
            getChildFragmentManager().beginTransaction().add(R.id.sheet_frame, viewVolunteer).commit();
        }
    }
}