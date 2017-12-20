package com.jekz.stepitup.ui.settings;

import com.jekz.stepitup.ui.BasePresenter;

/**
 * Created by evanalmonte on 12/20/17.
 */

public interface SettingsContract {
    interface View {
        void showProgress();

        void hidesProgress();

        void showMessage();

        void showHeightPicker();

        void showWeightPicker();

        void showGoalPicker();

        void reloadProfile();
    }

    interface Presenter extends BasePresenter<View> {
        void goBack();

        void saveWeight(int weight);

        void saveHeight(int feet, int inches);

        void saveHeight(int cm);

        void saveGoal(int goal);

        void saveToDB();
    }
}
