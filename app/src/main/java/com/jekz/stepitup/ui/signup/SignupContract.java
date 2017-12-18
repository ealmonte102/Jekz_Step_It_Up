package com.jekz.stepitup.ui.signup;

import com.jekz.stepitup.ui.BasePresenter;

/**
 * Created by evanalmonte on 12/18/17.
 */

public class SignupContract {
    interface View {
        void showUsernameError(String message);

        void showPasswordError(String message);

        void navigateToLogin();

        void showMessage(String message);

        void showProgress();

        void hideProgress();
    }

    interface Presenter extends BasePresenter<SignupContract.View> {
        void register(String username, String password);

        void login();
    }
}
