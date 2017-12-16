package com.jekz.stepitup.graphtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.jekz.stepitup.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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
        } catch (JSONException e) {e.printStackTrace();}

        //DBTest test = new DBTest(postData);
        //test.execute("https://jekz.herokuapp.com/db/session");
        asyncTask.postData = postData;
        asyncTask.execute("https://jekz.herokuapp.com/db/session");
    }

    @Override
    public void processFinish(JSONArray output) {
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.

        try {
            //Log.d("myTest", "Test Test")
            JSONObject r = output.getJSONObject(0);
            Log.d("myTest", "value: " + r.getInt("userid"));
        } catch (JSONException e) {e.printStackTrace();}
    }
}
