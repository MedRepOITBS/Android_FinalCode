package com.app.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.text.Editable;
import android.util.Log;
import android.widget.Toast;

import com.app.pojo.DoctorProfile;
import com.app.util.JSONParser;
import com.app.util.TempProfile;
import com.app.util.Utils;

import org.json.JSONObject;

import medrep.medrep.DoctorOTP_Activity;
import medrep.medrep.LoginActivity;
import medrep.medrep.OTPActivity;
import medrep.medrep.WelcomeActivity;

/**
 * Created by masood on 9/17/15.
 */
public class VerificationOtpTask extends AsyncTask<String, Void, String> {

//    private String verificationId;
//    private String otp;
    private Context context;
    private ProgressDialog dialog;
    private String username;
    private String email;
    private boolean OTP_Preference;
    private TempProfile tempProfile;

    public VerificationOtpTask(Context context, String username, String email){
        this.context = context;
        this.username = username;
        this.email = email;
    }

    public VerificationOtpTask(LoginActivity context, TempProfile tempProfile) {
        this.context = context;
        this.tempProfile = tempProfile;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(context, "please wait", "Loading..");
        dialog.show();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        dialog.dismiss();;
       /* if(context instanceof OTPActivity){
            ((OTPActivity)context).success();;
        }*/

        if(context instanceof DoctorOTP_Activity){
            if(result.equalsIgnoreCase("true")){
                //Success
//            DoctorProfile doctorProfile = DoctorProfile.getInstance();
                Intent intent = new Intent(context, WelcomeActivity.class);
                intent.putExtra(WelcomeActivity.EMAIL_KEY, email);
                intent.putExtra(WelcomeActivity.USERNAME_KEY, username);
                context.startActivity(intent);

                ((Activity)context).finish();

                if(context instanceof  DoctorOTP_Activity){
                    ((DoctorOTP_Activity)context).setOTP_Preference(true);
                }


            }else{
                Utils.DISPLAY_GENERAL_DIALOG((Activity) context, "Error!", result);
                if(context instanceof  DoctorOTP_Activity){
                    ((DoctorOTP_Activity)context).setOTP_Preference(false);
                }
            }
        }else if(context instanceof LoginActivity){
            if(result.equalsIgnoreCase("true")){
                ((LoginActivity)context).loginUser(tempProfile);
            }else{
//                Utils.DISPLAY_GENERAL_DIALOG((Activity) context, "Error!", result);
                Toast.makeText(context, "Error! OTP could not be verified. Try again later.", Toast.LENGTH_SHORT).show();
                if(context instanceof  DoctorOTP_Activity){
                    ((LoginActivity)context).finish();
                }
            }
        }
    }

    @Override
    protected String doInBackground(String... params) {
        try {

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = jsonParser.getJSON_Response(params[0], true);
            Verification verification = (Verification) jsonParser.jsonParser(jsonObject, Verification.class);

            if(verification.getStatus().equalsIgnoreCase("ok")){
                return "true";
            }else if(verification.getMessage() != null && !verification.getMessage().trim().equals("")){
                return verification.getMessage();
            }else {
                return "Failure";
            }

            /*String result = DoctorPostMethods.sendGet(params[0]);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.has("ok"))
                verificationId = jsonObject.getString("OK");
            if(jsonObject.has("message"))
                otp = jsonObject.getString("success");

            return  result;*/
        }catch (Exception ex){

        }
        return null;
    }


}
