package com.jekz.stepitup.ui.settings;

import android.util.Log;

import com.jekz.stepitup.adapter.SettingsListRecylerAdapter;
import com.jekz.stepitup.data.LoginPreferences;
import com.jekz.stepitup.data.SharedPrefsManager;

/**
 * Created by evanalmonte on 12/20/17.
 */

public class SettingsPresenter implements SettingsContract.Presenter, SettingsListRecylerAdapter
        .SettingsListPresenter {

    private static final String[] TITLES = {"Username", "Gender", "Height", "Weight", "Daily Step" +
                                                                                      " Goal"};
    LoginPreferences preferences;
    SettingsContract.View view;
    private SharedPrefsManager.Key[] DATA = {SharedPrefsManager.Key.USERNAME,
                                             SharedPrefsManager.Key.GENDER,
                                             SharedPrefsManager.Key.HEIGHT,
                                             SharedPrefsManager.Key.WEIGHT,
                                             SharedPrefsManager.Key.GOAL};

    public SettingsPresenter(LoginPreferences preferences) {
        preferences.put(SharedPrefsManager.Key.USERNAME, "evanalmonte");
        preferences.put(SharedPrefsManager.Key.GENDER, "male");
        preferences.put(SharedPrefsManager.Key.HEIGHT, 67);
        preferences.put(SharedPrefsManager.Key.WEIGHT, 145);
        preferences.put(SharedPrefsManager.Key.GOAL, 20000);

        this.preferences = preferences;
    }

    @Override
    public void goBack() {

    }

    @Override
    public void saveWeight(int weight) {
        preferences.put(SharedPrefsManager.Key.WEIGHT, weight);
        view.reloadProfile();
    }

    @Override
    public void saveHeight(int feet, int inches) {
        preferences.put(SharedPrefsManager.Key.HEIGHT, 12 * feet + inches);
        view.reloadProfile();
    }

    @Override
    public void saveHeight(int cm) {
        preferences.put(SharedPrefsManager.Key.HEIGHT, (int) (cm * .39));
        view.reloadProfile();
    }

    @Override
    public void saveGoal(int goal) {
        preferences.put(SharedPrefsManager.Key.GOAL, goal);
        view.reloadProfile();
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
        Log.d("Type", DATA[position].name());
        String data;
        switch (DATA[position]) {
            case GENDER:
                data = preferences.getString(DATA[position], "N/A");
                break;
            case USERNAME:
                data = preferences.getString(DATA[position], "N/A");
                break;
            case HEIGHT:
                data = String.valueOf(preferences.getInt(DATA[position], 0));
            {
                if (data != "N/A") {
                    int height = Integer.parseInt(data);
                    data = getFromattedHeight(height);
                }
                break;
            }
            default:
                data = String.valueOf(preferences.getInt(DATA[position]));
        }
        rowView.setDescriptionText(data);
        rowView.setTitleText(TITLES[position]);
    }

    @Override
    public void onViewAttached(SettingsContract.View view) {
        this.view = view;
    }

    @Override
    public void onViewDetached() {
        this.view = null;
    }

    private String getFromattedHeight(int height) {
        return "" + (int) (height / 12.0) + "ft " + height % 12 + "in";
    }
}
