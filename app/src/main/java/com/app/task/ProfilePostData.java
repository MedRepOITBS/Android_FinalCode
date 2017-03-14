package com.app.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.app.interfaces.GetResponse;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by GunaSekhar on 26-06-2016.
 */
public class ProfilePostData extends AsyncTask<String, Void, String> {

    private static ProgressDialog dialog;
    private static JSONArray map;
    public static GetResponse delegate;

    public ProfilePostData(JSONArray map) {
        this.map = map;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show((Context) delegate, "please wait", "Loading");
        dialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        JSONObject post_dict = new JSONObject();
        String JsonResponse = null;
        BufferedReader reader = null;
        System.out.println(map);
//        try {
//            //post_dict.put("connIdList" , map);
//            JSONArray array = new JSONArray(map);
//            post_dict.put("connIdList", array);
//            System.out.println(post_dict);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        try {
            URL url = new URL(params[0]);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            //urlConnection.setRequestProperty("Accept", "application/json");
//            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
//            wr.write(post_dict);
//            wr.flush();
//            wr.close();
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(String.valueOf((map)));
            // json data
            writer.close();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            while ((inputLine = reader.readLine()) != null)
                buffer.append(inputLine + "\n");
            if (buffer.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }
            JsonResponse = buffer.toString();
            //response data
            Log.i("PostActivty", JsonResponse);
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == 200) {
                return JsonResponse;
            }
            System.out.println("\nSending 'POST' request to URL : " + url);
            //System.out.println("Post parameters : " + urlParams);
            System.out.println("Response Code : " + responseCode);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        System.out.print(result);
        dialog.dismiss();
        delegate.response(result);
        delegate = null;
    }
}
