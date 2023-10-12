package com.hyeonjs.library;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequester {

    private String url;
    private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36";

    public HttpRequester(String url) {
        this.url = url;
    }

    public static HttpRequester create(String url) {
        return new HttpRequester(url);
    }

    public String get() {
        return execute(false);
    }

    public String post() {
        return execute(true);
    }

    private String execute(boolean isPost) {
        try {
            URL url = new URL(this.url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            if (isPost) {
                con.setRequestMethod("POST");
                con.setDoOutput(true);
            } else {
                con.setRequestMethod("GET");
            }
            con.setConnectTimeout(5000);
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setRequestProperty("User-Agent", userAgent);

            int code = con.getResponseCode();
            DataInputStream dis;
            if (200 <= code && code < 400) dis = new DataInputStream(con.getInputStream());
            else dis = new DataInputStream(con.getErrorStream());
            InputStreamReader isr = new InputStreamReader(dis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder str = new StringBuilder(br.readLine());
            String line;
            while ((line = br.readLine()) != null) {
                str.append("\n").append(line);
            }
            br.close();
            isr.close();
            dis.close();
            return str.toString();
        } catch (Exception e) {
            return e.toString();
//            Log.i("_hjs", e.toString());
        }
    }

}