package com.jekz.stepitup.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jekz.stepitup.R;
import com.jekz.stepitup.data.SharedPrefsManager;
import com.jekz.stepitup.data.request.RemoteLoginModel;
import com.jekz.stepitup.ui.home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity implements LoginMVP.View {
    @BindView(R.id.username)
    EditText usernameText;

    @BindView(R.id.password)
    EditText passwordText;


    @BindView(R.id.button_login)
    Button loginButton;

    @BindView(R.id.login_progress)
    ProgressBar progressBar;


    LoginMVP.Presenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        SharedPrefsManager manager = SharedPrefsManager.getInstance(getApplicationContext());
        loginPresenter = new LoginPresenter(new RemoteLoginModel(manager));
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

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @OnClick(R.id.button_login)
    public void onButtonClicked(View view) {
        loginPresenter.login(usernameText.getText().toString(), passwordText.getText().toString());
    }

    @Override
    public void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);
        loginButton.setAlpha(.5f);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        loginButton.setEnabled(true);
        loginButton.setAlpha(1);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
