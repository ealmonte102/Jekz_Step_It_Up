package com.jekz.stepitup.data.request;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by evanalmonte on 12/17/17.
 */

public class CookieRequest extends AsyncTask<String, Integer, String> {
    private static final String TAG = CookieRequest.class.getName();
    private final AsynchCookieCallback callback;

    public CookieRequest(AsynchCookieCallback callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);

            if (RequestString.isLocal()) {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(false);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                return urlConnection.getHeaderField("Set-Cookie");
            } else {
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(false);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                return urlConnection.getHeaderField("Set-Cookie");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR CONNECTING";
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "Cookie Retrieved: " + (result == null ? "No Cookie" : result));
        callback.processCookie(result);
    }

    public interface AsynchCookieCallback {
        void processCookie(String result);
    }
}
