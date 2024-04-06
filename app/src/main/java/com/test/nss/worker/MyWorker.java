package com.test.nss.worker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.test.nss.CheckConn;
import com.test.nss.DataBaseHelper;
import com.test.nss.DatabaseAdapter;
import com.test.nss.R;
import com.test.nss.api.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.test.nss.Helper.AUTH_TOKEN;

public class MyWorker extends Worker {
    private static final String uniqueWorkName = "com.test.nss.worker";
    private static final long repeatIntervalMin = 20;
    private static final long flexIntervalMin = 5;
    private static final String NOTIFY_ID = "nssNotif";

    private NotificationManager notificationManager;
    private Context context;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    public static void enqueueSelf() {
        WorkManager.getInstance().enqueueUniquePeriodicWork(uniqueWorkName, ExistingPeriodicWorkPolicy.KEEP, getOwnWorkRequest());
    }

    private static PeriodicWorkRequest getOwnWorkRequest() {
        Log.e("MyWorker", "working");
        return new PeriodicWorkRequest.Builder(
                MyWorker.class, repeatIntervalMin, TimeUnit.MINUTES, flexIntervalMin, TimeUnit.MINUTES)
                .build();
    }

    @NonNull
    public Result doWork() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        Log.e("MyWorker", "working2");
        DatabaseAdapter mdb = new DatabaseAdapter(context);
        mdb.createDatabase();
        mdb.open();
        Call<ResponseBody> insertAct = RetrofitClient.getInstance().getApi().getDailyAct("Token " + AUTH_TOKEN);
        Response<ResponseBody> responseAct = null;

        try {
            responseAct = insertAct.execute();
            if (responseAct.errorBody() != null) {
                Log.e("Error", "doWork: " + responseAct.errorBody().string());
            } else if (responseAct.isSuccessful() && responseAct.body() != null) {
                try {
                    JSONArray j = new JSONArray(responseAct.body().string());

                    if (j.length() > 0) {
                        deleteData("DailyActivityTemp");
                        Log.e("DailyActivityTemp", "doWork: ");
                        for (int i = 0; i < j.length(); i++) {
                            mdb.insertActAgain(
                                    j.getJSONObject(i).getString("id"),
                                    j.getJSONObject(i).getString("AssignedActivityName"),
                                    j.getJSONObject(i).getString("State")
                            );
                        }
                    }
                } catch (JSONException | IOException e) {
                    Log.e("Failed", e.toString());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mdb.close();

        if (insertAct.isExecuted()) {
            mdb.createDatabase();
            mdb.open();
            Cursor c = mdb.getUpdatedAct();

            if (c.getCount() > 0) {
                c.moveToFirst();
                if (c.getCount() < 2) {
                    String s;
                    Cursor m = mdb.getActLeaderId(c.getInt(c.getColumnIndex("actID")));
                    Log.e("AAA", m.getCount() + " " + mdb.getActLeaderId(c.getInt(c.getColumnIndex("actID"))) + " " + c.getString(c.getColumnIndex("actID")));
                    m.moveToFirst();
                    if (m.getCount() > 0) {
                        String abbBy = m.getString(m.getColumnIndex("Approved_by"));
                        if (abbBy.equals("null")) {
                            s = "PO";
                        } else
                            s = mdb.getLeaderName(Integer.parseInt(abbBy));

                        notify(c.getString(c.getColumnIndex("State")) + ": " + c.getString(c.getColumnIndex("ActivityName")) + " By " + s);
                    }
                } else {
                    notify("Approved: " + c.getCount() + " activities");
                }
            }
            CheckConn checkConn = new CheckConn();
            checkConn.syncDailyAct();
            mdb.close();
        }

        Call<ResponseBody> helpData = RetrofitClient.getInstance().getApi().getPoData("Token " + AUTH_TOKEN);
        Response<ResponseBody> responseHelp = null;
        try {
            responseHelp = helpData.execute();
            if (responseAct.errorBody() != null) {
                Log.e("Error", "doWork: " + responseAct.errorBody().string());
            } else if (responseHelp.isSuccessful() && responseHelp.body() != null) {
                try {
                    JSONArray j = new JSONArray(responseHelp.body().string());

                    if (j.length() > 0) {
                        DatabaseAdapter mDbHelper = new DatabaseAdapter(context);
                        mDbHelper.createDatabase();
                        mDbHelper.open();
                        deleteData("Help");
                        for (int i = 0; i < j.length(); i++) {
                            mDbHelper.insertHelpData(
                                    j.getJSONObject(i).getString("CollegeName"),
                                    "Po",
                                    j.getJSONObject(i).getString("PoName"),
                                    j.getJSONObject(i).getString("PoEmail"),
                                    j.getJSONObject(i).getString("PoContact"),
                                    j.getJSONObject(i).getString("PoStartYear"));
                        }
                        mDbHelper.close();
                    }
                } catch (JSONException | IOException e) {
                    Log.e("Failed", e.toString());
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mdb.close();
        Log.e("AAA", (responseAct != null && responseAct.isSuccessful()) + "" + (responseHelp != null && responseHelp.isSuccessful()));
        //Log.e("AAA", responseAct.isSuccessful() + "" + insertAct.isExecuted());

        return helpData.isExecuted() && insertAct.isExecuted() ? Result.success() : Result.failure();
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

    private void notify(String content) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, NOTIFY_ID);

        mBuilder.setSmallIcon(R.drawable.ic_nss_200);
        mBuilder.setContentTitle(context.getString(R.string.congrats));
        mBuilder.setContentText(content);
        mBuilder.setPriority(Notification.PRIORITY_DEFAULT);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFY_ID,
                    context.getString(R.string.congrats),
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(NOTIFY_ID);
        }

        mNotificationManager.notify(0, mBuilder.build());

    }
}
