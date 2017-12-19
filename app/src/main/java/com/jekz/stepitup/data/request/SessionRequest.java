package com.jekz.stepitup.data.request;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by evanalmonte on 12/18/17.
 */

public class SessionRequest extends AsyncTask<String, Integer, JSONObject> {
    private static final String TAG = SessionRequest.class.getName();

    SessionRequestCallback callback;
    String sessionId;
    String startTime;
    String endTime;
    int steps;

    public SessionRequest(String sessionId, String startTime, String endTime, int steps,
                          SessionRequestCallback callback) {
        this.sessionId = sessionId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.steps = steps;
        this.callback = callback;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection urlConnection;
            if (RequestString.isLocal()) {
                urlConnection = (HttpURLConnection) url.openConnection();
            } else {
                urlConnection = (HttpsURLConnection) url.openConnection();
            }
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Cookie", sessionId);
            //printHttpsCert(urlConnection);
            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            JSONObject data = new JSONObject();
            data.put("action", "sessions");
            data.put("start_time", startTime);
            data.put("end_time", endTime);
            data.put("steps", steps);
            out.write(data.toString());
            out.flush();
            out.close();


            StringBuilder builder = new StringBuilder();
            InputStream is;
            is = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                builder.append(inputLine).append("\n");
            }
            return new JSONObject(builder.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //{"rows":[{"message":"success","success":true,"currency_":"981941"}],"return_data":"sessions"}
    @Override
    protected void onPostExecute(JSONObject result) {
        if (result == null) {
            Log.d(TAG, SessionRequestResult.NETWORK_ERROR.name());
            callback.processResultOfSessionRequest(SessionRequestResult.NETWORK_ERROR);
            return;
        }
        try {
            JSONArray output = result.getJSONArray("rows");
            for (int i = 0; i < output.length(); i++) {
                JSONObject q = output.getJSONObject(i);
                if (q.getBoolean("success")) {
                    Log.d(TAG,
                            SessionRequestResult.SESSION_SAVED.name() + ": " + result.toString());
                    callback.processResultOfSessionRequest(SessionRequestResult.SESSION_SAVED);
                    callback.processCurrencyUpdate(q.getInt("currency_"));
                }
            }
        } catch (JSONException e) {
            Log.d(TAG, SessionRequestResult.UNSUCCESSFUL.name() + ": " + e.getMessage());
            callback.processResultOfSessionRequest(SessionRequestResult.UNSUCCESSFUL);
        }
    }

    public enum SessionRequestResult {
        NETWORK_ERROR,
        SESSION_SAVED,
        UNSUCCESSFUL
    }

    public interface SessionRequestCallback {
        void processResultOfSessionRequest(SessionRequestResult result);

        void processCurrencyUpdate(int x);
    }
}
