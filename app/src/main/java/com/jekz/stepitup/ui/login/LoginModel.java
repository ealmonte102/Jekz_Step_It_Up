package com.jekz.stepitup.ui.login;

import java.util.HashMap;

/**
 * Created by evanalmonte on 12/8/17.
 */

class LoginModel implements LoginMVP.Model {
    private final static HashMap<String, String> TEST_USERS = new HashMap<>();
    private final static String TEST_PASSWORD = "JEKZ";

    static {
        String[] testUsernames = {"Evan", "Zaheen", "Kevin", "Jun"};
        for (String name : testUsernames) {
            TEST_USERS.put(name.toLowerCase(), TEST_PASSWORD);
        }
    }

    @Override
    public void login(String username, String password, LoginCallback callback) {
        if (username.isEmpty() || password.isEmpty()) {callback.loginResult(false); }
        if (!TEST_USERS.containsKey(username.toLowerCase())) {
            callback.loginResult(false);
            return;
        }
        if (TEST_USERS.get(username.toLowerCase()).equals(password)) {
            callback.loginResult(true);
            return;
        }
        callback.loginResult(false);
    }
}
