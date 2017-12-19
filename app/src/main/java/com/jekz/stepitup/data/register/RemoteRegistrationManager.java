package com.jekz.stepitup.data.register;

import com.jekz.stepitup.data.request.RequestString;

/**
 * Created by evanalmonte on 12/18/17.
 */

public class RemoteRegistrationManager implements RegistrationManager {
    private static final String REGISTRATION_URL = RequestString.getURL() + "/signup";
    RegisterRequest.RegisterRequestCallback callback;

    @Override
    public void register(String username, String password, RegisterRequest
            .RegisterRequestCallback callback) {
        this.callback = callback;
        RegisterRequest registerRequest = new RegisterRequest(username, password, callback);
        registerRequest.execute(REGISTRATION_URL);
    }
}
