package com.app.adapter;

/**
 * Created by masood on 9/5/15.
 */
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pojo.DataObject;
import com.app.util.UserRoles;

import java.util.ArrayList;

import medrep.medrep.R;

public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DataObject> mDataset;
    private static MyClickListener myClickListener;
    int[] docNavDrawables = {
            R.mipmap.profile_icon,
            R.mipmap.dashboard_menu,
            R.mipmap.notifications,
            R.mipmap.surveys_menu,
            R.mipmap.activity_score_menu,
            R.mipmap.marketing_menu,
            R.mipmap.meetings,
            R.mipmap.discussion_forum,
            R.mipmap.search_for_drugs,
            R.mipmap.news,
            R.mipmap.setting,
            R.drawable.connect,
            R.mipmap.logout
    };

    int[] pharmaNavDrawables = {
            R.mipmap.profile_icon,
            R.mipmap.dashboard_menu,
            R.mipmap.notifications,
            R.mipmap.activity_score,
            R.mipmap.surveys_menu,
            R.mipmap.mdrep,
            R.mipmap.marketing_menu,
            R.mipmap.discussion_forum,
            R.mipmap.drug_sales,
            R.mipmap.prescription,
            R.mipmap.setting,
            R.drawable.connect,
            R.mipmap.logout
    };

    private int roleID = -1;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label;
        ImageView imageView;
//        TextView dateTime;

        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.textView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
//            dateTime = (TextView) itemView.findViewById(R.id.textView2);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter(ArrayList<DataObject> myDataset, int roleID) {
        mDataset = myDataset;
        this.roleID = roleID;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.doc_navagationdrawer_item, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getmText1());
        if(roleID != -1 && roleID == UserRoles.DOCTOR){
            holder.imageView.setImageResource(docNavDrawables[position]);
        }else{
            holder.imageView.setImageResource(pharmaNavDrawables[position]);
        }

//        holder.dateTime.setText(mDataset.get(position).getmText2());
    }

    public void addItem(DataObject dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
