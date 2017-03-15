package com.app.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.Html;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.db.Company;
import com.app.db.Notification;
import com.app.pojo.SignIn;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import medrep.medrep.ForgotPasswordActivity;
import medrep.medrep.LoginActivity;
import medrep.medrep.SplashScreen;
import pharma.model.PharmaRepProfile;

/**
 * Created by Kishore on 9/26/2015.
 */
public class Utils {

    public static final long DEFAULT_START_DATE = 20150101;


    public static Bitmap decodeBase64(String input)
    {
        if(input != null && input.trim().length() > 0){
            byte[] decodedByte = Base64.decode(input, 0);
            BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
            options.inPurgeable = true;
            options.inSampleSize=2;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length, options);
        }
        return null;
    }

    public static Bitmap decodeBase64(String input, boolean resize)
    {
        if(input != null && input.trim().length() > 0){
            if(resize){
                return decodeBase64(input);
            }else{
                byte[] decodedByte = Base64.decode(input, 0);
                return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
            }
        }
        return null;
    }

    public static boolean IS_TOKEN_EXPIRED(){

        System.out.println("SignIn.GET_TOKEN_EXPIRY_IN_MILLIS(): " + SignIn.GET_TOKEN_EXPIRY_IN_MILLIS());
        System.out.println("SignIn.GET_EXPIRES_IN(): " + SignIn.GET_EXPIRES_IN());
        System.out.println("Calendar.getInstance().getTimeInMillis(): " + Calendar.getInstance().getTimeInMillis());

        System.out.println("(SignIn.GET_EXPIRES_IN() - Calendar.getInstance().getTimeInMillis()): " +
                (SignIn.GET_EXPIRES_IN() - Calendar.getInstance().getTimeInMillis()));

        System.out.println("SignIn.getRefreshToken().getRefreshToken(): " + SignIn.getRefreshToken().getRefreshToken());

        long expiryDateTimeInMillis = SignIn.GET_EXPIRES_IN() * 1000;
        long expiresAt = Calendar.getInstance().getTimeInMillis() + expiryDateTimeInMillis;


        return (SignIn.GET_EXPIRES_IN() - Calendar.getInstance().getTimeInMillis()) <= 1000/*one second*/;
    }

    public static String GET_ACCESS_TOKEN(final Activity activity){
        if(IS_TOKEN_EXPIRED()){
            System.out.println("Token Expired");
            try{
                return REFRESH_TOKEN();
            }catch(NullPointerException e){
                e.printStackTrace();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "Session expired. Please login and try again.", Toast.LENGTH_SHORT).show();

                        SharedPreferences signPref = activity.getSharedPreferences(SplashScreen.SIGNIN_PREF, activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = signPref.edit();
                        editor.putBoolean(SplashScreen.IS_LOGGED_IN_KEY, false);
                        editor.putString("RefreshToken", null);
                        editor.putLong("RefreshExpiry", -1);
                        editor.putString("AccessToken", null);
                        editor.putLong("AccessExpiresIn", -1);
                        editor.putLong("AccessExpiry", -1);
                        editor.commit();


                        Intent intent = new Intent(activity, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(intent);
                    }
                });

                activity.finish();

                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Retrieving token");

        return SignIn.GET_ACCESS_TOKEN();
    }

    public static String REFRESH_TOKEN() throws JSONException {
        String url = HttpUrl.BASEURL + HttpUrl.REFRESHTOKEN + (SignIn.getRefreshToken().getRefreshToken());

//        ArrayList<AccessToken> accessTokens = new ArrayList<>();

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = parser.getJSON_Response(url, true);
        AccessToken accessToken = (AccessToken) parser.jsonParser(jsonObject, AccessToken.class);

//        AccessToken accessToken = accessTokens.get(0);

        com.app.pojo.RefreshToken refreshToken =
                new com.app.pojo.RefreshToken(
                        accessToken.getRefreshToken().getValue(),
                        accessToken.getRefreshToken().getExpiration());

        SignIn.setRefreshToken(refreshToken);

        if(!accessToken.isExpired()){
            String token = accessToken.getValue();

            SignIn.SET_ACCESS_TOKEN(token);
            SignIn.SET_TOKEN_EXPIRY_IN_MILLIS(accessToken.getExpiration());
            SignIn.SET_EXPIRES_IN(accessToken.getExpiresIn());
            return token;
        }else{
            return SignIn.GET_ACCESS_TOKEN();
        }
    }

    public static final int[] GET_THERAPEUTIC_AREA_IDS(ArrayList<Notification> notifications){
        int[] therapeuticIDS = new int[notifications.size()];

        for(int i = 0; i < notifications.size(); i++){
            therapeuticIDS[i] = notifications.get(i).getTherapeuticId();
        }

        return therapeuticIDS;
    }

    public static final int[] GET_NOTIFICATION_IDS(ArrayList<Notification> notifications){
        int[] notificationIDS = new int[notifications.size()];

        for(int i = 0; i < notifications.size(); i++){
            notificationIDS[i] = notifications.get(i).getNotificationId();
        }

        return notificationIDS;
    }

    public static String GET_IN_CLAUSE(int[] ids){

        String inClause = "(";

        for(int i = 0; i < ids.length; i++){
            if(i != ids.length - 1){
                inClause = inClause + ids[i] + ", ";
            }else{
                inClause = inClause + ids[i];
            }

        }

        inClause = inClause + ")";

        /*System.out.println("inClause: " + inClause);

        //replace the brackets with parentheses
        inClause = inClause.replace("[","(");
        inClause = inClause.replace("]",")");*/

        return inClause;
    }

    public static File getDataFolder(Context context) {
        File dataDir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            dataDir = new File(Environment.getExternalStorageDirectory(), "MedRepAppData");
            if(!dataDir.isDirectory()) {
                dataDir.mkdirs();
            }
        }

        if(!dataDir.isDirectory()) {
            dataDir = context.getFilesDir();
        }

        return dataDir;
    }


    public static final String bitmapToBase64(Bitmap bmp){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return encoded;
    }

    public static final String formatMonth(int month) throws ParseException {

        SimpleDateFormat monthParse = new SimpleDateFormat("MM");
        SimpleDateFormat monthDisplay = new SimpleDateFormat("MMMM");
        return monthDisplay.format(monthParse.parse(String.valueOf(month)));
    }

    public static String imageAsBase64(String picturePath) {
        return bitmapToBase64(BitmapFactory.decodeFile(picturePath));
    }

    public static HttpResponse makeRequest(String uri, String json) {
        try {
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(new StringEntity(json));
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            return new DefaultHttpClient().execute(httpPost);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static boolean isPharmaUpdate=false;

    public static void DISPLAY_GENERAL_DIALOG(Activity activity, String title, String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title)
                .setMessage(Html.fromHtml(msg))
                .setPositiveButton("OK", null);
        builder.create().show();
    }

    public static boolean isNetworkAvailable(Activity activity) {

        boolean isConnected = isNetworkAvailableWithOutDialog(activity);

        if(!isConnected){
            DISPLAY_GENERAL_DIALOG(activity,
                    "No Internet",
                    "You are not connected to internet. " +
                            "Please connect to internet and try again.");
        }

        return isConnected;
    }

    public static boolean isNetworkAvailableWithOutDialog(Activity activity) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE));

        boolean isConnected = connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();

        System.out.println("isConnected: " + isConnected);

        return isConnected;
    }

    public static void SET_PHARMA_COMPANY_LOGO(final Activity activity, final ImageView imageView) {

        final String imageName = ".PharmaCompanyLogo";

        final File f = new File(activity.getExternalCacheDir(), imageName);

        System.out.println("Kishore: " + f.getAbsolutePath());

        if(f.exists()){
            setBitmap(activity, imageView, f.getAbsolutePath());
            return;
        }

        AsyncTask<Void, Void, Void> async = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                String url = HttpUrl.PHARMA_GET_MY_COMPANY + GET_ACCESS_TOKEN(activity);

                JSONParser parser = new JSONParser();
                JSONObject jsonObject = parser.getJSON_Response(url, true);
                try {
                    if(jsonObject != null) {
                        Company company = (Company) parser.jsonParser(jsonObject, Company.class);
                        saveAndSetImage(decodeBase64(company.getDisplayPicture().getData()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            private void saveAndSetImage(final Bitmap bmp){

                if(bmp == null){
                    return;
                }

                try {
                    OutputStream outStream = new FileOutputStream(f);

                    bmp.compress(Bitmap.CompressFormat.PNG, 90, outStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                setBitmap(activity, imageView, f.getAbsolutePath());
            }
        };


        if(isNetworkAvailableWithOutDialog(activity)){
            async.execute();
        }

    }

    private static void setBitmap(final Activity activity, final ImageView imageView, final String absolutePath) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    Bitmap bmp = BitmapFactory.decodeFile(absolutePath);

                    imageView.setImageBitmap(bmp);
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();

                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;

                    Bitmap bmp = BitmapFactory.decodeFile(absolutePath, options);
                    imageView.setImageBitmap(bmp);
                }
            }
        });
    }

    private class ResendOTP{
        private String status;
        private String message;


        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static void RESEND_OTP(final Activity activity, String email) {
        String url = HttpUrl.BASEURL + HttpUrl.RESEND_OTP + email + "/";

        System.out.println("Url: " + url);

        new AsyncTask<String, Void, String>(){

            ProgressDialog pd;

            @Override
            protected String doInBackground(String... params) {
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = jsonParser.getJSON_Response(params[0], true);
                ResendOTP resendOTP = null;
                try {
                    resendOTP = (ResendOTP) jsonParser.jsonParser(jsonObject, ResendOTP.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if(resendOTP != null && resendOTP.getStatus().equalsIgnoreCase("Fail")){

                    final ResendOTP finalResendOTP = resendOTP;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.DISPLAY_GENERAL_DIALOG(activity, finalResendOTP.getStatus(), finalResendOTP.getMessage());
                        }
                    });

                    return "Failure";
                }else{
                    return "Success";
                }

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(activity);
                pd.setTitle("Requesting New OTP");
                pd.setMessage("Please wait...");
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                if(pd != null && pd.isShowing()){
                    pd.dismiss();
                }

                if(result.equalsIgnoreCase("success")){
                    if(activity instanceof ForgotPasswordActivity) {
                        ((ForgotPasswordActivity) activity).successResult();
                    }else if(activity instanceof LoginActivity){
                        ((LoginActivity) activity).resendOTP_Success();
                    }
                    Toast.makeText(activity, "OTP has been sent to registered mobile/email.", Toast.LENGTH_SHORT).show();
                }/*else{
                    Utils.DISPLAY_GENERAL_DIALOG(activity, "Failed", "Sending otp has been failed. Please try again later.");
                }*/
            }
        }.execute(url);
    }


    public static void UPDATE_PHARMA_PROFILE(final Activity activity, final PharmaRepProfile repProfile) {
        final ProgressDialog progress = new ProgressDialog(activity);
        progress.setCancelable(false);
        progress.setMessage("Please Wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setProgress(0);
        progress.setMax(100);
        progress.show();

        Thread thread = new Thread() {
            public void run() {
                try {
                    boolean boolValue = true;
                    int pharmaRollId = 3;


                    ServiceHandler sh = new ServiceHandler();

                    JSONObject obj = new JSONObject();
                    obj.put("username", repProfile.getEmailId());
                    obj.put("password", repProfile.getPassword());
                    obj.put("loginTime", "null");

                    obj.put("userSecurityId", "null");

                    obj.put("companyName", repProfile.getCompanyName());
                    obj.put("accountNonExpired", boolValue);
                    obj.put("accountNonLocked", boolValue);
                    obj.put("credentialsNonExpired", boolValue);
                    obj.put("enabled", boolValue);
                    obj.put("roleId", pharmaRollId);
                    obj.put("roleName", "Pharmacy");
                    obj.put("firstName", repProfile.getFirstName());
                    obj.put("middleName", "");
                    obj.put("lastName", repProfile.getLastName());
                    obj.put("alias", "null");
                    obj.put("title", "Mr.");
                    obj.put("phoneNo", repProfile.getPhoneNo());
                    obj.put("mobileNo", repProfile.getMobileNo());
                    obj.put("emailId", repProfile.getEmailId());
                    obj.put("alternateEmailId", "");

                    obj.put("repId", "2015");
                    obj.put("companyId", repProfile.getCompanyId());
                    obj.put("coveredArea", repProfile.getCoveredArea());
                    obj.put("managerId", repProfile.getManagerId());
                    obj.put("managerEmail", repProfile.getManagerEmail());
                    obj.put("coveredZone", repProfile.getCoveredZone());

                    JSONArray ja = new JSONArray();

                    for(Company.Location location: repProfile.getLocations()){
                        JSONObject joj = new JSONObject();
                        joj.put("locationId", location.getLocationId() + "");
                        joj.put("address1", location.getAddress1());
                        joj.put("address2", location.getAddress2());
                        joj.put("city", location.getCity());
                        joj.put("state", location.getState());
                        joj.put("country", location.getCountry());
                        joj.put("zipcode", location.getZipcode());
                        joj.put("type", location.getType());

                        ja.put(joj);
                    }


                    obj.put("locations", ja);
                    System.out.println("DATA SET:" + obj.toString());

                    String status;

                    if (Utils.isPharmaUpdate) {
                        String url = HttpUrl.UPDATEPHARMA + Utils.GET_ACCESS_TOKEN(activity);
                        status = sh.makeServiceCall(url, 2, obj.toString());
                    } else {
                        String url = HttpUrl.url_pharmaReg;
                        status = sh.makeServiceCall(url, 2, obj.toString());
                    }

                    postResult(activity, progress, status);

                } catch (Exception e) {

                    System.out.print("Exception:" + e.getMessage());


                }


//                handler.post(createUI);
            }
        };

        thread.start();
    }

    private static void postResult(final Activity activity, final ProgressDialog progress, final String status) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progress.dismiss();
                System.out.println("Data...clicked" + status);

                try {

                    JSONObject jobj = new JSONObject(status);

                    if (status != null && status.contains("Success")) {
//                        DISPLAY_GENERAL_DIALOG(activity, "Success", "Profile updated successfully");
                        Toast.makeText(activity, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        activity.finish();
                    } else {
                        DISPLAY_GENERAL_DIALOG(activity, jobj.getString("status"), jobj.getString("message"));
                        //   Toast.makeText(getApplicationContext(),"Registration Failed!",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /*public static Bitmap decodeSampledBitmapFromResource(String inputData,
                                                  int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        byte[] decodedByte = Base64.decode(inputData, 0);
        BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length, options);
    }*/

    public static Bitmap decodeSampledBitmapFromResource(String filePath,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

}
