package com.test.nss.ui.camp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.test.nss.R;


public class CampDetailsDays extends Fragment {

    View root;
    ListView listView;
    LinearLayout camp_main_details;
    ConstraintLayout campList;
    FrameLayout camp_details_days_frame;

    public CampDetailsDays() {
    }

    public static CampDetailsDays newInstance() {
        return new CampDetailsDays();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_camp_details_days, container, false);

        //actName = getArguments().getString("actName");
        camp_main_details = requireActivity().findViewById(R.id.camp_main_details);
        campList = root.findViewById(R.id.camp_details_days);
        camp_details_days_frame = root.findViewById(R.id.camp_details_days_frame);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = root.findViewById(R.id.camp_days_list);
        ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(requireContext(), R.array.days, android.R.layout.simple_list_item_1);
        listView.setAdapter(list);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            String day = parent.getItemAtPosition(position).toString();
            CampDetailsFrag campDetailsFrag = CampDetailsFrag.newInstance();

            Bundle args = new Bundle();
            args.putString("whichDay", day);

            campDetailsFrag.setArguments(args);
            //Toast.makeText(requireContext(), day, Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.camp_frag, campDetailsFrag, "CampDetailsFrag").addToBackStack(null).commit();
        });
    }

    @Override
    public void onDetach() {
        camp_main_details.setVisibility(View.VISIBLE);
//        ((ediary) requireActivity()).closeFragment(this);
        super.onDetach();
        /*camp_main_details.setVisibility(View.VISIBLE);
        campList.setVisibility(View.GONE);
        camp_details_days_frame.setVisibility(View.GONE);*/
        Log.e("AAA", "Called");
        //FragmentManager fm = requireActivity().getSupportFragmentManager();
        //fm.beginTransaction().remove(this).commit();
    }
}