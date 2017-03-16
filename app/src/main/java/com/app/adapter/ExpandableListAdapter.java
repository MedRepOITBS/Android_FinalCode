package com.app.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by guna on 14/08/16.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private JSONArray array;

    public ExpandableListAdapter(Context context, JSONArray array) {
        this.context = context;
        this.array = array;
    }
    @Override
    public int getGroupCount() {
        return array.length();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        try {
            return array.getJSONObject(0).getJSONObject("workExperience").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        try {
            return array.getJSONObject(groupPosition);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
