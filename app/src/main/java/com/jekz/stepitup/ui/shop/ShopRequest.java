package com.jekz.stepitup.ui.shop;

import android.os.AsyncTask;
import android.util.Log;

import com.jekz.stepitup.data.request.RequestString;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Owner on 12/3/2017.
 */

public class ShopRequest extends AsyncTask<String, Void, JSONObject> {
    private static final String TAG = ShopRequest.class.getName();

    public AsyncResponse delegate;

    private JSONObject postData;
    private String session;

    public ShopRequest(JSONObject postData, String session) {
        this.session = session;
        this.postData = postData;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            Log.d(TAG, "Executing request to " + params[0]);
            // This is getting the url from the string we passed in
            URL url = new URL(params[0]);

            // Create the urlConnection
            if (RequestString.isLocal()) {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Cookie", session);

                // Send the post body
                if (this.postData != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(urlConnection
                            .getOutputStream());
                    writer.write(postData.toString());
                    writer.flush();
                }

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                //receives bytes from server
                LinkedList<Byte> bytes = new LinkedList<Byte>();
                int len;
                byte[] buffer = new byte[4096];
                while (-1 != (len = in.read(buffer))) {
                    for (int i = 0; i < len; i++) {
                        bytes.add(buffer[i]);
                    }
                }

                //convert byte arraylist to array
                int size = bytes.size();
                byte[] b = new byte[size];
                for (int i = 0; i < size; i++) {
                    b[i] = bytes.removeFirst();
                }

                String jsonStr = new String(b, StandardCharsets.UTF_8);

                JSONArray resultArray;
                JSONObject resultObject;

                try {
                    //resultArray = new JSONArray(jsonStr);
                    resultObject = new JSONObject(jsonStr);
                } catch (JSONException e) {
                    //resultArray = new JSONArray("[" + jsonStr + "]")
                    resultObject = new JSONObject(jsonStr);
                }


                return resultObject;
                //return resultArray;
            } else {
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Cookie", session);

                // Send the post body
                if (this.postData != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(urlConnection
                            .getOutputStream());
                    writer.write(postData.toString());
                    writer.flush();
                }

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                //receives bytes from server
                LinkedList<Byte> bytes = new LinkedList<Byte>();
                int len;
                byte[] buffer = new byte[4096];
                while (-1 != (len = in.read(buffer))) {
                    for (int i = 0; i < len; i++) {
                        bytes.add(buffer[i]);
                    }
                }

                //convert byte arraylist to array
                int size = bytes.size();
                byte[] b = new byte[size];
                for (int i = 0; i < size; i++) {
                    b[i] = bytes.removeFirst();
                }

                String jsonStr = new String(b, StandardCharsets.UTF_8);

                JSONArray resultArray;
                JSONObject resultObject;

                try {
                    //resultArray = new JSONArray(jsonStr);
                    resultObject = new JSONObject(jsonStr);
                } catch (JSONException e) {
                    //resultArray = new JSONArray("[" + jsonStr + "]")
                    resultObject = new JSONObject(jsonStr);
                }


                return resultObject;
                //return resultArray;
            }


        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject result) {

        if (result != null) {
            delegate.processFinish(result);
        }

    }
}