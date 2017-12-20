package com.jekz.stepitup.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.jekz.stepitup.R;
import com.jekz.stepitup.adapter.SettingsListRecylerAdapter;
import com.jekz.stepitup.data.SharedPrefsManager;
import com.jekz.stepitup.ui.home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity implements SettingsContract.View {
    @BindView(R.id.recylerview_settings)
    RecyclerView recyclerView;

    SettingsContract.Presenter presenter;

    @BindView(R.id.constraintlayout_settings_height_imperial)
    ConstraintLayout heightLayoutImperial;

    @BindView(R.id.constraintlayout_settings_height_metric)
    ConstraintLayout heightLayoutMetric;

    @BindView(R.id.constraintlayout_settings_goal)
    ConstraintLayout goalLayout;

    @BindView(R.id.constraintlayout_settings_weight)
    ConstraintLayout weightLayout;

    @BindView(R.id.button_settings_save_db)
    Button saveButton;

    @BindView(R.id.numberpicker_settings_feet)
    NumberPicker feetNumberPicker;

    @BindView(R.id.numberpicker_settings_inches)
    NumberPicker inchesNumberPicker;

    @BindView(R.id.numberpicker_settings_cm)
    NumberPicker centimeterNumberPicker;

    @BindView(R.id.numberpicker_settings_goal)
    NumberPicker goalNumberPicker;

    @BindView(R.id.numberpicker_settings_weight)
    NumberPicker weightNumberPicker;

    SettingsListRecylerAdapter recylerAdapter;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onViewDetached();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewAttached(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        SettingsPresenter presenter = new SettingsPresenter(SharedPrefsManager.getInstance
                (getApplicationContext()));
        this.presenter = presenter;
        recylerAdapter = new SettingsListRecylerAdapter(presenter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recylerAdapter);
        inchesNumberPicker.setMaxValue(12);
        inchesNumberPicker.setMinValue(0);
        feetNumberPicker.setMinValue(0);
        feetNumberPicker.setMaxValue(9);
        centimeterNumberPicker.setMaxValue(250);
        centimeterNumberPicker.setMinValue(0);
        goalNumberPicker.setMaxValue(200000);
        weightNumberPicker.setMaxValue(400);
        weightNumberPicker.setMinValue(1);

        feetNumberPicker.setWrapSelectorWheel(false);
        inchesNumberPicker.setWrapSelectorWheel(false);
        goalNumberPicker.setWrapSelectorWheel(false);
        weightNumberPicker.setWrapSelectorWheel(false);
    }


    @Override
    public void showProgress() {

    }

    @Override
    public void hidesProgress() {

    }

    @Override
    public void showMessage() {

    }

    @Override
    public void showHeightPicker() {
        weightLayout.setVisibility(View.GONE);
        heightLayoutImperial.setVisibility(View.VISIBLE);
        goalLayout.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);
    }

    @Override
    public void showWeightPicker() {
        weightLayout.setVisibility(View.VISIBLE);
        heightLayoutImperial.setVisibility(View.GONE);
        heightLayoutMetric.setVisibility(View.GONE);
        goalLayout.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);
    }

    @Override
    public void showGoalPicker() {
        weightLayout.setVisibility(View.GONE);
        heightLayoutImperial.setVisibility(View.GONE);
        heightLayoutMetric.setVisibility(View.GONE);
        goalLayout.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.GONE);
    }

    @Override
    public void reloadProfile() {
        recylerAdapter.notifyDataSetChanged();
    }

    private void useMetricHeight() {
        heightLayoutImperial.setVisibility(View.INVISIBLE);
        heightLayoutMetric.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.button_settings_tometric, R.id.button_settings_toimperial})
    public void switchMetric(View view) {
        switch (view.getId()) {
            case R.id.button_settings_tometric:
                useMetricHeight();
                break;
            case R.id.button_settings_toimperial:
                heightLayoutMetric.setVisibility(View.INVISIBLE);
                heightLayoutImperial.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick({R.id.button_settings_save_goal, R.id.button_settings_save_imperial, R.id
            .button_settings_save_weight, R.id.button_settings_save_metric,
              R.id.button_settings_save_db})
    void saveProfile(View view) {
        switch (view.getId()) {
            case R.id.button_settings_save_goal:
                presenter.saveGoal(goalNumberPicker.getValue());
                break;
            case R.id.button_settings_save_imperial:
                presenter.saveHeight(feetNumberPicker.getValue(), inchesNumberPicker.getValue());
                break;
            case R.id.button_settings_save_weight:
                presenter.saveWeight(weightNumberPicker.getValue());
                break;
            case R.id.button_settings_save_metric:
                presenter.saveHeight(centimeterNumberPicker.getValue());
                break;
            case R.id.button_settings_save_db:
                presenter.saveToDB();
                break;
        }
        showSaveButton();
    }

    @OnClick(R.id.button_settings_back)
    void onBackButtonPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void showSaveButton() {
        goalLayout.setVisibility(View.INVISIBLE);
        heightLayoutMetric.setVisibility(View.INVISIBLE);
        heightLayoutImperial.setVisibility(View.INVISIBLE);
        weightLayout.setVisibility(View.INVISIBLE);
        saveButton.setVisibility(View.VISIBLE);
    }
}
