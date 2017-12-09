package com.jekz.stepitup.ui.login;

import android.os.Handler;

/**
 * Created by evanalmonte on 12/8/17.
 */

public class LoginPresenter implements LoginMVP.Presenter, LoginMVP.Model.LoginCallback {
    LoginMVP.View loginView;
    LoginMVP.Model model;
    int steps;

    public LoginPresenter() {
        model = new LoginModel();
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                loginView.setStepText(String.valueOf(steps));
                loginView.setStepProgress(steps);
                steps++;
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onViewAttached(LoginMVP.View view) {
        this.loginView = view;
    }

    @Override
    public void onViewDetached() {
        this.loginView = null;
    }

    @Override
    public void login(String username, String password) {
        model.login(username, password, this);
    }

    @Override
    public void loginResult(boolean loginSuccess) {
        if (loginSuccess) {
            loginView.showMessage("Login Successful!");
            loginView.startLoginActivity();
        } else {
            loginView.showMessage("Error Logging In");
        }
    }
}
