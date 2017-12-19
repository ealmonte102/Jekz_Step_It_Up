package com.jekz.stepitup.ui.settings;

/**
 * Created by evanalmonte on 12/20/17.
 */

public interface SettingsContract {
    interface View {
        void showProgress();

        void hidesProgress();

        void showMessage();

        void showHeightError();

        void showWeightError();
    }

    interface Presenter {
        void saveBodyInfo();

        void goBack();
    }
}
