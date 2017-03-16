package pharma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.db.Company;
import com.app.db.DoctorProfile;
import com.app.fragments.PharmaNavigationDrawerFragment;
import com.app.util.HttpUrl;
import com.app.util.JSONParser;
import com.app.util.Utils;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import medrep.medrep.R;
import pharma.model.CompanyDoctor;

/**
 * Created by samara on 18/10/2015.
 */
public class PharmaActivityScoreDetails extends AppCompatActivity {

    public static DoctorProfile DOCTOR_PROFILE = null;
    public static CompanyDoctor COMPANY_DOCTOR = null;

    public static final String DISABLE_COMPETITIVE_ANALYSIS_KEY = "DisableCompetitiveAnalysis";

    //    TextView doc_name,doc_mobile_number,doc_email,doc_Tarea,doc_score;
//    TextView header_text;
//    Spinner doc_location;
//    ImageView back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pharma_activity_score_details_appoint);

//        Toast.makeText(this, "We're here", Toast.LENGTH_SHORT).show();

        Intent intent = getIntent();
        final boolean disableCompetitiveAnalysis = intent.getBooleanExtra(DISABLE_COMPETITIVE_ANALYSIS_KEY, false);

        String doctorName = "";
        String therapeuticName = "";
        String emailID = "";
        String alternateEmail = "";
        String mobileNumber = "";
        String phoneNumber = "";
        ArrayList<Company.Location> locations = new ArrayList<>();

        if(DOCTOR_PROFILE == null && COMPANY_DOCTOR == null){
            Toast.makeText(this, "Invalid doctor profile.", Toast.LENGTH_SHORT).show();
            finish();
        }else if(DOCTOR_PROFILE != null){
            ((TextView) findViewById(R.id.back_tv)).setText("Doctor Activity Score");
            doctorName = DOCTOR_PROFILE.getFirstName() + " " + DOCTOR_PROFILE.getLastName();
            therapeuticName = DOCTOR_PROFILE.getTherapeuticName();
            emailID = DOCTOR_PROFILE.getEmailId();
            alternateEmail = DOCTOR_PROFILE.getAlternateEmailId();
            mobileNumber = DOCTOR_PROFILE.getMobileNo();
            phoneNumber =  DOCTOR_PROFILE.getPhoneNo();
            locations = DOCTOR_PROFILE.getLocations();
        }else if(COMPANY_DOCTOR != null){
            ((TextView) findViewById(R.id.back_tv)).setText("Doctor Details");
            doctorName = COMPANY_DOCTOR.getFirstName() + " " + COMPANY_DOCTOR.getLastName();
            therapeuticName = COMPANY_DOCTOR.getTherapeuticName();
            emailID = COMPANY_DOCTOR.getEmailId();
            alternateEmail = COMPANY_DOCTOR.getAlternateEmailId();
            mobileNumber = COMPANY_DOCTOR.getMobileNo();
            phoneNumber =  COMPANY_DOCTOR.getPhoneNo();
            locations = COMPANY_DOCTOR.getLocations();
        }else{
            Toast.makeText(this, "Invalid doctor profile.", Toast.LENGTH_SHORT).show();
            finish();
        }

        if(alternateEmail != null && alternateEmail.trim().length() > 0){
            emailID = emailID + "\n" + alternateEmail;
        }

        if(phoneNumber != null && phoneNumber.trim().length() > 0){
            mobileNumber = mobileNumber + "\n" + phoneNumber;
        }

        ((TextView)findViewById(R.id.tv_doc_name)).setText(doctorName);
        ((TextView)findViewById(R.id.tv_mobile_name)).setText(mobileNumber);
        ((TextView)findViewById(R.id.tv_email_name)).setText(emailID);
        ((TextView)findViewById(R.id.tv_therapatic_name)).setText(therapeuticName);
        Spinner docLocationSpinner =(Spinner)findViewById(R.id.spinner_location);

//        Toast.makeText(this, "locations: " + locations.size(), Toast.LENGTH_SHORT).show();

        if(locations != null && locations.size() > 0){
            String[] locationsArray = new String[locations.size()];

            for(int i = 0; i < locations.size(); i++){
                /*cityStrings[i] = locations.get(i).getCity();*/
                Company.Location tempLocation = locations.get(i);
                locationsArray[i] = tempLocation.getAddress1() + "\n" +
                        tempLocation.getAddress2() + ", " +
                        tempLocation.getCity() + ", " +
                        tempLocation.getState() + ", " +
                        tempLocation.getCountry() + ", " +
                        tempLocation.getZipcode();

                Log.d("Location", locationsArray[i]);
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    R.layout.location_spinner_text, locationsArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            docLocationSpinner.setAdapter(adapter);
        }else{
            docLocationSpinner.setVisibility(View.GONE);
        }

        ImageView back_btn=(ImageView)findViewById(R.id.back);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(Utils.isNetworkAvailableWithOutDialog(this)){
            ActivityScoreAsync activityScoreAsync = new ActivityScoreAsync();
            //activityScoreAsync.execute(COMPANY_DOCTOR != null);
            activityScoreAsync.execute(true);
        }else{
            Toast.makeText(this, "No network available, Please try later.", Toast.LENGTH_SHORT).show();
            finish();
        }

        Button doctorCompAnalysisButton = (Button) findViewById(R.id.btn_doc_com_any);

        if(disableCompetitiveAnalysis){
            doctorCompAnalysisButton.setVisibility(View.GONE);
        }

        doctorCompAnalysisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     Utils.DISPLAY_GENERAL_DIALOG(PharmaActivityScoreDetails.this, "Sorry", "This is paid Feature.");

                Utils.DISPLAY_GENERAL_DIALOG(PharmaActivityScoreDetails.this, "MedRep", "Please email us your request at info@erfolglifesciences.com or info@medrep.in.");

            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        PharmaNavigationDrawerFragment drawerFragment = (PharmaNavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);


    }

    private class ActivityScoreAsync extends AsyncTask<Boolean, Void, ActivityScore>{

          ProgressDialog pd;
        @Override
        protected ActivityScore doInBackground(Boolean... params) {

            boolean isCompanyDoctor = params[0];

            String url;

            if(isCompanyDoctor){

                int doctorID = (COMPANY_DOCTOR == null)?DOCTOR_PROFILE.getDoctorId():COMPANY_DOCTOR.getDoctorId();

                url = HttpUrl.PHARMA_GET_COMPANY_ACTIVITY_SCORE +
                        doctorID +
                        "?access_token=" + Utils.GET_ACCESS_TOKEN(PharmaActivityScoreDetails.this);

               // Toast.makeText(PharmaActivityScoreDetails.this,  "raj"+COMPANY_DOCTOR.getDoctorId(), Toast.LENGTH_LONG).show();
            }else{
                url = HttpUrl.PHARMA_GET_NOTIFICATION_ACTIVITY_SCORE +
                        DOCTOR_PROFILE.getDoctorId() +
                        "?access_token=" + Utils.GET_ACCESS_TOKEN(PharmaActivityScoreDetails.this);
               /// Toast.makeText(PharmaActivityScoreDetails.this, "hello"+ COMPANY_DOCTOR.getDoctorId(), Toast.LENGTH_SHORT).show();
            }

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = parser.getJSON_Response(url, true);

            ActivityScore activityScore = null;
            try {
                activityScore = (ActivityScore) parser.jsonParser(jsonObject, ActivityScore.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return activityScore;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  Toast.makeText(PharmaActivityScoreDetails.this,"Getting data...",Toast.LENGTH_LONG).show();
            pd = new ProgressDialog(PharmaActivityScoreDetails.this);
            pd.setTitle("Getting Doctors");
            pd.setMessage("Please wait, while we retrieve your company doctors.");
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected void onPostExecute(ActivityScore activityScore) {
            super.onPostExecute(activityScore);

            if(pd != null && pd.isShowing()){
                pd.dismiss();
            }

            if(activityScore == null){
                Toast.makeText(PharmaActivityScoreDetails.this, "Invalid data found.", Toast.LENGTH_SHORT).show();
                PharmaActivityScoreDetails.this.finish();
            }

            ((TextView)findViewById(R.id.tv_total_score)).setText("Total Score " + activityScore.getTotalScore());

            //Display graph here.
            displayGraph(activityScore);
        }

        private void displayGraph(ActivityScore activityScore) {
            GraphView graph = (GraphView) findViewById(R.id.graph);
//            graph.setLabelFor(0);
//            graph.setRight(50);
//            graph.setTitle("Sample test");

            ActivityScore.Activities activities = activityScore.getActivities();

            BarGraphSeries<DataPoint> series = new BarGraphSeries<>(
                    new DataPoint[] {
                            new DataPoint(1, activities.getNotification()),
                            new DataPoint(2, activities.getSurvey()),
                            new DataPoint(3, activities.getAppointment()),
                            new DataPoint(4, activities.getFeedback()) });
            series.setSpacing(50);
            // draw values on top
            series.setDrawValuesOnTop(true);
            series.setValuesOnTopColor(Color.BLACK);

            graph.getViewport().setXAxisBoundsManual(true);

            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(series.getHighestValueX() + 1);


            graph.getViewport().setYAxisBoundsManual(true);

            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(series.getHighestValueY() + 10);

            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            staticLabelsFormatter.setHorizontalLabels(new String[]{"", "Notifications", "Surveys", "Appointments", "  Feedback", ""});

            GridLabelRenderer renderer = graph.getGridLabelRenderer();
            renderer.setLabelFormatter(staticLabelsFormatter);
            renderer.setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
            renderer.setHighlightZeroLines(false);
            renderer.setNumHorizontalLabels(6);
            renderer.setTextSize(25f);
            renderer.setPadding(10);
            renderer.setLabelsSpace(1);
            renderer.reloadStyles();


            graph.addSeries(series);
        }
    }

    private class ActivityScore{
        private int doctorId;
        private String doctorName;
        private Activities activities;
        private int totalScore;

        public int getDoctorId() {
            return doctorId;
        }

        public void setDoctorId(int doctorId) {
            this.doctorId = doctorId;
        }

        public String getDoctorName() {
            return doctorName;
        }

        public void setDoctorName(String doctorName) {
            this.doctorName = doctorName;
        }

        public Activities getActivities() {
            return activities;
        }

        public void setActivities(Activities activities) {
            this.activities = activities;
        }

        public int getTotalScore() {
            return totalScore;
        }

        public void setTotalScore(int totalScore) {
            this.totalScore = totalScore;
        }

        private class Activities{
            private int Survey;
            private int Appointment;
            private int Feedback;
            private int Notification;

            public int getSurvey() {
                return Survey;
            }

            public void setSurvey(int survey) {
                Survey = survey;
            }

            public int getAppointment() {
                return Appointment;
            }

            public void setAppointment(int appointment) {
                Appointment = appointment;
            }

            public int getFeedback() {
                return Feedback;
            }

            public void setFeedback(int feedback) {
                Feedback = feedback;
            }

            public int getNotification() {
                return Notification;
            }

            public void setNotification(int notification) {
                Notification = notification;
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        DOCTOR_PROFILE = null;
        COMPANY_DOCTOR = null;
    }

}
