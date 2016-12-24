package com.example.tiger.hw1;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tiger.hw1.providers.HttpsProvider;
import com.example.tiger.hw1.providers.Response;
import com.example.tiger.hw1.tasks.AddUserTask;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.R.attr.cacheColorHint;
import static android.R.attr.data;

public class AddActivity extends AppCompatActivity implements AddUserTask.AddUserTaskListener {

    private ImageView ivPhotoAdd;
    private EditText etNameExt, etPhoneNumberExt, etAddressExt, etEmail, etDescription;
    private TextView tvNameExt, tvPhoneNumberExt, tvAddressExt, tvEmail, tvDescription;
    private SharedPreferences preferences;
    private DatePickerDialog dateBirdayDatePicker;
    private String token;
    private FrameLayout flAdd;
    private RelativeLayout rlTextView, rlEditText;
    private Person person;
    private MenuItem mSave, mEdit;
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ivPhotoAdd= (ImageView) findViewById(R.id.ivPhotoAdd);

        rlEditText= (RelativeLayout) findViewById(R.id.rlEditText);
        etAddressExt= (EditText) findViewById(R.id.etAddressExt);
        etEmail= (EditText) findViewById(R.id.etEmail);
        etNameExt= (EditText) findViewById(R.id.etNameExt);
        etPhoneNumberExt= (EditText) findViewById(R.id.etPhoneNumberExt);
        etDescription= (EditText) findViewById(R.id.etDescription);

        rlTextView= (RelativeLayout) findViewById(R.id.rlTextView);
        tvAddressExt= (TextView) findViewById(R.id.tvAddressExt);
        tvDescription= (TextView) findViewById(R.id.tvDescription);
        tvEmail= (TextView) findViewById(R.id.tvEmail);
        tvNameExt= (TextView) findViewById(R.id.tvNameExt);
        tvPhoneNumberExt= (TextView) findViewById(R.id.tvPhoneNumberExt);

        preferences = getSharedPreferences("Auth", MODE_PRIVATE);
        token=preferences.getString("TOKEN", null);
        flAdd= (FrameLayout) findViewById(R.id.flAdd);
        flAdd.setOnClickListener(null);

        person= (Person) getIntent().getSerializableExtra("Person");
        if (person!=null){
            isEdit=true;
            rlTextView.setVisibility(View.VISIBLE);
            rlEditText.setVisibility(View.GONE);

            tvAddressExt.setText(person.getAddress());
            tvDescription.setText(person.getDescription());
            tvPhoneNumberExt.setText(person.getPhoneNumber());
            tvNameExt.setText(person.getFullName());
            tvEmail.setText(person.getEmail());

            etAddressExt.setText(person.getAddress());
            etDescription.setText(person.getDescription());
            etPhoneNumberExt.setText(person.getPhoneNumber());
            etNameExt.setText(person.getFullName());
            etEmail.setText(person.getEmail());
        }
        else{
            rlTextView.setVisibility(View.GONE);
            rlEditText.setVisibility(View.VISIBLE);
        }


//        etDbayExt.setOnClickListener(this);
//        initDateBirthdayDatePicker();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        mSave=menu.findItem(R.id.mSave);
        mEdit=menu.findItem(R.id.mEdit);
        if (person!=null){
            mSave.setVisible(false);
            mEdit.setVisible(true);
        }
        else {
            mSave.setVisible(true);
            mEdit.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mSave:
                flAdd.setVisibility(View.VISIBLE);
                if (person==null)
                    person=new Person(null);
                if (isEdit && fillEdit()) break;
                person.setAddress(etAddressExt.getText().toString());
                person.setDescription(etDescription.getText().toString());
                person.setEmail(etEmail.getText().toString());
                person.setFullName(etNameExt.getText().toString());
                person.setPhoneNumber(etPhoneNumberExt.getText().toString());
                new AddUserTask(person,token,this).execute();
                break;
            case R.id.mEdit:
                mEdit.setVisible(false);
                mSave.setVisible(true);
                rlEditText.setVisibility(View.VISIBLE);
                rlTextView.setVisibility(View.GONE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean fillEdit() {
        View focusView = null;
        boolean cancel=false;
        if (TextUtils.isEmpty(etNameExt.getText().toString())) {
            etNameExt.setError(getString(R.string.error_field_required));
            focusView = etNameExt;
            cancel = true;
        }
        else if (TextUtils.isEmpty(etPhoneNumberExt.getText().toString())) {
            etPhoneNumberExt.setError(getString(R.string.error_field_required));
            focusView = etPhoneNumberExt;
            cancel = true;
        }
        else if (TextUtils.isEmpty(etAddressExt.getText().toString())) {
            etAddressExt.setError(getString(R.string.error_field_required));
            focusView = etAddressExt;
            cancel = true;
        }
        else if (TextUtils.isEmpty(etEmail.getText().toString())) {
            etEmail.setError(getString(R.string.error_field_required));
            focusView = etEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            flAdd.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    @Override
    public void addUserTaskCallback(Response response) {
        flAdd.setVisibility(View.GONE);
        if (response == null)
            Toast.makeText(AddActivity.this, "Smth went wrong. You are not registered.", Toast.LENGTH_SHORT).show();
        else {
            switch (response.getResponseCode()) {
                case 200:
                    Toast.makeText(AddActivity.this, "Contact added/updated", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 401:
                    Toast.makeText(AddActivity.this, "Wrong authorization!", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(AddActivity.this, "Fatal error...", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

//    private void initDateBirthdayDatePicker() {
//        Calendar newCalendar=Calendar.getInstance(); // объект типа Calendar мы будем использовать для получения даты
//        final SimpleDateFormat dateFormat=new SimpleDateFormat("dd MMM yyyy"); // это строка нужна для дальнейшего преобразования даты в строку
//        //создаем объект типа DatePickerDialog и инициализируем его конструктор обработчиком события выбора даты и данными для даты по умолчанию
//        dateBirdayDatePicker=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//            // функция onDateSet обрабатывает шаг 2: отображает выбранные нами данные в элементе EditText
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar newCal=Calendar.getInstance();
//                newCal.set(year,monthOfYear,dayOfMonth);
//                etDbayExt.setText(dateFormat.format(newCal.getTime()));
//            }
//        },newCalendar.get(Calendar.YEAR),newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//    }


}
