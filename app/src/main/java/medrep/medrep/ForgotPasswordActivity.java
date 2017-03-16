package medrep.medrep;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.util.GlobalVariables;
import com.app.util.HttpUrl;
import com.app.util.Utils;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.app.util.Utils.makeRequest;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText usernameEditText;
    private EditText newPasswordEditText;
    private EditText cnfirmPasswordEditText;
    private EditText otpEditText;
    private EditText verificationTypeEditText;
    private EditText emailIdEditText;
//    private Button nextButton,submitButton,resendotp;
    private Typeface typeface;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        setViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try{
            unregisterReceiver(incomingSMS_Receiver);
        }catch (Exception e){

        }
    }

    private void setViews(){
        typeface = GlobalVariables.getTypeface(ForgotPasswordActivity.this);
       // usernameEditText = (EditText)this.findViewById(R.id.edittext_username);
        newPasswordEditText = (EditText)this.findViewById(R.id.edittext_new_password);
        cnfirmPasswordEditText = (EditText)this.findViewById(R.id.edittext_cnfirm_password);
        otpEditText = (EditText)this.findViewById(R.id.edittext_otp);
      //  verificationTypeEditText = (EditText)this.findViewById(R.id.edittext_verification);
        emailIdEditText = (EditText)this.findViewById(R.id.edittext_email);
        findViewById(R.id.next).setOnClickListener(this);
        findViewById(R.id.Resend_otp).setOnClickListener(this);
        findViewById(R.id.submit).setOnClickListener(this);

        layout=(LinearLayout)this.findViewById(R.id.layout);

    }


    @Override
    public void onClick(View v) {
        String email = (emailIdEditText.getText() + "").trim();
        switch (v.getId()){
            case R.id.next:
                if(email == null || email.length() == 0 || email.isEmpty()) {
                    emailIdEditText.setError("Email address cannot be empty.");
                }else {
//                    new ForgotPasswordTask(ForgotPasswordActivity.this).execute(HttpUrl.BASEURL + HttpUrl.FORGOTPASSWORD + emailIdEditText.getText().toString());
                    Utils.RESEND_OTP(ForgotPasswordActivity.this, email);
                }
                break;
            case R.id.Resend_otp:
                Utils.RESEND_OTP(ForgotPasswordActivity.this, email);
                break;
            case R.id.submit:

                String desiredPassword = ((EditText)findViewById(R.id.edittext_new_password)).getText() + "";
                String confirmPassword = ((EditText)findViewById(R.id.edittext_cnfirm_password)).getText() + "";
                String otp = otpEditText.getText() + "";

                Map<String, String> comment = new HashMap<String, String>();
                comment.put("userName", email);
                comment.put("otp", otp);
                comment.put("newPassword", desiredPassword);
                comment.put("confirmPassword", confirmPassword);
                comment.put("verificationType", "SMS");

                final String json = new GsonBuilder().create().toJson(comment, Map.class);

                System.out.println("json: " + json);

                AsyncTask<Void, Void, JSONObject> async = new AsyncTask<Void, Void, JSONObject>() {

                    ProgressDialog pd;

                    @Override
                    protected JSONObject doInBackground(Void... params) {

                        final String url = HttpUrl.FORGOT_PASSWORD;
                        System.out.println("URL: " + url);

                        HttpResponse response = makeRequest(url, json);

                        String json_string = null;
                        try {
                            json_string = EntityUtils.toString(response.getEntity());
                            JSONObject temp1 = new JSONObject(json_string);
                            System.out.println("temp1: " + temp1);
                            return temp1;

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();

                        pd = new ProgressDialog(ForgotPasswordActivity.this);
                        pd.setTitle("Changing Password");
                        pd.setMessage("Please wait for few seconds");
                        pd.setCancelable(false);
                        pd.show();
                    }

                    @Override
                    protected void onPostExecute(JSONObject jsonObject) {
                        super.onPostExecute(jsonObject);

                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }

                        System.out.println("jsonObject: " + jsonObject);

                        try {
                            if (jsonObject.getString("status").equalsIgnoreCase("ok") || jsonObject.getString("message").equalsIgnoreCase("success")) {
//                                Password update is successful
                                Utils.DISPLAY_GENERAL_DIALOG(ForgotPasswordActivity.this, "Success", "Successfully changed password.");

                                finish();

                                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
//                                Password update is not successful
                                Utils.DISPLAY_GENERAL_DIALOG(ForgotPasswordActivity.this, "Failed", jsonObject.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                if(desiredPassword.isEmpty() || desiredPassword.trim().length() ==0){
                    //  desiredPwdET.setError("Password cannot be empty.");
                    Toast.makeText(ForgotPasswordActivity.this, "New Password cannot be empty.", Toast.LENGTH_SHORT).show();
                }else if(confirmPassword.isEmpty() || confirmPassword.trim().length() ==0){
                    //confirmPwdET.setError("Please confirm password.");
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter confirm password.", Toast.LENGTH_SHORT).show();
                }else if(desiredPassword.trim().length() > 0 && confirmPassword.trim().length() > 0) {
                    if (!desiredPassword.equals(confirmPassword)) {
                        // desiredPwdET.setError("Password do not match.");
                        Toast.makeText(ForgotPasswordActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                        //confirmPwdET.setError("Password do not match.");
                    } else {
                        if(Utils.isNetworkAvailable(ForgotPasswordActivity.this)){
                            async.execute();
                        }
                    }
                }

                break;
        }
    }

    /*private void setFields(){
        if(!usernameEditText.getText().toString().isEmpty() &&
                !newPasswordEditText.getText().toString().isEmpty() && !cnfirmPasswordEditText.getText().toString().isEmpty() &&
                !otpEditText.getText().toString().isEmpty() && !verificationTypeEditText.getText().toString().isEmpty()){
            ForgotPassword forgotPassword = new ForgotPassword();
            forgotPassword.setUsername(usernameEditText.getText().toString());
            forgotPassword.setNewPassword(newPasswordEditText.getText().toString());
            forgotPassword.setPassword(cnfirmPasswordEditText.getText().toString());
            forgotPassword.setOtp(otpEditText.getText().toString());
            forgotPassword.setVerificationType(verificationTypeEditText.getText().toString());
            new ForgotPasswordTask(this, forgotPassword).execute(HttpUrl.BASEURL+HttpUrl.FORGOTPASSWORD+emailIdEditText.getText().toString());
        }
    }*/

    public void successResult(){
        layout.setVisibility(View.VISIBLE);
        findViewById(R.id.email_lin).setVisibility(View.GONE);
        findViewById(R.id.next).setVisibility(View.GONE);

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");

        ForgotPasswordActivity.this.registerReceiver(incomingSMS_Receiver, filter);
    }

    public void duplicateData(String message){

    }

    public void failure(){

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
}
