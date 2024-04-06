package com.test.nss.ui.camp;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.DatabaseAdapter;
import com.test.nss.R;
import com.test.nss.ui.onClickInterface;

import java.util.ArrayList;
import java.util.List;

public class CampDetailsFrag extends Fragment {

    public CampDetailsFrag() {
    }

    public static CampDetailsFrag newInstance() {
        return new CampDetailsFrag();
    }

    Button go_back;
    LinearLayout camp_main_details;

    View root;
    RecyclerView recViewCampList;
    List<AdapterCampAct> dataCampAct;
    onClickInterface onClickInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_camp_activity, container, false);
        dataCampAct = addCampActData();
        go_back = root.findViewById(R.id.go_back);

        camp_main_details = requireActivity().findViewById(R.id.camp_main_details);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        go_back.setOnClickListener(v -> {
            fragmentManager.popBackStack();
            //fragmentManager.popBackStackImmediate("CampDetailsFrag", 0);
            //camp_main_details.setVisibility(View.VISIBLE);
            onDetach();
        });

        onClickInterface = abc -> {
            Bundle args = new Bundle();
            args.putString("actName", abc);
            args.putString("whichDay", getArguments().getString("whichDay"));
            CampInputDetailsFrag campDetailsDays = new CampInputDetailsFrag();
            campDetailsDays.setArguments(args);

            fragmentManager.beginTransaction().replace(R.id.camp_frag, campDetailsDays, "CampList").addToBackStack("CampDetailsFrag").commit();
        };

        CampActDataAdapter campActDataAdapter = new CampActDataAdapter(dataCampAct, requireContext(), onClickInterface);
        recViewCampList = root.findViewById(R.id.recCampList);

        recViewCampList.setHasFixedSize(true);
        recViewCampList.setLayoutManager(new LinearLayoutManager(requireContext()));
        recViewCampList.setAdapter(campActDataAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //camp_main_details.setVisibility(View.VISIBLE);
        FragmentManager fm = getChildFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.e("CampFrag", "onDetache: " + fm.getBackStackEntryCount());
            fm.popBackStack();
            //fm.popBackStackImmediate("CampDetailsDays", 0);
//            fm.popBackStack("CampDetailsFrag", 0);
            //fm.popBackStackImmediate();
        }
    }

    public List<AdapterCampAct> addCampActData() {
        ArrayList<AdapterCampAct> data3 = new ArrayList<>();

        DatabaseAdapter mDbHelper2 = new DatabaseAdapter(requireContext());
        mDbHelper2.createDatabase();
        mDbHelper2.open();
        Cursor c3 = mDbHelper2.getCampActList();
        Log.e("SSSAAA", "" + c3.getCount());

        while (c3.moveToNext()) {
            //Log.e("camp", c2.getString(c2.getColumnIndex("CampActivityList")));
            //Log.e("camp", c2.getString(c2.getColumnIndex("Camp_taluka")));
            data3.add(new AdapterCampAct(
                    c3.getString(c3.getColumnIndex("CampActivityName"))
            ));
        }
        mDbHelper2.close();
        return data3;
    }
}