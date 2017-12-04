package com.example.owner.graphtest;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by Owner on 12/3/2017.
 */

public class DBTest extends AsyncTask<String, Void, Void> {

    public AsyncResponse delegate;

    public InputStream in;

    JSONObject postData;

    public DBTest(JSONObject postData) {
        if (postData != null) {
            this.postData = postData;
        }
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            // This is getting the url from the string we passed in
            URL url = new URL(strings[0]);

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

            in = new BufferedInputStream(urlConnection.getInputStream());
            /**int readAmount = in.available();
            byte[] bytes = new byte[readAmount];
            in.read(bytes, 0, readAmount);
            for (byte b : bytes){
                System.out.print((char) b);
            }
            System.out.println();*/

            //onPostExecute(convertStreamToString(in));

            int statusCode = urlConnection.getResponseCode();

        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
        return null;
    }

    //@Override
    protected void onPostExecute(String result) {
        try {
            delegate.processFinish(result);
            in.close();
        }catch(IOException e){e.printStackTrace();}
    }

    static String convertStreamToString(java.io.InputStream is) {
        /**java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";*/
        try {
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) != -1) {
                result.write(buffer, 0, length);

            }
            return result.toString("UTF-8");
            // StandardCharsets.UTF_8.name() > JDK 7
        }catch(IOException e){e.printStackTrace();}
        return null;
    }
}