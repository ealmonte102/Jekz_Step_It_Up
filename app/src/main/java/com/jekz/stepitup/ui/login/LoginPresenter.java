package com.jekz.stepitup.ui.login;

import com.jekz.stepitup.data.request.LoginManager;
import com.jekz.stepitup.data.request.RemoteLoginModel;

/**
 * Created by evanalmonte on 12/8/17.
 */

class LoginPresenter implements LoginContract.Presenter, LoginManager.LoginCallback, LoginManager
        .LogoutCallback {
    private static final String TAG = RemoteLoginModel.class.getName();

    private LoginContract.View view;
    private LoginManager loginManager;


    LoginPresenter(LoginManager loginManager) {
        this.loginManager = loginManager;
    }

    @Override
    public void onViewAttached(LoginContract.View loginView) {
        this.view = loginView;
    }

    @Override
    public void onViewDetached() {
        view = null;
    }

    @Override
    public void login(String username, String password) {
        if (username.isEmpty()) {
            view.showUsernameError("Username cannot be empty");
        }
        if (password.isEmpty()) {
            view.showPasswordError("Password cannot be empty");
        } else {
            view.showProgress();
            loginManager.login(username.trim(), password, this);
        }
    }

    @Override
    public void signup() {
        view.navigateToSignup();
    }

    @Override
    public void loginResult(LoginResult result) {
        if (view == null) { return; }
        view.hideProgress();
        switch (result) {
            case SUCCESS:
                view.showMessage("Login Successful!");
                view.navigateToHome();
                break;
            case INVALID_CREDENTIALS:
                view.showMessage("Username and password do not match! Try again");
                view.showPasswordError("Password and username don't match");
                view.showUsernameError("Username and password don't match");
                break;
            case NETWORK_ERROR:
                view.showMessage("Error connecting with server, Please try again");
                break;
        }
    }

    @Override
    public void onLogout(boolean logoutSuccessful) { }
}
