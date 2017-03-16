package medrep.medrep;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.adapter.MyAdapter;

public class AppointmentDetailsActivity extends AppCompatActivity implements View.OnClickListener{


        TextView lblCompanyName, lblCreatedDate, lblDrugs, lbllocation, lblCreatedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_details);
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("Notification Details");
        HorizontalScrollView horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScroll);
        horizontalScrollView.setVisibility(View.GONE);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.searchLayout);
        linearLayout.setVisibility(View.GONE);
        lblCompanyName = (TextView) findViewById(R.id.lblCompanyName);
        lblCreatedDate = (TextView) findViewById(R.id.lblDate);
        lblDrugs = (TextView) findViewById(R.id.lblDrugName);
        lblCreatedTime = (TextView) findViewById(R.id.lblTime);
        lbllocation = (TextView) findViewById(R.id.lblLocation);
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(this);



        String companyName = getIntent().getStringExtra("companyName");
        String createdDate = getIntent().getStringExtra("createdDate");
        String companyDesc = getIntent().getStringExtra("companyDesc");
        String location = getIntent().getStringExtra("location");
        String drugName = getIntent().getStringExtra("drugName");
        lblCompanyName.setText(companyName);
        lblCreatedDate.setText(MyAdapter.getMonthFromTicks(createdDate) + " " + MyAdapter.getDateFromTicks(createdDate) + " " + MyAdapter.getYearFromTicks(createdDate));
        lblDrugs.setText(drugName);
        lbllocation.setText(location);
        lblCreatedTime.setText(MyAdapter.getTimeFromTicks(createdDate));
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
