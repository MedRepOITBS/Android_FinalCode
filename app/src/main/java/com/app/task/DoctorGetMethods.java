package com.app.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.app.json.AppointmentListTreapaticArea;
import com.app.json.RegisterParse;
import com.app.json.SignInParser;
import com.app.json.SurveryParser;
import com.app.pojo.AppointmentList;
import com.app.pojo.MyProfile;
import com.app.pojo.SurveryList;
import com.app.util.GlobalVariables;
import com.app.util.Utils;

import medrep.medrep.DoctorDashboard;
import medrep.medrep.DoctorGetProfileWats;
import medrep.medrep.RegisterActivity;

/**
 * Created by masood on 9/5/15.
 */
public class DoctorGetMethods extends AsyncTask<String,Void, Object> {

    private Context context;
    private ProgressDialog dialog;
    private String accessToken;

    private boolean launchAppointments = false;

    public DoctorGetMethods(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(context, "please wait","Loading");
        dialog.show();
    }

    @Override
    protected Object doInBackground(String... params) {

        if(params.length > 3){
            launchAppointments = params[3].equalsIgnoreCase("true");
        }
//        Log.d("TAG","my response------------------------------------ ");
        try {
//            String response = DoctorPostMethods.sendGet(HttpUrl.BASEURL+HttpUrl.SIGNIN+"&username=umar.ashraf@gmail.com&password=Test123");
            String response;

            if(params[2].equalsIgnoreCase("survery")) {
                params[1] = Utils.GET_ACCESS_TOKEN((Activity) context);
                response = DoctorPostMethods.sendGet(params[0] + params[1]);
                Log.d("TAG", "my response------------------------------------ " + response);
                return SurveryParser.surveryParser(response);
            }else if(params[2].equalsIgnoreCase("appointment")){
                params[1] = "20150101?access_token=" + Utils.GET_ACCESS_TOKEN((Activity) context);
                response = DoctorPostMethods.sendGet(params[0] + params[1]);
                Log.d("TAG", "my response------------------------------------ " + response);
                return AppointmentListTreapaticArea.parseAppointment(response);
            }else if(params[2].equalsIgnoreCase("doctorprofile")){
                params[1] = Utils.GET_ACCESS_TOKEN((Activity) context);
                response = DoctorPostMethods.sendGet(params[0] + params[1]);
                Log.d("TAG", "my response------------------------------------ " + response);
                accessToken = params[1];
                return  new RegisterParse().getMyProfile(response);
            }
            else {
                response = DoctorPostMethods.sendGet(params[0] + params[1]);
                Log.d("TAG", "my response------------------------------------ " + response);
                return SignInParser.getSignInParser(response);
            }

        }catch(Exception ex){
            ex.printStackTrace();
            if(!GlobalVariables.isConnected(context)){
                return "Check internet connection";
            }else{
                Log.d("TAG","authentication challenge " +ex.getMessage().contains("authentication challenge"));
                return null;
            }


        }

    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        dialog.dismiss();
        if(o!=null){
            if(o instanceof String){
                Toast.makeText(context, (String) o, Toast.LENGTH_SHORT).show();
            }else if(o instanceof SurveryList){
                SurveryList surveryList = (SurveryList) o;

                if(context instanceof DoctorDashboard){
                    ((DoctorDashboard)context).getSurveyList(surveryList);
                }
            }
//            else if(o instanceof AppointmentList){
//
//                AppointmentList appointmentList = (AppointmentList)o;
//              //  Toast.makeText(context, "AppointmentListTreapaticArea list", Toast.LENGTH_SHORT).show();
//                if(context instanceof DoctorDashboard){
//                    ((DoctorDashboard)context).getNotification(appointmentList, launchAppointments);
//                }

            }else if(o instanceof MyProfile){

//                if(context instanceof DoctorDashboard) {
                    Intent intent = new Intent(context, DoctorGetProfileWats.class);
                    context.startActivity(intent);
//                }
            }else {

                Toast.makeText(context, "signIn sucessfull", Toast.LENGTH_SHORT).show();
               /* Intent intent = new Intent(context, SignInDrawerActivity.class);
                intent.putExtra("signin",(SignIn)o);
                context.startActivity(intent);*/
            }
           /* Intent intent = new Intent(context, RegisterCategoryActivity.class);
            context.startActivity(intent);*/
        }
    }
