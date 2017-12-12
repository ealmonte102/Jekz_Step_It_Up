package com.jekz.stepitup.ui.login;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by evanalmonte on 12/11/17.
 */

public class LoginRequest extends AsyncTask<String, Integer, String> {
    boolean getCookie;
    String connectionid;
    AsyncLoginDelegate delegate;

    LoginRequest(boolean getCookie, AsyncLoginDelegate delegate) {
        super();
        this.getCookie = getCookie;
        this.delegate = delegate;
    }

    public void setConnectionId(String s) {
        connectionid = s;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL("https://jekz.herokuapp.com/login");

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            if (!getCookie) {
                urlConnection.setRequestProperty("Cookie", connectionid);
            }

            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            JSONObject data = new JSONObject();
            data.put("username", "evanalmonte");
            data.put("password", "andrew32!");
            out.write(data.toString());
            out.flush();
            out.close();

            return urlConnection.getHeaderField("Set-Cookie").split(";")[0];
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.loginResult(result);
    }
}
