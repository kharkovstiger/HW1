package com.example.tiger.hw1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter {
    List<Person> data;
    LayoutInflater inflater;
    Context context;

    public CustomAdapter(List<Person> data, Context context) {
        this.data = data;
        this.context = context;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.list_layout, parent, false);
        }
        Person p= data.get(position);
        ((TextView)view.findViewById(R.id.tvName)).setText(p.getFullName());
        ((TextView)view.findViewById(R.id.tvPhNumber)).setText(p.getPhoneNumber());
//        ((ImageView)view.findViewById(R.id.ivPhoto)).setImageResource(p.picture);

        return view;
    }
}
