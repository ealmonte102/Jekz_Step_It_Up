package com.jekz.stepitup.graphtest;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by Owner on 12/3/2017.
 */

public class DBTest extends AsyncTask<String, Void, JSONArray> {

    public AsyncResponse delegate;

    JSONObject postData;

    public DBTest(JSONObject postData) {
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
            String jsonStr = "";
            int readAmount = in.available();
            byte[] bytes = new byte[readAmount];
            in.read(bytes, 0, readAmount);
            for (byte b : bytes) {
                char c = (char) b;
                jsonStr += c;
            }

            JSONArray resultArray = new JSONArray(jsonStr);
            /**for (int i = 0; i < resultArray.length(); i++){
             JSONObject r = resultArray.getJSONObject(i);
             //System.out.println(r.getInt("userid"));
             //Log.d("myTest", "value: " + r.getInt("userid"));
             //Log.d("myTest", "inside for loop");
             userid = r.getInt("userid");
             }*/

            int statusCode = urlConnection.getResponseCode();

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