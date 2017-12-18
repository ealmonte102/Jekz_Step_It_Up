package com.jekz.stepitup.data.register;

import com.jekz.stepitup.data.register.RegisterRequest.RegisterRequestCallback;

/**
 * Created by evanalmonte on 12/18/17.
 */

public interface RegistrationManager {
    void register(String username, String password, RegisterRequestCallback callback);
}
