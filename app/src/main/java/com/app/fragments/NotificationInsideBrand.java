package com.app.fragments;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.db.Company;
import com.app.db.MedRepDatabaseHandler;
import com.app.db.Notification;
import com.app.db.TherapeuticCategory;
import com.app.util.Utils;

import java.util.ArrayList;

import medrep.medrep.R;

/**
 * Created by masood on 9/11/15.
 */
public class NotificationInsideBrand extends Fragment implements
        AdapterView.OnItemSelectedListener,
        AdapterView.OnItemClickListener,
        View.OnClickListener{

    Spinner spinner;
    private ListView detailsNotificationsLV;
    private ImageView back;
    public static Company COMPANY = null;

    private int[] notificationIDS;


    public static final int DEFAULT = 1;
    public static final int READ_NOTIFICATIONS = 2;
    public static final int FAVORITE_NOTIFICATION = 3;


    public static int VIEW_TYPE = DEFAULT;



//    private ArrayList<DetailedNotification> detailedNotifications = null;
    private ArrayList<Notification> notifications;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.doc_notf_inside_brand, container, false);
        spinner = (Spinner)v.findViewById(R.id.brand_spinner);
        spinner.setOnItemSelectedListener(this);
        detailsNotificationsLV = (ListView) v.findViewById(R.id.brand_list);
        detailsNotificationsLV.setEmptyView(v.findViewById(R.id.emptyElement));
        detailsNotificationsLV.setOnItemClickListener(this);
        back = (ImageView) v.findViewById(R.id.back);
        back.setOnClickListener(this);

        if(COMPANY != null && VIEW_TYPE == DEFAULT){
            String pictureData = COMPANY.getDisplayPicture().getData();

            if(pictureData != null && pictureData.trim().length() > 0){
                ((ImageView)v.findViewById(R.id.notif_img)).setImageBitmap(Utils.decodeBase64(pictureData));
            }


            notifications = MedRepDatabaseHandler.getInstance(getActivity()).getNotifications(COMPANY.getCompanyId());
        }else{
            v.findViewById(R.id.notif_img).setVisibility(View.GONE);
            notifications = getNotifications();
        }




        if(notifications != null && notifications.size() > 0){
            notificationIDS = Utils.GET_NOTIFICATION_IDS(notifications);

            ArrayList<TherapeuticCategory> therapeuticCategories =
                    MedRepDatabaseHandler.getInstance(getActivity()).
                            getTherapeuticAreaDetails(Utils.GET_THERAPEUTIC_AREA_IDS(notifications));
            if(therapeuticCategories != null && therapeuticCategories.size() > 0){
                spinner.setAdapter(new SpinnerAdapter(therapeuticCategories));
            }else{
                spinner.setEnabled(false);
            }

        }

        return v;
    }

    @Override
    public void onClick(View v) {
        goBack();
    }

    public void goBack(){
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setNotifications((int) spinner.getSelectedItemId());
    }

    /**
     * sets updated notifications based on the passed therapeuticID
     * @param therapeuticID
     */
    private void setNotifications(int therapeuticID) {
        /*int[] resultIDS = MedRepDatabaseHandler.getInstance(getActivity()).
                getNotificationIDs(notificationIDS, (int) spinner.getSelectedItemId());*/

       /* detailedNotifications =
                MedRepDatabaseHandler.getInstance(getActivity()).getDetailedNotification(resultIDS);*/

        System.out.println("therapeuticID: " + therapeuticID);

//        if(therapeuticID != -1){
            notifications = getNotifications();

            if(notifications != null){
                NotificationsAdapter notificationsAdapter  = new NotificationsAdapter();
                detailsNotificationsLV.setAdapter(notificationsAdapter);
            }else{
                Toast.makeText(getActivity(), "You have not Favorited  any Notification yet.", Toast.LENGTH_SHORT).show();
                goBack();
            }
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        COMPANY = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        setNotifications((int) id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        NotificationDetails.CURRENT_NOTIFICATION_ID = (int)id;
        NotificationDetails.CURRENT_NOTIFICATION_TITLE = ((Notification)parent.getItemAtPosition(position)).getNotificationName();

        NotificationDetails notificationDetails = new NotificationDetails();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, notificationDetails)/*replace(R.id.frame, notificationBrand)*/;
        fragmentTransaction.addToBackStack("NotificationDetails");
        fragmentTransaction.commit();

//        markNotificationAsRead(id);
    }

    /*private void markNotificationAsRead(long id) {
        ContentValues values = new ContentValues();
        values.put(Notification.COLUMN_NAMES.IS_READ, true);

        MedRepDatabaseHandler.getInstance(getActivity()).updateNotification(values, (int)id);
    }*/

    public ArrayList<Notification> getNotifications() {

        System.out.println("VIEW_TYPE: " + VIEW_TYPE);

        ArrayList<Notification> notifications;
        switch (VIEW_TYPE){
            case READ_NOTIFICATIONS:
                notifications = MedRepDatabaseHandler.getInstance(getActivity()).
                        getReadNotifications();
                break;
            case FAVORITE_NOTIFICATION:
                notifications = MedRepDatabaseHandler.getInstance(getActivity()).
                        getFavouriteNotifications();
                break;
            default:
                notifications = MedRepDatabaseHandler.getInstance(getActivity()).
                        getSelectedNotifications(notificationIDS,
                                (int) spinner.getSelectedItemId(),
                                COMPANY.getCompanyId());
                break;
        }

        return notifications;
    }

    /**
     * Adapter to set therapeutic list
     */
    private class SpinnerAdapter extends BaseAdapter{
        private ArrayList<TherapeuticCategory> therapeuticCategories;
        SpinnerAdapter(ArrayList<TherapeuticCategory> therapeuticCategories){
            this.therapeuticCategories = therapeuticCategories;
        }

        @Override
        public int getCount() {
            return therapeuticCategories.size() + 1;
        }


        public TherapeuticCategory getItem(int position) {
            if(position == 0){
                return null;
            }
            position = position - 1;
            return therapeuticCategories.get(position);
        }

        @Override
        public long getItemId(int position) {
            if(position == 0){
                return -1;
            }
            position = position - 1;
            return therapeuticCategories.get(position).getTherapeuticId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                convertView = inflater.inflate(R.layout.location_spinner_text, parent,false);
                viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }

            if(position == 0){
                viewHolder.textView.setText("Choose Therapeutic Area");
            }else{
                position = position - 1;
                viewHolder.textView.setText(therapeuticCategories.get(position).getTherapeuticName());
            }

            return convertView;
        }

        private class ViewHolder{
            TextView textView;
        }
    }

    private final class NotificationsAdapter extends BaseAdapter{
       /* private ArrayList<DetailedNotification> detailedNotifications;
        NotificationsAdapter(ArrayList<DetailedNotification> detailedNotifications){
            this.detailedNotifications = detailedNotifications;
        }*/

        @Override
        public int getCount() {
            return notifications.size();
        }


        public Notification getItem(int position) {
            return notifications.get(position);
        }

        @Override
        public long getItemId(int position) {
            return notifications.get(position).getNotificationId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                convertView = inflater.inflate(R.layout.row_notification, parent, false);
                holder = new ViewHolder();
                holder.titleTextView = (TextView)convertView.findViewById(R.id.notification_title);
                holder.descTextView = (TextView)convertView.findViewById(R.id.notification_desc);
                holder.ticketID_TextView = (TextView)convertView.findViewById(R.id.ticket_id);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            Notification notification = notifications.get(position);

//            holder.companyImageView.setImageResource(drawables[position]);
//            holder.companyImageView.setImageBitmap(Utils.decodeBase64(companies.get(position).getDisplayPicture().getData()));

            String title = notification.getNotificationName();
            String description = notification.getNotificationDesc();

            title = (title != null && title.trim().length() !=0)?title.trim():"No Title";
            description = (description != null && description.trim().length() !=0)?description.trim():"No Description";


            holder.titleTextView.setText(title);
            holder.descTextView.setText(description);
            holder.ticketID_TextView.setText(title.charAt(0) + "");
            return convertView;
        }


        class ViewHolder{
            TextView titleTextView;
            TextView descTextView;
            TextView ticketID_TextView;
            ImageView rightArrowImageView;
        }
    }
}
