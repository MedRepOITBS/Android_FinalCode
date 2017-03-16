package pharma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.util.HttpUrl;
import com.app.util.PharmaRegBean;
import com.app.util.ServiceHandler;
import com.app.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import medrep.medrep.R;

/**
 * Created by admin on 9/26/2015.
 */
public class PhramaRegistrationTwo extends AppCompatActivity {//} implements View.OnClickListener{
    public static final String EDIT_PROFILE = "EditProfile";
    Button next_btn;
    Button prev_btn;
    PhramaRegistrationTwo _activity;
    EditText et_fname,et_lname,et_mobileno,et_areacovered,et_mail;
    PharmaRegBean pharmaRegBean;
    Spinner spn_compId;
    ArrayList companies = new ArrayList<>();
    private ProgressDialog progress = null;
    private Thread thread;
    ArrayAdapter<String> spinnerArrayAdapter;
    String status;
    private Handler handler = new Handler();

    private boolean editProfileFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharma_reg_two);

        Intent intent = getIntent();
        editProfileFlag = intent.getBooleanExtra(EDIT_PROFILE, editProfileFlag);


         pharmaRegBean = PharmaRegBean.getInstance();

        Bundle b=getIntent().getExtras();
        if(b!=null){
            pharmaRegBean.setStrPassString(b.getString("selectedCategory"));
        }

        et_mail=(EditText)findViewById(R.id.edittext_mail);
        et_fname=(EditText)findViewById(R.id.edittext_firstname);
        et_lname=(EditText)findViewById(R.id.edittext_lastname);
        et_mobileno=(EditText)findViewById(R.id.edittext_mobile);
        spn_compId=(Spinner)findViewById(R.id.spn_compid);
        getCompanyList();



        next_btn=(Button)findViewById(R.id.nextButton);
        prev_btn=(Button)findViewById(R.id.backButton);
         spinnerArrayAdapter = new ArrayAdapter<String>(PhramaRegistrationTwo.this, android.R.layout.simple_spinner_item, companies); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       et_areacovered=(EditText)findViewById(R.id.edit_spinner);


        if(editProfileFlag){

            next_btn.setText("Update");
            spn_compId.setEnabled(false);
            spn_compId.setClickable(false); // Disabling selection
            et_mail.setText(PharmaUpdateActivity.REP_PROFILE.getEmailId());

            et_mail.setEnabled(false);

            et_fname.setText(PharmaUpdateActivity.REP_PROFILE.getFirstName());
            et_lname.setText(PharmaUpdateActivity.REP_PROFILE.getLastName());
            et_mobileno.setText(PharmaUpdateActivity.REP_PROFILE.getMobileNo());

            et_mobileno.setEnabled(false);

            et_areacovered.setText(PharmaUpdateActivity.REP_PROFILE.getCoveredArea());

        }else{
            et_mail.setText(pharmaRegBean.getMdEmailId());
            et_fname.setText(pharmaRegBean.getMdFirstName());
            et_lname.setText(pharmaRegBean.getMdLastName());
            et_mobileno.setText(pharmaRegBean.getMdMobileNumber());
            et_areacovered.setText(pharmaRegBean.getMdAreaCovered());
        }

         next_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 System.out.println(!spn_compId.getSelectedItem().toString().equalsIgnoreCase("Select Company"));
                 System.out.println(et_fname.getText().toString().length() > 0);
                 System.out.println(et_lname.getText().toString().length() > 0);
                 System.out.println(et_mobileno.getText().toString().length() > 0);

                 System.out.println("Kishore: " + (!spn_compId.getSelectedItem().toString().equalsIgnoreCase("Select Company") &&
                         et_fname.getText().toString().length() > 0 &&
                         et_lname.getText().toString().length() > 0 &&
                         et_mobileno.getText().toString().length() > 0));

                 if (!spn_compId.getSelectedItem().toString().equalsIgnoreCase("Select Company") && et_fname.getText().toString().length() > 0 && et_lname.getText().toString().length() > 0 && et_mobileno.getText().toString().length() > 0) {
                     if(editProfileFlag){

                         PharmaUpdateActivity.REP_PROFILE.setEmailId(et_mail.getText().toString());
                         PharmaUpdateActivity.REP_PROFILE.setFirstName(et_fname.getText().toString());
                         PharmaUpdateActivity.REP_PROFILE.setLastName(et_lname.getText().toString());
                         PharmaUpdateActivity.REP_PROFILE.setMobileNo(et_mobileno.getText().toString());
                         PharmaUpdateActivity.REP_PROFILE.setCoveredArea(et_areacovered.getText().toString());
                         PharmaUpdateActivity.REP_PROFILE.setCompanyName(spn_compId.getSelectedItem().toString());


                         Utils.isPharmaUpdate=true;
                         Utils.UPDATE_PHARMA_PROFILE(PhramaRegistrationTwo.this, PharmaUpdateActivity.REP_PROFILE);
                     }else{
                         pharmaRegBean.setMdEmailId(et_mail.getText().toString());
                         pharmaRegBean.setMdFirstName(et_fname.getText().toString());
                         pharmaRegBean.setMdLastName(et_lname.getText().toString());
                         pharmaRegBean.setMdMobileNumber(et_mobileno.getText().toString());
                         pharmaRegBean.setMdAreaCovered(et_areacovered.getText().toString());
                         pharmaRegBean.setMdCompanyName(spn_compId.getSelectedItem().toString());
                         Intent intent = new Intent(getApplicationContext(), PharmaRegisterThree.class);
                         startActivity(intent);
                     }
                 } else {
                     if (et_mail.getText().toString().length() <= 0)
                         et_mail.setError("E Mail id can't be empty");
                     if (et_fname.getText().toString().length() <= 0)
                         et_fname.setError("First Name can't be empty");
                     if (et_lname.getText().toString().length() <= 0)
                         et_lname.setError("Last Name can't be empty");
                     if (et_mobileno.getText().toString().length() <= 0)
                         et_mobileno.setError("Mobile No can't be empty");
                     if (spn_compId.getSelectedItem().toString().equalsIgnoreCase("Select Company")) {
                         Toast.makeText(getApplicationContext(), "Please select company", Toast.LENGTH_LONG).show();
                     }
                 }
             }
         });




        prev_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pharmaRegBean.setMdEmailId(et_mail.getText().toString());
                pharmaRegBean.setMdFirstName(et_fname.getText().toString());
                pharmaRegBean.setMdLastName(et_lname.getText().toString());
                pharmaRegBean.setMdMobileNumber(et_mobileno.getText().toString());
                pharmaRegBean.setMdAreaCovered(et_areacovered.getText().toString());
                pharmaRegBean.setMdCompanyName(spn_compId.getSelectedItem().toString());
                onBackPressed();

            }

        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void getCompanyList() {

        progress = new ProgressDialog(PhramaRegistrationTwo.this);
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
                    String url= HttpUrl.COMPANY_DETAILS_URL;

                    status=sh.makeServiceCall(url,1);
                }catch (Exception e){
                }

                handler.post(createUI);
            }
        };

        thread.start();
    }

    final Runnable createUI = new Runnable() {
        public void run() {
            progress.dismiss();

            try {

                if (status != null) {
                    JSONArray jarry = new JSONArray(status);
                    companies.add("Select Company");

                    for(int i=0;i<jarry.length();i++){
                        companies.add(jarry.getJSONObject(i).getString("companyName"));
                    }
                }
            }catch(JSONException e){
                Log.d("Get companies data", e.getMessage());
            }

            spn_compId.setAdapter(spinnerArrayAdapter);

            if(editProfileFlag){
                for(int i = 0; i<companies.size(); i++) {
                    if(companies.get(i).toString().equalsIgnoreCase(PharmaUpdateActivity.REP_PROFILE.getCompanyName() + "")){
                        spn_compId.setSelection(i);
                    }
                }
            }else{
                for(int i = 0;i<companies.size();i++) {
                    if(companies.get(i).toString().equalsIgnoreCase(pharmaRegBean.getMdCompanyName() + "")){
                        spn_compId.setSelection(i);
                    }
                }
            }



        }
    };
}
