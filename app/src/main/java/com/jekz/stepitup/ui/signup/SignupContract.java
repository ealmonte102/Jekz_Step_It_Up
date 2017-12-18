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
    }

    interface Presenter extends BasePresenter<SignupContract.View> {

    }
}
