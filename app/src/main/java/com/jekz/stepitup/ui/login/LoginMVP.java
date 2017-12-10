package com.jekz.stepitup.ui.login;

/**
 * Created by evanalmonte on 12/8/17.
 */

/**
 * Contract for Login MVP
 */
interface LoginMVP {
    /**
     * Interface representing a view
     */
    interface View {
        void showMessage(String error);

        void startLoginActivity();

        void setStepText(String text);

        void setStepProgress(float progress);
    }

    /**
     * Interface representing a presenter
     */
    interface Presenter {
        void onViewAttached(LoginMVP.View view);

        void onViewDetached();

        /**
         * Method which attempts to login using the specified credentials
         *
         * @param username string representing username
         * @param password string representing password
         */
        void login(String username, String password);

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
         * Interface representing callback to be notified on completion of login
         */
        interface LoginCallback {
            /**
             * @param loginSuccess true if login success, false otherwise
             */
            void loginResult(boolean loginSuccess);
        }
    }

}
