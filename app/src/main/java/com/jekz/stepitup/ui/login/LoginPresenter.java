package com.jekz.stepitup.ui.login;

import com.jekz.stepitup.data.request.LoginManager;
import com.jekz.stepitup.data.request.RemoteLoginModel;

/**
 * Created by evanalmonte on 12/8/17.
 */

class LoginPresenter implements LoginMVP.Presenter, LoginManager.LoginCallback, LoginManager
        .LogoutCallback {
    private static final String TAG = RemoteLoginModel.class.getName();

    private LoginMVP.View loginView;
    private LoginManager loginManager;


    LoginPresenter(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    @Override
    public void onViewAttached(LoginMVP.View loginView) {
        this.loginView = loginView;
        if (loginManager.isLoggedIn()) {
            loginView.disableLogin();
        } else {
            loginView.enableLogin();
        }
    }

    @Override
    public void onViewDetached() {
        loginView = null;
    }

    @Override
    public void login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            loginView.showMessage("Please enter both username and password");
        } else {
            loginView.disableLogin();
            loginManager.login(username, password, this);
        }
    }

    @Override
    public void logout() {
        loginManager.logout(this);
        loginView.enableLogin();
    }

    @Override
    public void loginResult(boolean loginSuccess) {
        if (loginView == null) { return; }
        loginView.enableLogin();
        if (loginSuccess) {
            loginView.showMessage("Login Successful!");
            loginView.startHomeActivity();
        } else {
            loginView.showMessage("Error Logging In");
        }
    }

    @Override
    public void onLogout(boolean logoutSuccessful) {
        if (logoutSuccessful) {
            loginView.showMessage("You are now logged out");
        } else {
            loginView.showMessage("You are already logged out");
        }
    }
}
