package com.backtolife.survey.server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class HTTPUtil {
    public static String postHTTP(String url, String json){
        // TODO(Assaf): Use SSL here.
        Scanner httpResponseScanner;
        try {
            URL serverUrl = new URL(url);
            URLConnection urlConnection = serverUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpConnection.setDoOutput(true);
            BufferedWriter httpRequestBodyWriter = new BufferedWriter(new
                    OutputStreamWriter(httpConnection.getOutputStream()));
            httpRequestBodyWriter.write(json);
            httpRequestBodyWriter.close();
            if(httpConnection.getResponseCode() != 200){
                System.out.println("HTTP post error: " + httpConnection.getResponseMessage());
                return null;
            }

            if (httpConnection.getInputStream() == null) {
                System.out.println("No stream");
                return "";
            }
            httpResponseScanner = new Scanner(httpConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        StringBuilder resp = new StringBuilder();
        while (httpResponseScanner.hasNext()) {
            String line = httpResponseScanner.nextLine();
            resp.append(line);
        }
        httpResponseScanner.close();
        return resp.toString();
    }
}
