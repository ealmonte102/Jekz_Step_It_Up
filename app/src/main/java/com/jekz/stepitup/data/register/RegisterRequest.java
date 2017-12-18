package com.jekz.stepitup.data.register;

import android.os.AsyncTask;
import android.util.Log;

import com.jekz.stepitup.data.register.RegisterRequest.RegisterRequestCallback.RegisterResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by evanalmonte on 12/18/17.
 */

public class RegisterRequest extends AsyncTask<String, Integer, String> {
    private static final String TAG = RegisterRequest.class.getName();
    private String username;
    private String password;
    private RegisterRequestCallback callback;

    public RegisterRequest(String username, String password, RegisterRequestCallback
            callback) {
        this.username = username;
        this.password = password;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);

            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
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
        return RegisterResult.NETWORK_ERROR.name();
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, result);
        if (result.equals(RegisterResult.NETWORK_ERROR.toString())) {
            callback.processRegistration(RegisterResult.NETWORK_ERROR);
        } else if (result.contains("Error signing up")) {
            callback.processRegistration(RegisterResult.USERNAME_TAKEN);
        } else {
            callback.processRegistration(RegisterResult.SUCCESSFUL);
        }
    }

    public interface RegisterRequestCallback {
        void processRegistration(RegisterResult result);

        enum RegisterResult {
            USERNAME_TAKEN,
            SUCCESSFUL,
            NETWORK_ERROR
        }
    }
}
