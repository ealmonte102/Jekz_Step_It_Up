package com.jekz.stepitup.ui.signup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jekz.stepitup.R;
import com.jekz.stepitup.ui.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends Activity implements SignupContract.View {

    @BindView(R.id.edittext_username)
    EditText usernameEditText;


    @BindView(R.id.edittext_password)
    EditText passwordEditText;

    SignupContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        presenter = new SignupPresenter();
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
    }

    @Override
    public void showPasswordError(String message) {
        passwordEditText.setError(message);
    }

    @Override
    public void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
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
