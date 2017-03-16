package pharma;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.app.fragments.PharmaNavigationDrawerFragment;

import com.app.pojo.RefreshToken;
import com.app.pojo.SignIn;
import com.app.util.HttpUrl;
import com.app.util.JSONParser;
import com.app.util.PharmaRegBean;
import com.app.util.UserRoles;
import com.app.util.Utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import medrep.medrep.LatestSurveyReportsActivity;
import medrep.medrep.R;
import medrep.medrep.SplashScreen;
import pharma.model.Appointment;

/**
 * Created by admin on 9/26/2015.
 */
public class PharmaDashBoard extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    public SignIn signIn;

    PharmaRegBean pharmaRegBean;

    public static String profileName;


    @Override
    protected void onPostResume() {
        super.onPostResume();

        if(PharmaUpdateActivity.REP_PROFILE != null){
            String firstName = (PharmaUpdateActivity.REP_PROFILE.getFirstName() == null ? "" : PharmaUpdateActivity.REP_PROFILE.getFirstName());
            String lastName = (PharmaUpdateActivity.REP_PROFILE.getLastName() == null ? "" : PharmaUpdateActivity.REP_PROFILE.getLastName());

            String profileName = firstName + " " + lastName;

            if(profileName.trim().equals("")){
                profileName = "User";
            }

            SharedPreferences signPref = getSharedPreferences(SplashScreen.SIGNIN_PREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = signPref.edit();
            editor.putString(SplashScreen.PROFILE_NAME_KEY, profileName);
            editor.commit();

            PharmaDashBoard.profileName = profileName;
        }

        if(profileName != null && !profileName.trim().equals("")){
            ((TextView)findViewById(R.id.pharma_username)).setText(" Welcome " + profileName);
        }
    }


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharma_dashboardmain);
        pharmaRegBean = new PharmaRegBean().getInstance();


        Intent intent = getIntent();

        if(profileName == null || profileName.trim().equals("")){
            profileName = intent.getStringExtra(SplashScreen.PROFILE_NAME_KEY);
        }


        ((TextView)findViewById(R.id.pharma_username)).setText(" Welcome "+ profileName);

    /*    if(pharmaRegBean.getStrProfileName().equalsIgnoreCase("")) {

            String profileName = intent.getString(PROFILE_NAME_KEY);

            ((TextView) findViewById(R.id.pharma_username)).setText(getString(R.string.dashboard_title) + " " + profileName);
        }else{
            ((TextView) findViewById(R.id.pharma_username)).setText("Mr." + " " + pharmaRegBean.getStrProfileName());
        }*/
        Bundle b = getIntent().getExtras();
        if(b!=null) {
            signIn = b.getParcelable("signin");
        }

        findViewById(R.id.header_slider).setOnClickListener(this);
        findViewById(R.id.p_trackcompaigns).setOnClickListener(this);
        findViewById(R.id.p_docter_activity_score).setOnClickListener(this);
        findViewById(R.id.appointments).setOnClickListener(this);
        findViewById(R.id.p_survery).setOnClickListener(this);
        findViewById(R.id.p_docter_medrep_perforamnce).setOnClickListener(this);

        if(Utils.isNetworkAvailableWithOutDialog(this)){
            SharedPreferences preferences = getSharedPreferences(SplashScreen.SIGNIN_PREF, MODE_PRIVATE);
            int roleID = preferences.getInt(SplashScreen.ROLE_ID, -1);

            GetMyUpcomingAppointmentsAsync getMyUpcomingAppointmentsAsync;

            switch (roleID){
                case UserRoles.PHARMA_MEDREP:
                    getMyUpcomingAppointmentsAsync = new GetMyUpcomingAppointmentsAsync();
                    getMyUpcomingAppointmentsAsync.execute(HttpUrl.PHARMA_GET_MY_UPCOMING_APPOINTMENTS);

                    ((TextView) findViewById(R.id.vpagertext)).setText("No Upcoming Appointments Found.");
                    break;
                case UserRoles.PHARMA_MANAGER_MEDREP:
                    getMyUpcomingAppointmentsAsync = new GetMyUpcomingAppointmentsAsync();
                    getMyUpcomingAppointmentsAsync.execute(HttpUrl.PHARMA_GET_PHARMA_TEAM_PENDING_APPOINTMENTS);

                    ((TextView) findViewById(R.id.vpagertext)).setText("No Pending Appointments Found.");
                    break;
            }

        }

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PharmaNavigationDrawerFragment drawerFragment = (PharmaNavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Save refresh token object and sign in related information in shared preference

        RefreshToken refreshToken = SignIn.getRefreshToken();
        String accessToken = SignIn.GET_ACCESS_TOKEN();
        long accessExpiresIn = SignIn.GET_EXPIRES_IN();

        System.out.println("accessExpiresIn: " + accessExpiresIn);

        long accessExpiry = SignIn.GET_TOKEN_EXPIRY_IN_MILLIS();

        try{
            SharedPreferences singInPref = getSharedPreferences(SplashScreen.SIGNIN_PREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = singInPref.edit();

            editor.putString("RefreshToken", refreshToken.getRefreshToken());
            editor.putLong("RefreshExpiry", refreshToken.getExpireToken());
            editor.putString("AccessToken", accessToken);
            editor.putLong("AccessExpiresIn", accessExpiresIn);
            editor.putLong("AccessExpiry", accessExpiry);
            editor.commit();
        }catch(NullPointerException e){
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {

        Intent intent;

        SharedPreferences preferences = getSharedPreferences(SplashScreen.SIGNIN_PREF, MODE_PRIVATE);
        int roleID = preferences.getInt(SplashScreen.ROLE_ID, -1);

        switch(v.getId()){
            case R.id.appointments:
                switch (roleID){
                    case UserRoles.PHARMA_MEDREP:
                        intent = new Intent(PharmaDashBoard.this, PharmaRepDashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                    case UserRoles.PHARMA_MANAGER_MEDREP:
                        intent = new Intent(PharmaDashBoard.this, PharmaManagerDashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                    default:
                        Toast.makeText(PharmaDashBoard.this, "Invalid user login. Logout and try again.", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            case R.id.header_slider:
                intent=new Intent(PharmaDashBoard.this, PharmaProductCompaign.class);
                startActivity(intent);
                break;
            case R.id.p_trackcompaigns:
                intent=new Intent(PharmaDashBoard.this, PharmaProductCompaign.class);
                startActivity(intent);
                break;
            case R.id.p_docter_activity_score:
                intent = new Intent(PharmaDashBoard.this, PharmaCompanyDoctors.class);
                startActivity(intent);
                break;
            case R.id.p_survery:
//                Utils.DISPLAY_GENERAL_DIALOG(PharmaDashBoard.this, "Coming Soon", "This Feature is presently under development.");
                intent = new Intent(PharmaDashBoard.this, LatestSurveyReportsActivity.class);
                startActivity(intent);
                break;
            case R.id.p_docter_medrep_perforamnce:
            //    Utils.DISPLAY_GENERAL_DIALOG(PharmaDashBoard.this, "Coming Soon", "Functionality is under development.");
                switch (roleID){
                    case UserRoles.PHARMA_MEDREP:
                        intent = new Intent(PharmaDashBoard.this, PharmaRepDashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                    case UserRoles.PHARMA_MANAGER_MEDREP:
                        intent = new Intent(PharmaDashBoard.this, TeamActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        Toast.makeText(PharmaDashBoard.this, "Invalid user login. Logout and try again.", Toast.LENGTH_SHORT).show();
                        break;
                }


                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(((DrawerLayout)findViewById(R.id.drawer_layout)).isDrawerOpen(GravityCompat.START)){
            ((DrawerLayout)findViewById(R.id.drawer_layout)).closeDrawers();
        }else
            super.onBackPressed();
    }

    private class AppointmentsAdapter extends PagerAdapter{
        ArrayList<Appointment> appointmentsList;
        public AppointmentsAdapter(ArrayList<Appointment> appointmentsList){
            this.appointmentsList = appointmentsList;
        }

        @Override
        public int getCount() {
            return appointmentsList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            Appointment appointment = appointmentsList.get(position);

            System.out.println("appointment.getTherapeuticName(): " + appointment.getTherapeuticName());

            View rootView = View.inflate(PharmaDashBoard.this, R.layout.appointment_ribben_pharma, null);

            TextView name = (TextView) rootView.findViewById(R.id.name);
            TextView monthTV = (TextView) rootView.findViewById(R.id.month_tv);
            TextView dateTV = (TextView) rootView.findViewById(R.id.date_tv);
            TextView nameDesignation = (TextView) rootView.findViewById(R.id.name_designation);
//            TextView repName = (TextView) rootView.findViewById(R.id.repname);
            name.setText(appointment.getDoctorName());
            nameDesignation.setText(appointment.getTitle());
//            repName.setText("Mr. "+appointment.getPharmaRepName());

            String datetime = appointment.getStartDate();

            System.out.println("Kishore DateTime: " + datetime);

            if(datetime != null && datetime.trim().length() > 0){
                //            int year = Integer.parseInt(datetime.substring(0, 4));
                if(datetime.trim().length() > 6){
                    int month = Integer.parseInt(datetime.substring(4, 6));

                    try {
                        monthTV.setText(Utils.formatMonth(month).substring(0,3).toUpperCase());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else{
                   monthTV.setVisibility(View.GONE);
                }

                if(datetime.trim().length() > 8){
                    int date = Integer.parseInt(datetime.substring(6, 8));
                    dateTV.setText(date + "");
                }else{
                   dateTV.setVisibility(View.GONE);
                }

            }else{
                monthTV.setVisibility(View.GONE);
                dateTV.setVisibility(View.GONE);
            }



            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Appointment appointment = appointmentsList.get(position);
                    /*PharmaRepMyAppointmentActivity.APPOINTMENT = appointment;

                    Intent intent = new Intent(PharmaDashBoard.this, PharmaRepMyAppointmentActivity.class);
                    intent.putExtra("Position", position);
                    intent.putExtra(PharmaRepAppointmentActivity.UPCOMING_APPOINTMENTS_KEY, true);
                    startActivity(intent);*/

                    Intent intent=new Intent(PharmaDashBoard.this, PharmaRepAppointmentActivity.class);
                    intent.putExtra(PharmaDocterDetails.MEDREP_NAME_KEY, appointment.getPharmaRepName());
                    intent.putExtra(PharmaDocterDetails.DOCTOR_ID_KEY, appointment.getDoctorId());
                    intent.putExtra(PharmaRepAppointmentActivity.START_DATE_KEY, appointment.getStartDate());
                    intent.putExtra(PharmaRepAppointmentActivity.COMPANY_NAME_KEY, appointment.getCompanyname());

                    intent.putExtra(PharmaRepAppointmentActivity.TITLE_KEY, appointment.getTitle());
                    intent.putExtra(PharmaRepAppointmentActivity.LOCATION_KEY, appointment.getLocation());
                    intent.putExtra(PharmaRepAppointmentActivity.DOCTOR_NAME, appointment.getDoctorName());
                    intent.putExtra(PharmaRepAppointmentActivity.THERAPEUTIC_NAME, appointment.getTherapeuticName());
                    intent.putExtra(PharmaRepAppointmentActivity.DESCRIPTION, appointment.getAppointmentDesc());
                    intent.putExtra(PharmaBrochureActivity.NOTIFICATION_ID_KEY, appointment.getNotificationId());

                    intent.putExtra(Constants.mParacetomolTitle, Constants.mPharmaDocterDetails);

                    intent.putExtra(PharmaRepAppointmentActivity.COMPLETED_APPOINTMENTS_KEY, false);
                    intent.putExtra(PharmaRepAppointmentActivity.UPCOMING_APPOINTMENTS_KEY, false);
                    startActivity(intent);
                }
            });

            container.addView(rootView);

            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout)object);
        }
    }

    private class GetMyUpcomingAppointmentsAsync extends AsyncTask<String, Void, ArrayList<Appointment>>{

        @Override
        protected ArrayList<Appointment> doInBackground(String... params) {
            String url = params[0] + SignIn.GET_ACCESS_TOKEN();

            JSONParser parser = new JSONParser();
            JSONArray jsonArray = parser.getJSON_Response(url);

            System.out.println("jsonArray" +  jsonArray);
            int largestNum = 0;

            JSONArray swappedArray = new JSONArray();
            if(jsonArray != null) {
                for(int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        if(jsonObject.isNull("createdOn")) {
                            swappedArray.put(jsonObject);
                        } else {
                            int createdOn = jsonObject.getInt("createdOn");
                            if(createdOn > largestNum) {
                                largestNum = createdOn;
                                swappedArray.put(jsonObject);
                            } else {
                                swappedArray.put(jsonObject);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("swappedArray" +  swappedArray);

            ArrayList<Appointment> appointments = new ArrayList<>();

            parser.jsonParser(swappedArray, Appointment.class, appointments);

            return appointments;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Appointment> appointments) {
            super.onPostExecute(appointments);

            System.out.println("Kishore " + appointments.size());

            final ViewPager pager = (ViewPager) findViewById(R.id.appointments_ribbon_view_pager);
            final TextView tv = (TextView) findViewById(R.id.vpagertext);
//            tv.setText("No Upcoming Appointments Found.");

            if(appointments == null || appointments.size() == 0){
                //  Toast.makeText(PharmaDashBoard.this, "No Appointments Found.", Toast.LENGTH_SHORT).show();
                tv.setVisibility(View.VISIBLE);
                pager.setVisibility(View.GONE);
                // finish();
            }else{
                //Set appointments adapter


                tv.setVisibility(View.GONE);
                pager.setVisibility(View.VISIBLE);

                //Set appointments adapter




                final AppointmentsAdapter appointmentsAdapter = new AppointmentsAdapter(appointments);

                pager.setAdapter(appointmentsAdapter);

                pager.post(new Runnable() {
                               public void run() {
                                   pager.setCurrentItem(0);
                               }
                           });

                        final ImageView leftArrowIV = (ImageView) findViewById(R.id.iv_lefttArraow);
                final ImageView rightArrowIV = (ImageView) findViewById(R.id.iv_rightArraow);
                leftArrowIV.setAlpha(0.5f);

                rightArrowIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int currentItem = pager.getCurrentItem();
                        int count = appointmentsAdapter.getCount();

                        System.out.println("currentItem: " + currentItem);
                        System.out.println("count: " + count);


                        if (currentItem < count - 1) {
                            pager.setCurrentItem(currentItem + 1, true);
                            leftArrowIV.setAlpha(1.0f);
                            leftArrowIV.setEnabled(true);
                        } else {
                            rightArrowIV.setAlpha(0.5f);
                            rightArrowIV.setEnabled(false);
                        }
                    }
                });

                leftArrowIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentItem = pager.getCurrentItem();

                        if (currentItem > 0) {
                            pager.setCurrentItem(currentItem - 1, true);
                            rightArrowIV.setAlpha(1.0f);
                            rightArrowIV.setEnabled(true);
                        } else {
                            leftArrowIV.setAlpha(0.5f);
                            leftArrowIV.setEnabled(false);
                        }
                    }
                });


            }
        }
    }
}

