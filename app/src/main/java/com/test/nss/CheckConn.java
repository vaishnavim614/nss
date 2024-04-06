package com.test.nss;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.test.nss.api.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static com.test.nss.Helper.AUTH_TOKEN;
import static com.test.nss.Helper.VEC;

public class CheckConn extends BroadcastReceiver {

    static boolean isSyncAct = false;
    static boolean isSyncCamp = false;
    Context context;

    public static <V> Integer getKey(Map<Integer, String> map, String value) {
        for (Integer key : map.keySet()) {
            if (value.equals(map.get(key))) {
                return key;
            }
        }
        return null;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("CheckConn", "onReceive: ");
        this.context = context;
        NetworkInfo.State wifiState;
        NetworkInfo.State mobileState;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiState = Objects.requireNonNull(cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState();
        mobileState = Objects.requireNonNull(cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState();

        boolean a = NetworkInfo.State.CONNECTED == wifiState;
        boolean b = NetworkInfo.State.CONNECTED == mobileState;

        if ((a || b) && isNetworkAvailable()) {
            Log.e("CheckConn", "Internet");
            updateDb(context);
        } else if (wifiState != null && mobileState != null) {
            Log.e("CheckConn", "No net");
        }
    }

    public void updateDb(Context context) {
        Call<ResponseBody> campList = RetrofitClient.getInstance().getApi().getCampList("Token " + AUTH_TOKEN);
        campList.enqueue(new Callback<ResponseBody>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONArray j = new JSONArray(response.body().string());

                        if (j.length() >= 0) {
                            DatabaseAdapter mDbHelper = new DatabaseAdapter(context);
                            mDbHelper.createDatabase();
                            mDbHelper.open();
                            deleteData("CampActivityList");
                            for (int i = 0; i < j.length(); i++) {
                                mDbHelper.insertCampActList(
                                        j.getJSONObject(i).getString("CampActivityName"),
                                        j.getJSONObject(i).getString("id")
                                );
                            }
                            mDbHelper.close();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Failed:campList", t.toString());
            }
        });

        Call<ResponseBody> clg = RetrofitClient.getInstance().getApi().getClgList();
        clg.enqueue(new Callback<ResponseBody>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        //JSONObject j = new JSONObject(response.body().string());
                        JSONArray j = new JSONArray(response.body().string());
                        if (j.length() >= 0) {
                            DatabaseAdapter mDbHelper = new DatabaseAdapter(context);
                            mDbHelper.createDatabase();
                            mDbHelper.open();
                            deleteData("CollegeNames");
                            for (int i = 0; i < j.length(); i++) {
                                mDbHelper.insertClgList(
                                        j.getJSONObject(i).getString("CollegeCode"),
                                        j.getJSONObject(i).getString("CollegeName"),
                                        j.getJSONObject(i).getString("State")
                                );
                            }
                            //mDbHelper.getCampDetails();
                            mDbHelper.close();
                        }
                        //ArrayAdapter<String> adapter = new ArrayAdapter<>(SignupActivity.this, R.layout.drop_down_start, clgList);
                        //dropdownClg.setAdapter(adapter);
                        Log.e("Added college", "");
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


        Call<ResponseBody> campDetails = RetrofitClient.getInstance().getApi().getCampDetails("Token " + AUTH_TOKEN);
        campDetails.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONArray j = new JSONArray(response.body().string());
                        //Log.e("insert", j.getJSONObject(0).getString("CollegeName"));

                        if (j.length() >= 0) {
                            DatabaseAdapter mDbHelper = new DatabaseAdapter(context);
                            mDbHelper.createDatabase();
                            mDbHelper.open();
                            deleteData("CampDetails");// Scan for sync state
                            for (int i = 0; i < j.length(); i++) {
                                mDbHelper.insertCampDetails(
                                        j.getJSONObject(i).getString("CollegeName"),
                                        j.getJSONObject(i).getString("CampFrom"),
                                        j.getJSONObject(i).getString("CampTo"),
                                        j.getJSONObject(i).getString("CampVenue"),
                                        j.getJSONObject(i).getString("CampPost"),
                                        j.getJSONObject(i).getString("CampTaluka"),
                                        j.getJSONObject(i).getString("CampDistrict")
                                );
                            }
                            //mDbHelper.getCampDetails();
                            mDbHelper.close();
                        }
                    } catch (JSONException | IOException e) {
                        Log.e("Failed", e.toString());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e("onFailure:campDetails", t.toString());
            }
        });

        DatabaseAdapter mDbHelper = new DatabaseAdapter(context);
        mDbHelper.createDatabase();
        mDbHelper.open();

        //Cursor c2 = mDbHelper.getActId("");
        //c.moveToFirst();
        Map<Integer, String> actIdHash = new HashMap<>();
        actIdHash.put(11, "First Year College");
        actIdHash.put(121, "First Year Area Based One");
        actIdHash.put(122, "First Year Area Based Two");
        actIdHash.put(13, "First Year University");
        actIdHash.put(21, "Second Year College");
        actIdHash.put(221, "Second Year Area Based One");
        actIdHash.put(222, "Second Year Area Based Two");
        actIdHash.put(23, "Second Year University");

        Cursor c = mDbHelper.getActListOff();
        //Log.e("AAAA", ""+c.getCount());
        //c.moveToFirst();
        if (c.getCount() > 0) {
            Log.wtf("AAAAA", "Entering");

            c.moveToFirst();

            int count = c.getCount();
            while (count > 0) {
                Cursor c2 = mDbHelper.getActAssigActNameAdmin(c.getString(c.getColumnIndex("ActivityName")));
                c2.moveToFirst();

                Call<ResponseBody> insertActOff = RetrofitClient.getInstance().getApi().sendActList(
                        "Token " + AUTH_TOKEN,
                        c.getString(c.getColumnIndex("VEC")),
                        c2.getInt(c2.getColumnIndex("id")),
                        //c.getInt(c.getColumnIndex("ActivityCode")),
                        c.getInt(c.getColumnIndex("HoursWorked")),
                        c.getString(c.getColumnIndex("Date")),
                        getKey(actIdHash, c.getString(c.getColumnIndex("ActivityCode"))),
                        Password.PASS,
                        c.getString(c.getColumnIndex("Descr")),
                        1
                );

                insertActOff.enqueue(new Callback<ResponseBody>() {
                    @Override
                    @EverythingIsNonNull
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            isSyncAct = true;
                        } else if (response.errorBody() != null) {
                            try {
                                Log.e("Error", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("error", e.toString());
                            }
                            isSyncAct = false;
                        } else {
                            isSyncAct = false;
                        }
                    }

                    @Override
                    @EverythingIsNonNull
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("ERROR", t.toString());
                    }
                });
                c.moveToNext();
                //c2.moveToNext();*/
                count = count - 1;
            }
        }
        if (isSyncAct) {
            mDbHelper.setSync("DailyActivity", 1);
            Toast.makeText(context, "Database Synced", Toast.LENGTH_SHORT).show();
        }
        mDbHelper.close();

        syncDailyAct();

        mDbHelper.createDatabase();
        mDbHelper.open();
        Cursor c2 = mDbHelper.getCampActListOff(0);
        c2.moveToFirst();
        Cursor c4 = mDbHelper.getRegDetails(VEC);
        c4.moveToFirst();

        if (c2.getCount() > 0) {

            int cu = c2.getCount();
            c2.moveToFirst();
            while (cu > 0) {
                Cursor c3 = mDbHelper.getCampId(c2.getString(c2.getColumnIndex("CampActivityTitle")));
                c3.moveToFirst();
                Call<ResponseBody> sendCampDetails = RetrofitClient.getInstance().getApi().sendCampDetail(
                        "Token " + AUTH_TOKEN,
                        c4.getString(c4.getColumnIndex("College_name")),
                        c2.getString(c2.getColumnIndex("CampActivityDescription")),
                        c2.getInt(c2.getColumnIndex("CampDay")),
                        VEC,
                        "1",
                        Password.PASS,
                        c3.getString(c3.getColumnIndex("CampId"))
                );
                sendCampDetails.enqueue(new Callback<ResponseBody>() {
                    @Override
                    @EverythingIsNonNull
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            isSyncCamp = true;
                        } else if (response.errorBody() != null) {
                            try {
                                Log.e("error:", response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            isSyncCamp = false;
                        } else
                            isSyncCamp = false;
                    }

                    @Override
                    @EverythingIsNonNull
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
                c2.moveToNext();
                cu = cu - 1;
            }
        }
        if (isSyncCamp)
            mDbHelper.setSync("CampActivities", 1);
        mDbHelper.close();

        Call<ResponseBody> campListAll = RetrofitClient.getInstance().getApi().getCampActListAll("Token " + AUTH_TOKEN);
        campListAll.enqueue(new Callback<ResponseBody>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONArray j = new JSONArray(response.body().string());

                        if (j.length() >= 0) {
                            DatabaseAdapter mDbHelper = new DatabaseAdapter(context);
                            mDbHelper.createDatabase();
                            mDbHelper.open();
                            deleteData("CampActivities");
                            for (int i = 0; i < j.length(); i++) {
                                mDbHelper.insertCampActListAll(
                                        j.getJSONObject(i).getInt("id"),
                                        j.getJSONObject(i).getString("CollegeName"),
                                        j.getJSONObject(i).getString("State"),
                                        j.getJSONObject(i).getString("CampActivityTitle"),
                                        j.getJSONObject(i).getString("CampActivityDescription"),
                                        j.getJSONObject(i).getString("Day"),
                                        VEC,
                                        1
                                );
                            }
                            mDbHelper.close();
                        }
                    } catch (JSONException | IOException e) {
                        Log.e("Failed", e.toString());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("onFailure:campListAll", t.toString());
            }
        });

        Call<ResponseBody> getHours = RetrofitClient.getInstance().getApi().getHours("Token " + AUTH_TOKEN);
        getHours.enqueue(new Callback<ResponseBody>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONArray j = new JSONArray(response.body().string());

                        if (j.length() > 0) {
                            DatabaseAdapter mDbHelper = new DatabaseAdapter(context);
                            mDbHelper.createDatabase();
                            mDbHelper.open();
                            deleteData("HoursList");
                            for (int i = 0; i < j.length(); i++) {
                                //Log.e("AA", "AAA");
                                mDbHelper.insertHours(
                                        j.getJSONObject(i).getString("Level"),
                                        j.getJSONObject(i).getInt("TotalHours")
                                );
                            }
                            mDbHelper.close();
                        }
                    } catch (JSONException | IOException e) {
                        Log.e("Failed", e.toString());
                        e.printStackTrace();
                    }
                } else if (response.errorBody() != null) {
                    try {
                        Log.e("Error:getHours", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        Call<ResponseBody> actList = RetrofitClient.getInstance().getApi().getActList("Token " + AUTH_TOKEN);
        actList.enqueue(new Callback<ResponseBody>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONArray j = new JSONArray(response.body().string());

                        if (j.length() >= 0) {
                            DatabaseAdapter mDbHelper = new DatabaseAdapter(context);
                            mDbHelper.createDatabase();
                            mDbHelper.open();
                            deleteData("ActivityListByAdmin");
                            for (int i = 0; i < j.length(); i++) {
                                mDbHelper.insertActAdmin(
                                        j.getJSONObject(i).getInt("id"),
                                        j.getJSONObject(i).getString("CollegeName"),
                                        j.getJSONObject(i).getString("ActivityName"),
                                        j.getJSONObject(i).getString("AssignedActivityName"),
                                        j.getJSONObject(i).getString("AssignedHours"),
                                        j.getJSONObject(i).getString("AssignedDate"),
                                        j.getJSONObject(i).getString("EndDate")
                                );
                            }
                            mDbHelper.close();
                        }
                    } catch (JSONException | IOException e) {
                        Log.e("Failed", e.toString());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("onFailure:campListAll", t.toString());
            }
        });

        Call<ResponseBody> insertLeaders = RetrofitClient.getInstance().getApi().insertLeaders("Token " + AUTH_TOKEN);
        insertLeaders.enqueue(new Callback<ResponseBody>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        //Log.e("Entered", "leaders");
                        JSONArray j = new JSONArray(response.body().string());
                        if (j.length() > 0) {
                            DatabaseAdapter mDbHelper = new DatabaseAdapter(context);
                            mDbHelper.createDatabase();
                            mDbHelper.open();
                            deleteData("Leaders");
                            for (int i = 0; i < j.length(); i++) {
                                mDbHelper.insertLeaders(
                                        j.getJSONObject(i).getString("Number"),
                                        j.getJSONObject(i).getString("Name "),
                                        j.getJSONObject(i).getString("Email"),
                                        j.getJSONObject(i).getString("VEC"),
                                        j.getJSONObject(i).getString("CollegeName"),
                                        j.getJSONObject(i).getString("id"),
                                        j.getJSONObject(i).getString("State")
                                );
                            }
                            mDbHelper.close();
                        }
                    } catch (JSONException | IOException e) {
                        Log.e("Failed", e.toString());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        leaderRef();
        Call<ResponseBody> getFyAct = RetrofitClient.getInstance().getApi().getFyAct("Token " + AUTH_TOKEN);
        getFyAct.enqueue(new Callback<ResponseBody>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONArray j = new JSONArray(response.body().string());
                        if (j.length() > 0) {
                            DatabaseAdapter mdb = new DatabaseAdapter(context);
                            deleteData("AreaData");
                            mdb.createDatabase();
                            mdb.open();
                            if (j.length() == 1) {
                                mdb.insertArea(
                                        j.getJSONObject(0).getInt("id"),
                                        j.getJSONObject(0).getString("ActivityName"),
                                        j.getJSONObject(0).getString("ABProjectName"),
                                        j.getJSONObject(0).getString("CollegeName"),
                                        1
                                );
                            } else {
                                for (int i = 0; i < j.length(); i++) {
                                    mdb.insertArea(
                                            j.getJSONObject(i).getInt("id"),
                                            j.getJSONObject(i).getString("ActivityName"),
                                            j.getJSONObject(i).getString("ABProjectName"),
                                            j.getJSONObject(i).getString("CollegeName"),
                                            1
                                    );
                                }
                            }
                            mdb.close();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        Call<ResponseBody> getSyAct = RetrofitClient.getInstance().getApi().getSyAct("Token " + AUTH_TOKEN);
        getSyAct.enqueue(new Callback<ResponseBody>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONArray j = new JSONArray(response.body().string());
                        if (j.length() > 0) {
                            deleteData("AreaDataPrev");
                            DatabaseAdapter mdb = new DatabaseAdapter(context);
                            mdb.createDatabase();
                            mdb.open();
                            if (j.length() == 1) {
                                mdb.insertArea(
                                        j.getJSONObject(0).getInt("id"),
                                        j.getJSONObject(0).getString("ActivityName"),
                                        j.getJSONObject(0).getString("ABProjectName"),
                                        j.getJSONObject(0).getString("CollegeName"),
                                        2
                                );
                            } else {
                                for (int i = 0; i < j.length(); i++) {
                                    mdb.insertArea(
                                            j.getJSONObject(i).getInt("id"),
                                            j.getJSONObject(i).getString("ActivityName"),
                                            j.getJSONObject(i).getString("ABProjectName"),
                                            j.getJSONObject(i).getString("CollegeName"),
                                            2
                                    );
                                }
                            }
                            mdb.close();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        Call<ResponseBody> pushHoursId = RetrofitClient.getInstance().getApi().getHoursId("Token " + AUTH_TOKEN);
        pushHoursId.enqueue(new Callback<ResponseBody>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONArray j = new JSONArray(response.body().string());
                        DatabaseAdapter mdb = new DatabaseAdapter(context);
                        mdb.createDatabase();
                        mdb.open();
                        //mdb.setActState();
                        if (j.length() > 0) {
                            int c;
                            deleteData("WorkHoursFy");
                            deleteData("WorkHoursSy");

                            for (int i = 0; i < j.length(); i++) {
                                String z = j.getJSONObject(i).getString("ActivityName");
                                String y = j.getJSONObject(i).getString("Level");
                                c = mdb.getHours(y);
                                if (z.startsWith("1")) {
                                    switch (y) {
                                        case "Area Based Level One":
                                            mdb.insertWorkId(j.getJSONObject(i).getInt("id"), y,
                                                    c,
                                                    j.getJSONObject(i).getInt("CompletedHours"),
                                                    j.getJSONObject(i).getInt("RemainingHours"), 121, 1);
                                            break;
                                        case "Area Based Level Two":
                                            mdb.insertWorkId(j.getJSONObject(i).getInt("id"), y,
                                                    c,
                                                    j.getJSONObject(i).getInt("CompletedHours"),
                                                    j.getJSONObject(i).getInt("RemainingHours"), 122, 1);
                                            break;
                                        case "College Level":
                                            mdb.insertWorkId(j.getJSONObject(i).getInt("id"), y,
                                                    c,
                                                    j.getJSONObject(i).getInt("CompletedHours"),
                                                    j.getJSONObject(i).getInt("RemainingHours"), 11, 1);
                                            break;
                                        case "University Level":
                                            mdb.insertWorkId(j.getJSONObject(i).getInt("id"), y,
                                                    c,
                                                    j.getJSONObject(i).getInt("CompletedHours"),
                                                    j.getJSONObject(i).getInt("RemainingHours"), 13, 1);
                                            break;
                                    }
                                } else if (z.startsWith("2"))
                                    switch (y) {
                                        case "Area Based Level One":
                                            mdb.insertWorkId(j.getJSONObject(i).getInt("id"), y,
                                                    c,
                                                    j.getJSONObject(i).getInt("CompletedHours"),
                                                    j.getJSONObject(i).getInt("RemainingHours"), 221, 2);
                                            break;
                                        case "Area Based Level Two":
                                            mdb.insertWorkId(j.getJSONObject(i).getInt("id"), y,
                                                    c,
                                                    j.getJSONObject(i).getInt("CompletedHours"),
                                                    j.getJSONObject(i).getInt("RemainingHours"), 222, 2);
                                            break;
                                        case "College Level":
                                            mdb.insertWorkId(j.getJSONObject(i).getInt("id"), y,
                                                    c,
                                                    j.getJSONObject(i).getInt("CompletedHours"),
                                                    j.getJSONObject(i).getInt("RemainingHours"), 21, 2);
                                            break;
                                        case "University Level":
                                            mdb.insertWorkId(j.getJSONObject(i).getInt("id"), y,
                                                    c,
                                                    j.getJSONObject(i).getInt("CompletedHours"),
                                                    j.getJSONObject(i).getInt("RemainingHours"), 23, 2);
                                            break;
                                        default:
                                            break;
                                    }
                            }
                        }
                        mdb.close();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
                DatabaseAdapter mdb = new DatabaseAdapter(context);
                mdb.createDatabase();
                mdb.open();
                mdb.setActState();
                mdb.close();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                DatabaseAdapter mdb = new DatabaseAdapter(context);
                mdb.createDatabase();
                mdb.open();
                mdb.setActState();
                mdb.close();
            }
        });
    }

    public void syncDailyAct() {
        Call<ResponseBody> insertAct = RetrofitClient.getInstance().getApi().getDailyAct("Token " + AUTH_TOKEN);
        insertAct.enqueue(new Callback<ResponseBody>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONArray j = new JSONArray(response.body().string());

                        if (j.length() > 0) {
                            DatabaseAdapter mDbHelper = new DatabaseAdapter(context);
                            mDbHelper.createDatabase();
                            mDbHelper.open();
                            deleteData("DailyActivity");
                            for (int i = 0; i < j.length(); i++) {
                                mDbHelper.insertAct(
                                        j.getJSONObject(i).getString("VEC"),
                                        j.getJSONObject(i).getString("ActivityName"),
                                        j.getJSONObject(i).getString("id"),
                                        j.getJSONObject(i).getString("Date"),
                                        j.getJSONObject(i).getString("AssignedActivityName"),
                                        j.getJSONObject(i).getString("Hours"),
                                        j.getJSONObject(i).getString("State"),
                                        j.getJSONObject(i).getString("ApprovedBy"),
                                        j.getJSONObject(i).getString("ActDescription"),
                                        1
                                );
                            }
                            mDbHelper.setApproved("DailyActivity");
                            mDbHelper.close();
                        }
                    } catch (JSONException | IOException e) {
                        Log.e("Failed", e.toString());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    /*private int date(String start_date, String end_date) {
        {
            // SimpleDateFormat converts the
            // string format to date object
            SimpleDateFormat sdf
                    = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

            // Try Class
            try {

                Date d1 = sdf.parse(start_date);
                Date d2 = sdf.parse(end_date);

                long difference_In_Time
                        = 0;
                if (d2 != null && d1 != null) {
                    difference_In_Time = d2.getTime() - d1.getTime();
                }

                long difference_In_Days
                        = TimeUnit
                        .MILLISECONDS
                        .toDays(difference_In_Time)
                        % 365;

                long difference_In_Years
                        = TimeUnit
                        .MILLISECONDS
                        .toDays(difference_In_Time)
                        / 365L;


                Log.e("AAA", "date: " + difference_In_Days);
                // Print result
                System.out.println(
                        difference_In_Years
                                + " years, "
                                + difference_In_Days
                                + " days, ");

            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }*/

    public void leaderRef() {
        Call<ResponseBody> volAct = RetrofitClient.getInstance().getApi().volAct("Token " + AUTH_TOKEN);
        volAct.enqueue(new Callback<ResponseBody>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONArray j = new JSONArray(response.body().string());

                        if (j.length() >= 0) {
                            Log.e("VolAct", "onResponse: " + j.length());
                            DatabaseAdapter mDbHelper = new DatabaseAdapter(context);
                            mDbHelper.createDatabase();
                            mDbHelper.open();
                            deleteData("VolAct");

                            for (int i = 0; i < j.length(); i++) {
                                mDbHelper.insertVolAct(
                                        j.getJSONObject(i).getInt("id"),
                                        j.getJSONObject(i).getString("Name"),
                                        j.getJSONObject(i).getString("Date"),
                                        j.getJSONObject(i).getInt("Hours"),
                                        j.getJSONObject(i).getString("VEC"),
                                        j.getJSONObject(i).getString("ActivityName"),
                                        j.getJSONObject(i).getString("AssignedActivityName"),
                                        j.getJSONObject(i).getString("AssignedActivityCode"),
                                        j.getJSONObject(i).getString("ActivityNameCode"),
                                        j.getJSONObject(i).getString("State")
                                );
                            }
                            mDbHelper.setApproved("VolAct");
                            mDbHelper.close();
                        }
                    } catch (JSONException | IOException e) {
                        Log.e("Failed", e.toString());
                        e.printStackTrace();
                    }
                }
                if (response.errorBody() != null) {
                    Log.e("error", "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

        Call<ResponseBody> volActAll = RetrofitClient.getInstance().getApi().volActAll("Token " + AUTH_TOKEN);
        volActAll.enqueue(new Callback<ResponseBody>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONArray j = new JSONArray(response.body().string());

                        if (j.length() >= 0) {
                            //Log.e("AAAA", "onResponse: " + j.length());
                            DatabaseAdapter mDbHelper = new DatabaseAdapter(context);
                            mDbHelper.createDatabase();
                            mDbHelper.open();
                            deleteData("VolActAll");

                            for (int i = 0; i < j.length(); i++) {
                                mDbHelper.insertVolAllAct(
                                        i + 1,
                                        j.getJSONObject(i).getString("FirstName") + " " + j.getJSONObject(i).getString("LastName"),
                                        j.getJSONObject(i).getString("DateOfRegistration"),
                                        j.getJSONObject(i).getString("VEC")
                                );
                            }
                            mDbHelper.close();
                        }
                    } catch (JSONException | IOException e) {
                        Log.e("Failed", e.toString());
                        e.printStackTrace();
                    }
                }
                if (response.errorBody() != null) {
                    Log.e("error", "onResponse: " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void deleteData(String table) {
        DatabaseAdapter mDbHelper2 = new DatabaseAdapter(context);
        mDbHelper2.createDatabase();
        mDbHelper2.open();
        DataBaseHelper mDb2 = new DataBaseHelper(context);
        SQLiteDatabase m = mDb2.getWritableDatabase();
        m.execSQL("DELETE FROM " + table);
        mDbHelper2.close();
        m.close();
        mDb2.close();
    }
}