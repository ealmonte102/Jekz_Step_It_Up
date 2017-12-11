package com.jekz.stepitup.ui.login;

import android.util.Log;

import com.jekz.stepitup.model.step.Session;
import com.jekz.stepitup.model.step.StepCounter;

/**
 * Created by evanalmonte on 12/8/17.
 */

class LoginPresenter implements LoginMVP.Presenter, LoginMVP.Model.LoginCallback, StepCounter
        .StepCounterCallback {
    final int STEP_GOAL = 100;
    LoginMVP.View loginView;
    LoginMVP.Model model;
    StepCounter stepCounter;

    LoginPresenter(StepCounter stepCounter) {
        model = new LoginModel();
        this.stepCounter = stepCounter;
    }

    @Override
    public void onViewAttached(LoginMVP.View loginView) {
        this.loginView = loginView;
        stepCounter.addListener(this);
    }

    @Override
    public void onViewDetached() {
        stepCounter.removeListener(this);
        loginView = null;
    }

    @Override
    public void login(String username, String password) {
        model.login(username, password, this);
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
        if (loginSuccess) {
            loginView.showMessage("Login Successful!");
            loginView.startLoginActivity();
        } else {
            loginView.showMessage("Error Logging In");
        }
    }

    @Override
    public void onStepDetected(int x) {
        if (loginView == null) { return; }
        loginView.setStepProgress((float) x / STEP_GOAL * 100);
        loginView.setStepText(String.valueOf(x));
    }

    @Override
    public void onSessionEnded(Session session) {
        Log.d("Session Ended", session.toString());
    }
}
