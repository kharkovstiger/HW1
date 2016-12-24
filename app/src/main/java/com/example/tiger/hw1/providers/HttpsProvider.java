package com.example.tiger.hw1.providers;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpsProvider {

    private static final String BASE_URL="https://telranstudentsproject.appspot.com/_ah/api/contactsApi/v1";

    private static HttpsProvider ourInstance = new HttpsProvider();

    public static HttpsProvider getInstance() {
        return ourInstance;
    }

    private HttpsProvider() {
    }

    public Response post(String json, String path) throws IOException {
        Response response =new Response();
        URL url=new URL(BASE_URL+path);
        HttpsURLConnection connection= (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(),"UTF-8"));
        writer.write(json);
        writer.flush();
        writer.close();

        String body="";
        BufferedReader reader;
        if (connection.getResponseCode()<400)
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        else
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            body += line;
        }

        Log.d("MY_TAG",connection.getResponseCode() + " "+body);
        response.setResponseCode(connection.getResponseCode());
        response.setBody(body);

        return response;
    }

    public Response post(String json, String path, String token) throws IOException {
        Response response =new Response();
        URL url=new URL(BASE_URL+path);
        HttpsURLConnection connection= (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", token);

        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(),"UTF-8"));
        writer.write(json);
        writer.flush();
        writer.close();

        String body="";
        BufferedReader reader;
        if (connection.getResponseCode()<400)
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        else
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            body += line;
        }

        Log.d("MY_TAG",connection.getResponseCode() + " "+body);
        response.setResponseCode(connection.getResponseCode());
        response.setBody(body);

        return response;
    }

    public Response get(String token, String path) throws IOException {
        Response response =new Response();
        URL url=new URL(BASE_URL+path);
        HttpsURLConnection connection= (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", token);
        connection.connect();

        String body="";
        BufferedReader reader;
        if (connection.getResponseCode()<400)
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        else
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            body += line;
        }

        Log.d("MY_TAG",connection.getResponseCode() + " "+body);
        response.setResponseCode(connection.getResponseCode());
        response.setBody(body);

        return response;
    }
}
