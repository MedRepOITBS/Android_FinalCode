package com.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import medrep.medrep.R;

/**
 * Created by chethan on 8/3/17.
 */
public class CustomSpinnerAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<String> arrayList = new ArrayList<>();

    public CustomSpinnerAdapter(Context context, ArrayList<String> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.custom_spinner_adapter, null);

        TextView textView = (TextView)convertView.findViewById(R.id.spinnerText);

        textView.setText(arrayList.get(position));

        return convertView;
    }
}
