package com.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import medrep.medrep.R;

/**
 * Created by GunaSekhar on 03-07-2016.
 */
public class AutoCompleteTextViewCustomAdapter extends ArrayAdapter<String> implements Filterable {
    private ArrayList<String> list;
    private ArrayList<String> mOriginalValues;
    private ArrayFilter mFilter;
    private int resource;
    private int textViewResourceId;
    private Context context;
    public AutoCompleteTextViewCustomAdapter(Context context, int resource, int textViewResourceId, ArrayList<String> list) {
        super(context, resource);
        System.out.print("%%%%%%%%%%%%%%%%%%%%%%%%%%%" + list);
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.resource = resource;
        mOriginalValues = new ArrayList<String>(list);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.auto_complete_text_view, null);
        TextView view = (TextView)convertView.findViewById(R.id.customTextView);
        view.setText(list.get(position));
        return convertView;
//        return super.getView(position, convertView, parent);
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {
        private Object lock;

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (lock) {
                    mOriginalValues = new ArrayList<String>(list);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    ArrayList<String> list = new ArrayList<String>(mOriginalValues);
                    results.values = list;
                    results.count = list.size();
                }
            } else {
                final String prefixString = prefix.toString().toLowerCase();
                ArrayList<String> values = mOriginalValues;
                int count = values.size();
                ArrayList<String> newValues = new ArrayList<String>(count);
                for (int i = 0; i < count; i++) {
                    String item = values.get(i);
                    if (item.toLowerCase().contains(prefixString)) {
                        newValues.add(item);
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            if (results.values != null) {
                list = (ArrayList<String>) results.values;
            } else {
                list = new ArrayList<String>();
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetChanged();
            }
        }
    }

}
