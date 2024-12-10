package com.example.loginapp;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ApiHandler {

    public interface ResponseCallback {
        void onResponse(String response);
        void onError(String error);
    }

    public void post(String apiUrl, HashMap<String, String> params, ResponseCallback callback) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/json");

                    JSONObject jsonObject = new JSONObject();
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        jsonObject.put(entry.getKey(), entry.getValue());
                    }

                    OutputStream os = connection.getOutputStream();
                    os.write(jsonObject.toString().getBytes());
                    os.close();

                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                    br.close();

                    return response.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String response) {
                if (response != null) {
                    callback.onResponse(response);
                } else {
                    callback.onError("Failed to connect to the server.");
                }
            }
        }.execute();
    }
}
