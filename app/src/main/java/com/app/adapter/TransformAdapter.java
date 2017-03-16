package com.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import medrep.medrep.R;
import medrep.medrep.TransformDetailActivity;

/**
 * Created by chethan on 3/10/2017.
 */

public class TransformAdapter extends BaseAdapter {

    public Context context;
    public JSONArray tempArray;
    public JSONArray resArray;

    public TransformAdapter(Context context, JSONArray resArray){
        this.context = context;
        this.resArray = resArray;
    }


    @Override
    public int getCount() {
        return tempArray.length();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.doctor_group_custom_list_view, null);
        Button groupIcon = (Button) convertView.findViewById(R.id.groupIcon);
        LinearLayout listitemLV = (LinearLayout)convertView.findViewById(R.id.listitemLV);


        final Intent intent = new Intent(context, TransformDetailActivity.class);
        try {
            final JSONObject object = tempArray.getJSONObject(position);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        String title = object.getString("title");
                        String description = object.getString("tagDesc");

                        if(object.has("innerImgUrl")){
                            if(!object.getString("innerImgUrl").equals("null")){
                                //                        videoUrl = object.getString("videoUrl");
                                intent.putExtra("innerImgUrl", object.getString("innerImgUrl"));
                            }
                        }
                        if(object.has("videoUrl")){
                            if(!object.getString("videoUrl").equals("null")){
                                //                        videoUrl = object.getString("videoUrl");
                                intent.putExtra("videoUrl", object.getString("videoUrl"));
                            }
                        }

                        intent.putExtra("description", description);
                        intent.putExtra("newsTitle", title);

                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });



//            if(object.isNull("Empty")){
                String title = object.getString("title");
                TextView name  = (TextView)convertView.findViewById(R.id.name);
                name.setText(title);
                String desc = object.getString("tagDesc");
                TextView description = (TextView)convertView.findViewById(R.id.groupDescription);
                description.setText(Html.fromHtml(desc));
                TextView source = (TextView)convertView.findViewById(R.id.source);
                String sourceName = null;
                String coverImgUrl = null;
                if(object.has("sourceName")){
                    sourceName = object.getString("sourceName");
                }
                String articalDate = object.getString("createdOn");
                if(object.has("coverImgUrl")){
                    coverImgUrl = object.getString("coverImgUrl");
                }

//                if(coverImgUrl == null) {
//                    groupIcon.setBackgroundResource(R.drawable.res_img);
//                } else {
//                    CommonAdapter.LoadImage loadImage = new CommonAdapter.LoadImage(groupIcon);
//                    loadImage.execute(coverImgUrl);
//                }

                source.setText("SOURCE: "+sourceName+", "+articalDate);

//            }else {
//                listitemLV.setVisibility(View.GONE);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public void filter(CharSequence searchString){
        tempArray = new JSONArray();
        try{
            if(searchString.length() == 0){
                tempArray = resArray;
            }else {
                for(int i=1; i<resArray.length(); i++){
                    JSONObject jsonObject = resArray.getJSONObject(i);
                    if(jsonObject.getString("title").contains(searchString)){
                        tempArray.put(jsonObject);
                    }
//                else {
//                    resArray.put(jsonObjectTemp);
//                }
                }
            }
            notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


