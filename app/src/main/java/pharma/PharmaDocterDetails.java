package pharma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.db.DoctorProfile;
import com.app.fragments.PharmaNavigationDrawerFragment;
import com.app.util.HttpUrl;
import com.app.util.JSONParser;
import com.app.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import medrep.medrep.R;
import pharma.model.DoctorNotificationStats;

/**
 * Created by admin on 10/10/2015.
 */
public class PharmaDocterDetails extends AppCompatActivity{


    PharmaDocterDetails _activity;

    LinearLayout convretedAppaionmtneListItme;

//    Button docter_activity_score;

    Button present_activity_score;

    public static final String COMPANY_DOCTOR_KEY = "CompanyDoctor";
    public static final String MEDREP_NAME_KEY = "MedRepName";
    public static final String DOCTOR_ID_KEY = "DoctorID";
    public static final String NOTIFICATION_ID_KEY = "NotificationID";

    private boolean isCompanyDoctor = false;


    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharma_docter_details);


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        _activity=this;
        title = (TextView)findViewById(R.id.back_tv);

        Intent intent = getIntent();

        isCompanyDoctor = intent.getBooleanExtra(COMPANY_DOCTOR_KEY, false);

        String medRepName = intent.getStringExtra(MEDREP_NAME_KEY);

        if(medRepName ==null || medRepName.trim().length() == 0){
            medRepName = "Unknown";
        }

        ((TextView) findViewById(R.id.med_rep_name)).setText("MedRep Name: "+medRepName);

        int doctorID = intent.getIntExtra(DOCTOR_ID_KEY, -1);
        final int notificationID = intent.getIntExtra(NOTIFICATION_ID_KEY, -1);


        Bundle bundle = intent.getExtras();
        if (null!=bundle){
            String value=  bundle.getString(Constants.mParacetomolTitle);
            title.setText(value);
        }

        if(Utils.isNetworkAvailable(_activity) && doctorID > 0){
            DoctorProfileAsync doctorProfileAsync = new DoctorProfileAsync();
            doctorProfileAsync.execute(doctorID);
        }else{
            Toast.makeText(_activity, "Unable to get doctor information.", Toast.LENGTH_SHORT).show();
            finish();
        }

        findViewById(R.id.Schedule_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.isNetworkAvailable(PharmaDocterDetails.this)){
                    if(notificationID == -1){
                        Utils.DISPLAY_GENERAL_DIALOG(PharmaDocterDetails.this, "No Feedback", "No feedback available for this notification.");
                    }else{
                        GetDoctorNotificationStatsAsync getDoctorNotificationStatsAsync = new GetDoctorNotificationStatsAsync(PharmaDocterDetails.this);
                        getDoctorNotificationStatsAsync.execute(notificationID);
                    }
                }
            }
        });

        findViewById(R.id.docter_activity_score).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.DISPLAY_GENERAL_DIALOG(PharmaDocterDetails.this, "MedRep ", "<html>Please email us your request at /n info@erfolglifesciences.com /n or /n info@medrep.in.</html>");
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        PharmaNavigationDrawerFragment drawerFragment = (PharmaNavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);

    }

    /*@Override
    public void onClick(View v) {
        if(v.getId()== R.id.present_activityscore){

            Intent intent=new Intent(_activity,PharmaActivityScoreDetails.class);
            //pass
            //Doctor name
            //Therapeutic area
            //email id
            //phone number
            //location
            startActivity(intent);
        }

    }*/

    private class DoctorProfileAsync extends AsyncTask<Integer, Integer, DoctorProfile>{
        ProgressDialog pd;

        @Override
        protected DoctorProfile doInBackground(Integer... params) {
            String url = HttpUrl.PHARMA_GET_DOCTOR_PROFILE + params[0] + "?access_token=" + Utils.GET_ACCESS_TOKEN(PharmaDocterDetails.this);
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = parser.getJSON_Response(url, true);

            DoctorProfile doctorProfile = null;
            try {
                doctorProfile = (DoctorProfile) parser.jsonParser(jsonObject, DoctorProfile.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return doctorProfile;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(PharmaDocterDetails.this);
            pd.setTitle("Getting Doctor Profile");
            pd.setMessage("Please wait, while we retrieve Doctor profile.");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(final DoctorProfile doctorProfile) {
            super.onPostExecute(doctorProfile);

            if(pd != null && pd.isShowing()){
                pd.dismiss();
            }

            if(doctorProfile != null){
                String doctorName = doctorProfile.getFirstName() + " " + doctorProfile.getLastName();
                String therapeuticName = doctorProfile.getTherapeuticName();
                String emailID = doctorProfile.getEmailId();

                String alternateEmail = doctorProfile.getAlternateEmailId();

                if(alternateEmail != null && alternateEmail.trim().length() > 0){
                    emailID = emailID + "\n" + alternateEmail;
                }

                String mobileNumber = doctorProfile.getMobileNo();

                String phoneNumber =  doctorProfile.getPhoneNo();

                if(phoneNumber != null && phoneNumber.trim().length() > 0){
                    mobileNumber = mobileNumber + "\n" + phoneNumber;
                }

                ((TextView) findViewById(R.id.doctor_name_tv)).setText("Dr. "+doctorName);
                ((TextView) findViewById(R.id.therapitic_name)).setText(therapeuticName);
                ((TextView) findViewById(R.id.email_tv)).setText(emailID);
                ((TextView) findViewById(R.id.tv_mobile_value)).setText(mobileNumber);

                Button activityScoreButton = (Button)findViewById(R.id.present_activityscore);
                activityScoreButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PharmaActivityScoreDetails.DOCTOR_PROFILE = doctorProfile;

                        Intent intent=new Intent(_activity, PharmaActivityScoreDetails.class);
                        startActivity(intent);
                    }
                });

            }
        }
    }
}
