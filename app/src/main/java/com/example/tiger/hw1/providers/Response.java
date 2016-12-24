package com.example.tiger.hw1.providers;

public class Response {
    private int responseCode;
    private String body;
    private String token;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Response() {
    }

    public Response(int responseCode, String body) {
        this.responseCode = responseCode;
        this.body = body;
    }
}
