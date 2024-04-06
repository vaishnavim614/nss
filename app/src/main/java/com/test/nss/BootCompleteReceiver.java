package com.test.nss;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.test.nss.worker.MyWorker;
import com.test.nss.worker.MyWorkerDb;

public class BootCompleteReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Log.w("onReceive", "BootCompleteReceiver");
        if (intent.getAction() == null || !intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
            return;

        MyWorkerDb.enqueueSelf();
        MyWorker.enqueueSelf();
    }
}
