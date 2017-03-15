package medrep.medrep;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.adapter.DoctorGroupListViewAdapter;
import com.app.adapter.MyAdapter;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class DoctorNotificationActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_notification);
        HorizontalScrollView horizontalScroll = (HorizontalScrollView)findViewById(R.id.horizontalScroll);
        horizontalScroll.setVisibility(View.GONE);
        LinearLayout searchLayout = (LinearLayout)findViewById(R.id.searchLayout);
        searchLayout.setVisibility(View.GONE);
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(this);
        //findViewById(R.id.BackButton).setOnClickListener(this);
        StickyListHeadersListView stickyList = (StickyListHeadersListView) findViewById(R.id.stickyListViews);
        ListView notificatonListView = (ListView)findViewById(R.id.notificatonListView);
        TextView title = (TextView)findViewById(R.id.title);
        if(getIntent().getExtras().getString("moreButton").equals("Appointment")){
            title.setText("Appointment");
//            TextView heading = (TextView)findViewById(R.id.heading);
//            heading.setText("Appointments");
            stickyList.setVisibility(View.VISIBLE);
            notificatonListView.setVisibility(View.GONE);
            MyAdapter stickyAdapter = new MyAdapter(this);
            stickyList.setAdapter(stickyAdapter);
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
}
