package com.jekz.stepitup.graphtest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.jekz.stepitup.R;
import com.jekz.stepitup.customview.LifeStatText;
import com.jekz.stepitup.data.SharedPrefsManager;
import com.jekz.stepitup.data.request.LoginManager;
import com.jekz.stepitup.data.request.RemoteLoginModel;
import com.jekz.stepitup.data.request.RequestString;
import com.jekz.stepitup.ui.home.HomeActivity;
import com.jekz.stepitup.ui.shop.AsyncResponse;
import com.jekz.stepitup.ui.shop.ShopRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class GraphActivity extends AppCompatActivity implements AsyncResponse {

    protected static int user_weight = 0;
    protected static int user_height = 0;
    protected static int total_steps = 0;
    protected static int total_time = 0;
    protected static int total_sessions = 0;
    protected static int daily_goal = 0;


    BarChart mChart;

    //ShopRequest user_data = new ShopRequest(null);
    //ShopRequest session_data = new ShopRequest(null);
    BarChart mChart2;
    BarChart mChart3;
    LifeStatText durationText;
    LifeStatText stepsText;
    LifeStatText caloriesText;
    private LoginManager loginManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        ButterKnife.bind(this);
        mChart = findViewById(R.id.first_chart);
        mChart2 = findViewById(R.id.second_chart);
        mChart3 = findViewById(R.id.third_chart);

        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        durationText = findViewById(R.id.lifestat_duration);
        stepsText = findViewById(R.id.lifestat_steps);
        caloriesText = findViewById(R.id.lifestat_calories);

        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);

        SharedPrefsManager manager = SharedPrefsManager.getInstance(getApplicationContext());
        loginManager = new RemoteLoginModel(manager);

        //retrieves user data
        String session = loginManager.getSession();
        //Log.d(TAG, "Current session cookie: " + session);
        JSONObject postData = new JSONObject();
        try {
            postData.put("action", "user_data");
            //postData.put("userid", 1);
        } catch (JSONException e) {e.printStackTrace();}
        ShopRequest user_data = new ShopRequest(postData, session);
        user_data.execute(RequestString.getURL() + "/api/db/retrieve");

        user_data.delegate = new AsyncResponse() {
            //user_data.delegate = new AsyncResponse() {
            @Override
            public void processFinish(JSONObject returnObject) {
                try {
                    JSONArray output = returnObject.getJSONArray("rows");
                    //Block for parsing user data
                    if (output.length() == 1) {
                        try {
                            for (int i = 0; i < output.length(); i++) {

                                JSONObject r = output.getJSONObject(i);

                                if (r.has("weight")) {
                                    try {
                                        user_weight = r.getInt("weight");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (r.has("height")) {
                                    try {
                                        user_height = r.getInt("height");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (r.has("total_steps")) {
                                    try {
                                        total_steps = r.getInt("total_steps");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (r.has("total_duration")) {
                                    try {
                                        total_time = r.getInt("total_duration");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (r.has("total_sessions")) {
                                    try {
                                        total_sessions = r.getInt("total_sessions");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else Log.d("myTest", "Nothing in first processFinish");

                                if (r.has("daily_goal")) {
                                    try {
                                        daily_goal = r.getInt("daily_goal");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else Log.d("myTest", "Nothing in first processFinish");
                            }
                            writeTotalTimeToTextView(total_time, total_sessions);
                            writeCaloriesToTextView(user_weight, total_time, total_sessions);
                            writeStepsToTextView(total_steps, total_sessions);
                        } catch (JSONException e) {e.printStackTrace();}
                    }
                } catch (JSONException e) {e.printStackTrace();}
            }
        };
        //user_data.postData = postData;

        //retrieves session data
        JSONObject postData2 = new JSONObject();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String currentDateandTime = sdf.format(new Date());
            postData2.put("action", "steps_by_week");
            //postData2.put("userid", 1);
            postData2.put("date", currentDateandTime);
        } catch (JSONException e) {e.printStackTrace();}
        ShopRequest session_data = new ShopRequest(postData2, session);
        session_data.execute(RequestString.getURL() + "/api/db/retrieve");

        session_data.delegate = new AsyncResponse() {
            //session_data.delegate = new AsyncResponse() {
            @Override
            public void processFinish(JSONObject returnObject) {
                try {
                    JSONArray output = returnObject.getJSONArray("rows");
                    //Block for parsing session data
                    if (output.length() >= 7) {
                        try {
                            double[] sessionLengthArray = new double[7];
                            int[] sessionStepArray = new int[7];
                            int arrayCounter = 0;
                            String[] sessionDateArray = new String[7];

                            for (int i = output.length() - 1; i >= output.length() - 7; i--) {
                                JSONObject r = output.getJSONObject(i);

                                //get time
                                DateFormat dateFormat = new SimpleDateFormat
                                        ("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                //dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                                Date date = dateFormat.parse(r.getString("session_date"));
                                //dateFormat.setTimeZone(TimeZone.getDefault());

                                DateFormat mdFormat = new SimpleDateFormat("MM-dd");
                                mdFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                                mdFormat.setTimeZone(TimeZone.getDefault());
                                String md = mdFormat.format(date);

                                sessionDateArray[arrayCounter] = md;
                                sessionStepArray[arrayCounter] = r.getInt("total_steps");
                                String totalHoursString = r.getString("total_hours");
                                sessionLengthArray[arrayCounter] = Double.parseDouble
                                        (totalHoursString);

                                arrayCounter++;
                            }

                            drawSessionLengthGraph(mChart, sessionLengthArray, sessionDateArray);
                            drawStepCountGraph(mChart2, sessionStepArray, sessionDateArray,
                                    daily_goal);
                            drawCalorieGraph(mChart3, user_weight, sessionLengthArray,
                                    sessionDateArray);
                        } catch (JSONException e) {
                            Log.d("myTest", "JSONException");
                        } catch (java.text.ParseException e) {
                            Log.d("timeTest", "parse exception");
                        }
                    }
                } catch (JSONException e) {e.printStackTrace();}
            }
        };
    }

    @Override
    public void processFinish(JSONObject output) {
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
    }

    protected void drawSessionLengthGraph(BarChart barChart, double[] sessionLengthArray,
                                          String[] sessionDateArray) {
        /**
         * put this in xml
         * <com.github.mikephil.charting.charts.BarChart
         android:id="@+id/barChart0"
         android:layout_width="match_parent"
         android:layout_height="261dp">
         </com.github.mikephil.charting.charts.BarChart>
         */

        List<BarEntry> barEntries = new ArrayList<>();


        barEntries.add(new BarEntry(0f, (float) sessionLengthArray[0]));
        barEntries.add(new BarEntry(1f, (float) sessionLengthArray[1]));
        barEntries.add(new BarEntry(2f, (float) sessionLengthArray[2]));
        barEntries.add(new BarEntry(3f, (float) sessionLengthArray[3]));
        barEntries.add(new BarEntry(4f, (float) sessionLengthArray[4]));
        barEntries.add(new BarEntry(5f, (float) sessionLengthArray[5]));
        barEntries.add(new BarEntry(6f, (float) sessionLengthArray[6]));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Session Length (Hours)");

        //bar formatting
        barDataSet.setColor(Color.BLUE);
        barDataSet.setValueTextColor(Color.BLACK);

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
        Description description = new Description();
        description.setText("Length of the past 7 sessions");
        barChart.setDescription(description);

        //x-axis
        String[] BarChartXValues = sessionDateArray;
        XAxis xAxis0 = barChart.getXAxis();
        xAxis0.setValueFormatter(new JekzXAxisValueFormatter(BarChartXValues));
        xAxis0.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis0.setDrawGridLines(false);
        xAxis0.setGranularity(1f); // only intervals of 1 day
        xAxis0.setLabelCount(7);

        barChart.invalidate();
    }

    protected void drawStepCountGraph(BarChart barChart, int[] sessionStepArray, String[] sessionDateArray, int daily_goal) {

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

        List<BarEntry> barEntries = new ArrayList<>();

        barEntries.add(new BarEntry(0f, (float) sessionStepArray[0]));
        barEntries.add(new BarEntry(1f, (float) sessionStepArray[1]));
        barEntries.add(new BarEntry(2f, (float) sessionStepArray[2]));
        barEntries.add(new BarEntry(3f, (float) sessionStepArray[3]));
        barEntries.add(new BarEntry(4f, (float) sessionStepArray[4]));
        barEntries.add(new BarEntry(5f, (float) sessionStepArray[5]));
        barEntries.add(new BarEntry(6f, (float) sessionStepArray[6]));

        BarDataSet barDataSet = new BarDataSet(barEntries, "Number of Steps");

        //bar formatting
        barDataSet.setColor(Color.BLUE);
        barDataSet.setValueTextColor(Color.BLACK);

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

        Description description = new Description();
        description.setText("Number of steps in the past 7 sessions");
        barChart.setDescription(description);

        //x-axis
        String[] BarChartXValues = sessionDateArray;
        XAxis xAxis1 = barChart.getXAxis();
        xAxis1.setValueFormatter(new JekzXAxisValueFormatter(BarChartXValues));
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis1.setDrawGridLines(false);
        xAxis1.setGranularity(1f); // only intervals of 1 day
        xAxis1.setLabelCount(7);

        //y-axis
        YAxis yAxis = barChart.getAxisLeft();
        LimitLine dailyGoal = new LimitLine((float) daily_goal, "Daily Step Goal");
        dailyGoal.setLineColor(Color.BLACK);
        dailyGoal.setLineWidth(4f);
        yAxis.addLimitLine(dailyGoal);

        barChart.invalidate();
    }

    protected void drawCalorieGraph(BarChart barChart, int weight, double[] sessionLengthArray, String[] sessionDateArray) {

        /**
         * include this in xml and format
         *
         *<com.github.mikephil.charting.charts.BarChart
         android:id="@+id/chart"
         android:layout_width="match_parent"
         android:layout_height="match_parent" />
         */

        List<BarEntry> barEntries = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            if (sessionLengthArray[i] > 0 && weight > 0) {
                double caloriesBurned = (weight / 2.2) * sessionLengthArray[i];
                barEntries.add(new BarEntry((float) i, (float) caloriesBurned));
            } else {
                barEntries.add(new BarEntry((float) i, 0f));
            }
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Calories Burned");

        //bar formatting
        barDataSet.setColor(Color.BLUE);
        barDataSet.setValueTextColor(Color.BLACK);

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

        Description description = new Description();
        description.setText("Calories in the past 7 sessions." + "\n" +
                            "Note: Graph will show 0 if weight is not set");
        barChart.setDescription(description);

        //x-axis
        String[] BarChartXValues = sessionDateArray;
        XAxis xAxis0 = barChart.getXAxis();
        xAxis0.setValueFormatter(new JekzXAxisValueFormatter(BarChartXValues));
        xAxis0.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis0.setDrawGridLines(false);
        xAxis0.setGranularity(1f); // only intervals of 1 day
        xAxis0.setLabelCount(7);

        barChart.invalidate();
    }

    protected void writeTotalTimeToTextView(int total_time, int total_sessions) {

        if (total_time == 0 || total_sessions == 0) {
            durationText.setBottomText(
                    "Lifetime total: 0 minutes" + "\n" + "Lifetime average: 0 minutes");
        } else {
            durationText.setBottomText(
                    "Lifetime total: " + total_time + " minutes" + "\n" + "Lifetime average: " +
                    (total_time / total_sessions) + " minutes");
        }
    }

    protected void writeStepsToTextView(int total_steps, int total_sessions) {


        if (total_steps == 0 || total_sessions == 0) {
            stepsText.setBottomText("Lifetime total: 0 steps" + "\n" + "Lifetime average: 0 steps");
        } else {
            stepsText.setBottomText(
                    "Lifetime total: " + total_steps + " steps" + "\n" + "Lifetime average: " +
                    (total_steps / total_sessions) + " steps");
        }
    }

    protected void writeCaloriesToTextView(int weight, int total_time, int total_sessions) {


        if (total_time == 0 || total_sessions == 0) {
            caloriesText.setBottomText("Estimated lifetime total: 0 calories" + "\n" +
                                       "Estimated lifetime average: 0 calories");
        } else {
            double total_calorie = (weight / 2.2) * (total_time / 3600);
            double average_calorie = total_calorie / total_sessions;

            caloriesText.setBottomText(
                    "Estimated lifetime total: " + total_calorie + " calories" + "\n" +
                    "Estimated lifetime average: " + average_calorie + " calories");
        }
    }

    @Override
    public void onBackPressed() {
        //Intentionally left blank to disable back button
    }


    @OnClick(R.id.button_graph_back)
    void onBackClicked(View view) {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
