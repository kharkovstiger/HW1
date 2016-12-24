package com.example.tiger.hw1.tasks;

import android.os.AsyncTask;
import android.view.View;

import com.example.tiger.hw1.Person;
import com.example.tiger.hw1.providers.HttpsProvider;
import com.example.tiger.hw1.providers.Response;
import com.google.gson.Gson;

import java.io.IOException;

public class AddUserTask extends AsyncTask<Void, Void, Response> {

    private Person person;
    private String token;
    private AddUserTaskListener listener;

    public AddUserTask(Person person, String token, AddUserTaskListener listener) {
        this.person = person;
        this.token=token;
        this.listener=listener;
    }

    @Override
    protected Response doInBackground(Void... params) {
        Response response=null;

        Gson gson=new Gson();
        String json=gson.toJson(person);
        try {
            response= HttpsProvider.getInstance().post(json,"/setContact",token);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);
        if (listener!=null)
            listener.addUserTaskCallback(response);
    }

    public interface AddUserTaskListener{
        void addUserTaskCallback(Response response);
    }
}
