package medrep.medrep;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fragments.DoctorNavigationDrawerFragment;
import com.app.fragments.PharmaNavigationDrawerFragment;
import com.app.util.HttpUrl;
import com.app.util.JSONParser;
import com.app.util.Utils;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kishore on 4/11/15.
 */
public class DoctorMyActivityScore extends AppCompatActivity{
RelativeLayout lin_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.doctor_activityscore);


        lin_back=(RelativeLayout)findViewById(R.id.lin_back);
        lin_back.setBackgroundColor(getResources().getColor(R.color.notification_header_color));
        if(Utils.isNetworkAvailable(this)){
            GetMyActivityScore getMyActivityScore = new GetMyActivityScore();
            getMyActivityScore.execute();
        }

        ((TextView)findViewById(R.id.back_tv)).setText("Activity Scores");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        DoctorNavigationDrawerFragment drawerFragment = (DoctorNavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);
    }

    private void displayGraph(ActivityScore activityScore) {
        GraphView graph = (GraphView) findViewById(R.id.graph);

        graph.removeAllSeries();


//        graph.setLabelFor(0);
//        graph.setRight(50);
        graph.setTitle(activityScore.getCompanyName());
        graph.setTitleColor(Color.BLUE);

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
        staticLabelsFormatter.setHorizontalLabels(new String[]{"", "Notifications", "Surveys", "Appointments", "Feedback", ""});

        GridLabelRenderer renderer = graph.getGridLabelRenderer();
        renderer.setLabelFormatter(staticLabelsFormatter);
        renderer.setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        renderer.setHighlightZeroLines(false);
        renderer.setNumHorizontalLabels(6);
        renderer.setTextSize(25f);
        renderer.setHorizontalLabelsColor(Color.BLACK);
        renderer.setLabelsSpace(3);

        renderer.setGridColor(Color.CYAN);
        renderer.reloadStyles();



        graph.addSeries(series);
    }



    private class GetMyActivityScore extends AsyncTask<Void, Void, ArrayList<ActivityScore>>{

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(DoctorMyActivityScore.this);
            pd.setTitle("Retrieving Activity Score");
            pd.setMessage("Please wait while we retrieve your activity score.");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(final ArrayList<ActivityScore> activityScores) {
            super.onPostExecute(activityScores);

            if(pd != null && pd.isShowing()){
                pd.dismiss();
            }

            if(activityScores == null || activityScores.size() == 0){
                Toast.makeText(DoctorMyActivityScore.this, "No activities found.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            System.out.println("activityScores.size(): " + activityScores.size());

            final ListView companiesListLV = (ListView) findViewById(R.id.companies_list);
            companiesListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ActivityScore activityScore = (ActivityScore) parent.getItemAtPosition(position);
                    displayGraph(activityScore);
                }
            });

            CompaniesAdapter companiesAdapter = new CompaniesAdapter(activityScores);
            companiesListLV.setAdapter(companiesAdapter);

            companiesListLV.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivityScore activityScore = activityScores.get(0);
                    displayGraph(activityScore);
                }
            }, 100);
        }

        @Override
        protected ArrayList<ActivityScore> doInBackground(Void... params) {
            String url = HttpUrl.DOCTOR_GET_DOCTOR_ACTIVITY_SCORE + Utils.GET_ACCESS_TOKEN(DoctorMyActivityScore.this);

            JSONParser parser = new JSONParser();
            JSONArray jsonArray = parser.getJSON_Response(url);

            ArrayList<ActivityScore> activityScores = new ArrayList<>();
            parser.jsonParser(jsonArray, ActivityScore.class, activityScores);

            return activityScores;
        }
    }

    private class CompaniesAdapter extends BaseAdapter{

        ArrayList<ActivityScore> activityScores;

        public CompaniesAdapter(ArrayList<ActivityScore> activityScores){
            this.activityScores = activityScores;
        }

        @Override
        public int getCount() {
            return activityScores.size();
        }

        @Override
        public ActivityScore getItem(int position) {
            return activityScores.get(position);
        }

        @Override
        public long getItemId(int position) {
            return activityScores.get(position).getCompanyId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(DoctorMyActivityScore.this);
                convertView = inflater.inflate(R.layout.doctor_activity_score_list_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.companyNameTV = (TextView) convertView.findViewById(R.id.company_name_tv);
                viewHolder.totalScoreTV = (TextView) convertView.findViewById(R.id.total_score_tv);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            ActivityScore activityScore = activityScores.get(position);


            viewHolder.companyNameTV.setText(activityScore.getCompanyName());
            viewHolder.totalScoreTV.setText(activityScore.getTotalScore() + " Pts");

            return convertView;
        }

        class ViewHolder {
            TextView companyNameTV, totalScoreTV;
        }
    }

    private class ActivityScore{
        private int doctorId;
        private String doctorName;
        private int companyId;
        private String companyName;
        private int totalScore;
        private Activities activities;


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

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public int getTotalScore() {
            return totalScore;
        }

        public void setTotalScore(int totalScore) {
            this.totalScore = totalScore;
        }

        public Activities getActivities() {
            return activities;
        }

        public void setActivities(Activities activities) {
            this.activities = activities;
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
}
