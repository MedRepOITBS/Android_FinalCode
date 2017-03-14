package medrep.medrep;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.pojo.DoctorProfile;
import com.app.task.OTPTask;
import com.app.task.VerificationOtpTask;
import com.app.util.HttpUrl;

import java.util.ArrayList;

public class OTPActivity extends AppCompatActivity{

    public static final String MOBILE_NUMBER_KEY = "MobileNumber";

    private String mobileNum;
    private String username;
    private String email;

    private Button button;
    private EditText otpEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_f_enter_otp);


        Intent intent = getIntent();

        mobileNum = intent.getStringExtra(MOBILE_NUMBER_KEY);
        username = intent.getStringExtra(WelcomeActivity.USERNAME_KEY);
        email = intent.getStringExtra(WelcomeActivity.EMAIL_KEY);

        System.out.println("mobileNum: " + mobileNum);

        otpEditText = (EditText) this.findViewById(R.id.enterotp);


        button = (Button) findViewById(R.id.bt_submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpStr = otpEditText.getText() + toString();
                if(otpStr != null && !otpStr.trim().equals("")){
                    String url = HttpUrl.BASEURL + HttpUrl.VERIFICATION_OTP + "token=" + otpStr + "&number=" + mobileNum;
                    System.out.println("url: " + url);

                    new VerificationOtpTask(OTPActivity.this, username, email).execute(url);
                }
            }
        });

        TextView resendOTP_TV = (TextView)findViewById(R.id.tv_resend);
        resendOTP_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /*@Override
    public void onClick(View v) {
        if(v == button){
            if(!otpEditText.getText().toString().isEmpty() && otpEditText.getText().toString().length() > 0)
//                new OTPTask(OTPActivity.this).execute(HttpUrl.BASEURL + HttpUrl.OTP + mobileNum);
            ;
        }
    }*/

    /*public void success(String otp, String mobileNumber){

        String url = HttpUrl.BASEURL+HttpUrl.VERIFICATION_OTP+"token="+otp+"&number="+mobileNumber;
        System.out.println("url: " + url);

//        http://183.82.106.234:8080/MedRepApplication/preapi/registration/verifyMobileNo?token=

        new VerificationOtpTask(OTPActivity.this).execute(HttpUrl.BASEURL+HttpUrl.VERIFICATION_OTP+"token="+otp+"&number="+mobileNumber);
    }*/

    /*public void success(){
        DoctorProfile doctorProfile = DoctorProfile.getInstance();
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.putExtra(WelcomeActivity.EMAIL_KEY, email);
        intent.putExtra(WelcomeActivity.USERNAME_KEY, username);
        startActivity(intent);
    }*/
}
