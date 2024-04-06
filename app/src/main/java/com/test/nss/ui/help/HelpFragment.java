package com.test.nss.ui.help;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rahul.bounce.library.BounceTouchListener;
import com.test.nss.DatabaseAdapter;
import com.test.nss.R;
import com.test.nss.ui.onClickInterface;

import java.util.ArrayList;
import java.util.List;


public class HelpFragment extends Fragment {

    View root;
    Toolbar toolbar;

    TextView namePo;
    TextView emailPo;
    TextView contactPo;
    TextView clgPo;
    BounceTouchListener bounceTouchListener;

    TextView toolbarTitle;
    CardView contactUs, mainCard;
    TextView sun;

    List<AdapterDataHelp> dataLeaderHelpList = new ArrayList<>();
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_help, container, false);

        toolbar = requireActivity().findViewById(R.id.toolbar);

        //toolbar.setVisibility(View.GONE);

        sun = root.findViewById(R.id.sun);
        toolbarTitle = requireActivity().findViewById(R.id.titleTool);
        toolbarTitle.setText(getString(R.string.menu_help));

        recyclerView = root.findViewById(R.id.leaderRecDet);

        onClickInterface onClickInterface;
        onClickInterface = abc -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + abc));
            startActivity(callIntent);
        };

        dataLeaderHelpList = addHelpData();
        MyListAdapterHelp helpDataAdapter = new MyListAdapterHelp(dataLeaderHelpList,
                requireContext(), onClickInterface);
        recyclerView.setAdapter(helpDataAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                helpDataAdapter.isShimmer = false;
                helpDataAdapter.notifyDataSetChanged();
            }
        }, 3000);

        return root;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contactUs = root.findViewById(R.id.contactUs);
        Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.shake);
        contactUs.startAnimation(animation);

        emailPo = root.findViewById(R.id.poEmail);
        contactPo = root.findViewById(R.id.poNo);
        namePo = root.findViewById(R.id.poName);
        clgPo = root.findViewById(R.id.poClg);
        mainCard = root.findViewById(R.id.mainCard);

        DatabaseAdapter mDbHelper = new DatabaseAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactUs.startAnimation(animation);
            }
        });
        if (mDbHelper.getHelpData().size() > 0) {
            clgPo.setText(mDbHelper.getHelpData().get(0));
            namePo.setText(mDbHelper.getHelpData().get(1));
            emailPo.setText(String.format(getString(R.string.email) + " %s", mDbHelper.getHelpData().get(2)));
            contactPo.setText(String.format(getString(R.string.contact_no) + " +%s", mDbHelper.getHelpData().get(3)));
        } else {
            emailPo.setText(getString(R.string.email));
            contactPo.setText(getString(R.string.contact_no));
        }
        mDbHelper.close();
        int maxSunTranslation = -(int) (580 * .25f);
        bounceTouchListener = new BounceTouchListener(recyclerView);
        bounceTouchListener.setOnTranslateListener(new BounceTouchListener.OnTranslateListener() {
            @Override
            public void onTranslate(float translation) {
                sun.setVisibility(View.VISIBLE);
                if (translation <= 0) {
                    //contactUs.setTranslationY(translation);
                    mainCard.setTranslationY(Math.max(maxSunTranslation, translation));
                    bounceTouchListener.setMaxAbsTranslation(-99);
                }
                if (translation >= 0.0 && translation <= 1) {
                    int color = ((int) (Math.random() * 16777215)) | (0xFF << 24);
                    sun.setTextColor(color);
                }
            }
        });

        recyclerView.setOnTouchListener(bounceTouchListener);

        contactPo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String co = contactPo.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + co.substring(co.lastIndexOf(" "))));
                startActivity(callIntent);
            }
        });
    }

    public List<AdapterDataHelp> addHelpData() {
        ArrayList<AdapterDataHelp> data3 = new ArrayList<>();

        DatabaseAdapter mDbHelper = new DatabaseAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        Cursor c3 = mDbHelper.getLeaders();

        while (c3.moveToNext()) {
            data3.add(new AdapterDataHelp(
                    c3.getString(c3.getColumnIndex("Name")),
                    c3.getString(c3.getColumnIndex("Email")),
                    c3.getString(c3.getColumnIndex("Contact")),
                    c3.getString(c3.getColumnIndex("CollegeName"))
            ));
        }
        mDbHelper.close();
        return data3;
    }
}