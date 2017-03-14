package medrep.medrep;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.pojo.SignIn;
import com.app.task.DoctorPostMethods;
import com.app.task.SignInTask;
import com.app.task.VerificationOtpTask;
import com.app.util.Config;
import com.app.util.GlobalVariables;
import com.app.util.HttpUrl;
import com.app.util.JSONParser;
import com.app.util.TempProfile;
import com.app.util.UserRoles;
import com.app.util.Utils;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import pharma.PharmaDashBoard;
import pharma.PharmaDocterDetails;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button signInButton;
    private TextView forgotTextView;
    private TextView medrepTextView;
    private Typeface typeface;
    private EditText usernameEdit;
    private EditText passwordEdit;
    private EditText otpEditText;
    private boolean unregisterReceiver;
    private String regId;
    private GoogleCloudMessaging gcm;
    private Context context;
    private static final String REG_ID = "regId";
    private static final String APP_VERSION = "appVersion";
    public static Bitmap dPictureBitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        SharedPreferences signPref = getSharedPreferences(SplashScreen.SIGNIN_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = signPref.edit();
        editor.putBoolean(SplashScreen.IS_LOGGED_IN_KEY, false);
        editor.putString("RefreshToken", null);
        editor.putLong("RefreshExpiry", -1);
        editor.putString("AccessToken", null);
        editor.putLong("AccessExpiresIn", -1);
        editor.putLong("AccessExpiry", -1);
        editor.commit();

        setContentView(R.layout.activity_login);
        getWindow().setBackgroundDrawableResource(R.mipmap.home_bg);
        /*ScrollView sv = (ScrollView)findViewById(R.id.scroll);
        sv.setEnabled(false);*/
        setView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(unregisterReceiver){
            unregisterReceiver(incomingSMS_Receiver);
        }
    }

    private void setView(){
        typeface = GlobalVariables.getTypeface(LoginActivity.this);

        medrepTextView =(TextView) this.findViewById(R.id.medrep_textview);
        medrepTextView.setTypeface(typeface);
        medrepTextView.setText(R.string.app_name);

        signInButton = (Button) this.findViewById(R.id.signButton);
        signInButton.setTypeface(typeface);
        signInButton.setText(getString(R.string.sign_in));
        signInButton.setOnClickListener(this);

        forgotTextView = (TextView) this.findViewById(R.id.forgotTextView);
        forgotTextView.setTypeface(typeface);
        forgotTextView.setText(getString(R.string.forgot));
        forgotTextView.setOnClickListener(this);

        SharedPreferences signPref = getSharedPreferences(SplashScreen.SIGNIN_PREF, MODE_PRIVATE);
        //SharedPreferences.Editor editor = signPref.edit();


        usernameEdit = (EditText) this.findViewById(R.id.email_edittext);
        if(signPref.contains("username")) {
//            SharedPreferences.Editor editor = signPref.edit();
//            String name = editor
            usernameEdit.setText(signPref.getString("username", null));
        }
//        usernameEdit.setText("vreddys@hotmail.com");
    //    usernameEdit.setText("ssreddy013@gmail.com");
//     usernameEdit.setText("Rep1@sain.com");
//        usernameEdit.setText("manager@MedRep.com");
//        usernameEdit.setText("Rep2@MedRep.com");
//        usernameEdit.setText("Rep1@MedRep.com");
//        usernameEdit.setText("vreddys@hotmail.com");
        passwordEdit = (EditText) this.findViewById(R.id.pwd_edittext);
        boolean pwd = signPref.contains("password");
        if(pwd) {
//            SharedPreferences.Editor editor = signPref.edit();
//            String name = editor
            passwordEdit.setText(signPref.getString("password", null));
        }
//      passwordEdit.setText("password");
//        passwordEdit.setText("test");
    }

    @Override
    public void onClick(View v) {

        if(v == forgotTextView){
          /*  if(true){
                return;
            }*/
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
        }else if(v == signInButton){
            Log.d("TAG", "my username" + usernameEdit.getText().toString().length());
            Log.d("TAG", "my password" + passwordEdit.getText().toString().length());
            String userName = usernameEdit.getText().toString().trim();
            String password = passwordEdit.getText().toString();
            SharedPreferences signPref = getSharedPreferences(SplashScreen.SIGNIN_PREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = signPref.edit();
            editor.putString("username", userName);
            editor.putString("password", password);
            editor.commit();

            if(userName == null || userName.length() == 0 ){
                Toast.makeText(LoginActivity.this, "Please enter the valid email id ", Toast.LENGTH_SHORT).show();
            }else if(password == null || password.length() == 0){
                Toast.makeText(LoginActivity.this, "Please enter the password ", Toast.LENGTH_SHORT).show();
            } else if(!GlobalVariables.validate(userName, GlobalVariables.EMAIL_PATTERN)){
                Toast.makeText(LoginActivity.this, "Please enter the valid email id", Toast.LENGTH_SHORT).show();
            }
            else{
//            String url = HttpUrl.BASEURL + HttpUrl.SIGNIN + "&username=" + userName + "&password=" + password;
                new SignInTask(LoginActivity.this).execute(userName, password);
            }
        }




        /*if(*//*usernameEdit.getText().toString()!= null&& *//*userName.length()==0){

        }else if(){

        }else if(*//*passwordEdit.getText().toString()!= null&& *//*password.length()==0){

        }else if(userName != null && userName.length()!=0 &&
                GlobalVariables.validate(userName, GlobalVariables.EMAIL_PATTERN)
                && password != null && password.length() != 0){
//            new SignInTask(LoginActivity.this).execute(usernameEdit.getText().toString(), passwordEdit.getText().toString());



        }*/


    }
    public void startSign(final Object o){

        SharedPreferences signPref = getSharedPreferences(SplashScreen.SIGNIN_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = signPref.edit();
        editor.putBoolean(SplashScreen.IS_LOGGED_IN_KEY, true);
        editor.commit();


        new AsyncTask<Void, Void, TempProfile>(){

            ProgressDialog pd;
            @Override
            protected TempProfile doInBackground(Void... params) {

                String url = HttpUrl.GET_MY_ROLE + Utils.GET_ACCESS_TOKEN(LoginActivity.this);

                System.out.println("Url: " + url);

                JSONParser parser = new JSONParser();
                JSONObject jsonObject = parser.getJSON_Response(url, true);
                TempProfile tempProfile = null;
                try {
                    tempProfile = (TempProfile) parser.jsonParser(jsonObject, TempProfile.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return tempProfile;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

//                pd = new ProgressDialog(LoginActivity.this);
//                pd.setTitle("Please Wait");
//                pd.setMessage("Retrieving your information");
//                pd.show();
            }

            @Override
            protected void onPostExecute(TempProfile tempProfile) {
                super.onPostExecute(tempProfile);

                System.out.println("(tempProfile != null): " + (tempProfile != null));

                if(tempProfile != null){

                    if(!tempProfile.getStatus().equalsIgnoreCase(TempProfile.OTP_VERIFIED_STRING)){
                        verifyOTP_AndLoginUser(tempProfile);
                        return;
                    }

                    loginUser(tempProfile);

                }else{
                    //display error message saying 'Profile could not be found.'
                    Utils.DISPLAY_GENERAL_DIALOG(LoginActivity.this, "Error", "Invalid Profile.");
                }


                if(pd != null){
                    pd.dismiss();
                }
            }
        }.execute();

        /*Intent intent = new Intent(LoginActivity.this, DoctorDashboard.class);
        intent.putExtra("signin",(SignIn)o);
       *//* intent.putExtra(DoctorDashboard.PROFILE_NAME_KEY, profilename);
        intent.putExtra(DoctorDashboard.ROLE_ID_KEY, roleID);*//*
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finishAffinity();*/
    }

    private void verifyOTP_AndLoginUser(final TempProfile tempProfile) {

        //Resend otp here
        final View view = View.inflate(this, R.layout.doctor_f_enter_otp, null);
        otpEditText = (EditText) view.findViewById(R.id.enterotp);

        Utils.RESEND_OTP(LoginActivity.this, tempProfile.getEmailId());

        view.findViewById(R.id.bt_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpStr = otpEditText.getText().toString().trim();

                System.out.println("otpStr: " + otpStr);

                if (otpStr != null && !otpStr.equals("")) {
                    //                    Verity OTP here
                    String url = HttpUrl.BASEURL + HttpUrl.VERIFICATION_OTP + "token=" + otpStr + "&number=" + tempProfile.getMobileNo();
                    System.out.println("url: " + url);

                    new VerificationOtpTask(LoginActivity.this, tempProfile).execute(url);
                } else {
                    otpEditText.setError("Invalid OTP");
                    Toast.makeText(LoginActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                    verifyOTP_AndLoginUser(tempProfile);
                }
            }
        });

        view.findViewById(R.id.tv_resend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.RESEND_OTP(LoginActivity.this, tempProfile.getEmailId());
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
              .setTitle("OTP Not Verified")
            //    .setMessage("OTP has beed sent to your registered mobile and email id. Please verify OTP.")
                .setView(view)
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.show();

    }

    public void loginUser(TempProfile tempProfile) {
        String firstName = (tempProfile.getFirstName() == null ? "" : tempProfile.getFirstName());
        String lastName = (tempProfile.getLastName() == null ? "" : tempProfile.getLastName());

        int roleID = tempProfile.getRoleId();

        String profileName = firstName + " " + lastName;

        System.out.println("RoleID: " + roleID);
        System.out.println("profileName: " + profileName);

        if(profileName.trim().equals("")){
            profileName = "User";
        }
        Intent intent;

        SharedPreferences signPref = getSharedPreferences(SplashScreen.SIGNIN_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = signPref.edit();
        editor.putInt(SplashScreen.ROLE_ID, roleID);
        editor.putString(SplashScreen.PROFILE_NAME_KEY, profileName);


        if (TextUtils.isEmpty(regId)) {

            regId = registerGCM();

            Log.d("MainActivity", "GCM RegId: " + regId);

        } else {

//            Toast.makeText(getApplicationContext(),
//
//                    "Already Registered with GCM Server!",
//
//                    Toast.LENGTH_LONG).show();
        }


        switch (roleID){
            case UserRoles.DOCTOR:
                intent = new Intent(LoginActivity.this, DoctorDashboard.class);
//                                intent.putExtra("signin",(SignIn)o);
                intent.putExtra(SplashScreen.PROFILE_NAME_KEY, profileName);
                startActivity(intent);
                finishAffinity();
                if (dPictureBitmap == null) {
                    try {
                        String accessToken = SignIn.GET_ACCESS_TOKEN();
                        getDisplayPic();
                        dPictureBitmap = new DownloadImageTask().execute().get();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            case UserRoles.PHARMA_MEDREP:

                System.out.println("Pharma User: " + profileName);

                intent=new Intent(LoginActivity.this, PharmaDashBoard.class);
//                                intent.putExtra("signin",(SignIn)o);



                intent.putExtra(SplashScreen.PROFILE_NAME_KEY, profileName);
                startActivity(intent);
                finishAffinity();
                break;
            case UserRoles.PHARMA_MANAGER_MEDREP:

                System.out.println("Pharma User");

                intent=new Intent(LoginActivity.this, PharmaDashBoard.class);
//                                intent.putExtra("signin",(SignIn)o);
                intent.putExtra(SplashScreen.PROFILE_NAME_KEY, profileName);
                startActivity(intent);
                finishAffinity();
                break;
            default:
                break;
        }

        editor.commit();
    }

    public BroadcastReceiver incomingSMS_Receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            final Bundle bundle = intent.getExtras();
            try {
                if (bundle != null)
                {
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (int i = 0; i < pdusObj .length; i++)
                    {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[])                                                                                                    pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                        String senderNum = phoneNumber ;
                        String message = currentMessage .getDisplayMessageBody();
                        try
                        {
                            System.out.println("message: " + message);
                            if (senderNum .equals("MD-MedRep"))
                            {
                                String temp = "Password is ";

                                String otp = message.substring(message.indexOf(temp) + temp.length(), message.indexOf("."));
//                                DoctorOTP_Activity.OTP = otp;
//                                DoctorOTP_Activity.SET_OTP(otp);

                                otpEditText.setText(otp);

                                /*Otp Sms = new Otp();
                                Sms.recivedSms(message );*/
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }

                    }
                }

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    public void resendOTP_Success() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");

        LoginActivity.this.registerReceiver(incomingSMS_Receiver, filter);

        unregisterReceiver = true;
    }

    //GCM related stuff

    public String registerGCM() {

        gcm = GoogleCloudMessaging.getInstance(this);

        regId = getRegistrationId(context);

        if (TextUtils.isEmpty(regId)) {

            registerInBackground();

            Log.d("MainActivity", "registerGCM – successfully registered with GCM server – regId: " + regId);

        } else {

//            Toast.makeText(getApplicationContext(),
//
//                    "RegId already available. RegId: " + regId,
//
//                    Toast.LENGTH_LONG).show();

            System.out.println("GCM Registration id: " + regId);

//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//            builder.setMessage(regId);
//
//            builder.setTitle("RegId");
//
//            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                public void onClick(DialogInterface dialog, int which) {
//
//                    // continue with delete
//
//                }
//
//            });

//            builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//
//                public void onClick(DialogInterface dialog, int which) {
//
//                    // do nothing
//
//                }
//
//            });
//
//            builder.show();

        }

        return regId;

    }

    private String getRegistrationId(Context context) {

        final SharedPreferences signPref = getSharedPreferences(SplashScreen.SIGNIN_PREF, MODE_PRIVATE);
        //SharedPreferences.Editor editor = signPref.edit();

        String registrationId = signPref.getString(REG_ID, "");

        if (registrationId.isEmpty()) {

            Log.i("MainActivity", "Registration not found.");

            return "";

        }

        int registeredVersion = signPref.getInt(APP_VERSION, Integer.MIN_VALUE);

        int currentVersion = getAppVersion(context);

        if (registeredVersion != currentVersion) {

            Log.i("MainActivity", "App version changed.");

            return "";

        }

        return registrationId;

    }

    private static int getAppVersion(Context context) {

        try {

            PackageInfo packageInfo = context.getPackageManager()

                    .getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {

            Log.d("MainActivity", "I never expected this! Going down, going down!" + e);

            throw new RuntimeException(e);

        }

    }

    @SuppressWarnings("unchecked")

    private void registerInBackground() {

        new AsyncTask() {

            @Override

            protected String doInBackground(Object... params) {

                String msg = "";

                try {

                    if (gcm == null) {

                        gcm = GoogleCloudMessaging.getInstance(context);

                    }

                    regId = gcm.register(Config.GOOGLE_PROJECT_ID);

                    Log.d("MainActivity", "registerInBackground – regId: "

                            + regId);

                    msg = "Device registered, registration ID=" + regId;
//                    Utils.DISPLAY_GENERAL_DIALOG(LoginActivity.this, regId, "Invalid Profile.");

                    storeRegistrationId(context, regId);

                } catch (IOException ex) {

                    msg = "Error :" + ex.getMessage();

                    Log.d("RegisterActivity", "Error: " + msg);

                }

                Log.d("RegisterActivity", "AsyncTask completed: " + msg);

                return msg;

            }

//            protected void onPostExecute(String msg) {
//
//                Toast.makeText(getApplicationContext(),
//
//                        "Registered with GCM Server." + msg, Toast.LENGTH_LONG)
//
//                .show();
//
//            }
        }.execute(null, null, null);

    }

    private void storeRegistrationId(Context context, String regId) {

        final SharedPreferences signPref = getSharedPreferences(SplashScreen.SIGNIN_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = signPref.edit();

        int appVersion = getAppVersion(context);

        Log.i("MainActivity", "Saving regId on app version " + appVersion);


        editor.putString(REG_ID, regId);

        editor.putInt(APP_VERSION, appVersion);

        editor.commit();

    }
    /**
     * Method running a background thread to download the profile image from web
     */
    private void getDisplayPic(){
        try {
            new GetDisplayPicTask().execute();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    /**
     * Class running a background thread to fetch the profile image details
     */
    public class GetDisplayPicTask extends AsyncTask<String, Void, String> {

        private Context context = getApplicationContext();

        @Override
        protected String doInBackground(String... params) {
            StringBuilder response = new StringBuilder();
            try {
                response.append(DoctorPostMethods.sendGet(HttpUrl.COMMONURL + "/getDisplayPicture?token=" + SignIn.GET_ACCESS_TOKEN()));
                Log.d("TAG", "my response------------------------------------ " + response);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            try {
                JSONObject jsonObject = new JSONObject(o);
                HttpUrl.displayPic = jsonObject.getString("dPicture");
                Log.d("TAG", "my pic------------------------------------ " + HttpUrl.displayPic);
                SharedPreferences singInPref = getSharedPreferences("dPicture", MODE_PRIVATE);
                SharedPreferences.Editor editor = singInPref.edit();
                editor.putString("dPicture", HttpUrl.displayPic);
                editor.commit();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    /**
     * Class running a background thread to download the profile image from web
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        Bitmap mIcon_val;

        protected Bitmap doInBackground(String... urls) {
            try {
                SharedPreferences signPref = getSharedPreferences("dPicture", MODE_PRIVATE);
                String profilePicture = signPref.getString("dPicture", "User");
                URL newurl = new URL(profilePicture);
                mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());

            }catch (Exception ex){
                ex.printStackTrace();
            }
            return mIcon_val;
        }

        protected void onPostExecute(Bitmap result) {

            dPictureBitmap = result;
        }
    }

}
