package pharma;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.app.db.Company;
import com.app.util.PharmaRegBean;
import com.app.util.Utils;

import java.util.ArrayList;

import medrep.medrep.R;

/**
 * Created by admin on 9/26/2015.
 */
public class PharmaRegistationAddress extends AppCompatActivity {

    Button next_btn;
    Button prev_btn;
    LinearLayout nextButton;
    PharmaRegistationAddress _activity;
    EditText et_Addtess1,et_Address2,et_State,et_City,et_ZipCode;
    PharmaRegBean pharmaRegBean;
    private boolean editProfileFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharma_registration_address);


        Intent intent = getIntent();
        editProfileFlag = intent.getBooleanExtra(PhramaRegistrationTwo.EDIT_PROFILE, editProfileFlag);

        _activity=this;
        pharmaRegBean=PharmaRegBean.getInstance();

        et_Addtess1=(EditText)findViewById(R.id.edittext_address_one);
        et_Address2=(EditText)findViewById(R.id.edittext_address_two);
        et_State=(EditText)findViewById(R.id.edittext_state);
        et_City=(EditText)findViewById(R.id.edittext_city);
        et_ZipCode=(EditText)findViewById(R.id.edittext_zipcode);

        next_btn=(Button)findViewById(R.id.nextButton);
        prev_btn=(Button)findViewById(R.id.backButton);

        if(editProfileFlag){

            next_btn.setText("Update");

            if(PharmaUpdateActivity.REP_PROFILE.getLocations() != null && PharmaUpdateActivity.REP_PROFILE.getLocations().size() > 0){
                Company.Location location = PharmaUpdateActivity.REP_PROFILE.getLocations().get(0);

                et_Addtess1.setText(location.getAddress1());
                et_Address2.setText(location.getAddress2());
                et_State.setText(location.getState());
                et_City.setText(location.getCity());
                et_ZipCode.setText(location.getZipcode());
            }
        }else{
            et_Addtess1.setText(pharmaRegBean.getMdaddress1());
            et_Address2.setText(pharmaRegBean.getMmdAddress2());
            et_State.setText(pharmaRegBean.getMdState());
            et_City.setText(pharmaRegBean.getMdCity());
            et_ZipCode.setText(pharmaRegBean.getMdZipcode());
        }



        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_Addtess1.getText().toString().length()>0 && et_State.getText().toString().length()>0 && et_City.getText().toString().length()>0) {
                    if(editProfileFlag){
                        Company.Location location = new Company.Location();
                        location.setAddress1(et_Addtess1.getText() + "");
                        location.setAddress2(et_Address2.getText() + "");
                        location.setState(et_State.getText() + "");
                        location.setCity(et_City.getText() + "");
                        location.setZipcode(et_ZipCode.getText() + "");

                        if(PharmaUpdateActivity.REP_PROFILE.getLocations() != null) {
                            PharmaUpdateActivity.REP_PROFILE.getLocations().clear();
                        }else{
                            PharmaUpdateActivity.REP_PROFILE.setLocations(new ArrayList<Company.Location>());
                        }

                        PharmaUpdateActivity.REP_PROFILE.getLocations().add(location);

                        Utils.UPDATE_PHARMA_PROFILE(PharmaRegistationAddress.this, PharmaUpdateActivity.REP_PROFILE);
                    }else {
                        pharmaRegBean.setMdaddress1(et_Addtess1.getText() + "");
                        pharmaRegBean.setMmdAddress2(et_Address2.getText() + "");
                        pharmaRegBean.setMdState(et_State.getText() + "");
                        pharmaRegBean.setMdCity(et_City.getText() + "");
                        pharmaRegBean.setMdZipcode(et_ZipCode.getText() + "");
                        Intent i = new Intent(getApplicationContext(), GenerateOTPActivity.class);
                        startActivity(i);
                    }
                }else{
                    if(et_Addtess1.getText().toString().length()<=0)
                        et_Addtess1.setError("Address1 can't be empty");

                    if(et_State.getText().toString().length()<=0)
                        et_State.setError("State can't be empty");

                    if(et_City.getText().toString().length()<=0)
                        et_City.setError("City can't be empty");
                }



            }
        });

        prev_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pharmaRegBean.setMdaddress1(et_Addtess1.getText().toString());
                pharmaRegBean.setMmdAddress2(et_Address2.getText().toString());
                pharmaRegBean.setMdState(et_State.getText().toString());
                pharmaRegBean.setMdCity(et_City.getText().toString());
                pharmaRegBean.setMdZipcode(et_ZipCode.getText().toString());
                onBackPressed();

            }

        });
    }

    @Override
    public void onBackPressed() {

        pharmaRegBean.setMdaddress1(et_Addtess1.getText().toString());
        pharmaRegBean.setMmdAddress2(et_Address2.getText().toString());
        pharmaRegBean.setMdState(et_State.getText().toString());
        pharmaRegBean.setMdCity(et_City.getText().toString());
        pharmaRegBean.setMdZipcode(et_ZipCode.getText().toString());
        super.onBackPressed();
    }
/*
    @Override
    public void onClick(View v) {

        if(et_Addtess1.getText().toString().length()>0 && et_State.getText().toString().length()>0 && et_City.getText().toString().length()>0) {
            pharmaRegBean.setMdaddress1(et_Addtess1.getText().toString());
            pharmaRegBean.setMmdAddress2(et_Address2.getText().toString());
            pharmaRegBean.setMdState(et_State.getText().toString());
            pharmaRegBean.setMdCity(et_City.getText().toString());
            pharmaRegBean.setMdZipcode(et_ZipCode.getText().toString());
            Intent i = new Intent(getApplicationContext(), GenerateOTPActivity.class);
            startActivity(i);
        }else{
            if(et_Addtess1.getText().toString().length()<=0)
            et_Addtess1.setError("Address1 can't be empty");

            if(et_State.getText().toString().length()<=0)
                et_State.setError("State can't be empty");

            if(et_City.getText().toString().length()<=0)
                et_City.setError("City can't be empty");
        }


    }*/
}

