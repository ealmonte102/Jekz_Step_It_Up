package com.example.owner.graphtest;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class GraphActivity extends AppCompatActivity implements AsyncResponse {

    DBRequest user_data = new DBRequest(null);
    DBRequest session_data = new DBRequest(null);

    protected static int user_weight = 0;
    protected static int user_height = 0;
    protected static int total_steps = 0;
    protected static int total_duration = 0;
    protected static int total_sessions = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        /**Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Log.d("timeTest", "String from DB is 2017-11-29T15:45:00.000Z");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = dateFormat.parse("2017-11-29T15:45:00.000Z");
            dateFormat.setTimeZone(TimeZone.getDefault());
            long time = date.getTime();
            java.sql.Timestamp timestamp = new Timestamp(time);
            Log.d("timeTest","time = " + timestamp.toString());
        } catch (java.text.ParseException e) {Log.d("timeTest","parse exception");};

        //retrieves user data
        user_data.delegate = this;
        JSONObject postData = new JSONObject();
        try {
            postData.put("data_type", "user_data");
            postData.put("userid", 1);
        } catch (JSONException e) {e.printStackTrace();}
        user_data.postData = postData;
        user_data.execute("https://jekz.herokuapp.com/api/db/retrieve");

        //retrieves session data
        session_data.delegate = this;
        JSONObject postData2 = new JSONObject();
        try {
            postData2.put("action", "sessions");
            postData2.put("userid", 1);
        } catch (JSONException e) {e.printStackTrace();}
        session_data.postData = postData2;
        session_data.execute("https://jekz.herokuapp.com/api/db/retrieve");

        //total_steps = 5;
        Log.d("myTest", "total steps: " + this.total_steps);

        //write to text view
        //writeDurationToTextView(this, 0 , total_duration, total_sessions);
        //writeStepsToTextView(this, 1, total_steps, total_sessions);
        //writeCaloriesToTextView(this, 2, user_weight, total_duration, total_sessions);
    }

    @Override
    public void processFinish(JSONArray output){
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.

        //Log.d("myTest", "made it to processFinish in GraphActivity");

        //Block for parsing user data
        if (output.length() == 1){
            getUserData(output);
            //Log.d("myTest", "total steps: " + total_steps);
        }

        //Block for parsing session data
        else if (output.length() > 1) {

        }
    }

    private void getUserData(JSONArray output){
        try {
            for (int i = 0; i < output.length(); i++){
                //Log.d("myTest", "inside for loop of processFinish");

                JSONObject r = output.getJSONObject(i);
                //Log.d("myTest", "JSONObject to string: " + r.toString());

                if (r.has("weight")){
                    try {
                        //user_weight = r.getInt("weight");
                        setUserWeight(r.getInt("weight"));
                        //Log.d("myTest", "weight: " + user_weight);
                    } catch (JSONException e){e.printStackTrace();}
                }

                if (r.has("height")){
                    try {
                        //user_height = r.getInt("height");
                        setUserHeight(r.getInt("height"));
                        //Log.d("myTest", "height: " + user_height);
                    } catch (JSONException e){e.printStackTrace();}
                }

                if (r.has("total_steps")){
                    try{
                        //total_steps = r.getInt("total_steps");
                        setTotalSteps(r.getInt("total_steps"));
                        //Log.d("myTest", "total steps in method: " + total_steps);
                    }catch (JSONException e){e.printStackTrace();}
                }

                if (r.has("total_duration")){
                    try{
                        //total_duration = r.getInt("total_duration");
                        setTotalDuration(r.getInt("total_duration"));
                        //Log.d("myTest", "total duration: " + total_duration);
                    }catch (JSONException e){e.printStackTrace();}
                }

                if (r.has("total_sessions")){
                    try{
                        //total_sessions = r.getInt("total_sessions");
                        setTotalSessions(r.getInt("total_sessions"));
                        //Log.d("myTest", "total sessions: " + total_sessions);
                    }catch (JSONException e){e.printStackTrace();}
                }
                else Log.d("myTest", "Nothing in processFinish");
            }
        }catch (JSONException e){e.printStackTrace();}

    }

    public void setUserWeight(int weight){
        this.user_weight = weight;
    }

    public void setUserHeight(int height){
        this.user_height = height;
    }

    public void setTotalSteps(int steps){
        this.total_steps = steps;
    }

    public void setTotalDuration(int duration){
        this.total_duration = duration;
    }

    public void setTotalSessions(int sessions){
        this.total_sessions = sessions;
    }

    protected static void drawSessionLengthGraph(Activity activity, int id){
        /**
         * put this in xml
         * <com.github.mikephil.charting.charts.BarChart
         android:id="@+id/barChart0"
         android:layout_width="match_parent"
         android:layout_height="261dp">
         </com.github.mikephil.charting.charts.BarChart>
         */

        //edit this to match id in xml
        //BarChart barChart = (BarChart) findViewById(R.id.activity);

        //edit this to match id in xml
        BarChart barChart = (BarChart) activity.findViewById(id);

        List<BarEntry> barEntries = new ArrayList<>();

        barEntries.add(new BarEntry(0f, 90f));
        barEntries.add(new BarEntry(1f, 80f));
        barEntries.add(new BarEntry(2f, 70f));
        barEntries.add(new BarEntry(3f, 30f));
        barEntries.add(new BarEntry(4f, 15f));
        barEntries.add(new BarEntry(5f, 0f));
        barEntries.add(new BarEntry(6f, 0f));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Session Length (Mins)");

        //bar formatting
        barDataSet.setColor(Color.GREEN);
        barDataSet.setValueTextColor(Color.GREEN);

        BarData barData = new BarData(barDataSet);

        //formatting and styling
        barData.setBarWidth(.5f);
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.animateXY(2000, 2500);
        barChart.setDrawBorders(false);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.setPinchZoom(true);
        barChart.setDoubleTapToZoomEnabled(false);

        //x-axis
        String[] BarChartXValues = new String[] {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        XAxis xAxis0 = barChart.getXAxis();
        xAxis0.setValueFormatter(new JekzXAxisValueFormatter(BarChartXValues));
        xAxis0.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis0.setDrawGridLines(false);
        xAxis0.setGranularity(1f); // only intervals of 1 day
        xAxis0.setLabelCount(7);

        barChart.invalidate();
    }

    protected static void drawStepCountGraph(Activity activity, int id){

        /**
         * put this in xml
         *
         * <com.github.mikephil.charting.charts.BarChart
         android:id="@+id/barChart1"
         android:layout_width="match_parent"
         android:layout_height="253dp"
         android:layout_alignParentBottom="true"
         android:layout_alignParentLeft="true"
         android:layout_alignParentStart="true"
         tools:layout_editor_absoluteX="0dp"
         tools:layout_editor_absoluteY="258dp">
         </com.github.mikephil.charting.charts.BarChart>
         */

        //edit this to match id in xml
        //BarChart barChart = (BarChart) findViewById(R.id.barChart0);

        //edit this to match id in xml
        BarChart barChart = (BarChart) activity.findViewById(id);

        List<BarEntry> barEntries = new ArrayList<>();

        barEntries.add(new BarEntry(0f,2867f));
        barEntries.add(new BarEntry(1f,3765f));
        barEntries.add(new BarEntry(2f,5612f));
        barEntries.add(new BarEntry(3f,2811f));
        barEntries.add(new BarEntry(4f,1143f));
        barEntries.add(new BarEntry(5f,6723f));
        barEntries.add(new BarEntry(6f,3201f));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Number of Steps");

        //bar formatting
        barDataSet.setColor(Color.GREEN);
        barDataSet.setValueTextColor(Color.GREEN);

        BarData barData = new BarData(barDataSet);

        //formatting and styling
        barData.setBarWidth(.5f);
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.animateXY(2000, 2500);
        barChart.setDrawBorders(false);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.setPinchZoom(true);
        barChart.setDoubleTapToZoomEnabled(false);


        String[] BarChartXValues = new String[] {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        //x-axis
        XAxis xAxis1 = barChart.getXAxis();
        xAxis1.setValueFormatter(new JekzXAxisValueFormatter(BarChartXValues));
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis1.setDrawGridLines(false);
        xAxis1.setGranularity(1f); // only intervals of 1 day
        xAxis1.setLabelCount(7);

        //y-axis
        YAxis yAxis = barChart.getAxisLeft();
        LimitLine dailyGoal = new LimitLine (3000f, "Daily Step Goal");
        dailyGoal.setLineColor(Color.YELLOW);
        dailyGoal.setLineWidth(4f);
        yAxis.addLimitLine(dailyGoal);

        barChart.invalidate();
    }

    protected static void drawCalorieGraph(Activity activity, int id){

        /**
         * include this in xml and format
         *
         *<com.github.mikephil.charting.charts.BarChart
         android:id="@+id/chart"
         android:layout_width="match_parent"
         android:layout_height="match_parent" />
         */

        //edit this to match id in xml
        //BarChart barChart = (BarChart) findViewById(R.id.barChart0);

        //edit this to match id in xml
        BarChart barChart = (BarChart) activity.findViewById(id);

        List<BarEntry> barEntries = new ArrayList<>();

        barEntries.add(new BarEntry(0f, 125f));
        barEntries.add(new BarEntry(1f, 102f));
        barEntries.add(new BarEntry(2f, 112f));
        barEntries.add(new BarEntry(3f, 135f));
        barEntries.add(new BarEntry(4f, 140f));
        barEntries.add(new BarEntry(5f, 110f));
        barEntries.add(new BarEntry(6f, 85f));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Calories Burned");

        //bar formatting
        barDataSet.setColor(Color.GREEN);
        barDataSet.setValueTextColor(Color.GREEN);

        BarData barData = new BarData(barDataSet);

        //formatting and styling
        barData.setBarWidth(.5f);
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.animateXY(2000, 2500);
        barChart.setDrawBorders(false);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.setPinchZoom(true);
        barChart.setDoubleTapToZoomEnabled(false);

        //x-axis
        String[] BarChartXValues = new String[] {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        XAxis xAxis0 = barChart.getXAxis();
        xAxis0.setValueFormatter(new JekzXAxisValueFormatter(BarChartXValues));
        xAxis0.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis0.setDrawGridLines(false);
        xAxis0.setGranularity(1f); // only intervals of 1 day
        xAxis0.setLabelCount(7);

        barChart.invalidate();
    }

    protected static void drawDistanceGraph(Activity activity, int id){


        /**
         * include this in xml and format
         *
         *<com.github.mikephil.charting.charts.BarChart
         android:id="@+id/chart"
         android:layout_width="match_parent"
         android:layout_height="match_parent" />
         */

        //edit this to match id in xml
        //BarChart barChart = (BarChart) findViewById(R.id.barChart0);

        //edit this to match id in xml
        BarChart barChart = (BarChart) activity.findViewById(id);

        List<BarEntry> barEntries = new ArrayList<>();

        barEntries.add(new BarEntry(0f, 1.2f));
        barEntries.add(new BarEntry(1f, 1.23f));
        barEntries.add(new BarEntry(2f, 1.3f));
        barEntries.add(new BarEntry(3f, 1.5f));
        barEntries.add(new BarEntry(4f, 0.9f));
        barEntries.add(new BarEntry(5f, 3.1f));
        barEntries.add(new BarEntry(6f, 0f));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Distanced Walked (mi)");

        //bar formatting
        barDataSet.setColor(Color.GREEN);
        barDataSet.setValueTextColor(Color.GREEN);

        BarData barData = new BarData(barDataSet);

        //formatting and styling
        barData.setBarWidth(.5f);
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.animateXY(2000, 2500);
        barChart.setDrawBorders(false);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.setPinchZoom(true);
        barChart.setDoubleTapToZoomEnabled(false);

        //x-axis
        String[] BarChartXValues = new String[] {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        XAxis xAxis0 = barChart.getXAxis();
        xAxis0.setValueFormatter(new JekzXAxisValueFormatter(BarChartXValues));
        xAxis0.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis0.setDrawGridLines(false);
        xAxis0.setGranularity(1f); // only intervals of 1 day
        xAxis0.setLabelCount(7);

        barChart.invalidate();
    }

    protected static void writeDurationToTextView(Activity activity, int id, int total_duration, int total_sessions){
        //Edit this based on textview name in XML
        //TextView textView = (TextView) findViewById(R.id.textView1);

        //Edit this based on xml
        TextView textView = (TextView) activity.findViewById(id);

        textView.setText("Lifetime total: " + total_duration + " minutes" + "\n" + "Lifetime average: " + (total_duration/total_sessions) + " minutes");
    }

    protected static void writeDistanceToTextView(Activity activity, int id){
        //Edit this based on textview name in XML
        //TextView textView = (TextView) findViewById(R.id.textView2);

        //Edit this based on xml
        TextView textView = (TextView) activity.findViewById(id);

        textView.setText("Estimated lifetime total: 6542" + " miles" + "\n" + "Estimated lifetime average: 3.1" + " miles");
    }

    protected static void writeStepsToTextView(Activity activity, int id, int total_steps, int total_sessions){
        //Edit this based on textview name in XML
        //TextView textView = (TextView) findViewById(R.id.textView3);

        //Edit this based on xml
        TextView textView = (TextView) activity.findViewById(id);

        textView.setText("Lifetime total: "+ total_steps + " steps" + "\n" + "Lifetime average: "+ (total_steps/total_sessions) + " steps");
    }

    protected static void writeCaloriesToTextView(Activity activity, int id, int weight, int total_duration, int total_sessions){
        //Edit this based on textview name in XML
        //TextView textView = (TextView) findViewById(R.id.textView4);

        //Edit this based on xml
        TextView textView = (TextView) activity.findViewById(id);

        double total_calorie = (weight/2.2) * (total_duration/3600);
        double average_calorie = total_calorie/total_sessions;

        textView.setText("Estimated lifetime total: " + total_calorie + " calories" + "\n" + "Estimated lifetime average: " + average_calorie + " calories");
    }
}
