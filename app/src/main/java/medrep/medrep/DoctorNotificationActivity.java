package medrep.medrep;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adapter.AppointmentModel;
import com.app.adapter.DoctorGroupListViewAdapter;
import com.app.adapter.MyAdapter;
import com.app.pojo.SignIn;
import com.app.task.DoctorPostMethods;

import org.json.JSONArray;

import java.util.ArrayList;

import pharma.AppointmentsAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class DoctorNotificationActivity extends AppCompatActivity implements View.OnClickListener{


    private ArrayList<AppointmentModel> appointmentModelArrayList = new ArrayList();
    private ArrayList appointmentDateList = new ArrayList();
    private ListView notificatonListView;
    private Context context;

    private AppointmentModel appointmentModel;
    private StickyListHeadersListView stickyList;
    private String companyName, createdDate, companyDesc, location, drugName;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_notification);
        context = this;
        HorizontalScrollView horizontalScroll = (HorizontalScrollView)findViewById(R.id.horizontalScroll);
        horizontalScroll.setVisibility(View.GONE);
        LinearLayout searchLayout = (LinearLayout)findViewById(R.id.searchLayout);
        searchLayout.setVisibility(View.GONE);
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(this);
        //findViewById(R.id.BackButton).setOnClickListener(this);
        stickyList = (StickyListHeadersListView) findViewById(R.id.stickyListViews);
        notificatonListView = (ListView)findViewById(R.id.notificatonListView);
        TextView title = (TextView)findViewById(R.id.title);
        if(getIntent().getExtras().getString("moreButton").equals("Appointment")) {
            title.setText("Meetings");
//            TextView heading = (TextView)findViewById(R.id.heading);
//            heading.setText("Appointments");
            stickyList.setVisibility(View.VISIBLE);
            notificatonListView.setVisibility(View.GONE);
            new GetAppointmentList().execute();
        } else if(getIntent().getExtras().getString("moreButton").equals("Notification")) {
            title.setText("Notifications");
            DoctorGroupListViewAdapter adapter = new DoctorGroupListViewAdapter(this, "notificationActivity");
            notificatonListView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.onBackClick:
                finish();
                break;
        }
    }

    public class GetAppointmentList extends AsyncTask<String, Void, String> {

        private Context context = getApplicationContext();
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(DoctorNotificationActivity.this);
            progressDialog.setMessage("Loading Meetingss..");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //        Log.d("TAG","my response------------------------------------ ");
            StringBuilder response = new StringBuilder();
            try {
                response.append(DoctorPostMethods.sendGet("http://122.175.50.252:8080/MedRepApplication/api/doctor" + "/getMyAppointment/20150101?access_token=" + SignIn.GET_ACCESS_TOKEN()));
                Log.d("TAG", "my response------------------------------------ " + response);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            try {
                JSONArray jsonObject = new JSONArray(o);
                // Getting JSON Array node

                // looping through All Contacts
                for (int i = 0; i < jsonObject.length(); i++) {
                    companyName = jsonObject.getJSONObject(i).getString("companyname");
                    createdDate = jsonObject.getJSONObject(i).getString("startDate");
                    companyDesc = jsonObject.getJSONObject(i).getString("appointmentDesc");
                    location = (jsonObject.getJSONObject(i).getString("location"));
                    drugName = (jsonObject.getJSONObject(i).getString("title"));
                    appointmentModel = new AppointmentModel();
                    appointmentModel.setAppointmentName(companyName);
                    appointmentModel.setAppointmentDate(createdDate);
                    appointmentModel.setAppoointmentDesc(companyDesc);
                    appointmentModel.setAppointmentLocation(location);
                    appointmentModel.setDrugName(drugName);
                    appointmentModelArrayList.add(appointmentModel);
                }

                MyAdapter stickyAdapter = new MyAdapter(appointmentModelArrayList, context);
                stickyList.setAdapter(stickyAdapter);
                stickyList.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
                    {
                        companyName =  appointmentModelArrayList.get(position).getAppointmentName();
                        createdDate = appointmentModelArrayList.get(position).getAppointmentDate();
                        companyDesc = appointmentModelArrayList.get(position).getAppoointmentDesc();
                        location = appointmentModelArrayList.get(position).getAppointmentLocation();
                        drugName = appointmentModelArrayList.get(position).getDrugName();
                        Intent intent = new Intent(DoctorNotificationActivity.this, AppointmentDetailsActivity.class);
                        intent.putExtra("companyName", companyName);
                        intent.putExtra("createdDate", createdDate);
                        intent.putExtra("companyDesc", companyDesc);
                        intent.putExtra("location", location);
                        intent.putExtra("drugName", drugName);

                        startActivity(intent);

                    }
                });

                progressDialog.dismiss();

            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
