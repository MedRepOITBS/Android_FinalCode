package pharma;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fragments.PharmaNavigationDrawerFragment;

import medrep.medrep.R;
import pharma.model.DoctorNotificationStats;

/**
 * Created by kishore on 7/11/15.
 */
public class PharmaDoctorNotificationStatsActivity extends AppCompatActivity{

    public static DoctorNotificationStats DOCTOR_NOTIFICATION_STATS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(DOCTOR_NOTIFICATION_STATS == null){
            Toast.makeText(this, "Feedback data for this notification is not available.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setContentView(R.layout.pharma_doctor_notification_stats);

        ((TextView) findViewById(R.id.back_tv)).setText(DOCTOR_NOTIFICATION_STATS.getNotificationName() + " FeedBack Results");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ((TextView) findViewById(R.id.ratpercent)).setText(String.valueOf(DOCTOR_NOTIFICATION_STATS.getRatingAverage())+" / 5");
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar1);
        ratingBar.setRating((float) DOCTOR_NOTIFICATION_STATS.getRatingAverage());

        double prescribeYesPercent = (DOCTOR_NOTIFICATION_STATS.getPrescribeYes()/DOCTOR_NOTIFICATION_STATS.getTotalCount()) * 100;
        double prescribeNoPercent = (DOCTOR_NOTIFICATION_STATS.getPrescribeNo()/DOCTOR_NOTIFICATION_STATS.getTotalCount()) * 100;
        double favoriteYesPercent = (DOCTOR_NOTIFICATION_STATS.getFavoriteYes()/DOCTOR_NOTIFICATION_STATS.getTotalCount()) * 100;
        double favoriteNoPercent = (DOCTOR_NOTIFICATION_STATS.getFavoriteNo()/DOCTOR_NOTIFICATION_STATS.getTotalCount()) * 100;
        double recommendYesPercent = (DOCTOR_NOTIFICATION_STATS.getRecomendYes()/DOCTOR_NOTIFICATION_STATS.getTotalCount()) * 100;
        double recommendNoPercent = (DOCTOR_NOTIFICATION_STATS.getRecomendNo()/DOCTOR_NOTIFICATION_STATS.getTotalCount()) * 100;

        ((ProgressBar) findViewById(R.id.prescribe_yes_pb)).setProgress((int) prescribeYesPercent);
        ((ProgressBar) findViewById(R.id.prescribe_no_pb)).setProgress((int) prescribeNoPercent);
        ((ProgressBar) findViewById(R.id.recommend_yes_pb)).setProgress((int) recommendYesPercent);
        ((ProgressBar) findViewById(R.id.recommend_no_pb)).setProgress((int) recommendNoPercent);

        /*((TextView) findViewById(R.id.prescribe_yes_progress_tv)).setText(String.format("%.2f", prescribeYesPercent) + "%");
        ((TextView) findViewById(R.id.prescribe_no_progress_tv)).setText(String.format("%.2f", prescribeNoPercent) + "%");
        ((TextView) findViewById(R.id.recommend_yes_progress_tv)).setText(String.format("%.2f", recommendYesPercent) + "%");
        ((TextView) findViewById(R.id.recommend_no_progress_tv)).setText(String.format("%.2f", recommendNoPercent) + "%");*/

        ((TextView) findViewById(R.id.prescribe_yes_progress_tv)).setText((int)prescribeYesPercent + "%");
        ((TextView) findViewById(R.id.prescribe_no_progress_tv)).setText((int) prescribeNoPercent + "%");
        ((TextView) findViewById(R.id.recommend_yes_progress_tv)).setText((int) recommendYesPercent + "%");
        ((TextView) findViewById(R.id.recommend_no_progress_tv)).setText((int) recommendNoPercent + "%");


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        PharmaNavigationDrawerFragment drawerFragment = (PharmaNavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        DOCTOR_NOTIFICATION_STATS = null;
    }

}
