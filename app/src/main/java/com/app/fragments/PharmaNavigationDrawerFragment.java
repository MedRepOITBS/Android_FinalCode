package com.app.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.adapter.DividerItemDecoration;
import com.app.adapter.MyRecyclerViewAdapter;
import com.app.pojo.DataObject;
import com.app.pojo.RefreshToken;
import com.app.pojo.SignIn;
import com.app.task.PharmaGetMethods;
import com.app.util.HttpUrl;
import com.app.util.UserRoles;
import com.app.util.Utils;

import java.util.ArrayList;

import medrep.medrep.LoginActivity;
import medrep.medrep.R;
import medrep.medrep.SplashScreen;
import pharma.PharmaCompanyDoctors;
import pharma.PharmaDashBoard;
import pharma.PharmaManagerDashboard;
import pharma.PharmaProductCompaign;
import pharma.PharmaRepDashboard;


public class PharmaNavigationDrawerFragment extends Fragment {

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
    private Activity signInactivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        signInactivity = (Activity)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        SharedPreferences signPref = getActivity().getSharedPreferences(SplashScreen.SIGNIN_PREF, getActivity().MODE_PRIVATE);
        int roleID = signPref.getInt(SplashScreen.ROLE_ID, -1);

        mAdapter = new MyRecyclerViewAdapter(getDataSet(), roleID);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();
        String[] myprofileList = getActivity().getResources().getStringArray(R.array.pharma_profile_list);
        int[] drawables = getActivity().getResources().getIntArray(R.array.pharma_drawable_list);
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

        switch (position){
            case 0:

                //Get My profile
              //  Log.d("TAG","get Accesss token"+signInactivity.signIn.GET_ACCESS_TOKEN());
                new PharmaGetMethods(getActivity()).execute(HttpUrl.BASEURL + HttpUrl.PHARMA_GET_MY_DETAILS, "" ,"pharmaprofile");
                break;
            case 1:
                //Dashboard
                if(!(getActivity() instanceof PharmaDashBoard)){
                    intent = new Intent(getActivity(), PharmaDashBoard.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                break;
            case 2:
                //Product Campaigns
                intent=new Intent(getActivity(), PharmaProductCompaign.class);
                startActivity(intent);

                break;
            case 3:
                //Pharma Activity score
                intent = new Intent(getActivity(), PharmaCompanyDoctors.class);
                startActivity(intent);
                break;
            case 4:
                //Survey
                Utils.DISPLAY_GENERAL_DIALOG(getActivity(), "Coming Soon", "This Feature is presently under development.");

                break;

            case 5:
                //Medrep

                SharedPreferences preferences = getActivity().getSharedPreferences(SplashScreen.SIGNIN_PREF, getActivity().MODE_PRIVATE);
                int roleID = preferences.getInt(SplashScreen.ROLE_ID, -1);

                switch (roleID){
                    case UserRoles.PHARMA_MEDREP:
                        intent = new Intent(getActivity(), PharmaRepDashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                    case UserRoles.PHARMA_MANAGER_MEDREP:
                        intent = new Intent(getActivity(), PharmaManagerDashboard.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                    default:
                        Toast.makeText(getActivity(), "Invalid user login. Logout and try again.", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            case 6:
                //other Markating Campaigns
                Utils.DISPLAY_GENERAL_DIALOG(getActivity(), "Coming Soon", "This Feature is presently under development.");

                break;


            case 7:
                //Drug Sales
                Utils.DISPLAY_GENERAL_DIALOG(getActivity(), "Coming Soon", "This Feature is presently under development.");

                break;

            case 8:
                //Prescriptio
                Utils.DISPLAY_GENERAL_DIALOG(getActivity(), "Coming Soon", "This Feature is presently under development.");

                break;
            case 9:
                //News and updates
                Utils.DISPLAY_GENERAL_DIALOG(getActivity(), "Coming Soon", "This Feature is presently under development.");

                break;

            case 10:
                //Settings
                Utils.DISPLAY_GENERAL_DIALOG(getActivity(), "Coming Soon", "This Feature is presently under development.");

                break;
            case 11:
                //Invite Contact
                Utils.DISPLAY_GENERAL_DIALOG(getActivity(), "Coming Soon", "This Feature is presently under development.");
                break;
            case 12:
                //Logout
                SharedPreferences signPref = getActivity().getSharedPreferences(SplashScreen.SIGNIN_PREF, getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = signPref.edit();
                editor.putBoolean(SplashScreen.IS_LOGGED_IN_KEY, false);
                editor.putString("RefreshToken", null);
                editor.putLong("RefreshExpiry", -1);
                editor.putString("AccessToken", null);
                editor.putLong("AccessExpiresIn", -1);
                editor.putLong("AccessExpiry", -1);
                editor.putInt(SplashScreen.ROLE_ID, -1);
                editor.putString(SplashScreen.PROFILE_NAME_KEY, "");
                editor.commit();

                PharmaDashBoard.profileName = "";

                getActivity().finish();
                intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                SignIn.setRefreshToken(null);
                SignIn.SET_ACCESS_TOKEN(null);
                SignIn.SET_EXPIRES_IN(0);
                SignIn.SET_TOKEN_EXPIRY_IN_MILLIS(0);
                break;
           /* case 1:
                //Notifications
                mDrawerLayout.closeDrawers();
                DocNotificatonBrand notificationBrand = new DocNotificatonBrand();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, notificationBrand);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

//                new DoctorGetMethods(getActivity()).execute(HttpUrl.BASEURL + HttpUrl.MYNOTIFICATION, "/20140911?access_token="+signInactivity.signIn.GET_ACCESS_TOKEN(), "notification");
                break;
            case 2:
                //Surveys
                new DoctorGetMethods(getActivity()).execute(HttpUrl.BASEURL + HttpUrl.SURVEYS, "", "survery");
                break;
            case 5:
                // Get Appointment Details
                new DoctorGetMethods(getActivity()).execute(HttpUrl.BASEURL + HttpUrl.DOCTOR_APPOINTMENT, "", "appointment");
                break;
            case 10:

                //Logout
                getActivity().finish();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;*/
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
