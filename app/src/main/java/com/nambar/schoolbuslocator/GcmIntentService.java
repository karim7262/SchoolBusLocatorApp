package com.nambar.schoolbuslocator;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmIntentService extends IntentService implements LocationListener {
    static final String TAG = "SchoolBusLocator";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.e(TAG, "GCM Error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.w(TAG, "Deleted messages on server: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.i(TAG, "Checking location... ");
                askForCurrentLocation();
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void askForCurrentLocation() {
        LocationManager lm = (LocationManager)getBaseContext().getSystemService(Context.LOCATION_SERVICE);
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.e(TAG, "GPS is not enabled");
            return;
        }
        Log.d(TAG, "requesting updated location...");
        lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, getMainLooper());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "got location " + location);
        if(location != null) {
            sendLocation(location);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void sendLocation(Location location) {
        String regid = PreferencesStore.getInstance().getRegistrationId(getBaseContext());
        SchoolBusLocatorServiceProxy.getInstance().updateLocationAsync(regid, location);
    }

}