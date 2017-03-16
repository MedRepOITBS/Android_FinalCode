package medrep.medrep;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.db.MedRepDatabaseHandler;
import com.app.fragments.DocNotificatonBrand;
import com.app.fragments.DoctorNavigationDrawerFragment;
import com.app.fragments.DoctorSurveyListLV;
import com.app.fragments.NotificationDetails;
import com.app.interfaces.GetResponse;
import com.app.json.SignInParser;
import com.app.pojo.AppointmentList;
import com.app.pojo.RefreshToken;
import com.app.pojo.SignIn;
import com.app.pojo.Survery;
import com.app.pojo.SurveryList;
import com.app.reminder.AlarmReceiver;
import com.app.task.AppointmentsAsyncTask;
import com.app.task.DoctorGetMethods;
import com.app.task.DoctorPostMethods;
import com.app.task.NotificationGetTask;
import com.app.util.GlobalVariables;
import com.app.util.HttpUrl;
import com.app.util.OnSwipeTouchListener;
import com.app.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DoctorDashboard extends AppCompatActivity implements View.OnClickListener , View.OnTouchListener, GetResponse {

    private Toolbar toolbar;
    private TextView  doctor_username;
    private AppointmentList appointmentsList;
    private ArrayList<TextView> notificationTextViewObjects = new ArrayList<>();
    private ArrayList<TextView> appointmentsTextViewObjects = new ArrayList<>();

   // private ScreenSlidePagerAdapter mPagerAdapter;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        System.out.println("requestCode == OPEN_IMAGE_REQ_CODE: " + (requestCode == NotificationDetails.OPEN_IMAGE_REQ_CODE));

        if(requestCode == NotificationDetails.OPEN_IMAGE_REQ_CODE){
            /*File imageFile = new File(getActivity().getExternalCacheDir().getAbsolutePath(), ".temp");
            if(imageFile != null && imageFile.exists()){
                imageFile.delete();
            }*/
            clearApplicationData(getExternalCacheDir().getAbsolutePath());
        }
    }

    public static void clearApplicationData(String path)
    {
        try {
            Runtime.getRuntime().exec(String.format("rm -rf %s", path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

LinearLayout header_slider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_dashbord1);

        if(Utils.isNetworkAvailableWithOutDialog(DoctorDashboard.this)){
            getAppointmentsList();
        }

        String accessToken = SignIn.GET_ACCESS_TOKEN();
        System.out.println("accessToen_______________________:"+ accessToken);
        Date date = new Date();
        String modifiedDate= new SimpleDateFormat("yyyyMMdd").format(date);
        System.out.println("date_______________________:"+ modifiedDate);
        String[] values = {accessToken, modifiedDate};
        NotificationGetTask asyncTask = new NotificationGetTask();
        asyncTask.delegate = this;
        //System.out.println(values);
        String url = HttpUrl.MYNOTIFICATION+modifiedDate+"?access_token="+accessToken;
        asyncTask.execute(url);

        AppointmentsAsyncTask appointmentAsyncTask = new AppointmentsAsyncTask(this);
        //System.out.println(values);
        String appointmentUrl = HttpUrl.DOCTOR_APPOINTMENTS + accessToken;
        appointmentAsyncTask.execute(appointmentUrl);

        header_slider=(LinearLayout)findViewById(R.id.header_slider);

        findViewById(R.id.header_slider).setOnClickListener(this);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.notifications).setOnClickListener(this);
        findViewById(R.id.survey).setOnClickListener(this);
        findViewById(R.id.activity_score).setOnClickListener(this);
        findViewById(R.id.marketing_campaigns).setOnClickListener(this);
        findViewById(R.id.search_for_drugs).setOnClickListener(this);
        findViewById(R.id.news).setOnClickListener(this);
        //findViewById(R.id.getAppointments_Button).setOnClickListener(this);
        findViewById(R.id.notificationMore).setOnClickListener(this);
        findViewById(R.id.appointmentMore).setOnClickListener(this);
        TextView notificationOne = (TextView)findViewById(R.id.notificationOne);
        TextView notificationTwo = (TextView)findViewById(R.id.notificationTwo);
        TextView notificationThree = (TextView)findViewById(R.id.notificationThree);
        notificationTextViewObjects.add(notificationOne);
        notificationTextViewObjects.add(notificationTwo);
        notificationTextViewObjects.add(notificationThree);

        TextView appointmentOne = (TextView)findViewById(R.id.appointmentOne);
        TextView appointmentTwo = (TextView)findViewById(R.id.appointmentTwo);
        TextView appointmentThree = (TextView)findViewById(R.id.appointmentThree);
        appointmentsTextViewObjects.add(appointmentOne);
        appointmentsTextViewObjects.add(appointmentTwo);
        appointmentsTextViewObjects.add(appointmentThree);

        doctor_username = (TextView) findViewById(R.id.doctor_username);
        doctor_username.setOnClickListener(this);


        DoctorNavigationDrawerFragment drawerFragment = (DoctorNavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                int backStackCount = fragmentManager.getBackStackEntryCount();

                System.out.println("fragmentManager.getBackStackEntryCount(): " + backStackCount);

                if (backStackCount == 0) {
                    View view = findViewById(R.id.dashboard);
                    if (view.getVisibility() == View.GONE) {
                        view.setVisibility(View.VISIBLE);
                    }
                }

            }
        });

        Intent intent = getIntent();

        if(intent.getBooleanExtra("StartNotifications", false)){

            int notificationID = intent.getIntExtra(AlarmReceiver.NOTIFICATION_ID_KEY, -100);

            if(notificationID != -100){
                NotificationDetails.CURRENT_NOTIFICATION_ID = notificationID;
                NotificationDetails.CURRENT_NOTIFICATION_TITLE = MedRepDatabaseHandler.getInstance(DoctorDashboard.this).getNotificationName(notificationID);

                NotificationDetails notificationDetails = new NotificationDetails();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, notificationDetails)/*replace(R.id.frame, notificationBrand)*/;
                fragmentTransaction.addToBackStack("NotificationDetails");
                fragmentTransaction.commit();
            }else{
                DocNotificatonBrand notificationBrand = new DocNotificatonBrand();
                /*FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, notificationBrand);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/

               // displayFragment(notificationBrand);
            }
        }

        if(intent.getBooleanExtra("StartSurveys", false)){
            if(Utils.isNetworkAvailable(DoctorDashboard.this)) {
                new DoctorGetMethods(DoctorDashboard.this).execute(HttpUrl.BASEURL + HttpUrl.GET_PENDING_SURVEYS, "", "survery");
            }
        }

        LinearLayout appointmentLayout = (LinearLayout)findViewById(R.id.appointmentLayout);
        appointmentLayout.setOnTouchListener(new OnSwipeTouchListener(DoctorDashboard.this) {
            @Override
            public void onSwipeRight() {
                Utils.DISPLAY_GENERAL_DIALOG(DoctorDashboard.this, "Coming Soon", "right swipe");
            }

            public void onSwipeLeft() {
                Utils.DISPLAY_GENERAL_DIALOG(DoctorDashboard.this, "Coming Soon", "left swipe");
            }
        });
    }

    public void getResult(String response) {

    }

    public void getResponse(String response) {
        ArrayList<String> AppointmentsTitles = new ArrayList<>();
        if(response != null) {
            try {
                JSONArray object = new JSONArray(response);
                if(object != null) {
                    for (int i = 0; i < object.length(); i++) {
                        String name = object.getJSONObject(i).getString("title");
                        if(name != null) {
                            AppointmentsTitles.add(name);
                        }

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        for(int i = 0; i < AppointmentsTitles.size(); i++) {
            if(i < appointmentsTextViewObjects.size()) {
                appointmentsTextViewObjects.get(i).setText(AppointmentsTitles.get(i).toString());
            }
        }
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


        SharedPreferences singInPref = getSharedPreferences(SplashScreen.SIGNIN_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = singInPref.edit();

        //editor.putString("RefreshToken", refreshToken.getRefreshToken());
        editor.putLong("RefreshExpiry", refreshToken.getExpireToken());
        editor.putString("AccessToken", accessToken);
        editor.putLong("AccessExpiresIn", accessExpiresIn);
        editor.putLong("AccessExpiry", accessExpiry);
        editor.commit();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        System.out.println("onResumeFragments is being called");
    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("onResume is being called");

        /*Intent intent = getIntent();
        String profileName = intent.getStringExtra(SplashScreen.PROFILE_NAME_KEY);
        ((TextView)findViewById(R.id.doctor_username)).setText(getString(R.string.dashboard_title) + " " + profileName);

        System.out.println("Kishore profileName: " + profileName);*/

        SharedPreferences signPref = getSharedPreferences(SplashScreen.SIGNIN_PREF, MODE_PRIVATE);
        String profileName = signPref.getString(SplashScreen.PROFILE_NAME_KEY, "User");
        ((TextView)findViewById(R.id.doctor_username)).setText(getString(R.string.dashboard_title) + " " + profileName);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.header_slider:
                DocNotificatonBrand notificationBrand1 = new DocNotificatonBrand();

               // displayFragment(notificationBrand1);
                break;
            case  R.id.notifications:
                DocNotificatonBrand notificationBrand = new DocNotificatonBrand();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, notificationBrand);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                displayFragment(notificationBrand);
                break;
//            case R.id.getAppointments_Button:
//                if(Utils.isNetworkAvailable(DoctorDashboard.this)){
//                    new DoctorGetMethods(this).execute(HttpUrl.BASEURL + HttpUrl.GET_APPOINTMENT,
//                            "20150101?access_token=", "appointment", "true");
//                }

               /*if(appointmentsList == null){
                   Toast.makeText(DoctorDashboard.this, "Appointment are being retrieved, please try after some time.", Toast.LENGTH_SHORT).show();
               }*/
                //break;
            case R.id.survey:
                if(Utils.isNetworkAvailable(DoctorDashboard.this)) {
                    new DoctorGetMethods(DoctorDashboard.this).execute(HttpUrl.BASEURL + HttpUrl.GET_PENDING_SURVEYS, "", "survery");
                }
                break;
            case R.id.activity_score:
//                Utils.DISPLAY_GENERAL_DIALOG(DoctorDashboard.this, "Coming Soon", "Functionality is under development.");
                Intent intent = new Intent(DoctorDashboard.this, DoctorMyActivityScore.class);
                startActivity(intent);
                break;
            case R.id.marketing_campaigns:
                //Utils.DISPLAY_GENERAL_DIALOG(DoctorDashboard.this, "Coming Soon", "This Feature is presently under development.");
                Intent campaignIntent = new Intent(this, MarketingCampaignsActivity.class);
                startActivity(campaignIntent);
                break;
            case R.id.search_for_drugs:
                //Utils.DISPLAY_GENERAL_DIALOG(DoctorDashboard.this, "Coming Soon", "This Feature is presently under development.");
                Intent drugActivity = new Intent(this, SearchForDrugsActivity.class);
                startActivity(drugActivity);
                break;
            case R.id.news:
                //Utils.DISPLAY_GENERAL_DIALOG(DoctorDashboard.this, "Coming Soon", "This Feature is presently under development.");
                Intent noContactsIntent = new Intent(this, TransformActivity.class);
                startActivity(noContactsIntent);
                break;
            case R.id.appointmentMore:
                Intent appointmentIntent = new Intent(this, DoctorNotificationActivity.class);
                appointmentIntent.putExtra("moreButton", "Appointment");
                startActivity(appointmentIntent);
                break;
            case R.id.notificationMore:
                Intent notificationIntent = new Intent(this, DoctorNotificationActivity.class);
                notificationIntent.putExtra("moreButton", "Notification");
                startActivity(notificationIntent);
                break;
        }

    }

    public void getSurveyList(SurveryList surveyList){

        ArrayList<Survery> surveysList = surveyList.getSurveryArrayList();
        if(surveysList != null && surveysList.size() > 0){
            DoctorSurveyListLV surveyListLV = new DoctorSurveyListLV();
            surveyListLV.setSurveyList(surveyList);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, surveyListLV);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            displayFragment(surveyListLV);
        }else{
            Toast.makeText(DoctorDashboard.this, "No surveys available.", Toast.LENGTH_SHORT).show();
        }


    }

//    public void getNotification(AppointmentList appointmentList, boolean launchAppointments) {
//        Log.d("TAG", "my appointment list " + appointmentList);
//
//        this.appointmentsList = appointmentList;
//
//        ArrayList<Appointment> appointments = appointmentList.getAppointmentArrayList();


//        final ViewPager pager = (ViewPager) findViewById(R.id.appointments_ribbon_view_pager);
//        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
//        mPagerAdapter.setAppointmentsList(appointments);
//        pager.setAdapter(mPagerAdapter);

//        if(appointments == null || appointments.size() == 0){
//            pager.setVisibility(View.GONE);
//            findViewById(R.id.vpagertext).setVisibility(View.VISIBLE);
//        }else{
//            pager.setVisibility(View.VISIBLE);
//            findViewById(R.id.vpagertext).setVisibility(View.GONE);
//        }
//
//        ImageView leftArrowIV = (ImageView) findViewById(R.id.iv_lefttArraow);
//        ImageView rightArrowIV = (ImageView) findViewById(R.id.iv_rightArraow);
//
//        rightArrowIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                int currentItem = pager.getCurrentItem();
//                int count = mPagerAdapter.getCount();
//
//                if(currentItem < count - 1){
//                    pager.setCurrentItem(currentItem + 1, true);
//                }
//            }
//        });
//
//        leftArrowIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int currentItem = pager.getCurrentItem();
//
//                if(currentItem > 0){
//                    pager.setCurrentItem(currentItem - 1, true);
//                }
//            }
//        });

//        if(launchAppointments){
//
//            if(appointmentsList != null && appointmentList.getAppointmentArrayList() != null && appointmentsList.getAppointmentArrayList().size() > 0){
//                Bundle bundle = new Bundle();
//                bundle.putParcelable("appointment", appointmentsList);
//                DoctorGetAppointmentListv notificationBrand = new DoctorGetAppointmentListv();
//                notificationBrand.setArguments(bundle);
//            /*FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.frame, notificationBrand);
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();*/
//
//               // displayFragment(notificationBrand);
//            }else{
//                Utils.DISPLAY_GENERAL_DIALOG(DoctorDashboard.this, "No Appointments", "No appointments found. Please try again later.");
//            }
//
//        }

        /*Bundle bundle = new Bundle();
        bundle.putParcelable("appointment", appointmentList);
        DoctorGetAppointmentListv notificationBrand = new DoctorGetAppointmentListv();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, notificationBrand);
        fragmentTransaction.addToBackStack(null);
        notificationBrand.setArguments(bundle);
        fragmentTransaction.commit();*/
   // }

    public void getAppointmentsList(){
        if(Utils.isNetworkAvailable(DoctorDashboard.this)) {
            new DoctorGetMethods(this).execute(HttpUrl.BASEURL + HttpUrl.GET_APPOINTMENT,
                    "", "appointment");
        }
    }
    @Override
    public void onBackPressed() {
        if(((DrawerLayout)findViewById(R.id.drawer_layout)).isDrawerOpen(GravityCompat.START)){
            ((DrawerLayout)findViewById(R.id.drawer_layout)).closeDrawers();
        }else
            super.onBackPressed();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void response(String result) {
        System.out.println(result);
        ArrayList<String> notificationNames = new ArrayList<>();
        try {
            if(result != null) {
                JSONArray object = new JSONArray(result);
                for (int i = 0; i < object.length(); i++) {
                    String name = object.getJSONObject(i).getString("notificationName");
                    if (name != null) {
                        notificationNames.add(name);
                    }

                }
            }
        } catch (JSONException e) {
                e.printStackTrace();
        }
        for(int i = 0; i < notificationNames.size(); i++) {
            if(i < notificationTextViewObjects.size()) {
                System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&"+ notificationNames.get(i));
                notificationTextViewObjects.get(i).setText(notificationNames.get(i).toString());
            }
        }
    }

//    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
//        private ArrayList<Appointment> appointmentsList;
//
//        public ScreenSlidePagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//
//            Appointment appointment = appointmentsList.get(position);
//
////            ScreenSlidePageFragment.APPOINTMENT = appointment;
//
//            ScreenSlidePageFragment screenSlidePageFragment = new ScreenSlidePageFragment();
//            screenSlidePageFragment.setAppointment(appointment);
//
//            return screenSlidePageFragment;
//        }
//
//        @Override
//        public int getCount() {
//            return appointmentsList.size();
//        }
//
//        public void setAppointmentsList(ArrayList<Appointment> appointmentsList) {
//            this.appointmentsList = appointmentsList;
//        }
//    }

//    public static class ScreenSlidePageFragment extends Fragment {
//        private Appointment appointment;
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            final ViewGroup rootView = (ViewGroup) inflater.inflate(
//                    R.layout.appointment_ribbon_item, container, false);
//
//            TextView name = (TextView) rootView.findViewById(R.id.name);
//            TextView monthTV = (TextView) rootView.findViewById(R.id.month_tv);
//            TextView dateTV = (TextView) rootView.findViewById(R.id.date_tv);
//            TextView nameDesignation = (TextView) rootView.findViewById(R.id.name_designation);
//
//            String datetime = appointment.getStartDate();
//
////            int year = Integer.parseInt(datetime.substring(0, 4));
//            int month = Integer.parseInt(datetime.substring(4, 6));
//            int date = Integer.parseInt(datetime.substring(6, 8));
//
//            name.setText(/*APPOINTMENT.getTitle()*/appointment.getCompanyName());
//          //  nameDesignation.setText(appointment.getAppointmentDesc());
//            nameDesignation.setText(appointment.getTitle());
//
//            try {
//                monthTV.setText(Utils.formatMonth(month).substring(0,3).toUpperCase());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            dateTV.setText(date + "");
//
//            rootView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    DoctorGetAppointmentDetails fragment = new DoctorGetAppointmentDetails();
//                    fragment.setAppointment(appointment, rootView);
//                   /* FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.replace(R.id.frame, fragment);
//                    fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();*/
//
//                    ((DoctorDashboard)getActivity()).displayFragment(fragment);
//                }
//            });


            //return rootView;
//            return null;
//        }
//
//        public void setAppointment(Appointment appointment) {
//            this.appointment = appointment;
//        }
//    }

    private void displayFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        findViewById(R.id.dashboard).setVisibility(View.GONE);
    }
}
