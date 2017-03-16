package com.app.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.app.fragments.DoctorSurveyListLV;
import com.app.interfaces.GetResponse;
import com.app.pojo.SignIn;
import com.app.util.HttpUrl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import medrep.medrep.DoctorDashboard;

/**
 * Created by GunaSekhar on 10-06-2016.
 */
public class NotificationGetTask extends AsyncTask<String, Void, String> {

    private static ProgressDialog dialog;
    public static GetResponse delegate;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(!(delegate instanceof DoctorSurveyListLV)) {
            dialog = new ProgressDialog((Context)delegate);
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(true);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        StringBuffer response = null;
        try {
            System.out.print(params[0]);
            URL api_url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection)api_url.openConnection();
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println("reslut from server: "+ result);
        if(!(delegate instanceof DoctorSurveyListLV)) {
            dialog.dismiss();
        }
        delegate.response(result);
    }


}
