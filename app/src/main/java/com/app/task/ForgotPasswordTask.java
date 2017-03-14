package com.app.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.app.pojo.ForgotPassword;

import org.json.JSONException;
import org.json.JSONObject;

import medrep.medrep.ForgotPasswordActivity;

/**
 * Created by masood on 8/26/15.
 */
public class ForgotPasswordTask extends AsyncTask<String, Void, Object> {

    private Activity activity;
    private ForgotPassword forgotPassword;
    private ProgressDialog dialog;
    String status;
    String message;

    public ForgotPasswordTask(Activity activity) {
        this.activity = activity;
//        this.forgotPassword = forgotPassword;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(activity);
        dialog.setTitle("Sending OTP");
        dialog.setMessage("Please wait, sending OTP to registered mobile number/ email-id..");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Object doInBackground(String... params) {

        String response ="";
        try {

            System.out.println(params[0]);

            if(params[1].equalsIgnoreCase("otp")){

            }else if(params[1].equalsIgnoreCase("verification_otp")){

            }else {
                response = DoctorPostMethods.sendGet(params[0]);
            }
            Log.d("TAG","my response is "+response);
            JSONObject jsonObject = new JSONObject(response);
            status = jsonObject.getString("status");
            message = jsonObject.getString("message");
        }catch (JSONException ex){
            ex.printStackTrace();
        }catch (Exception ex){

        }
        return message;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        dialog.dismiss();
        if(message != null){

            System.out.println("Kishore: message: " + message);

            if(message.equalsIgnoreCase("Success") && activity instanceof ForgotPasswordActivity){
                ((ForgotPasswordActivity)activity).successResult();
            }else if(activity instanceof ForgotPasswordActivity){
                ((ForgotPasswordActivity)activity).duplicateData(message);
            }
        }else{
            if(activity instanceof ForgotPasswordActivity)
                ((ForgotPasswordActivity)activity).failure();
        }
//        {"status":"OK","message":"Success"}
    }
}
