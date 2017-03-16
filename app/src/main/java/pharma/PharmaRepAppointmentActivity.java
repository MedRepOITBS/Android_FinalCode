package pharma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.db.DoctorProfile;
import com.app.fragments.PharmaNavigationDrawerFragment;
import com.app.util.HttpUrl;
import com.app.util.JSONParser;
import com.app.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

import medrep.medrep.R;

/**
 * Created by kishore on 1/11/15.
 */
public class PharmaRepAppointmentActivity extends AppCompatActivity{

    public static final String START_DATE_KEY = "StartDate";
    public static final String COMPANY_NAME_KEY = "CompanyName";

    public static final String COMPLETED_APPOINTMENTS_KEY = "CompletedAppointments";
    public static final String UPCOMING_APPOINTMENTS_KEY = "UpcomingAppointments";
    public static final String DURATION = "duration";
    public static final String TITLE_KEY = "Title";
    public static final String LOCATION_KEY = "Location";
    public static final String DOCTOR_NAME = "DoctorName";
    public static final String THERAPEUTIC_NAME = "TherapeuticName";
    public static final String DESCRIPTION = "Description";
    private boolean completedAppointments;
    private boolean upcomingAppointments;

    private ImageView pharmaProfilePic;
    private DoctorProfile doctorProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        completedAppointments = intent.getBooleanExtra(COMPLETED_APPOINTMENTS_KEY, false);
        upcomingAppointments = intent.getBooleanExtra(UPCOMING_APPOINTMENTS_KEY, false);

        System.out.println("completedAppointments: " + completedAppointments);

        if(completedAppointments){
            setContentView(R.layout.pharma_completed_appointments_manager);

        }else if(upcomingAppointments){
            setContentView(R.layout.pharma_completed_appointments_manager);
        }else{
            setContentView(R.layout.pharma_pending_appointment_manager);
        }
        String startDateValue = intent.getStringExtra(START_DATE_KEY);
        String duration = intent.getStringExtra(DURATION);

        final int doctorID = intent.getIntExtra(PharmaDocterDetails.DOCTOR_ID_KEY, -1);
        String startDate = intent.getStringExtra(START_DATE_KEY);
        String companyName = intent.getStringExtra(COMPANY_NAME_KEY);
        String title = intent.getStringExtra(TITLE_KEY);
        String location = intent.getStringExtra(LOCATION_KEY);
        String doctorName = intent.getStringExtra(DOCTOR_NAME);
        String therapeuticName = intent.getStringExtra(THERAPEUTIC_NAME);


        ((TextView) findViewById(R.id.doctor_name)).setText(doctorName);
        ((TextView) findViewById(R.id.terapatic_name)).setText(therapeuticName);
        try {
            ((TextView) findViewById(R.id.month)).setText(Utils.formatMonth(Integer.parseInt(startDate.substring(4, 6))).substring(0, 3));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ((TextView) findViewById(R.id.date)).setText(startDate.substring(6, 8));

        String time = startDate.substring(8, 10) + ":" + startDate.substring(10, 12);
        String date = startDate.substring(0, 4) + "-" + startDate.substring(4, 6) + "-" + startDate.substring(6, 8);

        ((TextView) findViewById(R.id.appointment_txt)).setText(time);
        ((TextView) findViewById(R.id.location_name)).setText(location);
        ((TextView) findViewById(R.id.company_name)).setText(companyName);
        if(completedAppointments || upcomingAppointments) {
            ((TextView) findViewById(R.id.drug_name)).setText(title);
            ((TextView) findViewById(R.id.appointments_description)).setText(date + " at " + time);
        }
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pharmaProfilePic = (ImageView) findViewById(R.id.pharma_profilepic);

        if(Utils.isNetworkAvailable(PharmaRepAppointmentActivity.this) && doctorID != -1){
            DoctorProfileAsync doctorProfileAsync = new DoctorProfileAsync();
            doctorProfileAsync.execute(doctorID);
        }else{
            Toast.makeText(PharmaRepAppointmentActivity.this, "Unable to get activity score.", Toast.LENGTH_SHORT).show();
            finish();
        }

        Button activityScoreButton = (Button) findViewById(R.id.present_activityscore);
        activityScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doctorProfile != null) {
                    PharmaActivityScoreDetails.DOCTOR_PROFILE = doctorProfile;

                    Intent intent = new Intent(PharmaRepAppointmentActivity.this, PharmaActivityScoreDetails.class);
//                    intent.putExtra(PharmaActivityScoreDetails.DISABLE_COMPETITIVE_ANALYSIS_KEY, completedAppointments || upcomingAppointments);
                    /*Change by kishore on Nov 7 2015
                    * Disable DISABLE_COMPETITIVE_ANALYSIS for pending appointments in manager
                    * */
                    intent.putExtra(PharmaActivityScoreDetails.DISABLE_COMPETITIVE_ANALYSIS_KEY, completedAppointments || upcomingAppointments);
                    startActivity(intent);
                } else {
                    Toast.makeText(PharmaRepAppointmentActivity.this, "Unable to show activity score.", Toast.LENGTH_SHORT).show();
                }


            }
        });


        final int notificationID = intent.getIntExtra(PharmaBrochureActivity.NOTIFICATION_ID_KEY, -1);

        findViewById(R.id.viewbrocher).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(PharmaRepAppointmentActivity.this, PharmaBrochureActivity.class);
                intent2.putExtra(PharmaBrochureActivity.NOTIFICATION_ID_KEY, notificationID);
                startActivity(intent2);
            }
        });



        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        PharmaNavigationDrawerFragment drawerFragment = (PharmaNavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);
    }

    private class DoctorProfileAsync extends AsyncTask<Integer, Integer, DoctorProfile>{
        ProgressDialog pd;

        @Override
        protected DoctorProfile doInBackground(Integer... params) {
            String url = HttpUrl.PHARMA_GET_DOCTOR_PROFILE + params[0] + "?access_token=" + Utils.GET_ACCESS_TOKEN(PharmaRepAppointmentActivity.this);
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

            pd = new ProgressDialog(PharmaRepAppointmentActivity.this);
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

                PharmaRepAppointmentActivity.this.doctorProfile = doctorProfile;


                DoctorProfile.ProfilePicture profilePicture = doctorProfile.getProfilePicture();

                String pictureData = (profilePicture == null)?null:profilePicture.getData();
                if(pictureData != null && pictureData.trim().length() > 0){
                    Bitmap bitmap = Utils.decodeBase64(pictureData);
                    if (bitmap != null) {
                        pharmaProfilePic.setImageBitmap(bitmap);
                    }
                }


            }
        }
    }

}
