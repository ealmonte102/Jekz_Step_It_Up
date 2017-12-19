package com.jekz.stepitup.ui.settings;

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


    public SettingsPresenter(LoginPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public void saveBodyInfo() {

    }

    @Override
    public void goBack() {

    }

    @Override
    public void onSettingsClicked(SettingsListRecylerAdapter.SettingType type) {
        switch (type) {
            case NAME:
                break;
            case GENDER:
                break;
            case HEIGHT:
                break;
            case WEIGHT:
                break;
            case STEP_GOAL:
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
}
