package com.jekz.stepitup.graphtest;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

import static android.content.ContentValues.TAG;

/**
 * Created by Owner on 12/3/2017.
 */

public class DBRequest extends AsyncTask<String, Void, JSONArray> {

    public AsyncResponse delegate;

    JSONObject postData;

    public DBRequest(JSONObject postData) {
        if (postData != null) {
            this.postData = postData;
        }
    }

    @Override
    protected JSONArray doInBackground(String... params) {
        try {
            // This is getting the url from the string we passed in
            URL url = new URL(params[0]);

            // Create the urlConnection
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");

            // Send the post body
            if (this.postData != null) {
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(postData.toString());
                writer.flush();
            }

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            //receives bytes from server
            LinkedList<Byte> bytes = new LinkedList<Byte>();
            int len;
            byte[] buffer = new byte[4096];
            while (-1 != (len = in.read(buffer))) {
                for (int i = 0; i < len; i++) {
                    bytes.add(buffer[i]);
                }
            }

            //convert byte arraylist to array
            int size = bytes.size();
            byte[] b = new byte[size];
            for (int i = 0; i < size; i++) {
                b[i] = bytes.removeFirst();
            }

            String jsonStr = new String(b, StandardCharsets.UTF_8);
            Log.d("myTest", "jsonStr: " + jsonStr);

            JSONArray resultArray;

            try {
                resultArray = new JSONArray(jsonStr);
            } catch (JSONException e) { resultArray = new JSONArray("[" + jsonStr + "]");}
            ;

            //Log.d("myTest", "after JSONArray");

            int statusCode = urlConnection.getResponseCode();

            //Log.d("myTest", "end of doInBackground");

            return resultArray;

        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONArray result) {
        delegate.processFinish(result);
    }
}