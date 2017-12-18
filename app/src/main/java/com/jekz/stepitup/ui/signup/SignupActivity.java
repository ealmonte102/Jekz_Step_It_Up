package com.jekz.stepitup.ui.signup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jekz.stepitup.R;
import com.jekz.stepitup.data.register.RemoteRegistrationManager;
import com.jekz.stepitup.ui.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends Activity implements SignupContract.View {

    @BindView(R.id.edittext_username)
    EditText usernameEditText;


    @BindView(R.id.edittext_password)
    EditText passwordEditText;

    @BindView(R.id.button_signup)
    Button signupButton;

    @BindView(R.id.login_progress)
    ProgressBar progressBar;

    SignupContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        presenter = new SignupPresenter(new RemoteRegistrationManager());
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewAttached(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onViewDetached();
    }

    @Override
    public void showUsernameError(String message) {
        usernameEditText.setError(message);
        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        usernameEditText.startAnimation(shake);
    }

    @Override
    public void showPasswordError(String message) {
        passwordEditText.setError(message);
        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        passwordEditText.startAnimation(shake);
    }

    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        signupButton.setEnabled(false);
        signupButton.setAlpha(.5f);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        signupButton.setEnabled(true);
        signupButton.setAlpha(1);
    }

    @OnClick({R.id.button_signup, R.id.link_login})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_signup:
                presenter.register(
                        usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                break;
            case R.id.link_login:
                presenter.login();
                break;
        }
    }
}
