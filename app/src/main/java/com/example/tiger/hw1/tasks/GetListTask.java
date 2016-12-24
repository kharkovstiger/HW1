package com.example.tiger.hw1.tasks;

import android.os.AsyncTask;

import com.example.tiger.hw1.providers.HttpsProvider;
import com.example.tiger.hw1.providers.Response;

import java.io.IOException;

public class GetListTask extends AsyncTask<Void, Void, Response>{

    private GetListTaskListener listener;
    private String token;

    public GetListTask(GetListTaskListener listener, String token) {
        this.listener = listener;
        this.token = token;
    }

    @Override
    protected Response doInBackground(Void... params) {
        Response response=null;
        try {
            response= HttpsProvider.getInstance().get(token, "/contactsarray");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);
        if (listener!=null)
            listener.getListCallback(response);
    }

    public interface GetListTaskListener{
        void getListCallback(Response response);
    }
}
