package com.test.nss.ui.main;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.airbnb.lottie.LottieAnimationView;
import com.test.nss.DatabaseAdapter;
import com.test.nss.Password;
import com.test.nss.R;
import com.test.nss.api.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static com.test.nss.Helper.AUTH_TOKEN;
import static com.test.nss.Helper.VEC;
import static com.test.nss.Helper.isFirst;

public class AddDetailsActivity extends Fragment {
    public MyListAdapter myListAdapter;
    public LottieAnimationView lottieAnimationView;

    View huh;
    ConstraintLayout constFyAct;
    LinearLayout nssHalvesLinear;
    ArrayList<String> actAssignList;
    ArrayList<String> actAssignListId;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    TextView actId;
    Button addSend;

    Button backActDetail;
    ConstraintLayout campActIn;
    LinearLayout actHeaderInput;
    TextView malHay;
    int whichAct;
    int act;
    private EditText act_desc;
    private TextView todayBtn;
    private TextView actDate;
    private EditText actHour;
    private Spinner drpdownactAssignName;

    public AddDetailsActivity() {
    }

    public AddDetailsActivity(MyListAdapter myListAdapter, LottieAnimationView lottieAnimationView) {
        this.myListAdapter = myListAdapter;
        this.lottieAnimationView = lottieAnimationView;
    }

    public static <K> String getRoot(Map<String, Integer> map, Integer value) {
        for (String key : map.keySet()) {
            if (value.equals(map.get(key))) {
                return key;
            }
        }
        return null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        huh = inflater.inflate(R.layout.fragment_add_details_activity, container, false);

//        clgList = getClgList();
        actAssignList = getAssignActList();
        actHeaderInput = requireActivity().findViewById(R.id.actHeaderInput);

        actHeaderInput.setVisibility(View.VISIBLE);
        constFyAct = requireActivity().findViewById(R.id.constFyAct);
        nssHalvesLinear = requireActivity().findViewById(R.id.nss_halves_Linear);
        malHay = requireActivity().findViewById(R.id.malHay);

        if (getArguments() != null) {
            whichAct = getArguments().getInt("whichAct");
            act = getArguments().getInt("act");
        } else {
            whichAct = -1;
            act = -1;
        }
        Log.e("AA", "" + whichAct);
        Log.e("AA", "" + act);

        backActDetail = huh.findViewById(R.id.backActDetail);

        return huh;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isEmpty(EditText e) {
        return e.getText().toString().trim().length() <= 0;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (constFyAct != null)
            constFyAct.setVisibility(View.GONE);
        nssHalvesLinear.setVisibility(View.GONE);

        addSend = huh.findViewById(R.id.addSend);
        campActIn = huh.findViewById(R.id.camp_act_in);

        actId = huh.findViewById(R.id.actdetailId);

        actDate = huh.findViewById(R.id.actDate);
        actHour = huh.findViewById(R.id.actHour);

        todayBtn = huh.findViewById(R.id.todayBtn);

        drpdownactAssignName = huh.findViewById(R.id.drpdown_actAssignName);

        act_desc = huh.findViewById(R.id.act_desc);
        actAssignListId = getAssignActListId();

        final Calendar cal = Calendar.getInstance();
        int currDay = cal.get(Calendar.DATE);
        int currMont = cal.get(Calendar.MONTH) + 1;
        int currYear = cal.get(Calendar.YEAR);

        String todayDate = "" + currYear + "-" + String.format(Locale.ENGLISH, "%02d", currMont) + "-" + String.format(Locale.ENGLISH, "%02d", currDay);
        todayBtn.setOnClickListener(view12 -> actDate.setText(todayDate));
        actDate.setOnClickListener(view1 -> {
            int dd = cal.get(Calendar.DAY_OF_MONTH);
            int mm = cal.get(Calendar.MONTH);
            int yr = cal.get(Calendar.YEAR);

            DatePickerDialog d = new DatePickerDialog(
                    requireContext(),
                    R.style.datePick,
                    onDateSetListener,
                    yr, mm, dd
            );
            if (d.getWindow() != null)
                //d.getWindow().set(new ColorDrawable(requireContext().getColor(R.color.bla)));
                d.show();
        });

        onDateSetListener = (datePicker, i, i1, i2) -> {
            i1 = i1 + 1;
            String date = i + "-" + String.format(Locale.ENGLISH, "%02d", i1) + "-" + String.format(Locale.ENGLISH, "%02d", i2);

            if (i2 > currDay - 8 && i2 <= currDay && i1 >= currMont && i >= currYear)
                actDate.setText(date);
            else
                Toast.makeText(requireContext(), "Enter today's date or 8 days before", Toast.LENGTH_SHORT).show();
        };

        Map<String, Integer> actIdHash = new HashMap<>();
        actIdHash.put("First Year College", 11);
        actIdHash.put("First Year Area Based One", 121);
        actIdHash.put("First Year Area Based Two", 122);
        actIdHash.put("First Year University", 13);
        actIdHash.put("Second Year College", 21);
        actIdHash.put("Second Year Area Based One", 221);
        actIdHash.put("Second Year Area Based Two", 222);
        actIdHash.put("Second Year University", 23);
        ArrayAdapter<String> a = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, actAssignList);
        drpdownactAssignName.setAdapter(a);
        DatabaseAdapter mdb = new DatabaseAdapter(requireContext());

        //String actName = getResources().getStringArray(R.array.valOfActNames)[act];
        if (drpdownactAssignName.getSelectedItem() != null) {
            addSend.setOnClickListener(view13 -> {
                mdb.createDatabase();
                mdb.open();
                String activityName = drpdownactAssignName.getSelectedItem().toString();
                Cursor m = mdb.getActAssigActNameAdmin(activityName);
                m.moveToFirst();
                int maxH = m.getInt(m.getColumnIndex("HoursAssigned"));

                int c;

                if (act_desc.getText().toString().equals("") || act_desc.getText().toString().trim().length() <= 0)
                    Toast.makeText(requireContext(), "Enter Description", Toast.LENGTH_SHORT).show();

                else if (actHour.getText().toString().equals("") || Integer.parseInt(actHour.getText().toString().trim()) <= 0)
                    Toast.makeText(requireContext(), "Work atleast an hour and enter", Toast.LENGTH_SHORT).show();

                else if (Integer.parseInt(actHour.getText().toString()) > maxH)
                    Toast.makeText(requireContext(), String.format(Locale.ENGLISH, "Cannot enter more than %d hours", maxH), Toast.LENGTH_SHORT).show();

                else if (actDate.getText().toString().equals("") || actDate.getText().toString().equals("YYYY/MM/DD"))
                    Toast.makeText(requireContext(), "Enter Date", Toast.LENGTH_SHORT).show();

                else if (!actDate.getText().toString().equals("") && drpdownactAssignName.getSelectedItem() != null
                        && !isEmpty(actHour)) {

                    mdb.createDatabase();
                    mdb.open();
                    if (isFirst)
                        c = mdb.getSumHoursSubmitted(actDate.getText().toString(), "First Year%");
                    else
                        c = mdb.getSumHoursSubmitted(actDate.getText().toString(), "Second Year%");
                    mdb.close();

                    //Toast.makeText(requireContext(), actDate.getText().toString() + " " + c, Toast.LENGTH_SHORT).show();
                    if (c <= 10) {
                        actId.setText(actAssignListId.get(drpdownactAssignName.getSelectedItemPosition()));

                        DatabaseAdapter mDbHelper = new DatabaseAdapter(requireContext());
                        mDbHelper.createDatabase();
                        mDbHelper.open();

                        Log.e("hmm", "" + whichAct + " " + activityName);
                        Log.e("hmm", "" + actDate.getText().toString());
                        Log.e("hmm", "" + drpdownactAssignName.getSelectedItem().toString());
                        Log.e("hmm", "" + actId.getText().toString());

                        String actName = getRoot(actIdHash, m.getInt(m.getColumnIndex("activityType")));
                        Log.e("AAA", "" + actName);
                        mDbHelper.insertActOff(
                                VEC,
                                actName,
                                actDate.getText().toString(),
                                drpdownactAssignName.getSelectedItem().toString(),
                                //actId.getText().toString(),
                                actHour.getText().toString(),
                                act_desc.getText().toString(),
                                0
                        );

                        if (isNetworkAvailable()) {
                            Log.e("AOO", "" + actName);
                            Log.e("AOO", "" + drpdownactAssignName.getSelectedItem().toString());
                            Log.e("AOO", "" + whichAct);
                            Log.e("AOO", "" + actId.getText().toString());


                            Call<ResponseBody> pushActList = RetrofitClient.getInstance().getApi().sendActList(
                                    "Token " + AUTH_TOKEN,
                                    VEC,
                                    m.getInt(m.getColumnIndex("id")),
                                    Integer.parseInt(actHour.getText().toString()),
                                    actDate.getText().toString(),
                                    m.getInt(m.getColumnIndex("activityType")),
                                    Password.PASS,
                                    act_desc.getText().toString(),
                                    1
                            );
                            m.close();
                            pushActList.enqueue(new Callback<ResponseBody>() {
                                @Override
                                @EverythingIsNonNull
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        Toast.makeText(requireContext(), "Data Entered", Toast.LENGTH_SHORT).show();
                                        /*try {
                                            Log.e("AAA", response.body().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }*/
                                        mDbHelper.createDatabase();
                                        mDbHelper.open();
                                        mDbHelper.setSync("DailyActivity", 1);
                                        mDbHelper.close();

                                        myListAdapter.list.add(0, new AdapterDataMain(actDate.getText().toString(), drpdownactAssignName.getSelectedItem().toString(), actHour.getText().toString(), actId.getText().toString(), 0, "Submitted", act_desc.getText().toString()));
                                        myListAdapter.notifyDataSetChanged();
                                        //FragmentManager fm = requireActivity().getSupportFragmentManager();
                                        //fm.popBackStack("AddDetailsActivity", 0);
                                        lottieAnimationView.playAnimation();
                                    } else if (response.errorBody() != null) {
                                        try {
                                            Log.e("onResponse:error", response.errorBody().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        lottieAnimationView.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                @EverythingIsNonNull
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.e("onFail", t.toString());
                                }
                            });
                        }
                        mDbHelper.close();
                        if (constFyAct != null)
                            constFyAct.setVisibility(View.VISIBLE);
                        nssHalvesLinear.setVisibility(View.VISIBLE);
                        //                add.setVisibility(View.VISIBLE);

                        actHeaderInput.setVisibility(View.GONE);
                        malHay.setVisibility(View.VISIBLE);
                        campActIn.setVisibility(View.GONE);
                    } else
                        Toast.makeText(requireContext(), "For date " + actDate.getText().toString() + " already added " + c + "hours cannot add more", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(requireContext(), "Device offline", Toast.LENGTH_SHORT).show();
                mdb.close();
            });
        }

        backActDetail.setOnClickListener(view14 -> {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(requireContext(), R.style.delDialog);
            builder2.setMessage("Exit without saving?");

            builder2.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

            builder2.setPositiveButton("Yes", (dialog, which) -> {
                dialog.cancel();
                if (constFyAct != null)
                    constFyAct.setVisibility(View.VISIBLE);
                nssHalvesLinear.setVisibility(View.VISIBLE);
                actHeaderInput.setVisibility(View.GONE);
                campActIn.setVisibility(View.GONE);
                malHay.setVisibility(View.VISIBLE);
            });
            builder2.show();
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (constFyAct != null)
            constFyAct.setVisibility(View.VISIBLE);
        nssHalvesLinear.setVisibility(View.VISIBLE);
        /*if (add2.getVisibility()==View.VISIBLE)
        add2.setVisibility(View.GONE);
        else
            add.setVisibility(View.GONE);*/

        campActIn.setVisibility(View.GONE);
        actHeaderInput.setVisibility(View.GONE);
        malHay.setVisibility(View.VISIBLE);
        //FragmentManager fm = requireActivity().getSupportFragmentManager();
    }

    public ArrayList<String> getAssignActList() {
        assert getArguments() != null;
        whichAct = getArguments().getInt("whichAct");
        Log.e("Hmmm", "" + whichAct);

        ArrayList<String> data3 = new ArrayList<>();

        DatabaseAdapter mDbHelper = new DatabaseAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        Cursor c3 = mDbHelper.getActAssigActNameOff(whichAct + "%");
        Log.e("actAssign:", "" + c3.getCount());

        //c2.moveToFirst();
        while (c3.moveToNext()) {
            data3.add(c3.getString(c3.getColumnIndex("ActivityName")));
        }
        mDbHelper.close();
        return data3;
    }

    public ArrayList<String> getAssignActListId() {
        assert getArguments() != null;
        whichAct = getArguments().getInt("whichAct");
        Log.e("AA", "getAssignActListId: " + whichAct);
        ArrayList<String> dataId = new ArrayList<>();

        DatabaseAdapter mDbHelper = new DatabaseAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();
        Cursor c2 = mDbHelper.getActAssigActNameOff(whichAct + "%");
        Log.e("actAssign:", "" + c2.getCount());

        //c2.moveToFirst();
        while (c2.moveToNext()) {
            dataId.add(c2.getString(c2.getColumnIndex("id")));
        }
        mDbHelper.close();
        return dataId;
    }
}