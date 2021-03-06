package com.jekz.stepitup.ui.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jekz.stepitup.data.SharedPrefsManager;
import com.jekz.stepitup.data.register.RegisterRequest;
import com.jekz.stepitup.data.register.RegistrationManager;
import com.jekz.stepitup.data.register.RemoteRegistrationManager;
import com.jekz.stepitup.data.request.CookieRequest;
import com.jekz.stepitup.data.request.LoginManager;
import com.jekz.stepitup.data.request.LoginRequest;
import com.jekz.stepitup.data.request.RemoteLoginModel;
import com.jekz.stepitup.data.request.RequestString;
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
        Intent intent;
        if (loginManager.isLoggedIn()) {
            intent = new Intent(this, HomeActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }
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
                request.execute(RequestString.getURL() + "/login");
            }
        });
        cookieRequest.execute(RequestString.getURL() + "/");
    }

    public void testRegister() {
        RegistrationManager manager = new RemoteRegistrationManager();
        manager.register("Dodecahedron", "123", new RegisterRequest.RegisterRequestCallback() {
            @Override
            public void processRegistration(RegisterResult result) {
                Log.d(TAG, result.name());
            }
        });
    }
}
