package com.example.owner.graphtest.graphui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.owner.graphtest.R;

/**
 * Created by evanalmonte on 12/11/17.
 */

public class LifeStatText extends ConstraintLayout {
    TextView topTextView;
    TextView bottomTextView;

    public LifeStatText(Context context) {
        super(context);
    }

    public LifeStatText(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.layout_lifetime_stats, this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LifeStatText);

        String topText = a.getString(R.styleable.LifeStatText_toptext);
        String bottomText = a.getString(R.styleable.LifeStatText_bottomtext);
        Log.d("Life Stat", "Top: " + topText);
        Log.d("Life Stat", "Bottom: " + bottomText);
        a.recycle();

        topTextView = findViewById(R.id.text_top);
        bottomTextView = findViewById(R.id.text_bottom);
        topTextView.setText(topText);
        bottomTextView.setText(bottomText);
    }

    public LifeStatText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void setTopText(String s) {
        if (s == null) { s = ""; }
        topTextView.setText(s);
    }


    public void setBottomText(String s) {
        if (s == null) { s = ""; }
        bottomTextView.setText(s);
    }
}
