package com.jekz.stepitup.data.request;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
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

    LoginRequest(String username, String password, String cookie, LoginRequestCallback callback) {
        this.username = username;
        this.password = password;
        this.cookie = cookie;
        this.callback = callback;
    }

    //CHECK IF COOKIE IS VALID
    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);

            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(false);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Cookie", cookie);
            printHttpsCert(urlConnection);

            return urlConnection.getHeaderField("Set-Cookie");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    @Override
    protected void onPostExecute(String result) {
        StringBuilder builder = new StringBuilder();
        if (result == null) {
            builder.append("Cookie is VALID");
            Log.d(TAG, builder.toString());
            callback.cookieValid();
        } else {
            builder.append("Cookie is INVALID");
            builder.append("\nNew Cookie: ").append(result);
            Log.d(TAG, builder.toString());
            LoginVerifier verifier = new LoginVerifier(result, username, password, callback);
            verifier.execute("http://jekz.herokuapp.com/login");
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

    interface LoginRequestCallback extends LoginVerifier.VerifierCallback {
        void cookieValid();
    }
}