package com.example.owner.graphtest;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


import static android.content.ContentValues.TAG;
import static com.example.owner.graphtest.DBTest.convertStreamToString;
import static java.lang.System.in;


public class MainActivity extends AppCompatActivity implements AsyncResponse {

    DBTest asyncTask = new DBTest(null);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this to set delegate/listener back to this class
        asyncTask.delegate = this;

        JSONObject postData = new JSONObject();
        try {
            postData.put("userid", 1);
            postData.put("start_time", "'2017-13-03 22:05:00 -5:00'");
            postData.put("end_time", "'2017-12-03 22:05:30 -5:00'");
            postData.put("steps", 70000);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //DBTest test = new DBTest(postData);
        //test.execute("https://jekz.herokuapp.com/db/session");
        asyncTask.postData = postData;
        asyncTask.execute("https://jekz.herokuapp.com/db/session");
        //asyncTask.execute("https://jekz.herokuapp.com/db");
        //Log.d("myTest", convertStreamToString(in));
        asyncTask.onPostExecute(convertStreamToString(in));
    }

    @Override
    public void processFinish(String output){
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.

        Log.d("myTest", output);
        //Log.d("myTest", "Test Test")
;    }
}
