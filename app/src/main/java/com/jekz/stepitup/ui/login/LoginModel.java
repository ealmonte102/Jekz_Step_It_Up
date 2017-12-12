package com.jekz.stepitup.ui.login;

import com.jekz.stepitup.data.LoginPreferences;
import com.jekz.stepitup.data.SharedPrefsManager.Key;

import java.util.HashMap;

class LoginModel implements LoginMVP.Model {

    private final static HashMap<String, String> TEST_USERS = new HashMap<>();
    private final static String TEST_PASSWORD = "JEKZ";

    static {
        String[] testUsernames = {"Evan", "Zaheen", "Kevin", "Jun"};
        for (String name : testUsernames) {
            TEST_USERS.put(name.toLowerCase(), TEST_PASSWORD);
        }
    }

    private LoginPreferences loginPreferences;

    public LoginModel(LoginPreferences loginPreferences) {
        this.loginPreferences = loginPreferences;
    }

    @Override
    public void login(String username, String password, LoginCallback callback) {
        if (username.isEmpty() || password.isEmpty()) {
            callback.loginResult(false);
            return;
        }
        if (!TEST_USERS.containsKey(username.toLowerCase())) {
            callback.loginResult(false);
            return;
        }
        if (!TEST_USERS.get(username.toLowerCase()).equals(password)) {
            callback.loginResult(false);
            return;
        }
        loginPreferences.put(Key.SESSION, "YES");
        loginPreferences.put(Key.USERNAME, username);
        callback.loginResult(true);
    }

    @Override
    public boolean isLoggedIn() {
        return loginPreferences.getString(Key.SESSION) != null;
    }

    @Override
    public void logout(LoginCallback callback) {
        if (loginPreferences.getString(Key.SESSION) == null) {
            callback.onLogout(false);
        } else {
            loginPreferences.remove(Key.SESSION, Key.USERNAME);
            callback.onLogout(true);
        }
    }
}
