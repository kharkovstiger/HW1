package com.example.tiger.hw1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ListExtendedActivity extends AppCompatActivity {

    private ImageView ivPhotoExt;
    private TextView tvNameExt, tvPhoneNumberExt, tvAddressExt, tvDbayExt, tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_extended);

        ivPhotoExt= (ImageView) findViewById(R.id.ivPhotoExt);
        tvNameExt= (TextView) findViewById(R.id.tvNameExt);
        tvPhoneNumberExt= (TextView) findViewById(R.id.tvPhoneNumberExt);
        tvAddressExt= (TextView) findViewById(R.id.tvAddressExt);
        tvDbayExt= (TextView) findViewById(R.id.tvDbayExt);
        tvDescription= (TextView) findViewById(R.id.tvDescription);

        Person person= (Person) getIntent().getSerializableExtra("Person");

//        ivPhotoExt.setImageResource(person.picture);
        tvNameExt.setText(person.getFullName());
        tvPhoneNumberExt.setText(person.getPhoneNumber());
        tvAddressExt.setText(person.getAddress());
        tvDbayExt.setText(person.getEmail());
        tvDescription.setText(person.getDescription());
    }
}
