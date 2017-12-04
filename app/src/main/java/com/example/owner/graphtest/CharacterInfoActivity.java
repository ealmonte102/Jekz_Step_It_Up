package com.example.owner.graphtest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.ContentValues.TAG;


public class CharacterInfoActivity extends Activity {

    /**@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.test);

        JSONObject postData = new JSONObject();
        try {
            postData.put("userid", 1);
            postData.put("start_time", "'2017-11-29 22:05:00 -5:00'");
            postData.put("start_time", "'2017-11-29 22:05:30 -5:00'");
            postData.put("start_time", "'2017-11-29 22:05:00 -5:00'");
            postData.put("steps", 10000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        DBTest test = new DBTest(postData);
        test.execute("https://jekz.herokuapp.com/db/session");
    }*/


    private class DBTest extends AsyncTask<String, Void, Void> {

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

                urlConnection.setRequestProperty("Content-Type", "application/json ");

                urlConnection.setRequestMethod("POST");


                // Send the post body
                if (this.postData != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                    writer.write(postData.toString());
                    writer.flush();
                }

                int statusCode = urlConnection.getResponseCode();

            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return null;
        }
    }
}
