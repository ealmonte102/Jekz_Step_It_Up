package com.jekz.stepitup.ui.login;

import com.jekz.stepitup.model.step.Session;
import com.jekz.stepitup.model.step.StepCounter;

/**
 * Created by evanalmonte on 12/8/17.
 */

class LoginPresenter implements LoginMVP.Presenter, LoginManager.LoginCallback, LoginManager
        .LogoutCallback, StepCounter.StepCounterCallback {
    private static final String TAG = RemoteLoginModel.class.getName();
    final int STEP_GOAL = 100;
    LoginMVP.View loginView;
    LoginManager loginManager;
    StepCounter stepCounter;

    LoginPresenter(StepCounter stepCounter, LoginManager loginManager) {
        this.loginManager = loginManager;
        this.stepCounter = stepCounter;
    }

    @Override
    public void onViewAttached(LoginMVP.View loginView) {
        this.loginView = loginView;
        stepCounter.addListener(this);
        if (loginManager.isLoggedIn()) {
            loginView.enableLoginButton(false);
        } else {
            loginView.enableLogoutButton(true);
            loginView.enableLogoutButton(false);
        }
    }

    @Override
    public void onViewDetached() {
        stepCounter.removeListener(this);
        loginView = null;
    }

    @Override
    public void login(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            loginView.showMessage("Please enter both username and password");
        } else {
            loginView.enableLoginButton(false);
            loginManager.login(username, password, this);
        }
    }

    @Override
    public void logout() {
        loginManager.logout(this);
        loginView.enableLoginButton(true);
        loginView.enableLogoutButton(false);
    }

    @Override
    public void registerSensor(boolean register) {
        if (register) {
            stepCounter.registerSensor();
        } else {
            stepCounter.unregisterSensor();
        }
    }

    @Override
    public void loginResult(boolean loginSuccess) {
        if (loginView == null) { return; }
        loginView.enableLoginButton(true);
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

    @Override
    public void onStepDetected(int x) {
        if (loginView == null) { return; }
        loginView.setStepProgress((float) x / STEP_GOAL * 100);
        loginView.setStepText(String.valueOf(x));
    }

    @Override
    public void onSessionEnded(Session session) { }
}
