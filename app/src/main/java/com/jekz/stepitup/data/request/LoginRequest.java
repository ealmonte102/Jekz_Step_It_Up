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
import java.net.URL;
import java.security.cert.Certificate;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by evanalmonte on 12/12/17.
 */

public class LoginRequest extends AsyncTask<String, Integer, String> {
    private static final String TAG = LoginRequest.class.getName();
    private String cookie;
    private String username;
    private String password;
    private LoginRequest.LoginRequestCallback callback;

    public LoginRequest(String username, String password, String cookie, LoginRequestCallback
            callback) {
        this.username = username;
        this.password = password;
        this.cookie = cookie;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);

            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Cookie", cookie);
            //printHttpsCert(urlConnection);
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
        return "ERROR";
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, result);
        if (result.contains(username.toLowerCase() + "'s Home Page")) {
            callback.onProcessLogin(cookie, username);
        } else if (result.contains("ERROR")) {
            callback.networkError();
        } else {
            callback.invalidCredentials();
        }
    }

    private void printHttpsCert(HttpsURLConnection con) {

        if (con != null) try {

            Log.d(TAG, "Response Code : " + con.getResponseCode());
            Log.d(TAG, "Cipher Suite : " + con.getCipherSuite());
            Log.d(TAG, "\n");

            Certificate[] certs = con.getServerCertificates();
            for (Certificate cert : certs) {
                Log.d(TAG, "Cert Type : " + cert.getType());
                Log.d(TAG, "Cert Hash Code : " + cert.hashCode());
                Log.d(TAG, "Cert Public Key Algorithm : "
                           + cert.getPublicKey().getAlgorithm());
                Log.d(TAG, "Cert Public Key Format : "
                           + cert.getPublicKey().getFormat());
                Log.d(TAG, "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public interface LoginRequestCallback {
        void onProcessLogin(String cookie, String username);

        void invalidCredentials();

        void networkError();
    }
}