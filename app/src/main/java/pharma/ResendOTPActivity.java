package pharma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.util.HttpUrl;
import com.app.util.PharmaRegBean;
import com.app.util.ServiceHandler;

import medrep.medrep.R;

/**
 * Created by admin on 9/26/2015.
 */
public class ResendOTPActivity extends AppCompatActivity {
    Button next_btn;
    Button prev_btn;
    private ProgressDialog progress = null;
    private Thread thread;
    private Handler handler = new Handler();
    Button nextButton;
    EditText memail;
    ResendOTPActivity _activity;
    String otp;
    String status;
    PharmaRegBean pharmaRegBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resend_activity_otp);
        _activity = this;

        pharmaRegBean=PharmaRegBean.getInstance();
      //  nextButton=(Button)findViewById(R.id.saveButton);
        memail=(EditText)findViewById(R.id.email_edittext);

        next_btn=(Button)findViewById(R.id.nextButton);
        prev_btn=(Button)findViewById(R.id.backButton);

        PharmaRegBean pharmaRegBean=PharmaRegBean.getInstance();
        System.out.println("result" + pharmaRegBean.getMdRmName());

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Data...clicked");

                if(!memail.getText().toString().equalsIgnoreCase("")) {
                    System.out.println("Data..ww..clicked");
                  //  report_Data();
                    System.out.println("Data..dd.clicked");
                    report_Data();

                   /* Intent i=new Intent(getApplicationContext(),ValidateOtpActivity.class);


                    startActivity(i);
*/

                }else{
                    Toast.makeText(getApplicationContext(),"Password mismatched",Toast.LENGTH_LONG).show();
                }

            }
        });



        prev_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }



    public void report_Data() {

        progress = new ProgressDialog(ResendOTPActivity.this);
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


                        status=sh.makeServiceCall(HttpUrl.url_resendOtp+memail.getText().toString()+"/", 1);//, obj.toString());



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

            if (!status.contains("Success")) {



                Intent i = new Intent(getApplicationContext(), ValidateOtpActivity.class);

                startActivity(i);

            }else{
                Toast.makeText(getApplicationContext(),"Registration Failed!",Toast.LENGTH_LONG).show();
            }
        }
    };
}

