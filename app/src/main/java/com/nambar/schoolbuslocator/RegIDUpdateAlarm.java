package com.nambar.schoolbuslocator;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import java.util.Calendar;

public class RegIDUpdateAlarm extends BroadcastReceiver
{
    private static String TAG = "RegIDUpdateAlarm";
    private static PendingIntent pendingIntent;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        Log.i(TAG, "Update alarm - updating REGID registration");

        SchoolBusLocatorServiceProxy.getInstance().updateRegistrationAsync(PreferencesStore.getInstance().getRegistrationId(context), "A", "B");

        wl.release();
    }

    public void setAlarm(Context context)
    {
        cancelAlarm(context);
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, RegIDUpdateAlarm.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP,
            AlarmManager.INTERVAL_FIFTEEN_MINUTES,
           AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
    }

    public void cancelAlarm(Context context)
    {
        if(pendingIntent != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        }
    }
}
