package com.test.nss.ui.work;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.snackbar.Snackbar;
import com.test.nss.DataBaseHelper;
import com.test.nss.DatabaseAdapter;
import com.test.nss.R;

import java.util.ArrayList;
import java.util.List;

import static com.github.mikephil.charting.utils.ColorTemplate.rgb;
import static com.test.nss.Helper.blackishy;
import static com.test.nss.Helper.isFirst;
import static com.test.nss.Helper.isSec;
import static com.test.nss.Helper.primaryColDark;

public class WorkFragment extends Fragment {

    public static final int[] CUSTOM = {rgb("#5e99ff"), rgb("#18FFFF"), rgb("#00BCBC"), rgb("#CC2BFF89")};

    View root;
    Toolbar toolbar;
    TextView firstButton;
    TextView secButton;
    FragmentManager fm;
    TextView toolbarTitle;
    TextView hoursInfo;
    PieChart pieChart;
    List<AdapterDataWork> adapterDataWorks, fyData, syData;
    ArrayList<PieEntry> data;
    PieDataSet pieData;
    PieData pieData1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        fyData = new ArrayList<>();
        syData = new ArrayList<>();
        root = inflater.inflate(R.layout.fragment_work, container, false);

        deleteData("WorkHoursFy");
        deleteData("WorkHoursSy");
        adapterDataWorks = WorkData();

        toolbarTitle = requireActivity().findViewById(R.id.titleTool);
        toolbarTitle.setText(getString(R.string.menu_work_hours));

        hoursInfo = root.findViewById(R.id.hoursInfo);

        firstButton = root.findViewById(R.id.firstButton);
        secButton = root.findViewById(R.id.secButton);
        pieChart = root.findViewById(R.id.pieChart);

        for (int i = 0; i < adapterDataWorks.size(); i++) {
            if (!adapterDataWorks.get(i).getCompHours().equals("0")) {
                pieChart.setVisibility(View.VISIBLE);
                break;
            }
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fm = getChildFragmentManager();

        view.post(() -> new PieChartAnim().execute());
        toolbar = requireActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);

        if (isFirst) {
            secButton.setTextColor(requireContext().getColor(R.color.grey));
            firstButton.setOnClickListener(v -> {
                firstButton.setTextColor(primaryColDark);

                hoursInfo.setVisibility(View.GONE);
                fm.beginTransaction().replace(R.id.work_details, WorkDetailsFirstFrag.newInstance(fyData)).commit();
            });
            secButton.setOnClickListener(v -> {
                Snackbar.make(v, "Please complete First Year", Snackbar.LENGTH_SHORT).show();
            });
        } else if (isSec) {
            secButton.setOnClickListener(v -> {
                secButton.setTextColor(primaryColDark);
                firstButton.setTextColor(blackishy);

                data.clear();
                for (int i = 4; i < adapterDataWorks.size(); i++) {
                    if (Integer.parseInt(adapterDataWorks.get(i).getCompHours()) > 0 && !adapterDataWorks.get(i).getCompHours().equals("0")) {
                        data.add(new PieEntry(Integer.parseInt(adapterDataWorks.get(i).getCompHours()), adapterDataWorks.get(i).getNature()));
                    }
                }
                pieData1.notifyDataChanged();
                pieData.notifyDataSetChanged();
                pieChart.notifyDataSetChanged();
                pieChart.invalidate();
                //pieChart.animateXY(800, 800, Easing.EaseInCirc);

                hoursInfo.setVisibility(View.GONE);
                fm.beginTransaction().replace(R.id.work_details, WorkDetailsSecFrag.newInstace(syData)).commit();
            });

            firstButton.setOnClickListener(view1 -> {
                firstButton.setTextColor(primaryColDark);
                secButton.setTextColor(blackishy);

                data.clear();
                for (int i = 0; i < adapterDataWorks.size() - 4; i++) {
                    if (Integer.parseInt(adapterDataWorks.get(i).getCompHours()) > 0 && !adapterDataWorks.get(i).getCompHours().equals("0")) {
                        data.add(new PieEntry(Integer.parseInt(adapterDataWorks.get(i).getCompHours()), adapterDataWorks.get(i).getNature()));
                    }
                }
                pieData1.notifyDataChanged();
                pieData.notifyDataSetChanged();
                pieChart.notifyDataSetChanged();
                pieChart.invalidate();
                //pieChart.animateXY(800, 800, Easing.EaseInCirc);

                hoursInfo.setVisibility(View.GONE);
                fm.beginTransaction().replace(R.id.work_details, WorkDetailsFirstFrag.newInstance(fyData)).addToBackStack(null).commit();
            });
        }
    }

    public List<AdapterDataWork> WorkData() {
        ArrayList<AdapterDataWork> data = new ArrayList<>();

        DatabaseAdapter m = new DatabaseAdapter(requireContext());
        m.createDatabase();
        m.open();

        int areaCompOne = m.getSumHours("First Year Area Based One");
        int areaCompTwo = m.getSumHours("First Year Area Based Two");
        int clgComp = m.getSumHours("First Year College");
        int univComp = m.getSumHours("First Year University");

        int areaCompOne2 = m.getSumHours("Second Year Area Based One");
        int areaCompTwo2 = m.getSumHours("Second Year Area Based Two");
        int clgComp2 = m.getSumHours("Second Year College");
        int univComp2 = m.getSumHours("Second Year University");

        int areaLvlOne = m.getHours("Area Based Level One");
        int areaLvlTwo = m.getHours("Area Based Level Two");
        int clgLvl = m.getHours("College Level");
        int univLvl = m.getHours("University Level");

        m.close();
        //Log.i("TAG", "firstHalfWorkData: " + areaCompTwo);
        int areaRemOneHours;
        int areaRemTwoHours;
        int univRemHours;
        int clgRemHours;

        if (areaCompOne >= 1 && areaLvlOne - areaCompOne > 0)
            areaRemOneHours = areaLvlOne - areaCompOne;
        else if (areaCompOne == 0)
            areaRemOneHours = areaLvlOne;
        else
            areaRemOneHours = Integer.parseInt("00");

        if (areaCompTwo >= 1 && areaLvlTwo - areaCompTwo > 0)
            areaRemTwoHours = areaLvlTwo - areaCompTwo;
        else if (areaCompTwo == 0)
            areaRemTwoHours = areaLvlTwo;
        else
            areaRemTwoHours = Integer.parseInt("00");

        if (clgComp >= 1 && clgLvl - clgComp > 0)
            clgRemHours = clgLvl - clgComp;
        else if (clgComp == 0)
            clgRemHours = clgLvl;
        else
            clgRemHours = Integer.parseInt("00");

        if (univComp >= 1 && univLvl - univComp > 0)
            univRemHours = univLvl - univComp;
        else if (univComp == 0)
            univRemHours = univLvl;
        else
            univRemHours = Integer.parseInt("00");

        m = new DatabaseAdapter(requireContext());
        m.createDatabase();
        m.open();
        m.insertWork(
                "Area Based Level One",
                areaLvlOne,
                areaCompOne,
                areaRemOneHours,
                1
        );
        m.insertWork(
                "Area Based Level Two",
                areaLvlTwo,
                areaCompTwo,
                areaRemTwoHours,
                1
        );
        m.insertWork(
                "University Level",
                univLvl,
                univComp,
                univRemHours,
                1
        );
        m.insertWork(
                "College Level",
                clgLvl,
                clgComp,
                clgRemHours,
                1
        );
        m.close();

        data.add(new AdapterDataWork("Area Based 1", String.valueOf(areaLvlOne), String.valueOf(areaCompOne), String.valueOf(areaRemOneHours)));
        data.add(new AdapterDataWork("Area Based 2", String.valueOf(areaLvlTwo), String.valueOf(areaCompTwo), String.valueOf(areaRemTwoHours)));
        data.add(new AdapterDataWork("University", String.valueOf(univLvl), String.valueOf(univComp), String.valueOf(univRemHours)));
        data.add(new AdapterDataWork("College", String.valueOf(clgLvl), String.valueOf(clgComp), String.valueOf(clgRemHours)));

        if (areaCompOne2 >= 1 && areaLvlOne - areaCompOne2 > 0)
            areaRemOneHours = areaLvlOne - areaCompOne2;
        else if (areaCompOne2 == 0)
            areaRemOneHours = areaLvlOne;
        else
            areaRemOneHours = Integer.parseInt("00");

        if (areaCompTwo2 >= 1 && areaLvlTwo - areaCompTwo2 > 0)
            areaRemTwoHours = areaLvlTwo - areaCompTwo2;
        else if (areaCompTwo2 == 0)
            areaRemTwoHours = areaLvlTwo;
        else
            areaRemTwoHours = Integer.parseInt("00");

        if (clgComp2 >= 1 && clgLvl - clgComp2 > 0)
            clgRemHours = clgLvl - clgComp2;
        else if (clgComp2 == 0)
            clgRemHours = clgLvl;
        else
            clgRemHours = Integer.parseInt("00");

        if (univComp2 >= 1 && univLvl - univComp2 > 0)
            univRemHours = univLvl - univComp2;
        else if (univComp2 == 0)
            univRemHours = univLvl;
        else
            univRemHours = Integer.parseInt("00");

        m = new DatabaseAdapter(requireContext());
        m.createDatabase();
        m.open();
        m.insertWork(

                "Area Based One",
                areaLvlOne,
                areaCompOne2,
                areaRemOneHours,
                2
        );
        m.insertWork(

                "Area Based Two",
                areaLvlTwo,
                areaCompTwo2,
                areaRemTwoHours,
                2
        );
        m.insertWork(

                "University",
                univLvl,
                univComp2,
                univRemHours,
                2
        );
        m.insertWork(

                "College",
                clgLvl,
                clgComp2,
                clgRemHours,
                2
        );
        m.close();

        data.add(new AdapterDataWork("Area Based 1", String.valueOf(areaLvlOne), String.valueOf(areaCompOne2), String.valueOf(areaRemOneHours)));
        data.add(new AdapterDataWork("Area Based 2", String.valueOf(areaLvlTwo), String.valueOf(areaCompTwo2), String.valueOf(areaRemTwoHours)));
        data.add(new AdapterDataWork("University", String.valueOf(univLvl), String.valueOf(univComp2), String.valueOf(univRemHours)));
        data.add(new AdapterDataWork("College", String.valueOf(clgLvl), String.valueOf(clgComp2), String.valueOf(clgRemHours)));
        return data;
    }

    public void deleteData(String table) {
        DatabaseAdapter mDbHelper2 = new DatabaseAdapter(requireContext());
        mDbHelper2.createDatabase();
        mDbHelper2.open();
        DataBaseHelper mDb2 = new DataBaseHelper(requireContext());
        SQLiteDatabase m = mDb2.getWritableDatabase();
        m.execSQL(String.format("UPDATE %s SET HoursWorked=NULL", table));
        m.close();
        mDb2.close();
        mDbHelper2.close();
    }

    @Override
    public void onDetach() {
        Fragment simpleFragment = fm
                .findFragmentById(R.id.work_details);
        //int c = fm.getBackStackEntryCount();
        Log.d("AAA", "onDetach() called WorkFrag" + simpleFragment + fm.getBackStackEntryCount());
        if (simpleFragment instanceof WorkDetailsFirstFrag || simpleFragment instanceof WorkDetailsSecFrag) {
            //((ediary) requireActivity()).closeFragment(simpleFragment);
            fm.popBackStack();
            simpleFragment.onDetach();
        }
//            WorkDetailsFirstFrag.newInstance().onDetach();
        /*if (simpleFragment instanceof WorkDetailsFirstFrag)
            fm.popBackStack();
        if (simpleFragment instanceof WorkDetailsSecFrag)
            fm.popBackStack();*/
        /*int c = fm.getBackStackEntryCount();
        if (c >= 0) {
            Log.e(null, "onDetache: " + fm.getBackStackEntryCount());
            fm.popBackStack();
            c--;
        }*/
        super.onDetach();
    }

    public class PieChartAnim extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... views) {
            data = new ArrayList<>();
            pieData = new PieDataSet(data, "");
            pieData1 = new PieData(pieData);

            for (int i = 0; i < adapterDataWorks.size() - 4; i++) {
                fyData.add(adapterDataWorks.get(i));
            }
            for (int i = 4; i < adapterDataWorks.size(); i++) {
                syData.add(adapterDataWorks.get(i));
            }

            if (isFirst) {
                firstButton.setTextColor(primaryColDark);
                secButton.setTextColor(blackishy);
                for (int i = 0; i < adapterDataWorks.size() - 4; i++) {
                    //Log.e("Values", adapterDataWorks.get(i).getCompHours());
                    if (Integer.parseInt(adapterDataWorks.get(i).getCompHours()) > 0 && !adapterDataWorks.get(i).getCompHours().equals("0")) {
                        data.add(new PieEntry(Integer.parseInt(adapterDataWorks.get(i).getCompHours()), adapterDataWorks.get(i).getNature()));
                    }
                }
            } else if (isSec) {
                firstButton.setTextColor(blackishy);
                secButton.setTextColor(primaryColDark);
                for (int i = 4; i < adapterDataWorks.size(); i++) {
                    if (Integer.parseInt(adapterDataWorks.get(i).getCompHours()) > 0 && !adapterDataWorks.get(i).getCompHours().equals("0")) {
                        data.add(new PieEntry(Integer.parseInt(adapterDataWorks.get(i).getCompHours()), adapterDataWorks.get(i).getNature()));
                    }
                }
            }

            pieData.setSliceSpace(3);
            pieData.setSelectionShift(7);

            int[] colors = new int[10];
            int counter = 0;

        /*for (int color : ColorTemplate.MATERIAL_COLORS
        ) {
            colors[counter] = color;
            counter++;
        }*/

            for (int color : CUSTOM
            ) {
                colors[counter] = color;
                counter++;
            }
            //Log.e("AAA", "" + colors.length);
            pieData.setColors(colors);

            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    String s = Float.toString(e.getY());

                    Toast.makeText(requireContext(), s.substring(0, s.indexOf(".")) + "h Worked", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected() {

                }
            });

            pieData.setValueFormatter(new PercentFormatter(pieChart));
            pieChart.setUsePercentValues(true);
            pieChart.setDrawEntryLabels(false);
            pieData.setValueTextSize(15f);

            Typeface t = Typeface.createFromAsset(requireActivity().getAssets(), "google_sans_bold.ttf");

            pieData1.setDataSet(pieData);
            pieData.setValueTypeface(t);

            Legend l = pieChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setWordWrapEnabled(true);
            l.setDrawInside(false);
            l.setTypeface(t);
            l.setTextColor(blackishy);

            pieChart.setCenterTextColor(primaryColDark);

            pieChart.setData(pieData1);
            pieChart.getDescription().setEnabled(false);
            pieChart.setCenterText("Total hours worked");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pieChart.animate();
            pieChart.animateXY(1500, 1500, Easing.EaseInOutExpo); //SINE QUART
            pieChart.invalidate();
            fm.beginTransaction().replace(R.id.work_details, WorkDetailsFirstFrag.newInstance(fyData)).commit();
        }
    }
}