package com.jekz.stepitup.ui.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jekz.stepitup.data.SharedPrefsManager;
import com.jekz.stepitup.data.request.LoginManager;
import com.jekz.stepitup.data.request.RemoteLoginModel;
import com.jekz.stepitup.ui.home.HomeActivity;
import com.jekz.stepitup.ui.login.LoginActivity;

public class SplashActivity extends Activity {
    private static final String TAG = SplashActivity.class.getName();
    public LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefsManager manager = SharedPrefsManager.getInstance(getApplicationContext());
        loginManager = new RemoteLoginModel(manager);
        loginManager.logout(new LoginManager.LogoutCallback() {
            @Override
            public void onLogout(boolean logoutSuccessful) {

            }
        });
        Intent intent;
        if (loginManager.isLoggedIn()) {
            intent = new Intent(this, HomeActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
