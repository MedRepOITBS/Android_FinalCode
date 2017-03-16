package com.app.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.adapter.DividerItemDecoration;
import com.app.adapter.MyRecyclerViewAdapter;
import com.app.pojo.DataObject;
import com.app.task.DoctorGetMethods;
import com.app.util.HttpUrl;
import com.app.util.Utils;

import java.util.ArrayList;

import medrep.medrep.DoctorDashboard;
import medrep.medrep.DoctorMyActivityScore;
import medrep.medrep.LoginActivity;
import medrep.medrep.MobileContactsActivity;
import medrep.medrep.ProfileViewActivity;
import medrep.medrep.R;
import medrep.medrep.SearchForDrugsActivity;
import medrep.medrep.SplashScreen;
import medrep.medrep.TransformActivity;


public class DoctorNavigationDrawerFragment extends Fragment {

    public static final String PREF_FILE_NAME = "medrepPref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";


    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View containerView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyRecyclerViewAdapter mAdapter;
//    private DoctorDashboard signInactivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        signInactivity = (DoctorDashboard)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        SharedPreferences signPref = getActivity().getSharedPreferences(SplashScreen.SIGNIN_PREF, getActivity().MODE_PRIVATE);
        int roleID = signPref.getInt(SplashScreen.ROLE_ID, -1);

        mAdapter = new MyRecyclerViewAdapter(getDataSet(), roleID);

        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();
        String[] myprofileList = getActivity().getResources().getStringArray(R.array.profile_list);
        int[] drawables = getActivity().getResources().getIntArray(R.array.drawable_list);
        for (int count = 0; count < myprofileList.length; count++){
            DataObject obj = new DataObject(myprofileList[count],
                    drawables[count]);
            results.add(count, obj);
        }
        return results;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i("TAG", " Clicked on Item " + position);
                LoginProfile(position);
            }
        });
    }

    private void LoginProfile(int position){
        Intent intent;
        SharedPreferences signPref = getActivity().getSharedPreferences(SplashScreen.SIGNIN_PREF, getActivity().MODE_PRIVATE);
        switch (position){
            case 0:
                //Get My profile
//                if(Utils.isNetworkAvailable(getActivity())) {
//                    new DoctorGetMethods(getActivity()).execute(HttpUrl.BASEURL + HttpUrl.DOCTOR_GET_MY_DETAILS, "", "doctorprofile");
//                }
                intent = new Intent(getActivity(), ProfileViewActivity.class);
                startActivity(intent);
                break;
            case 1:
                //Dashboard
                if(!(getActivity() instanceof DoctorDashboard)){
                    intent = new Intent(getActivity(), DoctorDashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else{
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                        fm.popBackStack();
                    }
                }

                break;
            case 2:
                //Notifications
                mDrawerLayout.closeDrawers();
                if(!(getActivity() instanceof DoctorDashboard)){
                    intent = new Intent(getActivity(), DoctorDashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("StartNotifications", true);
                    startActivity(intent);
                }else{
                    mDrawerLayout.closeDrawers();
                    DocNotificatonBrand notificationBrand = new DocNotificatonBrand();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, notificationBrand);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

//                new DoctorGetMethods(getActivity()).execute(HttpUrl.BASEURL + HttpUrl.MYNOTIFICATION, "/20140911?access_token="+signInactivity.signIn.GET_ACCESS_TOKEN(), "notification");
                break;
            case 3:
                //Surveys

                mDrawerLayout.closeDrawers();

                if(!(getActivity() instanceof DoctorDashboard)){
                    intent = new Intent(getActivity(), DoctorDashboard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("StartSurveys", true);
                    startActivity(intent);
                }else if(Utils.isNetworkAvailable(getActivity())) {
                    new DoctorGetMethods(getActivity()).execute(HttpUrl.BASEURL + HttpUrl.GET_PENDING_SURVEYS, "", "survery");
                }
                break;

            case 4:
                //activity score
                Intent aintent = new Intent(getActivity(), DoctorMyActivityScore.class);
                startActivity(aintent);
                break;
            case 5:
                //Markating Campagins
                Utils.DISPLAY_GENERAL_DIALOG(getActivity(), "Coming Soon", "This Feature is presently under development.");


                break;
            case 6:
                // Get Appointment Details
//                new DoctorGetMethods(getActivity()).execute(HttpUrl.BASEURL + HttpUrl.DOCTOR_APPOINTMENT, "", "appointment", "true");
                if(Utils.isNetworkAvailable(getActivity())) {
                    new DoctorGetMethods(getActivity()).execute(HttpUrl.BASEURL + HttpUrl.GET_APPOINTMENT,
                            "?access_token=" +
                                    Utils.GET_ACCESS_TOKEN(getActivity()), "appointment", "true");
                }
                break;
            case 7:
                //Discussion Forum
                Utils.DISPLAY_GENERAL_DIALOG(getActivity(), "Coming Soon", "This Feature is presently under development.");

                break;

            case 8:
                //search for drug
                //Utils.DISPLAY_GENERAL_DIALOG(getActivity(), "Coming Soon", "This Feature is presently under development.");
                intent = new Intent(getActivity(), SearchForDrugsActivity.class);
                startActivity(intent);
                break;

            case 9:
                //News and Updates
                //Utils.DISPLAY_GENERAL_DIALOG(getActivity(), "Coming Soon", "This Feature is presently development.");
                intent = new Intent(getActivity(), TransformActivity.class);
                startActivity(intent);
                break;
            case 10:
                //Markating Campagins
                Utils.DISPLAY_GENERAL_DIALOG(getActivity(), "Coming Soon", "This Feature is presently development.");
                break;
            case 11:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String name = signPref.getString("username", "");
                //SharedPreferences signPref = getActivity().getSharedPreferences(SplashScreen.SIGNIN_PREF, Context.MODE_PRIVATE);
                String message = "Dr. "+ name +"has invited you to join 'MedRep', a digital collboration platform for doctors. Please Download from iTunes and Google Playstore. iTunes link https://itunes.apple.com/in/app/medrep/id1087940083?mt=8  http://www.erfolglifesciences.com/";
                sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
//                intent = new Intent(getActivity(), MobileContactsActivity.class);
//                startActivity(intent);
                break;

            case 12:
                //Logout


                SharedPreferences.Editor editor = signPref.edit();
                editor.putBoolean(SplashScreen.IS_LOGGED_IN_KEY, false);
                editor.putString("RefreshToken", null);
                editor.putLong("RefreshExpiry", -1);
                editor.putString("AccessToken", null);
                editor.putLong("AccessExpiresIn", -1);
                editor.putLong("AccessExpiry", -1);
                editor.commit();


                getActivity().finish();
                intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        mDrawerLayout.closeDrawers();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*mUserLearnedDrawer = Boolean.valueOf(readToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if(savedInstanceState!=null)
            mFromSavedInstanceState = true;*/
    }
    int fragmentId;

    public void setUp(DrawerLayout drawerLayout, final Toolbar toolBar, int fragmentid) {
        this.fragmentId = fragmentid;
        containerView = getActivity().findViewById(fragmentid);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolBar, R.string.open, R.string.close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                /*if(!mUserLearnedDrawer){
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer+"");
                }*/
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                if(slideOffset < 0.6)
                    toolBar.setAlpha(1 - slideOffset);

            }
        };
   /*     if(!mUserLearnedDrawer&& !mFromSavedInstanceState){
            mDrawerLayout.openDrawer(containerView);
        }*/
        mDrawerLayout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readToPreferences(Context context, String preferenceName, String defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

}
