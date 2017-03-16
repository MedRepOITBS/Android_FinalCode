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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.db.MedRepDatabaseHandler;
import com.app.fragments.PharmaNavigationDrawerFragment;
import com.app.pharma.PharmaNotification;
import com.app.util.HttpUrl;
import com.app.util.JSONParser;
import com.app.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import medrep.medrep.R;


/**
 * Created by admin on 9/27/2015.
 */
public class PharmaCampainDetails extends AppCompatActivity implements View.OnClickListener {

    public static final String CAMPAIGN_NAME_KEY = "CampaignName";


    PharmaCampainDetails _activity;

    ImageView trackCompaingn;
    ImageView pcompanyname;
    LinearLayout layout_converted_to_appionement;
    private TextView convertedToApptTV;
    private TextView sentNumberTV;
    private TextView receivedNumberTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharma_compain_details);
        _activity=this;

        Intent intent = getIntent();

        String campaignName = intent.getStringExtra(CAMPAIGN_NAME_KEY);
        String titleValue = intent.getStringExtra("title");
       // System.out.println("campaignName " + campaignName);


        TextView title = (TextView) findViewById(R.id.back_tv);
        convertedToApptTV = (TextView) findViewById(R.id.converted_to_appt_tv);
        sentNumberTV = (TextView) findViewById(R.id.p_sentnumber);
        receivedNumberTV = (TextView) findViewById(R.id.tv_recivednumer);

        title.setText(titleValue);

        if(Utils.isNetworkAvailableWithOutDialog(PharmaCampainDetails.this)){
            NotificationStatsAsync notificationStatsAsync = new NotificationStatsAsync();
            notificationStatsAsync.execute();
        }else{
            convertedToApptTV.setText(PharmaNotificationDetails.CURRENT_NOTIFICATION.getTotalConvertedToAppointment() + "");
            sentNumberTV.setText(PharmaNotificationDetails.CURRENT_NOTIFICATION.getTotalSentNotification() + "");
            receivedNumberTV.setText(PharmaNotificationDetails.CURRENT_NOTIFICATION.getTotalViewedNotifcation() + "");
        }

        layout_converted_to_appionement=(LinearLayout)findViewById(R.id.layout_converted_to_appionement);
        layout_converted_to_appionement.setOnClickListener(_activity);

        final int notificationID = PharmaNotificationDetails.CURRENT_NOTIFICATION.getNotificationId();

        findViewById(R.id.Schedule_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkAvailable(PharmaCampainDetails.this)) {
                    if (notificationID == -1) {
                        Utils.DISPLAY_GENERAL_DIALOG(PharmaCampainDetails.this, "No Feedback", "No feedback available for this notification.");
                    } else {
                        GetDoctorNotificationStatsAsync getDoctorNotificationStatsAsync = new GetDoctorNotificationStatsAsync(PharmaCampainDetails.this);
                        getDoctorNotificationStatsAsync.execute(notificationID);
                    }
                }
            }
        });

        ImageView backIV = (ImageView) findViewById(R.id.back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


       /* pcompanyname=(ImageView)findViewById(R.id.p_company_name);
        pcompanyname.setOnClickListener(_activity);*/

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        PharmaNavigationDrawerFragment drawerFragment = (PharmaNavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.layout_converted_to_appionement){
            Intent intent=new Intent(_activity, PharmaConvertedToAppiontment.class);
            intent.putExtra(Constants.mParacetomolTitle, Constants.mConvertedToappointment);
            intent.putExtra(PharmaConvertedToAppiontment.NOTIFICATION_ID_KEY, PharmaNotificationDetails.CURRENT_NOTIFICATION.getNotificationId());
            startActivity(intent);
        }

    }

    private class NotificationStatsAsync extends AsyncTask<String, Integer, NotificationStats>{

        ProgressDialog pd;

        @Override
        protected NotificationStats doInBackground(String... params) {

            int notificationID = PharmaNotificationDetails.CURRENT_NOTIFICATION.getNotificationId();

            String url = HttpUrl.PHARMA_GET_NOTIFICATION_STATS +
                    notificationID +
                    "?access_token=" + Utils.GET_ACCESS_TOKEN(PharmaCampainDetails.this);

            System.out.println("Url: " + url);

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = parser.getJSON_Response(url, true);

            NotificationStats notificationStats = null;
            try {
                notificationStats = (NotificationStats) parser.jsonParser(jsonObject, NotificationStats.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            MedRepDatabaseHandler.getInstance(PharmaCampainDetails.this).updatePharmaNotification(notificationStats);
            return notificationStats;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(PharmaCampainDetails.this);
            pd.setTitle("Getting Stats");
            pd.setMessage("Please wait, while we retrieve notification stats.");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(NotificationStats notificationStats) {
            super.onPostExecute(notificationStats);

            if(pd != null && pd.isShowing()){
                pd.dismiss();
            }

            convertedToApptTV.setText(notificationStats.getTotalConvertedToAppointment() + "");
            sentNumberTV.setText(notificationStats.getTotalSentNotification() + "");
            receivedNumberTV.setText(notificationStats.getTotalViewedNotifcation() + "");
        }

    }

    public class NotificationStats{
        private int notificationId;
        private String notificationName;
        private int totalSentNotification;
        private int totalPendingNotifcation;
        private int totalViewedNotifcation;
        private int totalConvertedToAppointment;

        public int getNotificationId() {
            return notificationId;
        }

        public void setNotificationId(int notificationId) {
            this.notificationId = notificationId;
        }

        public String getNotificationName() {
            return notificationName;
        }

        public void setNotificationName(String notificationName) {
            this.notificationName = notificationName;
        }

        public int getTotalSentNotification() {
            return totalSentNotification;
        }

        public void setTotalSentNotification(int totalSentNotification) {
            this.totalSentNotification = totalSentNotification;
        }

        public int getTotalPendingNotifcation() {
            return totalPendingNotifcation;
        }

        public void setTotalPendingNotifcation(int totalPendingNotifcation) {
            this.totalPendingNotifcation = totalPendingNotifcation;
        }

        public int getTotalViewedNotifcation() {
            return totalViewedNotifcation;
        }

        public void setTotalViewedNotifcation(int totalViewedNotifcation) {
            this.totalViewedNotifcation = totalViewedNotifcation;
        }

        public int getTotalConvertedToAppointment() {
            return totalConvertedToAppointment;
        }

        public void setTotalConvertedToAppointment(int totalConvertedToAppointment) {
            this.totalConvertedToAppointment = totalConvertedToAppointment;
        }
    }
}
