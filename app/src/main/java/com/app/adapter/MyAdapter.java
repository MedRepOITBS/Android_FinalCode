package com.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import medrep.medrep.DoctorDashboard;
import medrep.medrep.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by GunaSekhar on 04-06-2016.
 */
public class MyAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private String[] countries;
    private LayoutInflater inflater;
    private Boolean cond = true;
    private ArrayList<AppointmentModel> mCategorys;

    public MyAdapter(ArrayList<AppointmentModel> locationModel, Context context) {
        this.mCategorys = locationModel;
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
        View row = convertView;

        ViewHolder holder = new ViewHolder();
        if (row == null) {
            //holder = new ViewHolder();
            row = inflater.inflate(R.layout.notification_custom_list_view, parent, false);
            holder = new ViewHolder();
            holder.appointmentName = (TextView) row.findViewById(R.id.appointmentName);
            holder.appointmentDesc = (TextView) row.findViewById(R.id.appointmentDesc);
            holder.appointmentDate = (TextView) row.findViewById(R.id.appointmentDate);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        try {

            String month = getMonthFromTicks(mCategorys.get(position).getAppointmentDate());
            String date = getDateFromTicks(mCategorys.get(position).getAppointmentDate());

            holder.appointmentName.setText(mCategorys.get(position).getAppointmentName());
            holder.appointmentDate.setText(month +  "\n" +" " + date);
            holder.appointmentDesc.setText(mCategorys.get(position).getAppoointmentDesc());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return row;
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

    /**
     * View Holder Class to Hold Data
     */
    static class ViewHolder {
        TextView appointmentName;
        TextView appointmentDesc;
        TextView appointmentDate;
    }

    public static String getMonthFromTicks(String ticks){


        String year = ticks.substring(0,4);
        String month = ticks.substring(4,6);
        if (month.equalsIgnoreCase("01")){
            month = "JAN";
        } else if (month.equalsIgnoreCase("02")){
            month = "FEB";
        } else if (month.equalsIgnoreCase("03")){
            month = "MAR";
        } else if (month.equalsIgnoreCase("04")){
            month = "APR";
        } else if (month.equalsIgnoreCase("05")){
            month = "MAY";
        } else if (month.equalsIgnoreCase("06")){
            month = "JUN";
        } else if (month.equalsIgnoreCase("07")){
            month = "JUL";
        } else if (month.equalsIgnoreCase("08")){
            month = "AUG";
        } else if (month.equalsIgnoreCase("09")){
            month = "SEP";
        } else if (month.equalsIgnoreCase("1O")){
            month = "OCT";
        } else if (month.equalsIgnoreCase("11")){
            month = "NOV";
        } else if (month.equalsIgnoreCase("12")){
            month = "DEC";
        }
        return month;
    }
    public static String getYearFromTicks(String ticks){
        String year = ticks.substring(0,4);
        return year;
    }

    public static String getDateFromTicks(String ticks){
        String date = ticks.substring(6,8);
        return date;
    }

    public static String getTimeFromTicks(String ticks){
        String hour = ticks.substring(8,10);
        String minute = ticks.substring(10,12);
        return hour + ":" + minute;
    }
}
