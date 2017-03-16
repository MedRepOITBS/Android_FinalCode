package com.app.task;

import android.util.Log;

import com.app.json.JSONTag;
import com.app.pojo.AddressProfile;
import com.app.pojo.Appointment;
import com.app.pojo.DoctorProfile;
import com.app.pojo.ForgotPassword;
import com.app.util.HttpUrl;
import com.app.util.UserRoles;
import com.app.util.Utils;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by masood on 8/11/15.
 */
public class DoctorPostMethods {

    public  static String sendGet(String url) throws Exception {

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
//        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url+"response code: "+responseCode);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
        return response.toString();
    }

    // HTTP POST request
    private void sendPost() throws Exception {

        String url = "https://selfsolve.apple.com/wcResults.do";
        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
//        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }

    public static String POST(String url, Object person){
        Log.d("TAG", "MY DOCTOR PROFILE 1");
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";
            JSONObject jsonObject = null;
            // 3. build jsonObject

            //Registration screen change class name
            if(person instanceof DoctorProfile) {
                Log.d("TAG", "MY DOCTOR PROFILE ");
                jsonObject = setJsonData(person);
            }
            else if(person instanceof  ForgotPassword)
                jsonObject = setForgotPassword(person);
            else if(person instanceof Appointment)
                jsonObject = setAppointment(person);

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

//            json = "{\"username\":\"kishorenallagolla@gmail.com\",\"password\":\"asd\",\"loginTime\":\"\",\"userSecurityId\":0,\"authorities\":[],\"accountNonExpired\":false,\"accountNonLocked\":false,\"enabled\":true,\"roleId\":1,\"firstName\":\"Kishore\",\"lastName\":\"NG\",\"title\":\"Dr\",\"phoneNo\":\"\",\"mobileNo\":\"7204937372\",\"emailId\":\"kishorenallagolla@gmail.com\",\"middleName\":\"\",\"alternateEmailId\":\"\",\"locations\":[{\"locationId\":\"\",\"address1\":\"Bangalore\",\"address2\":\"Bangalore\",\"city\":\"Bangalore\",\"state\":\"Kar\",\"country\":\"\",\"type\":2}],\"registrationYear\":\"2015\",\"registrationNumber\":\"123456789\",\"stateMedCouncil\":\"XP\",\"therapeuticId\":\"1\",\"alias\":\"<null>\",\"credentialsNonExpired\":1,\"qualification\":\"Doctor\",\"roleName\":\"Doctor\",\"status\":\"null\"}";

//            json = "{\"username\":\"kishorenallagolla@gmail.com\",\"password\":\"asd\",\"loginTime\":\"\",\"userSecurityId\":0,\"authorities\":[],\"accountNonExpired\":1,\"accountNonLocked\":1,\"enabled\":1,\"roleId\":1,\"firstName\":\"Kishore\",\"lastName\":\"NG\",\"title\":\"Dr\",\"phoneNo\":\"\",\"mobileNo\":\"7204937372\",\"emailId\":\"kishorenallagolla@gmail.com\",\"middleName\":\"\",\"alternateEmailId\":\"\",\"locations\":[{\"locationId\":\"\",\"address1\":\"Bangalore\",\"address2\":\"Bangalore\",\"city\":\"Bangalore\",\"state\":\"Kar\",\"country\":\"\",\"type\":2}],\"registrationYear\":\"2015\",\"registrationNumber\":\"123456789\",\"stateMedCouncil\":\"XP\",\"therapeuticId\":\"1\",\"alias\":\"<null>\",\"credentialsNonExpired\":1,\"qualification\":\"Doctor\",\"roleName\":\"Doctor\",\"status\":\"null\"}";

            System.out.println("json: " + json);

            Log.d("TAG", "MY DOCTOR PROFILE 3");

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        Log.d("TAG", "my response is " + result);
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    private static JSONObject setForgotPassword(Object object) {
        JSONObject jsonObject = null;
        if (object != null) {
            if (object instanceof ForgotPassword) {
                ForgotPassword forgotPassword = (ForgotPassword)object;
                try {
                    jsonObject = new JSONObject();
                    jsonObject.put(JSONTag.USERNAME, forgotPassword.getUsername());
                    jsonObject.put(JSONTag.NEWPASSWORD, forgotPassword.getNewPassword());
                    jsonObject.put(JSONTag.CNFIRM_PASSWORD, forgotPassword.getPassword());
                    jsonObject.put(JSONTag.OTP, forgotPassword.getOtp());
                    jsonObject.put(JSONTag.VERIFICATION_TYPE, forgotPassword.getVerificationType());
                }catch (JSONException ex){

                }
            }
        }
        return  jsonObject;
    }

    public static JSONObject setAppointment(Object object){
        JSONObject jsonObject = null;
        if(object != null) {
            if (object instanceof Appointment) {
                Appointment appointment = (Appointment)object;
                try{
                    jsonObject = new JSONObject();
                    jsonObject.put(JSONTag.APP_ID, appointment.getAppointmentId());
                    jsonObject.put(JSONTag.TITLE, appointment.getTitle());
                    jsonObject.put(JSONTag.APP_DESC, appointment.getAppointmentDesc());
                    jsonObject.put(JSONTag.DOCTOR_ID, appointment.getDoctorId());
                    jsonObject.put(JSONTag.NOT_ID, appointment.getNotificationId());
                    jsonObject.put(JSONTag.STARTDATE, appointment.getStartDate());
                    jsonObject.put(JSONTag.DURATION, String.valueOf(appointment.getDuration()));
                    jsonObject.put(JSONTag.FEEDBACK, appointment.getFeedback());
                    jsonObject.put(JSONTag.STATUS,appointment.getStatus());
                    jsonObject.put(JSONTag.CREATED_ON, appointment.getCreatedOn());
                    jsonObject.put(JSONTag.LOCATION, appointment.getLocation());
                }catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        }
        return jsonObject;
    }

    public static JSONObject setJsonData(Object object){

        JSONObject jsonObject = null;
        if(object != null){
            if(object instanceof DoctorProfile){
                DoctorProfile register = (DoctorProfile)object;
                try{
                    jsonObject = new JSONObject();
                    jsonObject.put(JSONTag.USERNAME, register.getEmail());
                    Log.d("TAG", "-----------" + register.getFirstName());
                    jsonObject.put(JSONTag.PASSWORD, register.getPassword());
                    Log.d("TAG", "-------" + register.getPassword());
                    jsonObject.put(JSONTag.LOGINTIME, "");
                    jsonObject.put(JSONTag.USERSECURITYID, 0);

                    JSONArray jArray = new JSONArray();
                    jsonObject.put(JSONTag.AUTHORITIES, jArray);

                    jsonObject.put(JSONTag.ACCOUNTNONEXPIRED, false);
                    jsonObject.put(JSONTag.ACCOUNTNONLOCKED, false);
                    jsonObject.put(JSONTag.ENABLED, true);
                    jsonObject.put(JSONTag.ROLEID, UserRoles.DOCTOR);
                    jsonObject.put(JSONTag.roleName, register.getSelectedCat());
                    Log.d("TAG", "---" + register.getSelectedCat());
                    jsonObject.put(JSONTag.FIRSTNAME, register.getFirstName());
                    Log.d("TAG", "---" + register.getFirstName());
                    jsonObject.put(JSONTag.LASTNAME, register.getLastName());
                    Log.d("TAG", "---" + register.getLastName());
                    jsonObject.put(JSONTag.ALIAS, null);
                    jsonObject.put(JSONTag.TITLE, "Dr");
                    jsonObject.put(JSONTag.PHONENO, register.getAltMobileNumber());
                    Log.d("TAG", "---" + register.getMobileNumber());
                    jsonObject.put(JSONTag.MOBILENO, register.getMobileNumber());
                    Log.d("TAG", "---" + register.getAltMobileNumber());
                    jsonObject.put(JSONTag.EMAILID, register.getEmail());
                    jsonObject.put(JSONTag.MIDDLENAME,"");
                    Log.d("TAG", "---" + register.getEmail());
                    jsonObject.put(JSONTag.ALTERNATEEMAILID, register.getAltEmail());

                    ArrayList<AddressProfile> addressArrayList = register.getAddressArrayList();
                    JSONArray jlocations = new JSONArray();

                    System.out.println("Kishore addressArrayList: " + addressArrayList.size());

//                JSONObject jobectLocations = null;
                    for (AddressProfile address: addressArrayList){
                        JSONObject jobectLocations = new JSONObject();
                        jobectLocations.put(JSONTag.LOCATIONID, "");
                        jobectLocations.put(JSONTag.ADDRESS1, address.getAddress1());

                        jobectLocations.put(JSONTag.ADDRESS2, address.getAddress2());

                        jobectLocations.put(JSONTag.CITY, address.getCity());
                        Log.d("TAG", "---" + address.getCity());
                        jobectLocations.put(JSONTag.STATE, address.getState());
                        Log.d("TAG", "---" + address.getState());
                        jobectLocations.put(JSONTag.COUNTRY, address.getCountry());
                        jobectLocations.put(JSONTag.ZIPCODE, address.getZipCode());
                        Log.d("TAG ZIP", "---" + address.getZipCode());

                      //  System.out.println("Kishore address.getZipCode(): " + address.getZipCode());

                        jobectLocations.put(JSONTag.TYPE, address.getType());
                        jlocations.put(jobectLocations);
                        jsonObject.put(JSONTag.LOCATIONS, jlocations);
                    }

//                jlocations.put(jobectLocations);



                    jsonObject.put(JSONTag.REGISTRATIONYEAR, registrationYear());
                    jsonObject.put(JSONTag.REGISTRATIONNUMBER, register.getDoctorId());
                    jsonObject.put(JSONTag.STATEMEDCOUNCIL, "XP");
                    jsonObject.put(JSONTag.THERAPEUTICID, register.getTherapeuticID());
                    jsonObject.put(JSONTag.QUALIFICATION, register.getSelectedCat());
                    System.out.println("Kishore: " + jsonObject);
                }catch(JSONException ex){
                    ex.printStackTrace();
                }
            }
        }
        return jsonObject;
    }

    private static  String registrationYear(){
        Format formatter = new SimpleDateFormat("yyyy");
        return formatter.format(new Date());
    }


    public static String UPLOAD_DP(String emailID, String imageBase64){
        Map<String, String> comment = new HashMap<String, String>();
        comment.put("dpId", null);
        comment.put("data", imageBase64);
        comment.put("mimeType", "JPEG");
        comment.put("loginId", emailID);
        comment.put("securityId", null);
        String json = new GsonBuilder().create().toJson(comment, Map.class);

        String url = HttpUrl.BASEURL + HttpUrl.UPLOAD_DP;

        HttpResponse response = Utils.makeRequest(url, json);

        String jsonString = null;
        try {
            jsonString = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    public static String UploadImage(/*Context context, */String emailID, String ba1){
        /*Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
        byte [] byte_arr = stream.toByteArray();
        String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);*/
        ArrayList<NameValuePair> nameValuePairs = new  ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("dpId", null));
        nameValuePairs.add(new BasicNameValuePair("data",ba1));
        nameValuePairs.add(new BasicNameValuePair("mimeType", "JPEG"));
        nameValuePairs.add(new BasicNameValuePair("loginId",emailID));
        nameValuePairs.add(new BasicNameValuePair("securityId", null));


        try{



            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost( HttpUrl.BASEURL + HttpUrl.UPLOAD_DP);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            String the_string_response = convertResponseToString(response);
            return the_string_response;
        }catch(Exception e){
            e.printStackTrace();
//            Toast.makeText(UploadImage.this, "ERROR " + e.getMessage(), Toast.LENGTH_LONG).show();
//            System.out.println("Error in http connection "+e.toString());
        }
        return null;
    }

    private static String convertResponseToString(HttpResponse response) throws IllegalStateException, IOException{

        String res = "";
        StringBuffer buffer = new StringBuffer();
        InputStream inputStream = response.getEntity().getContent();
        int contentLength = (int) response.getEntity().getContentLength(); //getting content lengthâ€¦..
//        Toast.makeText(UploadImage.this, "contentLength : " + contentLength, Toast.LENGTH_LONG).show();
        if (contentLength < 0){
        }
        else{
            byte[] data = new byte[512];
            int len = 0;
            try
            {
                while (-1 != (len = inputStream.read(data)) )
                {
                    buffer.append(new String(data, 0, len)); //converting to string and appending  to stringbufferâ€¦..
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                inputStream.close(); // closing the streamâ€¦..
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            res = buffer.toString();     // converting stringbuffer to stringâ€¦..

            return res;
            //System.out.println("Response => " +  EntityUtils.toString(response.getEntity()));
        }
        return res;
    }

    public static String imageUpload(String ba1){
        InputStream is = null;
        /*ArrayList < NameValuePair > nameValuePairs = new
                ArrayList < NameValuePair > ();
        nameValuePairs.add(new BasicNameValuePair("image", ba1));
        nameValuePairs.add(new BasicNameValuePair("dpId", ""));

        nameValuePairs.add(new BasicNameValuePair("mimeType", "image/png"));
        nameValuePairs.add(new BasicNameValuePair("loginId","ttt@gmail.com"));
        nameValuePairs.add(new BasicNameValuePair("securityId", null));*/


//        json = jsonObject.toString();

        Log.d("TAG", "MY DOCTOR PROFILE 3");

        // 5. set json to StringEntity

        try {
            JSONObject json = new JSONObject();
            json.put("image", ba1);
            json.put("dpId", null);
            json.put("mimeType","png");
            json.put("loginId","ttt@gmail.com");
            json.put("securityId", null);
            StringEntity se = new StringEntity(json.toString());
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new
                    HttpPost(HttpUrl.BASEURL + "/preapi/registration/uploadDP");
            httppost.setEntity(se);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL

            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

            return getStringFromInputStream(is);
            //Toast.makeText(SignUpActivity.this, "Joining Failed", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
            e.printStackTrace();
        }
        return  null;
    }
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }

}
