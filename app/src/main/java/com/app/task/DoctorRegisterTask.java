package com.app.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.app.pojo.DoctorProfile;
import com.app.pojo.Register;
import com.app.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import medrep.medrep.DoctorSetPasswordActivity;
import medrep.medrep.RegisterActivity;

/**
 * Created by masood on 8/13/15.
 */
public class DoctorRegisterTask extends AsyncTask<String,Void, Object> {

    private Context context;
    private DoctorProfile doctorProfile;
    private ProgressDialog dialog;
    private boolean updateProfile;
    String status;
    String message;

    public DoctorRegisterTask(Context context, DoctorProfile doctorProfile, boolean updateProfile){
        this.context = context;
        this.doctorProfile = doctorProfile;
        this.updateProfile = updateProfile;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(context, "Please wait", "Loading..");
        dialog.show();
    }

    @Override
    protected Object doInBackground(String... params) {

        String url = params[0];

        if(updateProfile){
            url = url + Utils.GET_ACCESS_TOKEN((Activity)context);
        }

        String response = DoctorPostMethods.POST(url, doctorProfile);

        System.out.println("Update Profile Response: " + response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            status = jsonObject.getString("status");
            message = jsonObject.getString("message");
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        return message;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        dialog.dismiss();
        if(message != null){
            if(context instanceof DoctorSetPasswordActivity){
                ((DoctorSetPasswordActivity)context).successResult(message);
            }

            if(message.equalsIgnoreCase("Success") && context instanceof RegisterActivity){
                ((RegisterActivity)context).successResult();
            }else if(context instanceof RegisterActivity){
                ((RegisterActivity)context).duplicateData(message);
            }
        }else{
            if(context instanceof RegisterActivity)
                ((RegisterActivity)context).failure();
        }
//        {"status":"OK","message":"Success"}
    }
}
