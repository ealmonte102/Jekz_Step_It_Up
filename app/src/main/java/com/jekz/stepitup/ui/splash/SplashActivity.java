package com.jekz.stepitup.ui.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jekz.stepitup.data.LoginPreferences;
import com.jekz.stepitup.data.SharedPrefsManager;
import com.jekz.stepitup.ui.home.HomeActivity;
import com.jekz.stepitup.ui.login.LoginActivity;

public class SplashActivity extends Activity {
    LoginPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = SharedPrefsManager.getInstance(getApplicationContext());
        Intent intent;
        if (preferences.getString(SharedPrefsManager.Key.SESSION) != null) {
            intent = new Intent(this, HomeActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
