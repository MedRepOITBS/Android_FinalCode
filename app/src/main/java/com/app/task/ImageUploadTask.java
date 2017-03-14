package com.app.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.app.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import medrep.medrep.LoginActivity;
import medrep.medrep.WelcomeActivity;

/**
 * Created by masood on 9/5/15.
 */
public class ImageUploadTask extends AsyncTask<String, Void, Object> {

    private Activity context;
    //    private ForgotPassword forgotPassword;
    private ProgressDialog dialog;
    String status;
    String message;
    String imageBase64;

    public ImageUploadTask(Activity context,String imageBase64) {
        this.context = context;
        this.imageBase64 = imageBase64;
//        this.forgotPassword = forgotPassword;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(context, "Updating Profile Picture", "Please wait while updating your profile picture.");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Object doInBackground(String... params) {
//        String response = DoctorPostMethods.UploadImage(params[0], imageBase64);
//        String response = DoctorPostMethods.imageUpload(ba1);
        String response = DoctorPostMethods.UPLOAD_DP(params[0], imageBase64);
        Log.d("TAG", "my response is " + response);
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
            if(message.equalsIgnoreCase("Success") ){
                System.out.println("Image upload success");
                if(context instanceof WelcomeActivity){
                    Toast.makeText(context, "Profile picture updated successfully", Toast.LENGTH_SHORT).show();
                    context.finish();

                    Intent intent=new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                }else{
//                    context.finish();
                    Utils.DISPLAY_GENERAL_DIALOG(context, "Success", "Profile picture updated successfully");
                }

//                ((ForgotPasswordActivity)context).sucessResult();
            }
        }else{
            Utils.DISPLAY_GENERAL_DIALOG(context, status, message);
        }
//        {"status":"OK","message":"Success"}
    }

    //http://stackoverflow.com/questions/5416038/uploading-image-to-server-in-multipart-along-with-json-data-in-android
}
