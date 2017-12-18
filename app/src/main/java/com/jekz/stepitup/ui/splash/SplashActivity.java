package com.jekz.stepitup.ui.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.jekz.stepitup.data.SharedPrefsManager;
import com.jekz.stepitup.data.request.CookieRequest;
import com.jekz.stepitup.data.request.LoginManager;
import com.jekz.stepitup.data.request.LoginRequest;
import com.jekz.stepitup.data.request.RemoteLoginModel;
import com.jekz.stepitup.ui.login.LoginActivity;

public class SplashActivity extends Activity {
    private static final String TAG = SplashActivity.class.getName();
    public LoginManager loginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefsManager manager = SharedPrefsManager.getInstance(getApplicationContext());
        loginManager = new RemoteLoginModel(manager);

        Intent intent;
        intent = new Intent(this, LoginActivity.class);
        /*
        if (loginManager.isLoggedIn()) {
            intent = new Intent(this, HomeActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }*/
        startActivity(intent);
        finish();
    }

    public void testLogin() {
        CookieRequest cookieRequest = new CookieRequest(new CookieRequest.AsynchCookieCallback() {
            @Override
            public void processCookie(String cookie) {
                LoginRequest request = new LoginRequest("EVANALMONTE", "andrew32!", cookie, new
                        LoginRequest.LoginRequestCallback() {

                            @Override
                            public void onProcessLogin(String cookie, String username) {
                            }

                            @Override
                            public void invalidCredentials() {

                            }

                            @Override
                            public void networkError() {

                            }
                        });
                request.execute("https://jekz.herokuapp.com/login");
            }
        });
        cookieRequest.execute("https://jekz.herokuapp.com/");
    }
}
