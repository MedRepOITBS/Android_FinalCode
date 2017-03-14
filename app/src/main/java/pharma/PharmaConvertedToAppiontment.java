package pharma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.db.DoctorProfile;
import com.app.db.MedRepDatabaseHandler;
import com.app.fragments.PharmaNavigationDrawerFragment;
import com.app.util.HttpUrl;
import com.app.util.JSONParser;
import com.app.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import medrep.medrep.R;
import pharma.model.Appointment;

/**
 * Created by admin on 10/10/2015.
 */
public class PharmaConvertedToAppiontment extends AppCompatActivity  {

    PharmaConvertedToAppiontment _activity;

    public static final String NOTIFICATION_ID_KEY = "NotificationID";

    private int currentNotificationID = -1;

//    LinearLayout convretedAppaionmtneListItme;


    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharma_converted_appiontment);
        _activity=this;

//        convretedAppaionmtneListItme=(LinearLayout)findViewById(R.id.list_itemm_convetedview);
        title=(TextView)findViewById(R.id.back_tv);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        currentNotificationID = intent.getIntExtra(NOTIFICATION_ID_KEY, -1);

        Bundle bundle = intent.getExtras();
        if (null!=bundle){
            String value=  bundle.getString(Constants.mParacetomolTitle);
            title.setText(value);
        }

        if(Utils.isNetworkAvailable(_activity) && currentNotificationID != -1){
            AppointmentsAsync appointmentsAsync = new AppointmentsAsync();
            appointmentsAsync.execute();
        }else{
            Toast.makeText(_activity, "Lost network connectivity.", Toast.LENGTH_SHORT).show();
            finish();
        }

//        convretedAppaionmtneListItme.setOnClickListener(_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        PharmaNavigationDrawerFragment drawerFragment = (PharmaNavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);


    }


    public class AppointmentsAsync extends AsyncTask<Void, Integer, ArrayList<Appointment>>{
        ProgressDialog pd;

        @Override
        protected ArrayList<Appointment> doInBackground(Void... params) {

            String url = HttpUrl.PHARMA_GET_APPOINTMENTS_BY_NOTIFICATION + currentNotificationID +
                    "?access_token=" + Utils.GET_ACCESS_TOKEN(PharmaConvertedToAppiontment.this);
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = parser.getJSON_Response(url);

            ArrayList<Appointment> appointments = new ArrayList<>();

            parser.jsonParser(jsonArray, Appointment.class, appointments);

            return appointments;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(PharmaConvertedToAppiontment.this);
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

            if(appointments == null || appointments.size() == 0){
                Toast.makeText(_activity, "No Appointments Found.", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                //Set appointments adapter
                ListView appointmentsLV = (ListView) findViewById(R.id.converted_appointments_lv);
                appointmentsLV.setAdapter(new AppointmentsAdapter(_activity, appointments));

                appointmentsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent=new Intent(_activity, PharmaDocterDetails.class);
                        Appointment appointment = (Appointment)parent.getItemAtPosition(position);
                        intent.putExtra(PharmaDocterDetails.MEDREP_NAME_KEY, appointment.getPharmaRepName());
                        intent.putExtra(PharmaDocterDetails.DOCTOR_ID_KEY, appointment.getDoctorId());
                        intent.putExtra(PharmaDocterDetails.NOTIFICATION_ID_KEY, currentNotificationID);
                        intent.putExtra(Constants.mParacetomolTitle, Constants.mPharmaDocterDetails);
                        startActivity(intent);
                    }
                });
            }

        }
    }

}

