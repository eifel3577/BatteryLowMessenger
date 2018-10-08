package com.example.batterylowmessenger.receivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.batterylowmessenger.services.ApplicationService;


public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";
    public static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(ACTION_BOOT)) {
            Intent monitorIntent = new Intent(context, ApplicationService.class);
            monitorIntent.putExtra(ApplicationService.HANDLE_REBOOT, true);
            context.startService(monitorIntent);
        }
    }
}

