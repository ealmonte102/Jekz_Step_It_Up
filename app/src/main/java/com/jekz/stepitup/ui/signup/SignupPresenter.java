package com.jekz.stepitup.ui.signup;


import com.jekz.stepitup.data.register.RegisterRequest;
import com.jekz.stepitup.data.register.RegistrationManager;

/**
 * Created by evanalmonte on 12/18/17.
 */

public class SignupPresenter implements SignupContract.Presenter, RegisterRequest
        .RegisterRequestCallback {
    private static final String TAG = SignupPresenter.class.getName();
    private final RegistrationManager registrationManager;

    SignupContract.View view;

    public SignupPresenter(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

    @Override
    public void onViewAttached(SignupContract.View view) {
        this.view = view;
    }

    @Override
    public void onViewDetached() {
        view = null;
    }

    @Override
    public void register(String username, String password) {
        boolean emptyString = false;
        if (username.isEmpty()) {
            view.showUsernameError("Username cannot be empty");
            emptyString = true;
        }
        if (password.isEmpty()) {
            view.showPasswordError("Password cannot be empty");
            emptyString = true;
        }
        if (emptyString) { return; }
        view.showProgress();
        registrationManager.register(username, password, this);
    }

    @Override
    public void login() {
        if (view == null) { return; }
        view.navigateToLogin();
    }

    @Override
    public void processRegistration(RegisterResult result) {
        view.hideProgress();
        switch (result) {
            case NETWORK_ERROR:
                view.showMessage("Error connecting with server, Please try again");
                break;
            case SUCCESSFUL:
                view.showMessage("You have successfully registered");
                view.navigateToLogin();
                break;
            case USERNAME_TAKEN:
                view.showUsernameError("Username is already taken");
                break;
            case INVALID_USERNAME:
                view.showUsernameError("Username can only contain letters and digits");
        }
    }
}
