package com.jekz.stepitup.ui.settings;

import android.util.Log;

import com.jekz.stepitup.adapter.SettingsListRecylerAdapter;
import com.jekz.stepitup.data.LoginPreferences;
import com.jekz.stepitup.data.SharedPrefsManager;
import com.jekz.stepitup.data.request.RequestString;
import com.jekz.stepitup.ui.shop.AsyncResponse;
import com.jekz.stepitup.ui.shop.ShopRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by evanalmonte on 12/20/17.
 */

public class SettingsPresenter implements SettingsContract.Presenter, SettingsListRecylerAdapter
        .SettingsListPresenter {
    private static final String TAG = SettingsPresenter.class.getName();
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
        this.preferences = preferences;
    }

    @Override
    public void goBack() {

    }

    @Override
    public void saveWeight(int weight) {
        preferences.put(SharedPrefsManager.Key.WEIGHT, String.valueOf(weight));
        view.reloadProfile();
    }

    @Override
    public void saveHeight(int feet, int inches) {
        preferences.put(SharedPrefsManager.Key.HEIGHT, String.valueOf(12 * feet + inches));
        view.reloadProfile();
    }

    @Override
    public void saveHeight(int cm) {
        preferences.put(SharedPrefsManager.Key.HEIGHT, String.valueOf((int) (cm * .39)));
        view.reloadProfile();
    }

    @Override
    public void saveGoal(int goal) {
        preferences.put(SharedPrefsManager.Key.GOAL, String.valueOf(goal));
        view.reloadProfile();
    }

    @Override
    public void saveToDB() {
        saveUserInfo();
        saveDailyGoal();

    }

    private void saveDailyGoal() {
        JSONObject postData = new JSONObject();
        try {
            postData.put("action", "set_daily_goal");
            postData.put("daily_goal", preferences.getString(SharedPrefsManager.Key.GOAL, null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ShopRequest shopRequest = new ShopRequest(postData, preferences.getString
                (SharedPrefsManager.Key.SESSION));
        shopRequest.delegate = new AsyncResponse() {
            @Override
            public void processFinish(JSONObject output) {
                Log.d(TAG, output.toString());
            }
        };
        shopRequest.execute(RequestString.getURL() + "/api/db/update");
    }

    private void saveUserInfo() {
        JSONObject postData = new JSONObject();
        try {
            postData.put("action", "update_user_info");
            postData.put("weight", preferences.getString(SharedPrefsManager.Key.WEIGHT, "0"));
            postData.put("height", preferences.getString(SharedPrefsManager.Key.HEIGHT, "0"));
            postData.put("gender", preferences.getString(SharedPrefsManager.Key.GENDER));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ShopRequest shopRequest = new ShopRequest(postData, preferences.getString
                (SharedPrefsManager.Key.SESSION));
        shopRequest.delegate = new AsyncResponse() {
            boolean success = false;
            boolean secondSuccess = false;
            @Override
            public void processFinish(JSONObject output) {
                Log.d(TAG, output.toString());
                if (!success) {
                    success = true;
                    view.showMessage("Successfully synced your height and weight");
                }
                if (success && !secondSuccess) {
                    secondSuccess = true;
                    view.showMessage("Successfully synced your daily goal");
                }
            }
        };
        shopRequest.execute(RequestString.getURL() + "/api/db/update");
    }

    @Override
    public void onSettingsClicked(SettingsListRecylerAdapter.SettingType type) {
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
        String data = preferences.getString(DATA[position]);
        Log.d(TAG, data);
        switch (DATA[position]) {
            case HEIGHT:
                int height = Integer.parseInt(data);
                data = getFromattedHeight(height);
                break;
            case WEIGHT:
                data = data + " lbs";
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
