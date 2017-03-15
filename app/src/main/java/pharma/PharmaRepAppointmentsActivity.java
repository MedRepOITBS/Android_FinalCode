package pharma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fragments.PharmaNavigationDrawerFragment;
import com.app.pojo.SignIn;
import com.app.util.HttpUrl;
import com.app.util.JSONParser;
import com.app.util.Utils;

import org.json.JSONArray;

import java.util.ArrayList;

import medrep.medrep.R;
import pharma.model.Appointment;

/**
 * Created by kishore on 4/11/15.
 */
public class PharmaRepAppointmentsActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayList<Appointment> completedAppointments = new ArrayList<>();
    ArrayList<Appointment> upcomingAppointments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharma_medrep_dashboard);

        Utils.SET_PHARMA_COMPANY_LOGO(this, (ImageView) findViewById(R.id.p_company_name));

        Intent intent = getIntent();

        final int repID = intent.getIntExtra(PharmaRepDetails.REP_ID_KEY, -1);

        System.out.println("RepID: " + repID);

        if(repID == -1){
            Toast.makeText(this, "Invalid profile.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if(Utils.isNetworkAvailable(this)){
            GetRepAppointmentsAsync getRepAppointmentsAsync = new GetRepAppointmentsAsync();
            getRepAppointmentsAsync.execute(repID);
        }else{
            //internet is not available.
        }


        TextView title = (TextView) findViewById(R.id.back_tv);
        title.setText("MedRep");

        findViewById(R.id.upcomming).setOnClickListener(this);
        findViewById(R.id.completed).setOnClickListener(this);

        findViewById(R.id.pending).setVisibility(View.GONE);


        findViewById(R.id.back).setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        PharmaNavigationDrawerFragment drawerFragment = (PharmaNavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()){
            case R.id.upcomming:
                if(upcomingAppointments != null && upcomingAppointments.size() > 0){
                    intent = new Intent(PharmaRepAppointmentsActivity.this, PharmaRepAppointments.class);
                    intent.putExtra(PharmaRepAppointments.APPOINTMENTS_TYPE_KEY, PharmaRepAppointments.AppointmentsType.UPCOMING);
                    PharmaRepAppointments.APPOINTMENTS = upcomingAppointments;
                    startActivity(intent);
                }else{
                    Toast.makeText(PharmaRepAppointmentsActivity.this, "No appointments found in this category", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.completed:
                if(completedAppointments != null && completedAppointments.size() > 0){
                    intent = new Intent(PharmaRepAppointmentsActivity.this, PharmaRepAppointments.class);
                    intent.putExtra(PharmaRepAppointments.APPOINTMENTS_TYPE_KEY, PharmaRepAppointments.AppointmentsType.COMPLETED);
                    PharmaRepAppointments.APPOINTMENTS = completedAppointments;
                    startActivity(intent);
                }else{
                    Toast.makeText(PharmaRepAppointmentsActivity.this, "No appointments found in this category", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.back:
                finish();
                break;
        }
    }


    private class GetRepAppointmentsAsync  extends AsyncTask<Integer, Void, ArrayList<Appointment>>{
        ProgressDialog pd;

        @Override
        protected ArrayList<Appointment> doInBackground(Integer... params) {
            String url = HttpUrl.PHARMA_GET_APPOINTMENTS_BY_REP + params[0] + "?access_token=" + SignIn.GET_ACCESS_TOKEN();

            JSONParser parser = new JSONParser();
            JSONArray jsonArray = parser.getJSON_Response(url);

            ArrayList<Appointment> appointments = new ArrayList<>();

            parser.jsonParser(jsonArray, Appointment.class, appointments);

            return appointments;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(PharmaRepAppointmentsActivity.this);
            pd.setTitle("Getting Appointments");
            pd.setMessage("Please wait, while we retrieve your team member's appointments.");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Appointment> appointments) {
            super.onPostExecute(appointments);

            if(pd != null && pd.isShowing()){
                pd.dismiss();
            }

            if(appointments == null || appointments.size() == 0){
//                Toast.makeText(PharmaRepAppointmentsActivity.this, "No Appointments Found.", Toast.LENGTH_SHORT).show();
//                finish();
            }else{
                //Set appointments adapter
                for(Appointment appointment: appointments){
                    if(appointment.getStatus().equalsIgnoreCase("Completed")){
                        completedAppointments.add(appointment);
                    }else{
                        upcomingAppointments.add(appointment);
                    }
                }
            }
        }
    }
}
