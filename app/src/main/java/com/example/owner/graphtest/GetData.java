package com.example.owner.graphtest;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;
import org.json.JSONObject;

public class GetData {
    static String url1 = "https://jekz.herokuapp.com/db/session";
    static String url2 = "http://localhost:3000/test";
    static URL url;
    static HttpsURLConnection conn = null;

    public static void main(String[] args){
        connection();
    }


    public static void connection(){
        try {
            url = new URL(url1);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());

            JSONObject data = new JSONObject();
            data.put("userid", 1);
            data.put("start_time", "'2017-11-30 10:45:00 -5:00'");
            data.put("end_time", "'2017-11-30 10:46:00 -5:00'" );
            data.put("steps", 4000000);
            out.write(data.toString());
            out.flush();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            int readAmount = in.available();
            byte[] bytes = new byte[readAmount];
            in.read(bytes, 0, readAmount);
            for (byte b : bytes){
                System.out.print((char) b);
            }
            System.out.println();
        } catch (MalformedURLException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
