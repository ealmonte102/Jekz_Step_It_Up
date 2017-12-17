package com.jekz.stepitup.ui.login;

/**
 * Created by evanalmonte on 12/8/17.
 */

import com.jekz.stepitup.ui.BasePresenter;

/**
 * Contract for Login MVP
 */
interface LoginMVP {
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
        void startHomeActivity();

        void enableLogin();

        void disableLogin();

        void showProgress();

        void hideProgress();
    }

    /**
     * Interface representing a presenter
     */
    interface Presenter extends BasePresenter<LoginMVP.View> {
        void onViewAttached(LoginMVP.View view);

        void onViewDetached();

        /**
         * Method which attempts to login using the specified credentials
         *
         * @param username string representing username
         * @param password string representing password
         */
        void login(String username, String password);

        /**
         * Logs out of the application
         */
        void logout();


    }
}
