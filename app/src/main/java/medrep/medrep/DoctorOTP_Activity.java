package medrep.medrep;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.pojo.DoctorProfile;
import com.app.task.VerificationOtpTask;
import com.app.util.HttpUrl;
import com.app.util.Utils;

import java.util.ArrayList;

/**
 * Created by kishore on 8/10/15.
 */
public class DoctorOTP_Activity extends Activity {

    public static final String OTP_VERIFIED_PREF_NAME = "OTP_Verified";
    public static final String OTP_VERIFIED_KEY = "IsOTP_Verified";

    public static final ArrayList<Activity> ACTIVITY_STACK = new ArrayList<>();

    public static final String MOBILE_NUMBER_KEY = "MobileNumber";
    public static String OTP;

    public static EditText otpEditText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (Activity activity: ACTIVITY_STACK){
            activity.finish();
        }

        ACTIVITY_STACK.clear();

        DoctorProfile.clearProfile();

        setContentView(R.layout.doctor_f_enter_otp);

        Intent intent = getIntent();

        final String mobileNum = intent.getStringExtra(MOBILE_NUMBER_KEY);
        final String username = intent.getStringExtra(WelcomeActivity.USERNAME_KEY);
        final String email = intent.getStringExtra(WelcomeActivity.EMAIL_KEY);

        otpEditText = (EditText) this.findViewById(R.id.enterotp);

        if(OTP != null && OTP.trim().length() > 0){
           SET_OTP(OTP);
        }

        Button submitButton = (Button) findViewById(R.id.bt_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpStr = otpEditText.getText().toString().trim();

                System.out.println("otpStr: " + otpStr);

                if (otpStr != null && !otpStr.equals("")) {
                    //                    Verity OTP here
                    String url = HttpUrl.BASEURL + HttpUrl.VERIFICATION_OTP + "token=" + otpStr + "&number=" + mobileNum;
                    System.out.println("url: " + url);

                    new VerificationOtpTask(DoctorOTP_Activity.this, username, email).execute(url);
                } else {
                    otpEditText.setError("Invalid OTP");
                }
            }
        });

        TextView resendOTP = (TextView) findViewById(R.id.tv_resend);
        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.RESEND_OTP(DoctorOTP_Activity.this, email);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        OTP = null;
        otpEditText = null;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DoctorOTP_Activity.this)
                .setTitle("Verify Later!")
                .setTitle("Do you want to verify Mobile Later?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setOTP_Preference(false);
                        DoctorOTP_Activity.this.finish();
                    }
                })
                .setNegativeButton("No", null);
        builder.show();
    }

    public void setOTP_Preference(boolean OTP_Preference) {
        SharedPreferences preferences = DoctorOTP_Activity.this.
                getSharedPreferences(
                        DoctorOTP_Activity.OTP_VERIFIED_PREF_NAME,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(DoctorOTP_Activity.OTP_VERIFIED_KEY, OTP_Preference);
        editor.commit();
    }

    public static void SET_OTP(String otp) {
        if(otpEditText != null){
            otpEditText.setText(otp);

            for (int i = DoctorOTP_Activity.ACTIVITY_STACK.size() - 1; i > 0; i--){
                if(DoctorOTP_Activity.ACTIVITY_STACK.get(i) instanceof  DoctorSetPasswordActivity){
                    try {
                        DoctorOTP_Activity.ACTIVITY_STACK.get(i).unregisterReceiver(DoctorSetPasswordActivity.incomingSMS_Receiver);
                    }catch(Exception e){

                    }
                    break;
                }
            }
        }
    }
}
