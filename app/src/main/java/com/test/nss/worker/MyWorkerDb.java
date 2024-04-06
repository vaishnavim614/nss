package com.test.nss.worker;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.test.nss.CheckConn;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class MyWorkerDb extends Worker {
    private static final String uniqueWorkNameDb = "com.test.nss.workerDb";
    private static final int hourOfTheDay = 4; //AM
    private static final int repeatIntervalDay = 1;
    static long flex = calculateFlex();
    private Context context;

    public MyWorkerDb(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    public static void enqueueSelf() {
        Log.e("MyWorkerDb", "working");
        WorkManager.getInstance().enqueueUniquePeriodicWork(uniqueWorkNameDb, ExistingPeriodicWorkPolicy.KEEP, getOwnWorkRequest());
    }

    private static PeriodicWorkRequest getOwnWorkRequest() {
        return new PeriodicWorkRequest.Builder(
                MyWorkerDb.class,
                repeatIntervalDay, TimeUnit.DAYS,
                flex, TimeUnit.MILLISECONDS).build();
    }

    private static long calculateFlex() {
        // Initialize the calendar with today and the preferred time to run the job.
        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.HOUR_OF_DAY, MyWorkerDb.hourOfTheDay);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);

        // Initialize a calendar with now.
        Calendar cal2 = Calendar.getInstance();

        if (cal2.getTimeInMillis() < cal1.getTimeInMillis()) {
            // Add the worker periodicity.
            cal2.setTimeInMillis(cal2.getTimeInMillis() + TimeUnit.DAYS.toMillis(MyWorkerDb.repeatIntervalDay));
        }

        long delta = (cal2.getTimeInMillis() - cal1.getTimeInMillis());

        return ((delta > PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS) ? delta
                : PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS);
    }

    @NonNull
    @Override
    public Result doWork() {
        CheckConn checkConn = new CheckConn();
        if (checkConn.isNetworkAvailable()) {
            checkConn.updateDb(context);
            return Result.success();
        } else
            return Result.failure();
    }
}
