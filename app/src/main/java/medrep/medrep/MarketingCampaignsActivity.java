package medrep.medrep;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

public class MarketingCampaignsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marketing_campaigns);

        findViewById(R.id.horizontalScroll).setVisibility(View.GONE);
        findViewById(R.id.searchLayout).setVisibility(View.GONE);
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("Marketing Campaigns");
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
