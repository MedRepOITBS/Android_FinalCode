package com.app.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.app.adapter.NotificationBrandAdapter;
import com.app.db.Company;
import com.app.db.MedRepDatabaseHandler;
import com.app.db.Notification;
import com.app.db.NotificationType;
import com.app.db.TherapeuticCategory;
import com.app.util.HttpUrl;
import com.app.util.JSONParser;
import com.app.util.Utils;

import org.json.JSONArray;

import java.util.ArrayList;

import medrep.medrep.R;

/**
 * Created by masood on 9/11/15.
 */
public class DocNotificatonBrand extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    //    ImageView imageView;
    ListView brandListView;
    private ImageView back;

    private ArrayList<Company> companies;
    private Button readNotificationsButton;
    private Button favouriteButton;

    private View progressLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.doc_notf_brand, container, false);

        progressLayout = v.findViewById(R.id.progress_layout);

        brandListView = (ListView) v.findViewById(R.id.brand_list);
        brandListView.setOnItemClickListener(this);

        favouriteButton = (Button) v.findViewById(R.id.favourite_button);
        favouriteButton.setOnClickListener(this);

        readNotificationsButton = (Button) v.findViewById(R.id.read_notificaions_button);
        readNotificationsButton.setOnClickListener(this);

        back = (ImageView) v.findViewById(R.id.back);
        back.setOnClickListener(this);

        return v;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(Utils.isNetworkAvailableWithOutDialog(getActivity())){
            if(DocNotificatonBrand.this.companies == null || DocNotificatonBrand.this.companies.size() == 0){
                CompanyDetailsAsync companyDetailsAsync = new CompanyDetailsAsync();
                companyDetailsAsync.execute();
            }else{
                if(progressLayout != null){
                    progressLayout.setVisibility(View.GONE);
                }
                brandListView.setAdapter(new NotificationBrandAdapter(getActivity(), DocNotificatonBrand.this.companies));
            }
        }else{
            Toast.makeText(getActivity(), "No internet available, loading offline data.", Toast.LENGTH_SHORT).show();

            ArrayList<Company> companies = MedRepDatabaseHandler.getInstance(getActivity()).getAllAvailableCompanies();

            displayCompaniesFromDatabase(companies);
        }
    }

    private void goBack(){
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onClick(View v) {
        if(v == back){
            goBack();
        }else if(v == readNotificationsButton){
            System.out.println("Selected read notifications");

            NotificationInsideBrand notificationBrand = new NotificationInsideBrand();
            NotificationInsideBrand.VIEW_TYPE = NotificationInsideBrand.READ_NOTIFICATIONS;
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, notificationBrand);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else if(v == favouriteButton){
            System.out.println("Selected favourite notifications");

            NotificationInsideBrand notificationBrand = new NotificationInsideBrand();
            NotificationInsideBrand.VIEW_TYPE = NotificationInsideBrand.FAVORITE_NOTIFICATION;
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, notificationBrand);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*GetMyNotificationsAsync getMyNotificationsAsync = new GetMyNotificationsAsync(id);
        getMyNotificationsAsync.execute();*/
        NotificationInsideBrand notificationBrand = new NotificationInsideBrand();
        NotificationInsideBrand.COMPANY = (Company)parent.getItemAtPosition(position);
        NotificationInsideBrand.VIEW_TYPE = NotificationInsideBrand.DEFAULT;
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, notificationBrand);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /*private class GetMyNotificationsAsync extends AsyncTask<Void, Integer, ArrayList<Notification>> {

        ProgressDialog pd;
        private int companyID;

        public GetMyNotificationsAsync(long id) {
            companyID = (int) id;
        }

        @Override
        protected ArrayList<Notification> doInBackground(Void... params) {
            ArrayList<Notification> notifications = getMyNotifications();
            MedRepDatabaseHandler.getInstance(getActivity()).addNotifications(notifications);
            return notifications;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (pd == null) {
                pd = new ProgressDialog(getActivity());
            }
            pd.setCancelable(false);
            pd.setTitle(R.string.company_info_dialog_title);
            pd.setMessage(getString(R.string.company_info_dialog_msg));
            pd.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Notification> notifications) {
            super.onPostExecute(notifications);

            if (pd != null) {
                pd.dismiss();
            }

            NotificationInsideBrand notificationBrand = new NotificationInsideBrand();
            NotificationInsideBrand.COMPANY_ID = companyID;
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, notificationBrand);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
*/
    private class CompanyDetailsAsync extends AsyncTask<Void, Integer, ArrayList<Company>> {

//        ProgressDialog progressDialog;

        @Override
        protected ArrayList<Company> doInBackground(Void... params) {

            MedRepDatabaseHandler databaseHandler = MedRepDatabaseHandler.getInstance();

            if(databaseHandler == null){
                databaseHandler = MedRepDatabaseHandler.getInstance(getActivity());
            }

            ArrayList<NotificationType> notificationTypes = getNotificationTypes();
            databaseHandler.addNotificationTypes(notificationTypes);

           /* System.out.println("Notification Types");
            for (NotificationType notificationType: notificationTypes){
                System.out.println("Notification Type: " + notificationType.getTypeId());
                System.out.println("Notification Type: " + notificationType.getTypeName());
                System.out.println("Notification Type: " + notificationType.getTypeDesc());
            }*/

            ArrayList<TherapeuticCategory> therapeuticAreaDetails = getTherapeuticAreaDetails();
            databaseHandler.addTherapeuticAreaDetails(therapeuticAreaDetails);

            ArrayList<Company> companies = getCompanies();
            databaseHandler.addCompanies(companies);


            ArrayList<Notification> notifications = getMyNotifications();
            if(notifications != null){
                databaseHandler.addNotifications(notifications);
            }

            return companies;
        }

        private ArrayList<Company> getCompanies() {
            String url = HttpUrl.COMPANY_DETAILS_URL;

            ArrayList<Company> companies = new ArrayList<>();

            JSONParser parser = new JSONParser();
            JSONArray jsonArray = parser.getJSON_Response(url);
            parser.jsonParser(jsonArray, Company.class, companies);

            return companies;
        }

        private ArrayList<TherapeuticCategory> getTherapeuticAreaDetails() {
            String url = HttpUrl.THERAPEUTIC_AREA_DETAILS_URL;

            ArrayList<TherapeuticCategory> therapeuticCategories = new ArrayList<>();

            JSONParser parser = new JSONParser();
            JSONArray jsonArray = parser.getJSON_Response(url);
            parser.jsonParser(jsonArray, TherapeuticCategory.class, therapeuticCategories);

            return therapeuticCategories;
        }

        private ArrayList<NotificationType> getNotificationTypes() {
            String url = HttpUrl.NOTIFICATION_TYPE_URL;

            ArrayList<NotificationType> notificationTypes = new ArrayList<>();

            JSONParser parser = new JSONParser();
            JSONArray jsonArray = parser.getJSON_Response(url);
            parser.jsonParser(jsonArray, NotificationType.class, notificationTypes);

            return notificationTypes;
        }

        private ArrayList<Notification> getMyNotifications() {

//            int startDate = MedRepDatabaseHandler.getInstance(getActivity()).getDoctorNotificationsLatestUpdatedDate();

            String url = HttpUrl.MYNOTIFICATION + /*dummStartDate*/20140917 + "?access_token=" + Utils.GET_ACCESS_TOKEN(getActivity());

            ArrayList<Notification> notifications = new ArrayList<>();

            JSONParser parser = new JSONParser();
            JSONArray jsonArray = parser.getJSON_Response(url);
            parser.jsonParser(jsonArray, Notification.class, notifications);
            return notifications;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setTitle(R.string.company_info_dialog_title);
            progressDialog.setMessage(getString(R.string.company_info_dialog_msg));
            if(!progressDialog.isShowing()){progressDialog.show();}*/

        }

        @Override
        protected void onPostExecute(ArrayList<Company> companies) {
            super.onPostExecute(companies);

            /*if (progressDialog != null) {
                progressDialog.dismiss();
            }

            progressDialog = null;*/

            if(progressLayout != null){
                progressLayout.setVisibility(View.GONE);
            }

            displayCompaniesFromDatabase(companies);
        }
    }

    private void displayCompaniesFromDatabase(ArrayList<Company> companies) {

        System.out.println("companies.size(): " + companies.size());

        DocNotificatonBrand.this.companies = MedRepDatabaseHandler.getInstance(getActivity()).getCompaniesWithNotifications(companies);

        System.out.println(DocNotificatonBrand.this.companies == null);

        if(DocNotificatonBrand.this.companies != null && DocNotificatonBrand.this.companies.size() > 0){
            brandListView.setAdapter(new NotificationBrandAdapter(getActivity(), DocNotificatonBrand.this.companies));
            if(progressLayout != null){
                progressLayout.setVisibility(View.GONE);
            }
        }else{
            goBack();
            Utils.DISPLAY_GENERAL_DIALOG(getActivity(), "No Companies Found", "No companies are available.");
        }

    }

}
