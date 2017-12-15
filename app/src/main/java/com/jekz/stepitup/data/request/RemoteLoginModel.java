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
    private final static String LOGIN_URL = "https://jekz.herokuapp.com/login";
    private LoginPreferences loginPreferences;
    private String username;
    private String password;
    private LoginCallback callback;

    public RemoteLoginModel(LoginPreferences loginPreferences) {
        this.loginPreferences = loginPreferences;
    }

    @Override
    public void login(String username, String password, final LoginCallback callback) {
        Log.d(TAG, "Trying remote connection login");
        this.username = username;
        this.password = password;
        this.callback = callback;
        String cookie = loginPreferences.getString(SharedPrefsManager.Key.SESSION);
        LoginRequest loginRequest = new LoginRequest(username, password, cookie, this);
        loginRequest.execute(LOGIN_URL);
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
                    "Does not exist");
            Log.d(TAG, "Current local cookie: " + localCookie);
            Date date = simpleDateFormat.parse(loginPreferences.getString(SharedPrefsManager.Key
                    .EXPIRE_DATE, ""));
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
    public void onValidCredentials(String cookie) {
        String[] cookieheader = cookie.split(";");
        String sessionID = cookieheader[0];
        String expirationDate = cookieheader[2].split("=")[1];
        Log.d(TAG, "New generated cookie: " + sessionID + "\n" +
                   "Expires:" + expirationDate);
        loginPreferences.put(SharedPrefsManager.Key.SESSION, sessionID);
        loginPreferences.put(SharedPrefsManager.Key.EXPIRE_DATE, expirationDate);
        loginPreferences.put(SharedPrefsManager.Key.USERNAME, username);
        callback.loginResult(true);
    }

    @Override
    public void onInvalidCredentials() {
        callback.loginResult(false);
    }

    @Override
    public void cookieValid() {
        callback.loginResult(true);
    }

}
