package com.app.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.app.interfaces.GetResponse;
import com.app.pojo.SignIn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import medrep.medrep.SurveyDetailsActivity;

/**
 * Created by Gunasekhar on 01/03/17.
 */

public class SurveyReportsGetAsyncTask extends AsyncTask<String, Void, String> {
    private GetResponse context;
    private ProgressDialog progress;

    public SurveyReportsGetAsyncTask(GetResponse context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = new ProgressDialog((Activity)context);
        progress.setCancelable(false);
        progress.setMessage("Please Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setProgress(0);
        progress.setMax(100);
        progress.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String accessToken = SignIn.GET_ACCESS_TOKEN();
        System.out.println(accessToken);
        StringBuffer response = null;
        try {
            URL url = new URL(params[0] + "?token=" + accessToken);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
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
        progress.dismiss();
        context.response(result);
    }
 }
