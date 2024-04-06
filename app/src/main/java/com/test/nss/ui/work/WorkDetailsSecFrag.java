package com.test.nss.ui.work;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.nss.R;

import java.util.List;

import me.itangqi.waveloadingview.WaveLoadingView;

import static com.test.nss.Helper.color5;
import static com.test.nss.Helper.color6;
import static com.test.nss.Helper.color7;
import static com.test.nss.Helper.color8;
import static com.test.nss.Helper.waveDur;
import static com.test.nss.Helper.zerof;


public class WorkDetailsSecFrag extends Fragment {

    static List<AdapterDataWork> workListData2;
    public Context context;
    WaveLoadingView wave1, wave2, wave3, wave4;
    View root;
    TextView area, area2, univ, clg;
    ProgressBar progArea1, progArea2, progUniv, progClg;

    public WorkDetailsSecFrag() {
    }

    public static WorkDetailsSecFrag newInstace(List<AdapterDataWork> adapterDataWork) {
        workListData2 = adapterDataWork;
        return new WorkDetailsSecFrag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = requireContext();

        root = inflater.inflate(R.layout.fragment_work_details_sec, container, false);
        //workListData2 = secHalfWorkData();

        wave1 = root.findViewById(R.id.wave1);
        wave2 = root.findViewById(R.id.wave2);
        wave3 = root.findViewById(R.id.wave3);
        wave4 = root.findViewById(R.id.wave4);

        progArea1 = root.findViewById(R.id.progArea1);
        progArea2 = root.findViewById(R.id.progArea2);
        progClg = root.findViewById(R.id.progClg);
        progUniv = root.findViewById(R.id.progUniv);

        area = root.findViewById(R.id.syOneWorked);
        area2 = root.findViewById(R.id.syTwoWorked);
        univ = root.findViewById(R.id.syUnivWorked);
        clg = root.findViewById(R.id.syClgWorked);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        wave1.setWaterLevelRatio(zerof);
        wave2.setWaterLevelRatio(zerof);
        wave3.setWaterLevelRatio(zerof);
        wave4.setWaterLevelRatio(zerof);

        wave1.setAnimDuration(waveDur);
        wave2.setAnimDuration(waveDur);
        wave3.setAnimDuration(waveDur);
        wave4.setAnimDuration(waveDur);

        int areaComp = computeHour(Float.parseFloat(workListData2.get(0).getCompHours()), Float.parseFloat(workListData2.get(0).getTotalHours()));
        int area2Comp = computeHour(Float.parseFloat(workListData2.get(1).getCompHours()), Float.parseFloat(workListData2.get(1).getTotalHours()));
        int univComp = computeHour(Float.parseFloat(workListData2.get(2).getCompHours()), Float.parseFloat(workListData2.get(2).getTotalHours()));
        int clgComp = computeHour(Float.parseFloat(workListData2.get(3).getCompHours()), Float.parseFloat(workListData2.get(3).getTotalHours()));

        wave1.setWaveColor(color5);
        wave2.setWaveColor(color6);
        wave3.setWaveColor(color7);
        wave4.setWaveColor(color8);

        wave1.setProgressValue(areaComp);
        wave2.setProgressValue(area2Comp);
        wave3.setProgressValue(univComp);
        wave4.setProgressValue(clgComp);

        startCount(areaComp, area, progArea1);
        startCount(area2Comp, area2, progArea2);
        startCount(univComp, univ, progUniv);
        startCount(clgComp, clg, progClg);

        RecyclerView recyclerViewWork = root.findViewById(R.id.secWorkRec);

        WorkDataAdapter adapterWork = new WorkDataAdapter(workListData2, context);
        recyclerViewWork.setHasFixedSize(true);
        recyclerViewWork.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewWork.setAdapter(adapterWork);
    }

    private void startCount(int x, TextView tv, ProgressBar p) {
        ValueAnimator animator = ValueAnimator.ofInt(0, x);
        animator.setDuration(2500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                tv.setText(String.format("%s%%", animation.getAnimatedValue().toString()));
                p.setProgress(Integer.parseInt(animation.getAnimatedValue().toString()));
            }
        });
        animator.start();
    }

    private int computeHour(float a, float b) {
        return (int) ((a / b) * 100);
    }

    /*public List<AdapterDataWork> secHalfWorkData() {
        ArrayList<AdapterDataWork> data = new ArrayList<>();

        Cursor col, univ, area1, area2;
        DatabaseAdapter m = new DatabaseAdapter(context);

        m.createDatabase();
        m.open();

        col = m.getHoursDet(c, 2);
        col.moveToFirst();

        univ = m.getHoursDet(u, 2);
        univ.moveToFirst();

        area1 = m.getHoursDet(a1, 2);
        area1.moveToFirst();

        area2 = m.getHoursDet(a2, 2);
        area2.moveToFirst();

        if (area1 != null && area2 != null && univ != null && col != null && area1.getString(area1.getColumnIndex("HoursWorked")) != null
                && area2.getString(area2.getColumnIndex("HoursWorked")) != null
                && univ.getString(univ.getColumnIndex("HoursWorked")) != null && col.getString(col.getColumnIndex("HoursWorked")) != null) {
            data.add(new AdapterDataWork("Area Based One", area1.getString(area1.getColumnIndex("TotalHours")),
                    area1.getString(area1.getColumnIndex("HoursWorked")), area1.getString(area1.getColumnIndex("RemainingHours"))));

            data.add(new AdapterDataWork("Area Based Two", area2.getString(area2.getColumnIndex("TotalHours")),
                    area2.getString(area2.getColumnIndex("HoursWorked")), area2.getString(area2.getColumnIndex("RemainingHours"))));


            data.add(new AdapterDataWork("University", univ.getString(univ.getColumnIndex("TotalHours")),
                    univ.getString(univ.getColumnIndex("HoursWorked")), univ.getString(univ.getColumnIndex("RemainingHours"))));


            data.add(new AdapterDataWork("College", col.getString(col.getColumnIndex("TotalHours")),
                    col.getString(col.getColumnIndex("HoursWorked")), col.getString(col.getColumnIndex("RemainingHours"))));
        } else {
            data.add(new AdapterDataWork("Area Based One", "0", "0", "0"));
            data.add(new AdapterDataWork("Area Based Two", "0", "0", "0"));
            data.add(new AdapterDataWork("University", "0", "0", "0"));
            data.add(new AdapterDataWork("College", "0", "0", "0"));
        }
        m.close();
        return data;
    }*/
}
