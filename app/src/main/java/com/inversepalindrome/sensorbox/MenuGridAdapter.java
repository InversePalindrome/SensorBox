/*
Copyright (c) 2019 Inverse Palindrome
SensorBox - MenuGridAdapter.java
https://inversepalindrome.com/
*/


package com.inversepalindrome.sensorbox;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;


public class MenuGridAdapter extends BaseAdapter {
    private Context context;

    private final String[] labels;
    private final int[] imageIDs;

    public MenuGridAdapter(Context context, String[] labels, int[] imageIDs){
       this.context = context;

       this.labels = labels;
       this.imageIDs = imageIDs;
    }

    @Override
    public int getCount(){
        return labels.length;
    }

    @Override
    public Object getItem(int i){
        return null;
    }

    @Override
    public long getItemId(int i){
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent){
        View gridView;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null){
           gridView = inflater.inflate(R.layout.grid_single, null);

           TextView textView = gridView.findViewById(R.id.grid_text);
           textView.setText(labels[i]);

           ImageView imageView = gridView.findViewById(R.id.grid_image);
           imageView.setImageResource(imageIDs[i]);
        }
        else{
           gridView = convertView;
        }

        return gridView;
    }
}
