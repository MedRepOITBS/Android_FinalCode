package com.app.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.app.util.GlobalVariables;
import com.app.util.JSONParser;
import com.app.util.PharmaRegBean;
import com.app.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pharma.PharmaUpdateActivity;
import pharma.model.PharmaRepProfile;


public class PharmaGetMethods extends AsyncTask<String,Void, Object> {
    String response=null;
    private Context context;
    private ProgressDialog dialog;
//    private String accessToken;
//PharmaRegBean prb;
    public PharmaGetMethods(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(context, "Loading","Please wait while loading required information.");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Object doInBackground(String... params) {
//        Log.d("TAG","my response------------------------------------ ");
        try {
//            String response = DoctorPostMethods.sendGet(HttpUrl.BASEURL+HttpUrl.SIGNIN+"&username=umar.ashraf@gmail.com&password=Test123");
            if (params[2].equalsIgnoreCase("pharmaprofile")) {

                String url = params[0] + Utils.GET_ACCESS_TOKEN((Activity)context);

                JSONParser parser = new JSONParser();
                JSONObject jsonObject = parser.getJSON_Response(url, true);

                PharmaRepProfile repProfile = (PharmaRepProfile) parser.jsonParser(jsonObject, PharmaRepProfile.class);

                return repProfile;
             //   return SurveryParser.surveryParser(response);
            }/*else if(params[2].equalsIgnoreCase("appointment")){
                params[1] = "20150101?access_token=" + Utils.GET_ACCESS_TOKEN();
                response = DoctorPostMethods.sendGet(params[0] + params[1]);
                Log.d("TAG", "my response------------------------------------ " + response);
                return AppointmentListTreapaticArea.parseAppointment(response);
            }else if(params[2].equalsIgnoreCase("doctorprofile")){
                params[1] = Utils.GET_ACCESS_TOKEN();
                response = DoctorPostMethods.sendGet(params[0] + params[1]);
                Log.d("TAG", "my response------------------------------------ " + response);
                accessToken = params[1];
                return  new RegisterParse().getMyProfile(response);
            }
            else {
                response = DoctorPostMethods.sendGet(params[0] + params[1]);
                Log.d("TAG", "my response------------------------------------ " + response);
                return SignInParser.getSignInParser(response);
            }*/
            return response;

        } catch (Exception ex) {
            ex.printStackTrace();
            if (!GlobalVariables.isConnected(context)) {
                return "Check internet connection";
            } else {
                Log.d("TAG", "authentication challenge " + ex.getMessage().contains("authentication challenge"));
                return null;
            }


        }


    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        dialog.dismiss();

        if(o instanceof PharmaRepProfile){

            PharmaUpdateActivity.REP_PROFILE = (PharmaRepProfile) o;

            Intent intent = new Intent(context, PharmaUpdateActivity.class);
            intent.putExtra("myprofile", true);
//        intent.putExtra("accesstoken", accessToken);
            context.startActivity(intent);
        }


        /*prb= PharmaRegBean.getInstance();
        Utils.isPharmaUpdate=true;

        try {
            JSONObject jobj = new JSONObject(response);
            prb.setMdEmailId(jobj.getString("emailId"));
            prb.setMdFirstName(jobj.getString("firstName"));
            prb.setMdLastName(jobj.getString("lastName"));
            prb.setMdMobileNumber(jobj.getString("mobileNo"));

            prb.setMdAreaCovered(jobj.getString("coveredArea"));

            prb.setRmMail(jobj.getString("managerEmail"));
            prb.setRmMobile(jobj.getString("phoneNo"));
            prb.setMdRmName("");

            JSONArray jArray=jobj.getJSONArray("locations");

            JSONObject job=jArray.getJSONObject(0);

            prb.setMdaddress1(job.getString("address1"));
            prb.setMmdAddress2(job.getString("address2"));
            prb.setMdState(job.getString("state"));
            prb.setMdCity(job.getString("city"));
            prb.setMdZipcode(job.getString("zipcode"));
            prb.setMdCompanyName(jobj.getString("companyName"));

        }catch(JSONException e){

        }*/





   /*     if(o!=null){
            if(o instanceof String){
                Toast.makeText(context, (String) o, Toast.LENGTH_SHORT).show();
            }else if(o instanceof SurveryList){
                Toast.makeText(context, "Survery List", Toast.LENGTH_SHORT).show();
            } else if(o instanceof AppointmentList){

                AppointmentList appointmentList = (AppointmentList)o;
              //  Toast.makeText(context, "AppointmentListTreapaticArea list", Toast.LENGTH_SHORT).show();
                if(context instanceof DoctorDashboard){
                    ((DoctorDashboard)context).getNotification(appointmentList);
                }

            }else if(o instanceof MyProfile){

                if(context instanceof DoctorDashboard) {
                Intent intent = new Intent(context, RegisterActivity.class);
                intent.putExtra("myprofile", true);
                    intent.putExtra("accesstoken", accessToken);
//                    Bundle b1 = new Bundle();

                    context.startActivity(intent);
                }
            }else {
       *//* b1.putString("selectedCategory",register.getSelectedCat());
                    b1.putParcelable("register", register);*//*
                    *//*intent.putExtra("register", register);
                    Log.d("TAG", "My selected category is "+register.getSelectedCat());
                    intent.putExtra("selectedCategory",register.getSelectedCat());*//*
//                    intent.putExtra("bundle", b1);
                Toast.makeText(context, "signIn sucessfull", Toast.LENGTH_SHORT).show();
               *//* Intent intent = new Intent(context, SignInDrawerActivity.class);
                intent.putExtra("signin",(SignIn)o);
                context.startActivity(intent);*//*
            }
           *//* Intent intent = new Intent(context, RegisterCategoryActivity.class);
            context.startActivity(intent);*//*
        }else{
            Toast.makeText(context, "signIn unsucessfull", Toast.LENGTH_SHORT).show();
        }*/
    }
}
