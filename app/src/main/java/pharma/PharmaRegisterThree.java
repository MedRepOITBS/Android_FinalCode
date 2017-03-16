package pharma;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.app.util.PharmaRegBean;

import medrep.medrep.R;

/**
 * Created by admin on 9/26/2015.
 */
public class PharmaRegisterThree extends AppCompatActivity {

    Button next_btn;
    Button prev_btn;
    LinearLayout nextButton;
    PharmaRegisterThree _activity;
    EditText et_rm_name,et_rm_mobile,et_rm_mail;
    PharmaRegBean pharmaRegBean;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharma_reg_three);
         pharmaRegBean=PharmaRegBean.getInstance();
        _activity=this;


       et_rm_name=(EditText)findViewById(R.id.edittext_report_m_name);
        et_rm_mail=(EditText)findViewById(R.id.ed_report_emaiid);
        et_rm_mobile=(EditText)findViewById(R.id.ed_report_mobilenumber);
        next_btn=(Button)findViewById(R.id.nextButton);
        prev_btn=(Button)findViewById(R.id.backButton);

        et_rm_name.setText(pharmaRegBean.getMdRmName());
        et_rm_mail.setText(pharmaRegBean.getRmMail());
        et_rm_mobile.setText(pharmaRegBean.getRmMobile());


        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_rm_mail.getText().toString().length() != 0) {
                    pharmaRegBean.setRmMail(et_rm_mail.getText().toString());
                    pharmaRegBean.setRmMobile(et_rm_mobile.getText().toString());
                    pharmaRegBean.setMdRmName(et_rm_name.getText().toString());

                    Intent i = new Intent(getApplicationContext(), PharmaRegistationAddress.class);


                    startActivity(i);
                } else {

                    if (et_rm_mail.getText().toString().length() <= 0)
                        et_rm_mail.setError("Report Manager E Mail id can't be empty");
                    if (et_rm_name.getText().toString().length() <= 0)
                        et_rm_name.setError("Report Manager Name can't be empty");

                }

            }
        });

        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_rm_name.setText(pharmaRegBean.getMdRmName());
                et_rm_mail.setText(pharmaRegBean.getRmMail());
                et_rm_mobile.setText(pharmaRegBean.getRmMobile());
                onBackPressed();

            }

        });
    }

    @Override
    public void onBackPressed() {

        et_rm_name.setText(pharmaRegBean.getMdRmName());
        et_rm_mail.setText(pharmaRegBean.getRmMail());
        et_rm_mobile.setText(pharmaRegBean.getRmMobile());
        super.onBackPressed();
    }

   /* @Override
    public void onClick(View v) {


if(et_rm_mail.getText().toString().length()!=0 && et_rm_mobile.getText().toString().length()!=0 && et_rm_name.getText().toString().length()!=0) {
    pharmaRegBean.setRmMail(et_rm_mail.getText().toString());
    pharmaRegBean.setRmMobile(et_rm_mobile.getText().toString());
    pharmaRegBean.setMdRmName(et_rm_name.getText().toString());

    Intent i = new Intent(getApplicationContext(), PharmaRegistationAddress.class);


    startActivity(i);
}else{

    if(et_rm_mail.getText().toString().length()<=0)
        et_rm_mail.setError("Report Manager E Mail id can't be empty");
    if(et_rm_name.getText().toString().length()<=0)
        et_rm_name.setError("Report Manager Name can't be empty");

}

    }*/
}

