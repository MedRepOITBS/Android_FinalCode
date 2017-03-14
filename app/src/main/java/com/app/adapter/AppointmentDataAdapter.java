//package com.app.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//import medrep.medrep.R;
//import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
//
///**
// * Created by amit kumar on 2/10/2016.
// */
//public class AppointmentDataAdapter extends BaseAdapter implements StickyListHeadersAdapter {
//
//    private ArrayList<AppointmentModel> mCategorys;
//    private Context mContext;
//    private LayoutInflater l_Inflater;
//    private Boolean cond = true;
//
//    public AppointmentDataAdapter(ArrayList<AppointmentModel> locationModel, Context mContext)
//    {
//        this.mCategorys = locationModel;
//        this.mContext = mContext;
//        l_Inflater = LayoutInflater.from(mContext);
//    }
//
//    @Override
//    public int getCount() {
//        return mCategorys.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        View row = convertView;
//
//        ViewHolder holder = new ViewHolder();
//        if (row == null) {
//            row = l_Inflater.inflate(R.layout.notification_custom_list_view, null);
//            holder = new ViewHolder();
//            holder.appointmentName = (TextView) row.findViewById(R.id.notifictionName);
//            holder.appointmentDesc = (TextView) row.findViewById(R.id.notification_desc);
//
//            row.setTag(holder);
//        } else {
//            holder = (ViewHolder) row.getTag();
//        }
//
//        try {
//            holder.appointmentName.setText(mCategorys.get(position).getAppointmentName());
//            holder.appointmentDesc.setText(mCategorys.get(position).getAppoointmentDesc());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return row;
//    }
//
//
//    /**
//     * View Holder Class to Hold Data
//     */
//    static class ViewHolder {
//        TextView appointmentName;
//        TextView appointmentDesc;
//    }
//
//    @Override
//    public View getHeaderView(int position, View convertView, ViewGroup parent) {
//        System.out.println(position);
//        // HeaderViewHolder holder;
//
//        if(position < 5) {
//            cond = false;
//            System.out.println("Today: " +position);
//            convertView = l_Inflater.inflate(R.layout.header, parent, false);
//            TextView text = (TextView) convertView.findViewById(R.id.text);
//            text.setText("Today");
//        } else if(position >= 5) {
//            cond = true;
//            System.out.println("Tomorrow: " +position);
//            convertView = l_Inflater.inflate(R.layout.header, parent, false);
//            TextView text = (TextView)convertView.findViewById(R.id.text);
//            text.setText("Tomorrow");
//        }
//        TextView appointmentTitle = (TextView) convertView.findViewById(R.id.notification_title);
//        TextView appointmentDesc = (TextView) convertView.findViewById(R.id.notification_desc);
//
////        else {
////            holder = (HeaderViewHolder) convertView.getTag();
////        }
//        //set header text as first char in name
//        //String headerText = "" + countries[position].subSequence(0, 1).charAt(0);
//        // holder.text.setText(headerText);
//        return convertView;
//    }
//    @Override
//    public long getHeaderId(int position) {
//        //return the first character of the country as ID because this is what headers are based upon
//        return position;
//    }
//
//
//}
