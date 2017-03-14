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
 * Created by kishore on 2/11/15.
 */
public class PharmaRepMyAppointmentsActivity extends AppCompatActivity{

    public static final String APPOINTMENTS_TYPE_KEY = "AppointmentsType";

    private int appointmentsType = -1;

    public class AppointmentsType{
        public static final int Completed = 0;
        public static final int Upcoming = 1;
        public static final int Pending = 2;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == OPEN_APPOINTMENT_REQ_CODE && resultCode == APPOINTMENT_ACCEPTED_RES_CODE){
            if(SELECTED_APPOINTMENT_POS != -1 && appointments != null && appointments.size() > SELECTED_APPOINTMENT_POS && appointmentsAdapter != null){
                appointments.remove(SELECTED_APPOINTMENT_POS);
                SELECTED_APPOINTMENT_POS = -1;
                appointmentsAdapter.notifyDataSetChanged();

                if(appointments.size() == 0){
                    Toast.makeText(PharmaRepMyAppointmentsActivity.this, "No pending appointments.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }else if(requestCode == OPEN_APPOINTMENT_REQ_CODE && resultCode == APPOINTMENT_COMPLETED_RES_CODE){
            if(SELECTED_APPOINTMENT_POS != -1 && appointments != null && appointments.size() > SELECTED_APPOINTMENT_POS && appointmentsAdapter != null){
                appointments.remove(SELECTED_APPOINTMENT_POS);

                SELECTED_APPOINTMENT_POS = -1;
                appointmentsAdapter.notifyDataSetChanged();

                if(appointments.size() == 0){
                    Toast.makeText(PharmaRepMyAppointmentsActivity.this, "No upcoming appointments.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    private static final int OPEN_APPOINTMENT_REQ_CODE = 100;
    public static final int APPOINTMENT_ACCEPTED_RES_CODE = 101;
    public static final int APPOINTMENT_COMPLETED_RES_CODE = 102;
    private static int SELECTED_APPOINTMENT_POS = -1;
    private AppointmentsAdapter appointmentsAdapter;
    private ArrayList<Appointment> appointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pharma_converted_appiontment);


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        appointmentsType = getIntent().getIntExtra(APPOINTMENTS_TYPE_KEY, -1);

        switch (appointmentsType){
            case AppointmentsType.Completed:
                ((TextView) findViewById(R.id.back_tv)).setText("Completed Appointments");
                break;
            case AppointmentsType.Upcoming:
                ((TextView) findViewById(R.id.back_tv)).setText("Upcoming Appointments");
                break;
            case AppointmentsType.Pending:
                ((TextView) findViewById(R.id.back_tv)).setText("Pending Appointments");
                break;
        }

        if(Utils.isNetworkAvailable(this)){
            GetMyPendingAppointmentsAsync getMyPendingAppointmentsAsync = new GetMyPendingAppointmentsAsync();
            getMyPendingAppointmentsAsync.execute();
        }else{
            Toast.makeText(this, "Lost network connectivity.", Toast.LENGTH_SHORT).show();
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        PharmaNavigationDrawerFragment drawerFragment = (PharmaNavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);
    }

    private class GetMyPendingAppointmentsAsync extends AsyncTask<Void, Void, ArrayList<Appointment>>{
        ProgressDialog pd;

        @Override
        protected ArrayList<Appointment> doInBackground(Void... params) {

            String baseUrl = "";

            switch (appointmentsType){
                case AppointmentsType.Completed:
                    baseUrl = HttpUrl.PHARMA_GET_MY_COMPLETED_APPOINTMENTS;
                    break;
                case AppointmentsType.Upcoming:
                    baseUrl = HttpUrl.PHARMA_GET_MY_UPCOMING_APPOINTMENTS;
                    break;
                case AppointmentsType.Pending:
                    baseUrl = HttpUrl.PHARMA_GET_MY_PENDING_APPOINTMENTS;
                    break;
            }

            if(baseUrl != null && baseUrl.trim().length() > 0){
                String url = baseUrl + Utils.GET_ACCESS_TOKEN(PharmaRepMyAppointmentsActivity.this);

                JSONParser parser = new JSONParser();
                JSONArray jsonArray = parser.getJSON_Response(url);

                ArrayList<Appointment> appointments = new ArrayList<>();

                parser.jsonParser(jsonArray, Appointment.class, appointments);

                return appointments;
            }else{
                finish();
                return null;
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(PharmaRepMyAppointmentsActivity.this);
            pd.setTitle("Getting Appointments");
            pd.setMessage("Please wait, while we retrieve your pending appointments.");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Appointment> tempAppointments) {
            super.onPostExecute(tempAppointments);

            if(pd != null && pd.isShowing()){
                pd.dismiss();
            }

            if(tempAppointments == null || tempAppointments.size() == 0){
                Toast.makeText(PharmaRepMyAppointmentsActivity.this, "No Appointments Found.", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                //Set appointments adapter
                ListView appointmentsLV = (ListView) findViewById(R.id.converted_appointments_lv);
                PharmaRepMyAppointmentsActivity.this.appointments = tempAppointments;
                appointmentsAdapter = new AppointmentsAdapter(PharmaRepMyAppointmentsActivity.this, appointments);

                appointmentsLV.setAdapter(appointmentsAdapter);

                appointmentsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Appointment appointment = (Appointment) parent.getItemAtPosition(position);
                        PharmaRepMyAppointmentActivity.APPOINTMENT = appointment;
                        SELECTED_APPOINTMENT_POS = position;

                        Intent intent = new Intent(PharmaRepMyAppointmentsActivity.this, PharmaRepMyAppointmentActivity.class);
                        intent.putExtra("Position", position);
                        intent.putExtra(PharmaRepAppointmentActivity.COMPLETED_APPOINTMENTS_KEY, appointmentsType == AppointmentsType.Completed);
                        intent.putExtra(PharmaRepAppointmentActivity.UPCOMING_APPOINTMENTS_KEY, appointmentsType == AppointmentsType.Upcoming);

                        if(appointmentsType == AppointmentsType.Pending){
                            startActivityForResult(intent, OPEN_APPOINTMENT_REQ_CODE);
                        }else if(appointmentsType == AppointmentsType.Upcoming){
                            startActivityForResult(intent, OPEN_APPOINTMENT_REQ_CODE);
                        }else{
                            startActivity(intent);
                        }
                    }
                });
            }
        }
    }
}
