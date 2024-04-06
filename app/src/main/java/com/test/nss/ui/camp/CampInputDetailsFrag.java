package com.test.nss.ui.camp;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.test.nss.DatabaseAdapter;
import com.test.nss.Helper;
import com.test.nss.Password;
import com.test.nss.R;
import com.test.nss.api.RetrofitClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static com.test.nss.Helper.VEC;
import static com.test.nss.Helper.blackishy;


public class CampInputDetailsFrag extends Fragment {

    View root;
    TextView which_day;
    TextView campActName;
    EditText campDesc;
    Button submit;
    LinearLayout camp_main_details;
    String actName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_camp_input, container, false);
        assert getArguments() != null;

        which_day = root.findViewById(R.id.which_day);

        actName = getArguments().getString("actName");

        which_day.setTextColor(blackishy);
        which_day.setText(getArguments().getString("whichDay")); //one

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        submit = root.findViewById(R.id.submit_camp_detail);

        camp_main_details = requireActivity().findViewById(R.id.camp_main_details);

        campActName = root.findViewById(R.id.campActName);
        campDesc = root.findViewById(R.id.campDesc);

        campActName.setText(String.format("Activity: %s", actName)); //two
        //campDesc.getText().toString(); //3

        String day = which_day.getText().toString();

        DatabaseAdapter mDbHelper = new DatabaseAdapter(requireContext());
        mDbHelper.createDatabase();
        mDbHelper.open();

        Cursor c = mDbHelper.getCampId(actName);
        c.moveToFirst();
        String campId = c.getString(0);
        mDbHelper.close();
        submit.setOnClickListener(view1 -> {

            FragmentManager fm = requireActivity().getSupportFragmentManager();
            if (!isEmpty(campDesc) && (!isEmptyStr(which_day.getText().toString()))) {

                mDbHelper.createDatabase();
                mDbHelper.open();
                Cursor cursor = mDbHelper.getRegDetails(VEC);
                cursor.moveToFirst();

                mDbHelper.insertCampActListAllOff(
                        actName,
                        campDesc.getText().toString(),
                        day.substring(day.indexOf(" ") + 1),
                        0
                );
                mDbHelper.close();

                if (isNetworkAvailable()) {
                    Call<ResponseBody> sendCampDetails = RetrofitClient.getInstance().getApi().sendCampDetail(
                            "Token " + Helper.AUTH_TOKEN,
                            cursor.getString(cursor.getColumnIndex("College_name")),
                            campDesc.getText().toString(),
                            Integer.parseInt(day.substring(day.indexOf(" ") + 1)),
                            Helper.VEC,
                            "1",
                            Password.PASS,
                            campId);
                    sendCampDetails.enqueue(new Callback<ResponseBody>() {
                        @Override
                        @EverythingIsNonNull
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {

                            } else if (response.errorBody() != null) {
                                try {
                                    Log.e("CampInput", response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        @EverythingIsNonNull
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("onFailCampDetails", t.toString());
                        }
                    });
                    fm.popBackStack();
                    fm.popBackStack();
                    fm.popBackStack();
                    Toast.makeText(requireContext(), "Data Entered", Toast.LENGTH_SHORT).show();
                } else if (!isNetworkAvailable()) {
                    Toast.makeText(requireContext(), "Device offline", Toast.LENGTH_SHORT).show();
                    fm.popBackStack();
                    fm.popBackStack();
                    fm.popBackStack();
                    Toast.makeText(requireContext(), "Entered locally", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Enter something...", Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(requireContext(), "Enter details.", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean isEmpty(EditText e) {
        return e.getText().toString().trim().length() <= 0;
    }

    private boolean isEmptyStr(String e) {
        return e.trim().length() <= 0;
    }
}