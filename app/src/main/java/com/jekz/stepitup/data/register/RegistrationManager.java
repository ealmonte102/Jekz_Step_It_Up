package com.jekz.stepitup.data.register;

/**
 * Created by evanalmonte on 12/18/17.
 */

public interface RegistrationManager {
    void register(String username, String password, RegisterCallback callback);

    enum RegisterResult {
        NETWORK_ERROR,
        USERNAME_TAKEN,
        SUCCESS
    }

    interface RegisterCallback {
        void onRegisterResult(RegisterResult result);
    }
}
