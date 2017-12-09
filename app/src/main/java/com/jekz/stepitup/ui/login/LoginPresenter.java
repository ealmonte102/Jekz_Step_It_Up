package com.jekz.stepitup.ui.login;

import android.os.Handler;
import android.util.Log;

/**
 * Created by evanalmonte on 12/8/17.
 */

public class LoginPresenter implements LoginMVP.Presenter, LoginMVP.Model.LoginCallback {
    final Handler handler = new Handler();
    LoginMVP.View loginView;
    LoginMVP.Model model;
    int steps;
    Runnable runnable;

    public LoginPresenter() {
        model = new LoginModel();
    }

    @Override
    public void onViewAttached(LoginMVP.View view) {
        this.loginView = view;
        runnable = new Runnable() {
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
    public void onViewDetached() {
        this.loginView = null;
        handler.removeCallbacks(runnable);
    }

    @Override
    public void login(String username, String password) {
        handler.removeCallbacks(runnable);
        model.login(username, password, this);
    }

    @Override
    public void loginResult(boolean loginSuccess) {
        Log.d("TAG", "Login Result Called");
        if (loginSuccess) {
            loginView.showMessage("Login Successful!");
            loginView.startLoginActivity();
        } else {
            loginView.showMessage("Error Logging In");
        }
    }
}
