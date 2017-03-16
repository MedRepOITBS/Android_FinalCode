package pharma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fragments.PharmaNavigationDrawerFragment;
import com.app.util.HttpUrl;
import com.app.util.JSONParser;
import com.app.util.Utils;

import org.json.JSONArray;

import java.util.ArrayList;

import medrep.medrep.R;
import pharma.model.Appointment;

/**
 * Created by kishore on 1/11/15.
 */
public class PharmaRepAppointments extends AppCompatActivity{

    public static final String APPOINTMENTS_TYPE_KEY = "AppointmentsType";

    public static ArrayList<Appointment> APPOINTMENTS;

    private boolean completedAppointments = false;
    private boolean upcomingAppointments = false;

    public static final class AppointmentsType{
        public static final String UPCOMING = "Upcoming";
        public static final String COMPLETED = "Completed";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pharma_converted_appiontment);
        System.out.println(APPOINTMENTS);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();

        String temp = intent.getStringExtra(APPOINTMENTS_TYPE_KEY);

        if(temp != null && temp.trim().length() > 0){
            if(temp.equalsIgnoreCase(AppointmentsType.COMPLETED)){
                ((TextView)findViewById(R.id.back_tv)).setText("Completed Appointments");
                completedAppointments = true;
            }else{
                ((TextView)findViewById(R.id.back_tv)).setText("Upcoming Appointments");
                upcomingAppointments = true;
            }
            setAppointments(APPOINTMENTS);
        }else{
            if(Utils.isNetworkAvailable(this)){
                GetTeamPendingAppointments getTeamPendingAppointments = new GetTeamPendingAppointments();
                getTeamPendingAppointments.execute();
            }else{
                Toast.makeText(this, "Lost network connectivity.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }



        /*GetTeamPendingAppoinments getTeamPendingAppoinments = new GetTeamPendingAppoinments();
        getTeamPendingAppoinments.execute();*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        PharmaNavigationDrawerFragment drawerFragment = (PharmaNavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        APPOINTMENTS = null;
    }

    private class GetTeamPendingAppointments extends AsyncTask<Void, Void, ArrayList<Appointment>> {
        ProgressDialog pd;

        @Override
        protected ArrayList<Appointment> doInBackground(Void... params) {
            String url = HttpUrl.PHARMA_GET_PHARMA_TEAM_PENDING_APPOINTMENTS + Utils.GET_ACCESS_TOKEN(PharmaRepAppointments.this);

            JSONParser parser = new JSONParser();
            JSONArray jsonArray = parser.getJSON_Response(url);

            ArrayList<Appointment> appointments = new ArrayList<>();

            parser.jsonParser(jsonArray, Appointment.class, appointments);

            return appointments;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(PharmaRepAppointments.this);
            pd.setTitle("Getting Appointments");
            pd.setMessage("Please wait, while we retrieve appointments for this notification.");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Appointment> appointments) {
            super.onPostExecute(appointments);

            if(pd != null && pd.isShowing()){
                pd.dismiss();
            }

            //Set appointments adapter
            setAppointments(appointments);


        }
    }

    private void setAppointments(final ArrayList<Appointment> appointments) {

        if(appointments == null || appointments.size() == 0){
            Toast.makeText(PharmaRepAppointments.this, "No Appointments Found.", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            ListView appointmentsLV = (ListView) findViewById(R.id.converted_appointments_lv);
            appointmentsLV.setAdapter(new AppointmentsAdapter(PharmaRepAppointments.this, appointments));

            appointmentsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent=new Intent(PharmaRepAppointments.this, PharmaRepAppointmentActivity.class);
                    Appointment appointment = (Appointment)parent.getItemAtPosition(position);
                    intent.putExtra(PharmaDocterDetails.MEDREP_NAME_KEY, appointment.getPharmaRepName());
                    intent.putExtra(PharmaDocterDetails.DOCTOR_ID_KEY, appointment.getDoctorId());
                    intent.putExtra(PharmaRepAppointmentActivity.START_DATE_KEY, appointment.getStartDate());
                    intent.putExtra(PharmaRepAppointmentActivity.DURATION, appointment.getDuration());
                    intent.putExtra(PharmaRepAppointmentActivity.TITLE_KEY, appointment.getTitle());
                    intent.putExtra(PharmaRepAppointmentActivity.LOCATION_KEY, appointment.getLocation());
                    intent.putExtra(PharmaRepAppointmentActivity.DOCTOR_NAME, appointment.getDoctorName());
                    intent.putExtra(PharmaRepAppointmentActivity.THERAPEUTIC_NAME, appointment.getTherapeuticName());
                    intent.putExtra(PharmaRepAppointmentActivity.DESCRIPTION, appointment.getAppointmentDesc());
                    intent.putExtra(PharmaBrochureActivity.NOTIFICATION_ID_KEY, appointment.getNotificationId());

                    intent.putExtra(Constants.mParacetomolTitle, Constants.mPharmaDocterDetails);

                    intent.putExtra(PharmaRepAppointmentActivity.COMPLETED_APPOINTMENTS_KEY, completedAppointments);
                    intent.putExtra(PharmaRepAppointmentActivity.UPCOMING_APPOINTMENTS_KEY, upcomingAppointments);
                    startActivity(intent);
                }
            });
        }
    }
}
