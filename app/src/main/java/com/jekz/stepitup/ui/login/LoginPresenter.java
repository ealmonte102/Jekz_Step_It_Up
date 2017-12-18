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
            loginView.showProgress();
            loginManager.login(username, password, this);
        }
    }

    @Override
    public void loginResult(LoginResult result) {
        if (loginView == null) { return; }
        loginView.hideProgress();
        switch (result) {
            case SUCCESS:
                loginView.showMessage("Login Successful!");
                loginView.startHomeActivity();
                break;
            case INVALID_CREDENTIALS:
                loginView.showMessage("Username and password do not match");
                break;
            case NETWORK_ERROR:
                loginView.showMessage("Could not connect, please try again with internet " +
                                      "connection");
                break;
        }
    }

    @Override
    public void onLogout(boolean logoutSuccessful) { }
}
