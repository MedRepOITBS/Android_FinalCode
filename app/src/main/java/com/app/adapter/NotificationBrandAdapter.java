package com.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.db.Company;
import com.app.util.Utils;

import java.util.ArrayList;


import medrep.medrep.R;

/**
 * Created by masood on 9/12/15.
 */
public class NotificationBrandAdapter extends BaseAdapter{

   /* private int[] drawables = {
            R.mipmap.comapny_logo,
            R.mipmap.comapny_logo_dr,
            R.mipmap.comapny_logo,
            R.mipmap.comapny_logo_dr,
            R.mipmap.comapny_logo,
            R.mipmap.comapny_logo_dr,
            R.mipmap.comapny_logo,
            R.mipmap.comapny_logo_dr,
            R.mipmap.comapny_logo,
            R.mipmap.comapny_logo_dr,
            R.mipmap.comapny_logo,
            R.mipmap.comapny_logo_dr,
            R.mipmap.comapny_logo,
            R.mipmap.comapny_logo_dr,
            R.mipmap.comapny_logo,
            R.mipmap.comapny_logo_dr,
            R.mipmap.comapny_logo,
            R.mipmap.comapny_logo_dr
    };*/
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Company> companies;

    public NotificationBrandAdapter(Context context, ArrayList<Company> companies){
        this.context = context;
        this.companies = companies;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return companies.size();
    }

    @Override
    public Company getItem(int position) {
        return companies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return companies.get(position).getCompanyId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.row_notification_brand, parent, false);
            holder = new ViewHolder();
            holder.companyname=(TextView)convertView.findViewById(R.id.company_name);
            holder.companyImageView = (ImageView)convertView.findViewById(R.id.imageview_brand);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
//            holder.companyImageView.setImageResource(drawables[position]);

        String pictureData = companies.get(position).getDisplayPicture().getData();

        if(pictureData != null && pictureData.trim().length() > 0){
            holder.companyImageView.setImageBitmap(Utils.decodeBase64(pictureData));
        }

        holder.companyname.setText(companies.get(position).getCompanyName());
        return convertView;
    }


    class ViewHolder{
        ImageView companyImageView;
        ImageView rightArrowImageView;
        TextView companyname;
    }
}
