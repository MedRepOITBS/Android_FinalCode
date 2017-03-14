package com.app.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import medrep.medrep.DoctorDashboard;

/**
 * Created by GunaSekhar on 11-06-2016.
 */
public class AppointmentsAsyncTask extends AsyncTask<String, Void, String> {

    private ProgressDialog dialog;
    private Context context;
    public AppointmentsAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(context, "please wait", "Loading");
        dialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        StringBuffer response = null;
        try {
            URL api_url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection)api_url.openConnection();
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));
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
        dialog.dismiss();
        //DoctorDashboard dashboard = new DoctorDashboard();
        ((DoctorDashboard)context).getResponse(result);
        //dashboard.getResult(result);
    }


}