package com.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import medrep.medrep.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by GunaSekhar on 04-06-2016.
 */
public class MyAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private String[] countries;
    private LayoutInflater inflater;
    private Boolean cond = true;

    public MyAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        countries = context.getResources().getStringArray(R.array.countries);
    }

    @Override
    public int getCount() {
        return 15;
    }

    @Override
    public Object getItem(int position) {
        return countries[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       // ViewHolder holder;

        if (convertView == null) {
            //holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.notification_custom_list_view, parent, false);
//            holder.text = (TextView) convertView.findViewById(R.id.notificationDescription);
//            convertView.setTag(holder);
        }
//        else {
//            holder = (ViewHolder) convertView.getTag();
//        }

        //holder.text.setText(countries[position]);

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        System.out.println(position);
       // HeaderViewHolder holder;

        if(position < 5) {
            cond = false;
            System.out.println("Today: " +position);
            convertView = inflater.inflate(R.layout.header, parent, false);
            TextView text = (TextView) convertView.findViewById(R.id.text);
            text.setText("Today");
        } else if(position >= 5) {
            cond = true;
            System.out.println("Tomorrow: " +position);
            convertView = inflater.inflate(R.layout.header, parent, false);
            TextView text = (TextView)convertView.findViewById(R.id.text);
            text.setText("Tomorrow");
        }
//        else {
//            holder = (HeaderViewHolder) convertView.getTag();
//        }
        //set header text as first char in name
        //String headerText = "" + countries[position].subSequence(0, 1).charAt(0);
       // holder.text.setText(headerText);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return position;
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView text;
    }

}
