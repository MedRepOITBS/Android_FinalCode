package pharma;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.fragments.PharmaNavigationDrawerFragment;
import com.app.util.Utils;

import medrep.medrep.R;
import medrep.medrep.SplashScreen;


public class PharmaRepDashboard  extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onBackPressed() {
        backPressed();
    }

    private void backPressed() {
        Intent intent=new Intent(PharmaRepDashboard.this, PharmaDashBoard.class);
//                                intent.putExtra("signin",(SignIn)o);
        intent.putExtra(SplashScreen.PROFILE_NAME_KEY, PharmaDashBoard.profileName);
        startActivity(intent);

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharma_medrep_dashboard);

        Utils.SET_PHARMA_COMPANY_LOGO(this, (ImageView) findViewById(R.id.p_company_name));

        TextView title = (TextView) findViewById(R.id.back_tv);
        title.setText("Rep Dashboard");

        findViewById(R.id.upcomming).setOnClickListener(this);
        findViewById(R.id.completed).setOnClickListener(this);
        findViewById(R.id.pending).setOnClickListener(this);


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
                intent = new Intent(PharmaRepDashboard.this, PharmaRepMyAppointmentsActivity.class);
                intent.putExtra(PharmaRepMyAppointmentsActivity.APPOINTMENTS_TYPE_KEY, PharmaRepMyAppointmentsActivity.AppointmentsType.Upcoming);
                startActivity(intent);
                break;
            case R.id.completed:
                intent = new Intent(PharmaRepDashboard.this, PharmaRepMyAppointmentsActivity.class);
                intent.putExtra(PharmaRepMyAppointmentsActivity.APPOINTMENTS_TYPE_KEY, PharmaRepMyAppointmentsActivity.AppointmentsType.Completed);
                startActivity(intent);
                break;
            case R.id.pending:
                intent = new Intent(PharmaRepDashboard.this, PharmaRepMyAppointmentsActivity.class);
                intent.putExtra(PharmaRepMyAppointmentsActivity.APPOINTMENTS_TYPE_KEY, PharmaRepMyAppointmentsActivity.AppointmentsType.Pending);
                startActivity(intent);
                break;
            case R.id.back:
                backPressed();
                break;
        }
    }
}