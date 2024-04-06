package com.test.nss.ui.work;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.test.nss.Password;
import com.test.nss.R;
import com.test.nss.api.RetrofitClient;

import java.io.IOException;
import java.util.List;

import me.itangqi.waveloadingview.WaveLoadingView;
import okhttp3.ResponseBody;
import okhttp3.internal.annotations.EverythingIsNonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.test.nss.Helper.AUTH_TOKEN;
import static com.test.nss.Helper.color1;
import static com.test.nss.Helper.color2;
import static com.test.nss.Helper.color3;
import static com.test.nss.Helper.color4;
import static com.test.nss.Helper.waveDur;
import static com.test.nss.Helper.zerof;

public class WorkDetailsFirstFrag extends Fragment {

    public static boolean isCont = false;
    static List<AdapterDataWork> workListData;
    public Context context;
    WaveLoadingView wave1, wave2, wave3, wave4;
    View root;
    FloatingActionButton notif;
    TextView area, area2, univ, clg;
    ProgressBar progArea1, progArea2, progUniv, progClg;

    public WorkDetailsFirstFrag() {
    }

    public static WorkDetailsFirstFrag newInstance(List<AdapterDataWork> adapterDataWork) {
        workListData = adapterDataWork;
        return new WorkDetailsFirstFrag();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = requireContext();
        root = inflater.inflate(R.layout.fragment_work_details_first, container, false);

        //workListData = firstHalfWorkData();

        wave1 = root.findViewById(R.id.wave1);
        wave2 = root.findViewById(R.id.wave2);
        wave3 = root.findViewById(R.id.wave3);
        wave4 = root.findViewById(R.id.wave4);

        wave1.setWaterLevelRatio(zerof);
        wave2.setWaterLevelRatio(zerof);
        wave3.setWaterLevelRatio(zerof);
        wave4.setWaterLevelRatio(zerof);

        wave1.setAnimDuration(waveDur);
        wave2.setAnimDuration(waveDur);
        wave3.setAnimDuration(waveDur);
        wave4.setAnimDuration(waveDur);

        notif = root.findViewById(R.id.notif);

        progArea1 = root.findViewById(R.id.progArea1);
        progArea2 = root.findViewById(R.id.progArea2);
        progClg = root.findViewById(R.id.progClg);
        progUniv = root.findViewById(R.id.progUniv);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int areaCompOne = computeHour(Float.parseFloat(workListData.get(0).getCompHours()), Float.parseFloat(workListData.get(0).getTotalHours()));
        int areaCompTwo = computeHour(Float.parseFloat(workListData.get(1).getCompHours()), Float.parseFloat(workListData.get(1).getTotalHours()));
        int univComp = computeHour(Float.parseFloat(workListData.get(2).getCompHours()), Float.parseFloat(workListData.get(2).getTotalHours()));
        int clgComp = computeHour(Float.parseFloat(workListData.get(3).getCompHours()), Float.parseFloat(workListData.get(3).getTotalHours()));

        area = root.findViewById(R.id.fyOneWorked);
        area2 = root.findViewById(R.id.fyTwoWorked);
        univ = root.findViewById(R.id.fyUnivWorked);
        clg = root.findViewById(R.id.fyClgWorked);

        wave1.setWaveColor(color1);
        wave2.setWaveColor(color2);
        wave3.setWaveColor(color3);
        wave4.setWaveColor(color4);

        wave1.setProgressValue(areaCompOne);
        wave2.setProgressValue(areaCompTwo);
        wave3.setProgressValue(univComp);
        wave4.setProgressValue(clgComp);

        startCount(areaCompOne, area, progArea1);
        startCount(areaCompTwo, area2, progArea2);
        startCount(univComp, univ, progUniv);
        startCount(clgComp, clg, progClg);

        RecyclerView recyclerViewWork = root.findViewById(R.id.firstWorkRec);
        WorkDataAdapter adapterWork = new WorkDataAdapter(workListData, context);
        recyclerViewWork.setHasFixedSize(true);
        recyclerViewWork.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewWork.setAdapter(adapterWork);

        areaCompOne = Integer.parseInt(workListData.get(0).getCompHours());
        areaCompTwo = Integer.parseInt(workListData.get(1).getCompHours());
        univComp = Integer.parseInt(workListData.get(2).getCompHours());
        clgComp = Integer.parseInt(workListData.get(3).getCompHours());

        /*
        Cursor c2;
        DatabaseAdapter m = new DatabaseAdapter(context);
        m.open();
        m.createDatabase();
        c2 = m.getHoursDet(c, 1);
        c2.moveToFirst();
        clgComp = c2.getInt(c2.getColumnIndex("HoursWorked"));

        c2 = m.getHoursDet(u, 1);
        c2.moveToFirst();
        univComp = c2.getInt(c2.getColumnIndex("HoursWorked"));

        c2 = m.getHoursDet(a1, 1);
        c2.moveToFirst();
        areaCompOne = c2.getInt(c2.getColumnIndex("HoursWorked"));
        areaLvlOne = c2.getInt(c2.getColumnIndex("HoursWorked"));

        c2 = m.getHoursDet(a2, 1);
        c2.moveToFirst();
        areaCompTwo = c2.getInt(c2.getColumnIndex("HoursWorked"));
        areaLvlTwo = c2.getInt(c2.getColumnIndex("HoursWorked"));
        m.close();*/

        SharedPreferences sharedPreferences = context.getSharedPreferences("KEY", MODE_PRIVATE);
        isCont = sharedPreferences.getBoolean("isCont", false);

        //Log.e("AA", "" + isCont);
        if (clgComp >= 20 && univComp >= 20) {
            if (areaCompOne >= 20 && areaCompTwo >= 20 && areaCompOne + areaCompTwo >= 80 && !isCont) {
                revealFab();
                notif.setOnClickListener(view1 -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.delDialog);
                    builder.setTitle("Congratulations!");
                    builder.setMessage("Do you want to continue for next year?");
                    builder.setCancelable(false);

                    builder.setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.cancel());

                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface2, int i) {
                            AlertDialog.Builder b = new AlertDialog.Builder(context, R.style.delDialog);
                            b.setMessage("Are you sure?");
                            SharedPreferences shareit = context.getSharedPreferences("KEY", MODE_PRIVATE);
                            SharedPreferences.Editor eddy = shareit.edit();

                            b.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    //dialogInterface2.dismiss();
                                    Call<ResponseBody> selfRegContinue = RetrofitClient.getInstance().getApi().putContinue("Token " + AUTH_TOKEN, Password.PASS);
                                    selfRegContinue.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        @EverythingIsNonNull
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful() && response.body() != null) {
                                                try {
                                                    isCont = true;
                                                    eddy.putBoolean("isCont", true);
                                                    eddy.apply();
                                                    Log.e("Done", response.body().string());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            } else if (response.errorBody() != null) {
                                                try {
                                                    Log.e("Done", response.errorBody().string());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                                        }
                                    });
                                    root.post(WorkDetailsFirstFrag.this::hideFab);
                                }
                            });
                            b.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    //dialogInterface2.cancel();
                                    isCont = false;
                                    eddy.putBoolean("isCont", false);
                                    eddy.apply();
                                }
                            });
                            b.show();
                        }
                    });
                    builder.show();
                });
            }
        }
    }

    private void startCount(int x, TextView tv, ProgressBar p) {
        ValueAnimator animator = ValueAnimator.ofInt(0, x);
        animator.setDuration(2500);
        animator.addUpdateListener(animation -> {
            tv.setText(String.format("%s%%", animation.getAnimatedValue().toString()));
            p.setProgress(Integer.parseInt(animation.getAnimatedValue().toString()));
        });
        animator.start();
    }

    private int computeHour(float a, float b) {
        return (int) ((a / b) * 100);
    }

    /*public List<AdapterDataWork> firstHalfWorkData() {
        ArrayList<AdapterDataWork> data = new ArrayList<>();

        Cursor col, univ, area1, area2;
        DatabaseAdapter m = new DatabaseAdapter(context);
        m.createDatabase();
        m.open();
        m.createDatabase();
        m.open();

        col = m.getHoursDet(c, 1);
        col.moveToFirst();

        univ = m.getHoursDet(u, 1);
        univ.moveToFirst();

        area1 = m.getHoursDet(a1, 1);
        area1.moveToFirst();

        area2 = m.getHoursDet(a2, 1);
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

    private void revealFab() {
        View v = root.findViewById(R.id.notif);
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
        View v = root.findViewById(R.id.notif);
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