package com.mthotengera.alarmmanagernotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mthotengera.alarmmanagernotification.services.AlarmService;

/**
 * Created by mt250219 on 10/12/15.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, AlarmService.class);
        context.startService(service);
    }
}
