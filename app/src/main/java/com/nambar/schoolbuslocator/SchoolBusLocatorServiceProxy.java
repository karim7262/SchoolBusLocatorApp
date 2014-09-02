package com.nambar.schoolbuslocator;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nambar on 9/1/14.
 */
public class SchoolBusLocatorServiceProxy {

    private static SchoolBusLocatorServiceProxy s_instance = new SchoolBusLocatorServiceProxy();
    static final String TAG = "SchoolBusLocator";

    private SchoolBusLocatorServiceProxy() {
    }

    public static SchoolBusLocatorServiceProxy getInstance() {
        return s_instance;
    }

    public void updateRegistrationAsync(final String regid, final String group, final String name) {

        (new AsyncTask<Void,Void,HttpResponse>() {
            @Override
            protected HttpResponse doInBackground(Void... voids) {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost post = new HttpPost("http://findmyschoolbus.appspot.com/registerdevice");
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("regid", regid));
                    params.add(new BasicNameValuePair("group", group));
                    params.add(new BasicNameValuePair("name", name));
                    post.setEntity(new UrlEncodedFormEntity(params));
                    return httpClient.execute(post);
                } catch (Exception e) {
                    Log.e(TAG, "error while updating registration", e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(HttpResponse response) {
                if(response != null) {
                    Log.i(TAG, "updated registration - get response " + response.getStatusLine().getStatusCode());
                }
            }
        }).execute();

    }


    public void updateLocationAsync(final String regid, final Location location) {

        (new AsyncTask<Void,Void,HttpResponse>() {
            @Override
            protected HttpResponse doInBackground(Void... voids) {
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost post = new HttpPost("http://findmyschoolbus.appspot.com/updatelocation");
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("regid", regid));
                    params.add(new BasicNameValuePair("lat", String.valueOf(location.getLatitude())));
                    params.add(new BasicNameValuePair("lon", String.valueOf(location.getLongitude())));
                    post.setEntity(new UrlEncodedFormEntity(params));
                    return httpClient.execute(post);
                } catch (Exception e) {
                    Log.e(TAG, "error while updating registration", e);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(HttpResponse response) {
                if(response != null) {
                    Log.i(TAG, "updated registration - get response " + response.getStatusLine().getStatusCode());
                }
            }
        }).execute();

    }
}
