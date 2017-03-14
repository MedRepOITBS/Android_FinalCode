package com.app.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.app.pojo.RefreshToken;
import com.app.pojo.SignIn;
import com.app.util.HttpUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by GunaSekhar on 02-07-2016.
 */
public class RefreshAccessToken extends AsyncTask {

    private ProgressDialog dialog;
    private Context context;

    public RefreshAccessToken(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        StringBuffer response = null;
        String ip = HttpUrl.COMMONURL;
        String refreshToken = RefreshToken.getRefreshToken();
        System.out.println(refreshToken);
        String url = "http://122.175.50.252:8080/MedRepApplication/oauth/token?grant_type=refresh_token&client_id=restapp&client_secret=restapp&refresh_token=" + refreshToken;
        try {
            URL api_url = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection)api_url.openConnection();
            int responseCode = urlConnection.getResponseCode();
            System.out.println(responseCode);
            if(responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String inputLine;
                response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.print("$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                System.out.print(response.toString());
                return response.toString();
                //SignIn.SET_ACCESS_TOKEN(response.toString());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(String result) {
        System.out.println(result);
        dialog.dismiss();
        try {
            JSONObject object = new JSONObject(result);
            String accessToken = object.getString("value");
            SignIn.SET_ACCESS_TOKEN(accessToken);
            long expiration = object.getLong("expiration");
            SignIn.SET_EXPIRES_IN(expiration);
            SignIn.SET_TOKEN_EXPIRY_IN_MILLIS(expiration);
            JSONObject refreshToken = new JSONObject("refreshToken");
            String refreshValue = refreshToken.getString("value");
            long expires = refreshToken.getLong("expiration");
            new RefreshToken(refreshValue, expires);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
