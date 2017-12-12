package com.jekz.stepitup.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by evanalmonte on 12/11/17.
 */

public class SharedPrefsManager implements LoginPreferences {
    private static final String PREF_NAME = "com.jekz.stepitup.login.LoginDetails";
    private static SharedPrefsManager INSTANCE;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private SharedPrefsManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPrefsManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SharedPrefsManager(context);
        }
        return INSTANCE;
    }

    @Override
    public void put(Key key, String value) {
        editor = prefs.edit();
        editor.putString(key.name(), value);
        editor.apply();
        editor = null;
    }

    @Override
    public void put(Key key, int value) {
        editor = prefs.edit();
        editor.putInt(key.name(), value);
        editor.apply();
        editor = null;
    }

    @Override
    public String getString(Key key, String defaultValue) {
        return prefs.getString(key.name(), defaultValue);
    }

    @Override
    public String getString(Key key) {
        return prefs.getString(key.name(), null);
    }

    @Override
    public int getInt(Key key) {
        return prefs.getInt(key.name(), 0);
    }

    @Override
    public int getInt(Key key, int defaultValue) {
        return prefs.getInt(key.name(), defaultValue);

    }

    @Override
    public void remove(Key... keys) {
        editor = prefs.edit();
        for (Key key : keys) {
            editor.remove(key.name());
        }
        editor.apply();
        editor = null;
    }

    public enum Key {
        USERNAME,
        SESSION
    }
}
