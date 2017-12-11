package com.example.owner.graphtest.graphui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.example.owner.graphtest.JekzXAxisValueFormatter;
import com.example.owner.graphtest.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    BarChart mChart;
    BarChart mChart2;
    BarChart mChart3;
    LifeStatText durationText;
    LifeStatText stepsText;
    LifeStatText caloriesText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mChart = findViewById(R.id.first_chart);
        mChart2 = findViewById(R.id.second_chart);
        mChart3 = findViewById(R.id.third_chart);
        test(mChart);
        test(mChart2);
        test(mChart3);

        Toolbar toolbar = findViewById(R.id.toolbar);
        durationText = findViewById(R.id.lifestat_duration);
        stepsText = findViewById(R.id.lifestat_steps);
        caloriesText = findViewById(R.id.lifestat_calories);

        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
    }

    private void test(BarChart barChart) {

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
        String[] BarChartXValues = new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        XAxis xAxis0 = barChart.getXAxis();
        xAxis0.setValueFormatter(new JekzXAxisValueFormatter(BarChartXValues));
        xAxis0.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis0.setDrawGridLines(false);
        xAxis0.setGranularity(1f); // only intervals of 1 day
        xAxis0.setLabelCount(7);

        barChart.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
