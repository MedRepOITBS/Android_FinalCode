package com.app.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.app.interfaces.GetResponse;
import com.app.pojo.SignIn;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import medrep.medrep.R;

/**
 * Created by GunaSekhar on 14-06-2016.
 */
public class PostData extends AsyncTask<String, Void, String> {
    public GetResponse delegate;
    private ProgressDialog dialog;
    private HashMap base;

    public PostData(HashMap base) {
        this.base = base;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show((Context) delegate, "please wait", "Loading");
        dialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
//        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.news1);
//        String myBase64Image = encodeToBase64(largeIcon, Bitmap.CompressFormat.JPEG, 100);
        String JsonResponse = null;
        BufferedReader reader = null;
        String POST_PARAMS = params[0]+ SignIn.GET_ACCESS_TOKEN();
//        String urlParams = "group_name=FirstGroup&group_long_desc=sfdgagafggdggfgfg&group_short_desc=sampletest&group_img_data="+base+"&group_mimeType=png";
//        StringBuffer response = null;
        JSONObject post_dict = new JSONObject();
        try {
            post_dict.put("group_name" , base.get("groupName"));
            post_dict.put("group_long_desc", base.get("longDescription"));
            post_dict.put("group_short_desc", base.get("shortDescription"));
            post_dict.put("group_img_data", base.get("image"));
            post_dict.put("group_mimeType", "png");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            URL url = new URL(POST_PARAMS);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
//            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
//            wr.write(post_dict);
//            wr.flush();
//            wr.close();
            Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
            writer.write(String.valueOf(post_dict));
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
            int responseCode = urlConnection.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            //System.out.println("Post parameters : " + urlParams);
            System.out.println("Response Code : " + responseCode);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(response.toString());
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        dialog.dismiss();
        delegate.response(result);
    }
}
