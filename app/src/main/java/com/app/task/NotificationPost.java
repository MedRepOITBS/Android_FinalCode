package com.app.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.ColorInt;
import android.util.Log;

import com.app.interfaces.GetResponse;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import medrep.medrep.DoctorDashboard;

/**
 * Created by chethan on 3/11/2017.
 */

public class NotificationPost extends AsyncTask<String, Void, String> {

    private ProgressDialog dialog;
    public GetResponse delegate;
    private Context context;
    private String operation;

    public NotificationPost(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(context,"Loading...", "Loading");
//        dialog.setMessage("Loading...");
//        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String response =null;
        operation = params[1];
        try{
            JSONObject jsonObjectTemp = new JSONObject();
            jsonObjectTemp.put("", "");
            URL api_url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection)api_url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

//            Log.i("FFFFFFFFFFFFFFFF", urlConnection.getResponseCode()+"");

            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(params[2]);
            writer.close();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            response = reader.readLine();
            Log.i("FFFFFFFFFFFFFFFF", reader.readLine()+"");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.println("reslut from server: "+ result);
        dialog.dismiss();
        if(operation.contains("getNotificationCount")){
            ((DoctorDashboard)context).getNotificationCount(result);
        }
    }

}
