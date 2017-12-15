package com.jekz.stepitup.data.request;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by evanalmonte on 12/12/17.
 */

class LoginVerifier extends AsyncTask<String, Integer, String> {
    private static final String TAG = LoginVerifier.class.getName();
    private String cookie;
    private String username;
    private String password;
    private VerifierCallback callback;

    LoginVerifier(String cookie, String username, String password, VerifierCallback callback) {
        this.cookie = cookie;
        this.username = username;
        this.password = password;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Cookie", cookie);

            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            JSONObject data = new JSONObject();
            data.put("username", username);
            data.put("password", password);
            out.write(data.toString());
            out.flush();
            out.close();

            StringBuilder builder = new StringBuilder();
            InputStream is;
            is = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                builder.append(inputLine).append("\n");
            }
            return builder.toString();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return "Error while attempting connection";
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.contains(username + "'s Home Page")) {
            Log.d(TAG, username + " is now logged in");
            callback.onValidCredentials(cookie);
        } else {
            Log.d(TAG, "user: " + username + " and supplied password do not match");
            callback.onInvalidCredentials();
        }
    }

    interface VerifierCallback {
        void onValidCredentials(String cookie);

        void onInvalidCredentials();
    }
}
