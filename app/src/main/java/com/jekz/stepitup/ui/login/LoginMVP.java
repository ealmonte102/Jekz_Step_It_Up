package com.jekz.stepitup.ui.login;

/**
 * Created by evanalmonte on 12/8/17.
 */

public interface LoginMVP {
    interface View {
        void showMessage(String error);

        void startLoginActivity();

        void setStepText(String text);

        void setStepProgress(float progress);
    }

    interface Presenter {
        void onViewAttached(LoginMVP.View view);

        void onViewDetached();

        void login(String username, String password);
    }

    interface Model {
        void login(String username, String password, LoginCallback callback);

        interface LoginCallback {
            void loginResult(boolean loginSuccess);
        }
    }

}
