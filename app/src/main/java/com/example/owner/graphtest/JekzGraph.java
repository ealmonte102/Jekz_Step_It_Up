package com.example.owner.graphtest;

import android.app.Activity;
import android.graphics.Color;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun on 11/29/2017.
 */

public class JekzGraph {

    public static void drawSessionLengthGraph(Activity activity){
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
        BarChart barChart = (BarChart) activity.findViewById(0);

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

    public static void drawStepCountGraph(Activity activity){

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
        BarChart barChart = (BarChart) activity.findViewById(0);

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

    public static void drawCalorieGraph(Activity activity){

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
        BarChart barChart = (BarChart) activity.findViewById(0);

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

    public static void drawDistanceGraph(Activity activity){


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
        BarChart barChart = (BarChart) activity.findViewById(0);

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

    public static void writeDurationToTextView(Activity activity){
        //Edit this based on textview name in XML
        //TextView textView = (TextView) findViewById(R.id.textView1);

        //Edit this based on xml
        TextView textView = (TextView) activity.findViewById(0);

        textView.setText("Lifetime total: 2000" + " minutes" + "\n" + "Lifetime average: 61" + " minutes");
    }

    public static void writeDistanceToTextView(Activity activity){
        //Edit this based on textview name in XML
        //TextView textView = (TextView) findViewById(R.id.textView2);

        //Edit this based on xml
        TextView textView = (TextView) activity.findViewById(0);

        textView.setText("Lifetime total: 6542" + " miles" + "\n" + "Lifetime average: 3.1" + " miles");
    }

    public static void writeStepsToTextView(Activity activity){
        //Edit this based on textview name in XML
        //TextView textView = (TextView) findViewById(R.id.textView3);

        //Edit this based on xml
        TextView textView = (TextView) activity.findViewById(0);

        textView.setText("Lifetime total: 8621" + " steps" + "\n" + "Lifetime average: 5310" + " steps");
    }

    public static void writeCaloriesToTextView(Activity activity){
        //Edit this based on textview name in XML
        //TextView textView = (TextView) findViewById(R.id.textView4);

        //Edit this based on xml
        TextView textView = (TextView) activity.findViewById(0);

        textView.setText("Lifetime total: 94150" + " calories" + "\n" + "Lifetime average: 150" + " calories");
    }
}
