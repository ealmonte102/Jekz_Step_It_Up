package com.jekz.stepitup.ui.login;

/**
 * Created by evanalmonte on 12/8/17.
 */

import com.jekz.stepitup.ui.BasePresenter;

/**
 * Contract for Login MVP
 */
interface LoginContract {
    /**
     * Interface representing a view
     */
    interface View {
        /**
         * Displays a message
         *
         * @param message message to be displayed
         */
        void showMessage(String message);

        /**
         * Navigate to home activity
         */
        void navigateToHome();

        void navigateToSignup();

        void showProgress();

        void hideProgress();

        void showUsernameError(String message);

        void showPasswordError(String message);
    }

    /**
     * Interface representing a presenter
     */
    interface Presenter extends BasePresenter<LoginContract.View> {
        void onViewAttached(LoginContract.View view);

        void onViewDetached();

        /**
         * Method which attempts to login using the specified credentials
         *
         * @param username string representing username
         * @param password string representing password
         */
        void login(String username, String password);

        void signup();
    }
}
