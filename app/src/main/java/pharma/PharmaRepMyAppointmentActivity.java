package pharma;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.db.DoctorProfile;
import com.app.fragments.PharmaNavigationDrawerFragment;
import com.app.util.HttpUrl;
import com.app.util.JSONParser;
import com.app.util.Utils;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import medrep.medrep.R;
import pharma.model.Appointment;

import static com.app.util.Utils.makeRequest;

/**
 * Created by kishore on 2/11/15.
 */
public class    PharmaRepMyAppointmentActivity extends AppCompatActivity implements View.OnClickListener{

    public static Appointment APPOINTMENT;

    public static final String COMPLETED_APPOINTMENTS_KEY = "CompletedAppointments";
    public static final String UPCOMING_APPOINTMENTS_KEY = "UpcomingAppointments";
    private boolean upcomingAppointments;
    private boolean completedAppointments;

    TextView Appoint_notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (APPOINTMENT == null) {
            Toast.makeText(this, "Invalid Appointment.", Toast.LENGTH_SHORT).show();
            finish();
        }

        Intent intent = getIntent();

        completedAppointments = intent.getBooleanExtra(COMPLETED_APPOINTMENTS_KEY, false);
        upcomingAppointments = intent.getBooleanExtra(UPCOMING_APPOINTMENTS_KEY, false);
        final boolean pendingAppointments = !(completedAppointments || upcomingAppointments);

        System.out.println("completedAppointments: Samara" + completedAppointments);

        if (completedAppointments) {
            setContentView(R.layout.pharma_completed_appointments_manager);
            ((TextView) findViewById(R.id.back_tv)).setText("Completed Appointments");
            if (APPOINTMENT.getFeedback() != null && APPOINTMENT.getFeedback().trim().length() > 0) {
                ((TextView) findViewById(R.id.appointments_description)).setText(APPOINTMENT.getFeedback().trim());
            } else {
                findViewById(R.id.appointments_description_layout).setVisibility(View.GONE);
            }

            findViewById(R.id.viewbrocher).setOnClickListener(this);
        } else if (upcomingAppointments) {
            setContentView(R.layout.pharma_rep_upcoming_appointments);
            ((TextView) findViewById(R.id.back_tv)).setText("Upcoming Appointments");
            findViewById(R.id.Schedule_button).setOnClickListener(this);
            findViewById(R.id.viewbrocher).setOnClickListener(this);
        } else {
            setContentView(R.layout.pharma_pending_appointments_medrep);
            ((TextView) findViewById(R.id.back_tv)).setText("Pending Appointments");
            findViewById(R.id.viewbrocher).setOnClickListener(this);
        }

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        PharmaNavigationDrawerFragment drawerFragment = (PharmaNavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);


        Appoint_notes = (EditText) findViewById(R.id.appointment_notes);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((TextView) findViewById(R.id.doctor_name)).setText(APPOINTMENT.getDoctorName());
        ((TextView) findViewById(R.id.terapatic_name)).setText(APPOINTMENT.getTherapeuticName());
        ((TextView) findViewById(R.id.location_name)).setText(APPOINTMENT.getLocation());
        ((TextView) findViewById(R.id.company_name)).setText(APPOINTMENT.getCompanyname());
        ((TextView) findViewById(R.id.drug_name)).setText(APPOINTMENT.getTitle());

        String startDate = APPOINTMENT.getStartDate();

        if (startDate != null && startDate.length() > 12) {
            try {

                ((TextView) findViewById(R.id.month)).setText(
                        Utils.formatMonth(
                                Integer.parseInt(startDate.substring(4, 6))).
                                substring(0, 3).toUpperCase().toString());
        }catch(ParseException e){
            e.printStackTrace();
        }

            ((TextView) findViewById(R.id.date)).setText(startDate.substring(6, 8));
            ((TextView) findViewById(R.id.appointment_txt)).setText(startDate.substring(8, 10) + ":" + startDate.substring(10, 12));
        }


        if(pendingAppointments)
            findViewById(R.id.accept_appointment).setOnClickListener(this);

        if(Utils.isNetworkAvailable(this) && APPOINTMENT.getDoctorId() > 0){
            DoctorProfileAsync doctorProfileAsync = new DoctorProfileAsync();
            doctorProfileAsync.execute(APPOINTMENT.getDoctorId());
        }else{
            Toast.makeText(this, "Unable to get doctor information.", Toast.LENGTH_SHORT).show();
            finish();
        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        APPOINTMENT = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.accept_appointment:
                AlertDialog.Builder builder = new AlertDialog.Builder(PharmaRepMyAppointmentActivity.this)
                        .setTitle("Accept Appointment")
                        .setMessage("Are you sure  you want to Accept Appointment")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(Utils.isNetworkAvailable(PharmaRepMyAppointmentActivity.this)){
                                    AcceptAppointmentAsync acceptAppointmentAsync = new AcceptAppointmentAsync();
                                    acceptAppointmentAsync.execute();
                                }
                            }
                        })
                        .setNegativeButton("No", null);
                builder.show();
                break;
            case R.id.Schedule_button:

               // if ((EditText)findViewById(R.id.appointment_notes).getText().length() <= 0)
                    if (Appoint_notes.getText().toString().length() <= 0)
            {
                Toast.makeText(PharmaRepMyAppointmentActivity.this, "Appointment Notes can't be empty", Toast.LENGTH_SHORT).show();

                //   Appoint_notes.setError("Appointment Notes can't be empty");
            }else {

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(PharmaRepMyAppointmentActivity.this)
                                .setTitle("Message..!")
                                .setMessage("Are you sure this Appointment is completed?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        completeAppointment();
                                        //    PharmaRepMyAppointmentActivity.this.finish();
                                    }
                                })
                                .setNegativeButton("No", null);
                        builder1.show();
                    }
                break;
            case R.id.viewbrocher:
                Intent intent = new Intent(PharmaRepMyAppointmentActivity.this, PharmaBrochureActivity.class);
                intent.putExtra(PharmaBrochureActivity.NOTIFICATION_ID_KEY, APPOINTMENT.getNotificationId());
                startActivity(intent);
                break;
        }
    }

    private void completeAppointment() {

        Map<String, String> comment = new HashMap<String, String>();
        comment.put("appointmentId", APPOINTMENT.getAppointmentId() + "");
        comment.put("feedback", ((EditText) findViewById(R.id.appointment_notes)).getText() + "");
        comment.put("status", "Completed");

        final String json = new GsonBuilder().create().toJson(comment, Map.class);

        System.out.println("json: " + json);

        AsyncTask<Void, Void, JSONObject> async = new AsyncTask<Void, Void, JSONObject>() {

            ProgressDialog pd;

            @Override
            protected JSONObject doInBackground(Void... params) {

                final String url = HttpUrl.PHARMA_UPDATE_APPOINTMENT_COMPLETE + Utils.GET_ACCESS_TOKEN(PharmaRepMyAppointmentActivity.this);
                System.out.println("URL: " + url);

                HttpResponse response = makeRequest(url, json);

                String json_string = null;
                try {
                    json_string = EntityUtils.toString(response.getEntity());
                    JSONObject temp1 = new JSONObject(json_string);
                    System.out.println("temp1: " + temp1);
                    return temp1;

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                pd = new ProgressDialog(PharmaRepMyAppointmentActivity.this);
                pd.setTitle("Submitting Appointment Notes");
                pd.setMessage("Please wait for few seconds");
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }

                System.out.println("jsonObject: " + jsonObject);

                try {

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");


                    if (status.equalsIgnoreCase("ok") || message.equalsIgnoreCase("success")) {
                        Toast.makeText(PharmaRepMyAppointmentActivity.this, "Submitted Appointment Notes Successfully", Toast.LENGTH_SHORT).show();
                        setResult(PharmaRepMyAppointmentsActivity.APPOINTMENT_COMPLETED_RES_CODE);
                        PharmaRepMyAppointmentActivity.this.finish();
                    /*    AlertDialog.Builder builder2 = new AlertDialog.Builder(PharmaRepMyAppointmentActivity.this)
                                .setTitle("Message..!")
                                .setMessage("Submited Appointment Notes Successfully")
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        PharmaRepMyAppointmentActivity.this.finish();
                                        //    PharmaRepMyAppointmentActivity.this.finish();
                                    }
                                });
                              // .setNegativeButton("No", null);
                        builder2.show();*/

                    //    Utils.DISPLAY_GENERAL_DIALOG(PharmaRepMyAppointmentActivity.this, "Message..!", "Completed Appointment Successfully");


                    } else {

                     //   Utils.DISPLAY_GENERAL_DIALOG(PharmaRepMyAppointmentActivity.this, "Message..!", jsonObject.getString("message"));

//                        Toast.makeText(PharmaRepMyAppointmentActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            Utils.DISPLAY_GENERAL_DIALOG(PharmaRepMyAppointmentActivity.this, status, message);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        if(Utils.isNetworkAvailable(PharmaRepMyAppointmentActivity.this)) async.execute();

    }


    private class DoctorProfileAsync extends AsyncTask<Integer, Integer, DoctorProfile> {
        ProgressDialog pd;

        @Override
        protected DoctorProfile doInBackground(Integer... params) {
            String url = HttpUrl.PHARMA_GET_DOCTOR_PROFILE + params[0] +
                    "?access_token=" + Utils.GET_ACCESS_TOKEN(PharmaRepMyAppointmentActivity.this);
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

            pd = new ProgressDialog(PharmaRepMyAppointmentActivity.this);
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

                DoctorProfile.ProfilePicture profilePicture = doctorProfile.getProfilePicture();

                String pictureData = (profilePicture == null)?null:profilePicture.getData();

                if(pictureData != null && pictureData.trim().length() > 0){
                    Bitmap bmp = Utils.decodeBase64(pictureData);
                    if(bmp != null){
                        ((ImageView)findViewById(R.id.pharma_profilepic)).setImageBitmap(bmp);
                    }
                }



                Button activityScoreButton = (Button)findViewById(R.id.present_activityscore);
                activityScoreButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PharmaActivityScoreDetails.DOCTOR_PROFILE = doctorProfile;

                        Intent intent=new Intent(PharmaRepMyAppointmentActivity.this, PharmaActivityScoreDetails.class);
                        intent.putExtra(PharmaActivityScoreDetails.DISABLE_COMPETITIVE_ANALYSIS_KEY, completedAppointments || upcomingAppointments);
                        startActivity(intent);
                    }
                });

            }
        }
    }

    private class AcceptAppointmentAsync extends AsyncTask<Void, Void, Boolean>{
        ProgressDialog pd;

        @Override
        protected Boolean doInBackground(Void... params) {

            Map<String, String> comment = new HashMap<String, String>();
            comment.put("appointmentId", APPOINTMENT.getAppointmentId() +"");
            comment.put("status", "Accepted");
            String json = new GsonBuilder().create().toJson(comment, Map.class);

            System.out.println("json: " + json);

            String url = HttpUrl.PHARMA_ACCEPT_APPOINTMENT + Utils.GET_ACCESS_TOKEN(PharmaRepMyAppointmentActivity.this);


            HttpResponse response = Utils.makeRequest(url, json);

            String json_string = null;
            try {
                json_string = EntityUtils.toString(response.getEntity());
                JSONObject temp1 = new JSONObject(json_string);
                System.out.println("temp1: " + temp1);
                if(temp1.getString("status").equalsIgnoreCase("ok") || temp1.getString("message").equalsIgnoreCase("success")){
                    return true;
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(PharmaRepMyAppointmentActivity.this);
            pd.setTitle("Sending Acceptance");
            pd.setMessage("Please wait, while sending your acceptance.");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if(pd != null && pd.isShowing()){
                pd.dismiss();
            }

            if(result){
                Toast.makeText(PharmaRepMyAppointmentActivity.this, "Appointment has been accepted successfully.", Toast.LENGTH_SHORT).show();
                setResult(PharmaRepMyAppointmentsActivity.APPOINTMENT_ACCEPTED_RES_CODE);
                finish();
            }else{
                AlertDialog.Builder builder1 = new AlertDialog.Builder(PharmaRepMyAppointmentActivity.this)
                        .setTitle("Meeting conflict Exists")
                        .setMessage("You already have a meeting scheduled at this time. Accept anyway?")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                               // completeAppointment();
                                //    PharmaRepMyAppointmentActivity.this.finish();
                            }
                        })
                        .setNegativeButton("Cancel", null);
                builder1.show();
                //Utils.DISPLAY_GENERAL_DIALOG(PharmaRepMyAppointmentActivity.this, "Meeting conflict Exists", "You already have a meeting scheduled at this time. Accept anyway?");
            }


        }
    }
}
