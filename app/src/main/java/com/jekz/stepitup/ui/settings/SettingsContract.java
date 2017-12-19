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

        void showWeightError();

        void showHeightPicker();

        void showWeightPicker();

        void showGoalPicker();

    }

    interface Presenter extends BasePresenter<View> {
        void goBack();
    }
}
