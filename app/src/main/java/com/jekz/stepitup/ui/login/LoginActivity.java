package com.jekz.stepitup.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jekz.stepitup.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        loginPresenter = new LoginPresenter();
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


    @OnClick(R.id.button_login)
    public void onClickLoginButton(View view) {
        loginPresenter.login(usernameText.getText().toString(), passwordText.getText().toString());
    }

    @Override
    public void startLoginActivity() {
        Intent intent = new Intent(this, com.jekz.stepitup.ui.shop.ShopActivity.class);
        startActivity(intent);
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
    public void showMessage(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}