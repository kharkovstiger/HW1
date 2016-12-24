package com.example.tiger.hw1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiger.hw1.providers.HttpsProvider;
import com.example.tiger.hw1.providers.Response;
import com.example.tiger.hw1.tasks.AddUserTask;
import com.example.tiger.hw1.tasks.GetListTask;
import com.example.tiger.hw1.tasks.RemoveUserTask;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListActivity extends AppCompatActivity implements GetListTask.GetListTaskListener, RemoveUserTask.RemoveUserTaskListener {

    private static final int MENU_DELETE = 0;
    private static final int MENU_EDIT = 1;
    private static final int MENU_INFO = 2;
    private ListView lvMain;
    private List<Person> persons;
    private ProgressBar pbList;
    private SharedPreferences preferences;
    private TextView tvEmptyList;
    private CustomAdapter adapter;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        preferences = getSharedPreferences("Auth", MODE_PRIVATE);
        token=preferences.getString("TOKEN", null);

        pbList= (ProgressBar) findViewById(R.id.pbList);
        tvEmptyList= (TextView) findViewById(R.id.tvEmptyList);
        lvMain= (ListView) findViewById(R.id.lvMain);

        registerForContextMenu(lvMain);

        persons=new ArrayList<>();
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mUpdate:
                getData();
                break;
            case R.id.mAdd:
                Intent intent=new Intent(this, AddActivity.class);
                startActivity(intent);
                break;
            case R.id.mClear:
                new ClearTask().execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        lvMain.setVisibility(View.GONE);
        tvEmptyList.setVisibility(View.GONE);
        pbList.setVisibility(View.VISIBLE);

        new GetListTask(this, token).execute();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0,MENU_DELETE,0,"Delete note");
        menu.add(0,MENU_EDIT,0,"Edit note");
        menu.add(0,MENU_INFO,0,"Info note");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent;
        switch (item.getItemId()){
            case MENU_DELETE:
                // получаем инфу о пункте списка
                Person p=new Person(persons.get(acmi.position).getContactId());
                lvMain.setVisibility(View.GONE);
                tvEmptyList.setVisibility(View.GONE);
                pbList.setVisibility(View.VISIBLE);
                new RemoveUserTask(p,token,this).execute();
                break;
            case MENU_EDIT:
                intent=new Intent(this, AddActivity.class);
                intent.putExtra("Person",persons.get(acmi.position));
                startActivity(intent);
                break;
            case MENU_INFO:
                intent=new Intent(this, ListExtendedActivity.class);
                intent.putExtra("Person",persons.get(acmi.position));
                startActivity(intent);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void getListCallback(Response response) {

        Person[] contacts = null;
        if (response == null)
            Toast.makeText(ListActivity.this, "Smth went wrong. You are not registered.", Toast.LENGTH_SHORT).show();
        else {
            switch (response.getResponseCode()) {
                case 401:
                    Toast.makeText(ListActivity.this, "Wrong authorization!", Toast.LENGTH_SHORT).show();
                    break;
                case 200:
                    Gson gson = new Gson();

                    try {
                        String json=new JSONObject(response.getBody()).getString("contacts");
                        Log.d("MY_TAG",json);
                        contacts = gson.fromJson(json, Person[].class);
                    }
                    catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    Toast.makeText(ListActivity.this, "Fatal error...", Toast.LENGTH_LONG).show();
                    break;
            }
        }
        if (contacts!=null)
            persons=Arrays.asList(contacts);

        pbList.setVisibility(View.GONE);
        if (persons==null || persons.size()==0)
            tvEmptyList.setVisibility(View.VISIBLE);
        else
            lvMain.setVisibility(View.VISIBLE);

        adapter=new CustomAdapter(persons, this);
        lvMain.setAdapter(adapter);
    }

    @Override
    public void removeUserTaskCallback(Response response) {
        if (response == null)
            Toast.makeText(ListActivity.this, "Smth went wrong. You are not registered.", Toast.LENGTH_SHORT).show();
        else {
            switch (response.getResponseCode()) {
                case 401:
                    Toast.makeText(ListActivity.this, "Wrong authorization!", Toast.LENGTH_SHORT).show();
                    break;
                case 409:
                    Toast.makeText(ListActivity.this, "Wrong authorization!", Toast.LENGTH_SHORT).show();
                    break;
                case 200:
                    Toast.makeText(ListActivity.this, "Contact deleted", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(ListActivity.this, "Fatal error...", Toast.LENGTH_LONG).show();
                    break;
            }
        }
        getData();
    }

    private class ClearTask extends AsyncTask<Void, Void, Response>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lvMain.setVisibility(View.GONE);
            tvEmptyList.setVisibility(View.GONE);
            pbList.setVisibility(View.VISIBLE);
        }

        @Override
        protected Response doInBackground(Void... params) {
            Response response=null;
//            response= HttpsProvider.getInstance().post()
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            getData();
        }
    }
}
