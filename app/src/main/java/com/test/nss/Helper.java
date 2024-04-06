package com.test.nss;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.util.Log;

import com.test.nss.api.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static android.content.Context.MODE_PRIVATE;

public class Helper {
    public static int primaryColDark;
    public static int blackishy;
    public static int transparent;
    public static int primaryCol;
    public static int primaryColLight;
    public static int red;
    public static int green;
    public static int kesar;
    public static int sbColorText;
    public static int black;
    public static int white;
    public static int blackGrey;
    public static String AUTH_TOKEN;
    public static String VEC;
    public static int isLeader;
    public static int leaderId;
    public static String name;
    public static boolean isFirst;
    public static boolean isSec;
    public static boolean isNight;

    public static int color1 = ((int) (Math.random() * 16317215)) | (0xFF << 24);
    public static int color2 = ((int) (Math.random() * 16777115)) | (0xFF << 24);
    public static int color3 = ((int) (Math.random() * 16779815)) | (0xFF << 24);
    public static int color4 = ((int) (Math.random() * 16747215)) | (0xFF << 24);
    public static int color5 = ((int) (Math.random() * 14317215)) | (0xFF << 24);
    public static int color6 = ((int) (Math.random() * 14777115)) | (0xFF << 24);
    public static int color7 = ((int) (Math.random() * 15779815)) | (0xFF << 24);
    public static int color8 = ((int) (Math.random() * 13747215)) | (0xFF << 24);

    public static float zerof = 0f;
    public static int waveDur = 2000;
    Context context;

    @SuppressLint("Range")
    public Helper(Context context) {
        this.context = context;

        blackishy = context.getColor(R.color.blackish);
        transparent = context.getColor(R.color.transparent);
        primaryCol = context.getColor(R.color.colorPrimary);
        primaryColLight = context.getColor(R.color.colorPrimaryLight);
        primaryColDark = context.getColor(R.color.colorPrimaryDark);
        red = context.getColor(R.color.red);
        green = context.getColor(R.color.greenNic);
        kesar = context.getColor(R.color.kesar);
        sbColorText = context.getColor(R.color.sbColorText);
        black = context.getColor(R.color.black);
        white = context.getColor(R.color.white);
        blackGrey = context.getColor(R.color.blackGrey);
        isNight = (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) != Configuration.UI_MODE_NIGHT_NO;

        SharedPreferences sharedPreferences = context.getSharedPreferences("KEY", MODE_PRIVATE);

        AUTH_TOKEN = sharedPreferences.getString("AUTH_TOKEN", "");
        VEC = sharedPreferences.getString("VEC", "");

        if (!sharedPreferences.getBoolean("isAboutDone", true)) {
            Call<ResponseBody> call2 = RetrofitClient.getInstance().getApi().isLeader("Token " + AUTH_TOKEN);
            call2.enqueue(new Callback<ResponseBody>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    SharedPreferences.Editor eddy = sharedPreferences.edit();
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            JSONArray j2 = new JSONArray(response.body().string());
                            if (j2.length() > 0) {
                                isLeader = 1;
                                eddy.putInt("isLeader", 1);
                                eddy.putInt("leaderId", j2.getJSONObject(0).getInt("id"));
                                isLeader = 1;
                                leaderId = j2.getJSONObject(0).getInt("id");
                            } else {
                                eddy.putInt("isLeader", 0);
                                isLeader = 0;
                            }
                            eddy.apply();

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
            Log.e("AAA", leaderId + "" + isLeader);
        } else {
            isLeader = sharedPreferences.getInt("isLeader", 0);
            leaderId = sharedPreferences.getInt("leaderId", -1);
            Log.e("AAA", leaderId + "" + isLeader);
        }

        DatabaseAdapter mdb = new DatabaseAdapter(context);
        mdb.createDatabase();
        mdb.open();
        Cursor c = mdb.getRegDetails(VEC);
        c.moveToFirst();
        name = c.getString(c.getColumnIndex("First_name")) + " " + c.getString(c.getColumnIndex("Last_name"));
        isFirst = c.getString(c.getColumnIndex("State")).equals("First Year");
        isSec = c.getString(c.getColumnIndex("State")).equals("Second Year");
        mdb.close();
    }

    @SuppressLint("Range")
    public void add() {
        Cursor c;
        Call<ResponseBody> insertHours;
        DatabaseAdapter mdb = new DatabaseAdapter(context);
        mdb.createDatabase();
        mdb.open();

        if (isFirst) {
            c = mdb.getAllDetHours(1);
            c.moveToFirst();
            if (c.getCount() > 0) {
                for (int i = 0; i < c.getCount(); i++) {
                    //Log.e("AAA " + c.getString(c.getColumnIndex("id")), "" + c.getString(c.getColumnIndex("actCode")) + " " + c.getString(c.getColumnIndex("NatureOfWork")) + " " + c.getString(c.getColumnIndex("HoursWorked")));
                    insertHours = RetrofitClient.getInstance().getApi().insertHour(
                            "Token " + AUTH_TOKEN,
                            c.getInt(c.getColumnIndex("HoursWorked")),
                            c.getInt(c.getColumnIndex("RemainingHours")),
                            VEC,
                            c.getInt(c.getColumnIndex("actCode")),
                            c.getString(c.getColumnIndex("NatureOfWork")),
                            Password.PASS,
                            c.getInt(c.getColumnIndex("id"))
                    );
                    insertHours.enqueue(new Callback<ResponseBody>() {
                        @Override
                        @EverythingIsNonNull
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                    c.moveToNext();
                }
            }
            c.close();
        } else if (isSec) {
            c = mdb.getAllDetHours(2);
            c.moveToFirst();
            if (c.getCount() > 0) {
                for (int i = 0; i < c.getCount(); i++) {
                    //Log.e("AAA " + c.getString(c.getColumnIndex("id")), "" + c.getString(c.getColumnIndex("actCode")) + " " + c.getString(c.getColumnIndex("NatureOfWork")) + " " + c.getString(c.getColumnIndex("HoursWorked")));

                    insertHours = RetrofitClient.getInstance().getApi().insertHour(
                            "Token " + AUTH_TOKEN,
                            c.getInt(c.getColumnIndex("HoursWorked")),
                            c.getInt(c.getColumnIndex("RemainingHours")),
                            VEC,
                            c.getInt(c.getColumnIndex("actCode")),
                            c.getString(c.getColumnIndex("NatureOfWork")),
                            Password.PASS,
                            c.getInt(c.getColumnIndex("id"))
                    );

                    insertHours.enqueue(new Callback<ResponseBody>() {
                        @Override
                        @EverythingIsNonNull
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                    c.moveToNext();
                }
            }
            c.close();
        }
        mdb.close();
    }

    public String getKey(Integer value) {
        Map<String, Integer> map = new HashMap<>();

        map.put("First Year College", 11);
        map.put("First Year Area Based One", 121);
        map.put("First Year Area Based Two", 122);
        map.put("First Year University", 13);
        map.put("Second Year College", 21);
        map.put("Second Year Area Based One", 221);
        map.put("Second Year Area Based Two", 222);
        map.put("Second Year University", 23);

        for (String key : map.keySet()) {
            if (value.equals(map.get(key))) {
                return key;
            }
        }
        return null;
    }
}