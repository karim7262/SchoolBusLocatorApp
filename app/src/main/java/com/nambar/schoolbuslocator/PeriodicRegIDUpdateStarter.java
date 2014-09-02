package com.nambar.schoolbuslocator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by nambar on 9/2/14.
 */
public class PeriodicRegIDUpdateStarter extends BroadcastReceiver
{
    RegIDUpdateAlarm alarm = new RegIDUpdateAlarm();
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            alarm.setAlarm(context);
        }
    }
}