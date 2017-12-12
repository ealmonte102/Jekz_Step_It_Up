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

        /**
         * Display the number of steps walked so far
         * @param text number of steps walked for the current session
         */
        void setStepText(String text);

        /**
         * Display the current progress
         * @param progress current progress
         */
        void setStepProgress(float progress);
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

        void registerSensor(boolean register);
    }


    /**
     * Interface representing a model
     */
    interface Model {
        /**
         * Attempt to login in using specified username and password, on completion, callback is
         * notified
         */
        void login(String username, String password, LoginCallback callback);

        /**
         * Check whether the user is logged in
         *
         * @return true if logged in, false otherwise
         */
        boolean isLoggedIn();

        void logout(LoginCallback callback);
        /**
         * Interface representing callback to be notified on completion of login
         */
        interface LoginCallback {
            /**
             * @param loginSuccess true if login success, false otherwise
             */
            void loginResult(boolean loginSuccess);

            void onLogout(boolean logoutSuccessful);
        }
    }

}
