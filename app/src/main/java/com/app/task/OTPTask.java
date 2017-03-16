package com.app.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.app.pojo.DoctorProfile;
import com.app.util.JSONParser;

import org.json.JSONObject;

import medrep.medrep.DoctorOTP_Activity;
import medrep.medrep.DoctorSetPasswordActivity;
import medrep.medrep.OTPActivity;
import medrep.medrep.RegisterActivity;
import medrep.medrep.WelcomeActivity;

/**
 * Created by masood on 9/12/15.
 */
public class OTPTask extends AsyncTask<String, Void, OTP>{

//    private String verificationId;
//    private String otp;
    private Context context;
    private ProgressDialog dialog;
    private DoctorProfile doctorProfile;
//    private String status;

    public OTPTask(Context context, DoctorProfile doctorProfile){
        this.context = context;
        this.doctorProfile = doctorProfile;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(context, "please wait", "Loading..");
        dialog.show();
    }

    @Override
    protected void onPostExecute(OTP otp) {
        super.onPostExecute(otp);
        dialog.dismiss();;
        if(context instanceof RegisterActivity) {
            String otpStr = otp.getOtp();
            String mobileNum = otp.getVerificationId();

            if(otpStr == null || otpStr.trim().equals("")){
                //Display an error message here
            }else if(mobileNum == null || mobileNum.trim().equals("")){
                //Display error message saying invalid mobile number
            }else{
                //Display message saying OTP has been sent to the mobile number.
                Intent intent = new Intent(context, OTPActivity.class);
                System.out.println("doctorProfile.getMobileNumber(): " + mobileNum);
                intent.putExtra(OTPActivity.MOBILE_NUMBER_KEY, mobileNum);
                intent.putExtra(WelcomeActivity.USERNAME_KEY, doctorProfile.getFirstName());
                intent.putExtra(WelcomeActivity.EMAIL_KEY, doctorProfile.getEmail());
            /*intent.putExtra("username",firstname);*/
                context.startActivity(intent);
//                ((OTPActivity) context).success(otp.getOtp(), otp.getVerificationId());
            }

//            if (!status.equalsIgnoreCase("Fail"))

        }if(context instanceof DoctorSetPasswordActivity) {
            String otpStr = otp.getOtp();
            String mobileNum = otp.getVerificationId();

            if(otpStr == null || otpStr.trim().equals("")){
                //Display an error message here
            }else if(mobileNum == null || mobileNum.trim().equals("")){
                //Display error message saying invalid mobile number
            }else{
                //Display message saying OTP has been sent to the mobile number.
                Intent intent = new Intent(context, DoctorOTP_Activity.class);
                System.out.println("doctorProfile.getMobileNumber(): " + mobileNum);
                intent.putExtra(DoctorOTP_Activity.MOBILE_NUMBER_KEY, mobileNum);
                intent.putExtra(WelcomeActivity.USERNAME_KEY, doctorProfile.getFirstName());
                intent.putExtra(WelcomeActivity.EMAIL_KEY, doctorProfile.getEmail());
            /*intent.putExtra("username",firstname);*/
                context.startActivity(intent);
//                ((OTPActivity) context).success(otp.getOtp(), otp.getVerificationId());
            }

//            if (!status.equalsIgnoreCase("Fail"))

        }
            else
                Toast.makeText(context, "INVALID user id ", Toast.LENGTH_LONG).show();


    }

    @Override
    protected OTP doInBackground(String... params) {
        try {

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = jsonParser.getJSON_Response(params[0], true);
            OTP otp = (OTP) jsonParser.jsonParser(jsonObject, OTP.class);

            /*String result = DoctorPostMethods.sendGet(params[0]);
            JSONObject jsonObject = new JSONObject(result);
            if(jsonObject.has("status"))
                status = jsonObject.getString("status");
            if(jsonObject.has("verificationId"))
                verificationId = jsonObject.getString("verificationId");
            if(jsonObject.has("otp"))
                otp = jsonObject.getString("otp");*/

//            return  result;
            return otp;
        }catch (Exception ex){
//            return otp;

        }
        return null;
    }
}
