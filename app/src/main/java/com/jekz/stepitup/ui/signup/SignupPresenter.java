package com.jekz.stepitup.ui.signup;


/**
 * Created by evanalmonte on 12/18/17.
 */

public class SignupPresenter implements SignupContract.Presenter {
    private static final String TAG = SignupPresenter.class.getName();

    SignupContract.View view;

    public SignupPresenter() {

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
    }

    @Override
    public void login() {
        if (view == null) { return; }
        view.navigateToLogin();
    }
}
