package pharma;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.util.HttpUrl;
import com.app.util.PharmaRegBean;
import com.app.util.ServiceHandler;

import medrep.medrep.R;
import medrep.medrep.WelcomeActivity;

/**
 * Created by admin on 9/26/2015.
 */
public class ValidateOtpActivity extends AppCompatActivity {


    Button nextButton;
 //   ValidateOtpActivity _activity;
    EditText et_otp;
    String otp;
    TextView tv_resend;
    private ProgressDialog progress = null;
    private Thread thread;
    private Handler handler = new Handler();
    PharmaRegBean pharmaBean;
    ServiceHandler sh;
    String status;

    String strotp="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_f_enter_otp);

        nextButton=(Button)findViewById(R.id.bt_submit);

     ///   _activity = this;
        et_otp=(EditText)findViewById(R.id.enterotp);
        tv_resend=(TextView)findViewById(R.id.tv_resend);
        pharmaBean=PharmaRegBean.getInstance();


        sh=new ServiceHandler();
        myDalog("OTP has been sent to registered Mobile ");
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_otp.getText().toString().length()>0) {
                    strotp=et_otp.getText().toString();
                    verifyOtp();

                }else{
                    et_otp.setError("OTP can't be empty");
                }
            }
        });

        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

         report_Data();
            }
        });

    }

//http://183.82.106.234:8080/MedRepApplication/preapi/registration/verifyMobileNo?token=34842&number=9247204720


    public void verifyOtp() {

        progress = new ProgressDialog(ValidateOtpActivity.this);
        progress.setCancelable(false);
        String plz_craetedb = "Please Wait...";
        progress.setMessage(plz_craetedb);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setProgress(0);
        progress.setMax(100);
        progress.show();

        thread = new Thread() {
            public void run() {
                try {

                    ServiceHandler sh=new ServiceHandler();


                    status=sh.makeServiceCall(HttpUrl.BASEURL + "/preapi/registration/verifyMobileNo?token="+strotp+"&number="+pharmaBean.getMdMobileNumber(), 1);//, obj.toString());



                }catch (Exception e){

                    System.out.print("Exception:"+e.getMessage());


                }

                handler.post(verifycreateUI);
            }
        };

        thread.start();
    }

    final Runnable verifycreateUI = new Runnable() {
        public void run() {
            progress.dismiss();
            System.out.println("Data...clicked"+status);

            if (status.contains("Success")) {
                pharmaBean=null;
                myDalog("OTP verificcation successfull");


            }else{
                //+    Toast.makeText(getApplicationContext(), "Registration failed.Please try again!", Toast.LENGTH_LONG).show();
                myDalog("OTP verification failed!");
            }
        }
    };



    public void report_Data() {

        progress = new ProgressDialog(ValidateOtpActivity.this);
        progress.setCancelable(false);
        String plz_craetedb = "Please Wait...";
        progress.setMessage(plz_craetedb);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setProgress(0);
        progress.setMax(100);
        progress.show();

        thread = new Thread() {
            public void run() {
                try {

                    ServiceHandler sh=new ServiceHandler();


                    status=sh.makeServiceCall(HttpUrl.BASEURL+""+ HttpUrl.url_resendOtp+pharmaBean.getMdEmailId()+"/", 1);//, obj.toString());



                }catch (Exception e){

                    System.out.print("Exception:"+e.getMessage());


                }

                handler.post(createUI);
            }
        };

        thread.start();
    }

    final Runnable createUI = new Runnable() {
        public void run() {
            progress.dismiss();
            System.out.println("Data...clicked"+status);

            if (status.contains("Success")) {

            myDalog("OTP has been sent to registered Mobile ");


            }else{
            //+    Toast.makeText(getApplicationContext(), "Registration failed.Please try again!", Toast.LENGTH_LONG).show();
                myDalog("OTP sent failed Please try again!!");
            }
        }
    };




    public void myDalog(String msg) {
        final Dialog dialog = new Dialog(ValidateOtpActivity.this);

        dialog.setContentView(R.layout.dialog);
        // Set dialog title
        dialog.setTitle("Medrep Message");

        Button next_btn;
        Button prev_btn;

        next_btn = (Button)dialog.findViewById(R.id.nextButton);
        prev_btn = (Button) dialog.findViewById(R.id.backButton);

        if(msg.equalsIgnoreCase("OTP verificcation successfull")){
            prev_btn.setVisibility(View.GONE);
        }
        Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(i);

        // set values for custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.textDialog);
        text.setText(msg);
        ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
        //image.setImageResource(R.drawable.image0);
        prev_btn.setVisibility(View.GONE);
        dialog.show();
        next_btn.setText("Yes");
        if (msg.equalsIgnoreCase("OTP is not verified Do you want to Quit!")) {
            prev_btn.setVisibility(View.VISIBLE);
            prev_btn.setText("No");
        }

        //  Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
        // if decline button is clicked, close the custom dialog
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                dialog.dismiss();

            }
        });
        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();

            }
        });
    }

    @Override
    public void onBackPressed() {

        if(strotp.equalsIgnoreCase("")) {
           myDalog("OTP is not verified Do you want to Quit!");
        }else{
            pharmaBean=null;
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
            finish();
        }


    }
}

