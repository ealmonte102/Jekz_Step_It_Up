package com.jekz.stepitup.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jekz.stepitup.JekzApplication;
import com.jekz.stepitup.R;
import com.jekz.stepitup.data.LoginPreferences;
import com.jekz.stepitup.data.SharedPrefsManager;
import com.jekz.stepitup.ui.home.HomeActivity;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity implements LoginMVP.View {
    @BindView(R.id.circular_progress_steps)
    CircularProgressBar circularProgressBar;

    @BindView(R.id.edittext_username)
    EditText usernameText;

    @BindView(R.id.edittext_password)
    EditText passwordText;

    @BindView(R.id.text_step_count)
    TextView stepText;

    LoginMVP.Presenter loginPresenter;
    LoginPreferences loginPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginPreferences = SharedPrefsManager.getInstance(getApplicationContext());
        loginPresenter = new LoginPresenter(
                ((JekzApplication) (getApplication())).getStepCounter(), loginPreferences);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginPresenter.registerSensor(true);
    }


    @Override
    protected void onPause() {
        super.onPause();
        loginPresenter.registerSensor(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginPresenter.onViewAttached(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.onViewDetached();
    }


    @OnClick({R.id.button_login, R.id.button_logout})
    public void onClickLoginButton(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                loginPresenter.login(usernameText.getText().toString(),
                        passwordText.getText().toString());
                break;
            case R.id.button_logout:
                loginPresenter.logout();
                break;
        }
    }

    @OnClick({R.id.button_start, R.id.button_end})
    public void sessionButton(View view) {
        switch (view.getId()) {
            case R.id.button_start:
                loginPresenter.registerSensor(true);
                break;
            case R.id.button_end:
                loginPresenter.registerSensor(false);
                break;
        }
    }

    @Override
    public void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void setStepText(String text) {
        stepText.setText(text);
    }

    @Override
    public void setStepProgress(float progress) {
        circularProgressBar.setProgress(progress);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
