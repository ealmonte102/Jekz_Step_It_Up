package com.jekz.stepitup.data.request;

import android.util.Log;

import com.jekz.stepitup.data.LoginPreferences;
import com.jekz.stepitup.data.SharedPrefsManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by evanalmonte on 12/12/17.
 */

public class RemoteLoginModel implements LoginManager, LoginRequest.LoginRequestCallback {
    private static final String TAG = RemoteLoginModel.class.getName();
    private final static String COOKIE_URL = RequestString.getURL();
    private final static String LOGIN_URL = COOKIE_URL + "/login";
    private LoginPreferences loginPreferences;
    private LoginCallback callback;

    public RemoteLoginModel(LoginPreferences loginPreferences) {
        this.loginPreferences = loginPreferences;
    }

    @Override
    public void login(final String username, final String password, final LoginCallback callback) {
        this.callback = callback;
        String cookie = loginPreferences.getString(SharedPrefsManager.Key.SESSION);
        CookieRequest request = new CookieRequest(new CookieRequest.AsynchCookieCallback() {
            @Override
            public void processCookie(String result) {
                LoginRequest loginRequest = new LoginRequest(username, password, result,
                        RemoteLoginModel.this);
                loginRequest.execute(LOGIN_URL);
            }
        });
        request.execute(COOKIE_URL);
    }

    @Override
    public void logout(LogoutCallback callback) {
        if (loginPreferences.getString(SharedPrefsManager.Key.SESSION) == null) {
            callback.onLogout(false);
        } else {
            loginPreferences.remove(
                    SharedPrefsManager.Key.SESSION,
                    SharedPrefsManager.Key.USERNAME,
                    SharedPrefsManager.Key.EXPIRE_DATE);
            callback.onLogout(true);
        }
    }

    @Override
    public boolean isLoggedIn() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, d MMM yyyy H:m:s z", Locale
                .US);
        try {
            Log.d(TAG, "Checking local cookie");
            String localCookie = loginPreferences.getString(SharedPrefsManager.Key.EXPIRE_DATE,
                    "No expire date stored");
            Log.d(TAG, "Current local cookie: " + localCookie);
            Date date = simpleDateFormat.parse(loginPreferences.getString(SharedPrefsManager.Key
                    .EXPIRE_DATE, "No cookie stored"));
            Date current = new Date(System.currentTimeMillis());
            Log.d(TAG, "Parsed date: " + date.toString());
            Log.d(TAG, "Current date: " + current.toString());
            if (current.before(date)) {
                Log.d(TAG, "Local cookie is valid");
                Log.d(TAG, "Session ID: " + getSession());
                Log.d(TAG, "Username: " + getUsername());
                return true;
            }
        } catch (ParseException ignored) {}
        return false;
    }

    @Override
    public String getSession() {
        return loginPreferences.getString(SharedPrefsManager.Key.SESSION);
    }

    @Override
    public String getUsername() {
        return loginPreferences.getString(SharedPrefsManager.Key.USERNAME);
    }

    @Override
    public void onProcessLogin(String cookie, String username) {
        String[] cookieheader = cookie.split(";");
        String sessionID = cookieheader[0];
        String expirationDate = cookieheader[2].split("=")[1];
        loginPreferences.put(SharedPrefsManager.Key.SESSION, sessionID);
        loginPreferences.put(SharedPrefsManager.Key.EXPIRE_DATE, expirationDate);
        loginPreferences.put(SharedPrefsManager.Key.USERNAME, username);
        updateProfile(0, 0, 0, "male");
        callback.loginResult(LoginCallback.LoginResult.SUCCESS);
    }

    @Override
    public void invalidCredentials() {
        Log.d(TAG, "Invalid credentials");
        callback.loginResult(LoginCallback.LoginResult.INVALID_CREDENTIALS);
    }

    @Override
    public void networkError() {
        Log.d(TAG, "Network error");
        callback.loginResult(LoginCallback.LoginResult.NETWORK_ERROR);
    }

    private void updateProfile(int goal, int weight, int height, String gender) {
        loginPreferences.put(SharedPrefsManager.Key.GOAL, goal);
        loginPreferences.put(SharedPrefsManager.Key.WEIGHT, weight);
        loginPreferences.put(SharedPrefsManager.Key.HEIGHT, height);
        loginPreferences.put(SharedPrefsManager.Key.GENDER, gender);
    }
}
