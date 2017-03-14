package com.app.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.app.fragments.CallMedRepFragment;
import com.app.pojo.Appointment;
import com.app.util.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import medrep.medrep.DoctorGetAppointmentDetails;

/**
 * Created by masood on 9/12/15.
 */
public class DoctorCreateAppointmentTask extends AsyncTask<String, Void, Boolean> {

    private ProgressDialog dialog;
    private Context context;
    private Appointment appointment;
    String status;

    private Fragment fragment;

    private String json;

   /* public DoctorCreateAppointmentTask(Context context, Fragment fragment, Appointment appointment){
        this.context = context;
        this.appointment = appointment;
        this.fragment = fragment;
    }*/

    public DoctorCreateAppointmentTask(Context context, Fragment fragment, String json){
        this.context = context;
        this.json = json;
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(context, "please wait", "Loading..");
        dialog.show();
    }

    @Override
    protected Boolean doInBackground(String... params) {

        HttpResponse response = Utils.makeRequest(params[0], json);

        System.out.println("params[0]: " + params[0]);

        String json_string = null;
        try {
            json_string = EntityUtils.toString(response.getEntity());
            JSONObject temp1 = new JSONObject(json_string);
 System.out.println("temp1: " + temp1);
            if(temp1.getString("status").equalsIgnoreCase("ok") || temp1.getString("message").equalsIgnoreCase("success")){
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        System.out.println("response.toString(): " + response.toString());
        /*String response = DoctorPostMethods.POST(params[0], appointment);
        try {
            JSONObject jsonObject = new JSONObject(response);
            status = jsonObject.getString("status");
            message = jsonObject.getString("message");
        }catch (JSONException ex){
            ex.printStackTrace();
        }*/
        return false;

    }


    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        dialog.dismiss();
        if(result){
            if(fragment instanceof CallMedRepFragment) {
                ((CallMedRepFragment) fragment).success("Sucess");
            }
            else if(fragment instanceof DoctorGetAppointmentDetails){
                ((DoctorGetAppointmentDetails)fragment).success("Sucess");
            }

        }else{
            if(fragment instanceof CallMedRepFragment) {
                ((CallMedRepFragment) fragment).failure();
            }
            else if(fragment instanceof DoctorGetAppointmentDetails){
                ((DoctorGetAppointmentDetails)fragment).failure();
            }
        }
    }
}
