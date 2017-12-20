package com.jekz.stepitup.data.request;

/**
 * Created by evanalmonte on 12/18/17.
 */

public class RequestString {

    private static final String BASE_URL = "http://10.0.2.2:3000";
    private static final String BASE_HEROKU_URL = "https://jekz.herokuapp.com";
    private static boolean localMode = false;

    public static String getURL() {
        return localMode ? BASE_URL : BASE_HEROKU_URL;
    }

    public static boolean isLocal() {
        return localMode;
    }
}
