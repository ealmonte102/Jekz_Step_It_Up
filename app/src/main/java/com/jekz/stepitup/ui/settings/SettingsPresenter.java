package com.jekz.stepitup.ui.settings;

import android.util.Log;

import com.jekz.stepitup.adapter.SettingsListRecylerAdapter;
import com.jekz.stepitup.data.LoginPreferences;

/**
 * Created by evanalmonte on 12/20/17.
 */

public class SettingsPresenter implements SettingsContract.Presenter, SettingsListRecylerAdapter
        .SettingsListPresenter {

    LoginPreferences preferences;
    String[] TITLES = {"Username", "Gender", "Height", "Weight", "Daily Step Goal"};
    String[] DATA = {"evanalmonte", "Male", "5' 3''", "124lb", "5000 steps"};
    SettingsContract.View view;

    public SettingsPresenter(LoginPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public void goBack() {

    }

    @Override
    public void onSettingsClicked(SettingsListRecylerAdapter.SettingType type) {
        Log.d("Test", type.name());
        switch (type) {
            case NAME:
                break;
            case GENDER:
                break;
            case HEIGHT:
                view.showHeightPicker();
                break;
            case WEIGHT:
                view.showWeightPicker();
                break;
            case STEP_GOAL:
                view.showGoalPicker();
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return SettingsListRecylerAdapter.SettingType.values()[position].ordinal();
    }

    @Override
    public int getItemCount() {
        return TITLES.length;
    }

    @Override
    public void onBindSettingsViewholderAtPosition(int position, SettingsListRecylerAdapter
            .SettingsRowView rowView) {
        rowView.setTitleText(TITLES[position]);
        rowView.setDescriptionText(DATA[position]);
    }

    @Override
    public void onViewAttached(SettingsContract.View view) {
        this.view = view;
    }

    @Override
    public void onViewDetached() {
        this.view = null;
    }
}
