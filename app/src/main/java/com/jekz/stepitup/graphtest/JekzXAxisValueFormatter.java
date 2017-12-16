package com.jekz.stepitup.graphtest;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by Jun on 11/27/2017.
 */

public class JekzXAxisValueFormatter implements IAxisValueFormatter {

    private String[] mValues;

    public JekzXAxisValueFormatter(String[] values) {
        this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        return mValues[(int) value];
    }

    /** this is only needed if numbers are returned, else return 0
     @Override public int getDecimalDigits() { return 1; }*/
}
