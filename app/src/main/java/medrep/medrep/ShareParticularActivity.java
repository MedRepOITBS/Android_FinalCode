package medrep.medrep;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.interfaces.GetResponse;
import com.app.pojo.SignIn;
import com.app.task.NotificationGetTask;
import com.app.util.HttpUrl;

public class ShareParticularActivity extends AppCompatActivity implements GetResponse{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_particular);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("Share");
        final TextView numLikes = (TextView)findViewById(R.id.numLikes);
        final TextView shareCount = (TextView)findViewById(R.id.shareCount);
//        final TextView commentCount = (TextView)findViewById(R.id.commentCount);
        int typeId = getIntent().getExtras().getInt("topicId");
        System.out.println(typeId);
        getDataFromServer(typeId);
        LinearLayout likeLayout = (LinearLayout)findViewById(R.id.likeLayout);
        likeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalLikes = Integer.parseInt(numLikes.getText().toString());
                totalLikes += 1;
                numLikes.setText(totalLikes+"");
            }
        });

        LinearLayout shareLayout = (LinearLayout)findViewById(R.id.shareLayout);
        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalLikes = Integer.parseInt(shareCount.getText().toString());
                totalLikes += 1;
                shareCount.setText(totalLikes+"");
            }
        });

        LinearLayout messagelayout = (LinearLayout)findViewById(R.id.messagelayout);
        messagelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int totalLikes = Integer.parseInt(commentCount.getText().toString());
//                totalLikes += 1;
//                commentCount.setText(totalLikes+"");
                Intent intent = new Intent(ShareParticularActivity.this, EditContactInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getDataFromServer(int topicId) {
        String ip = HttpUrl.COMMONURL;
        String accessToken = SignIn.GET_ACCESS_TOKEN();
        String url = ip + "/medrep-web/GetShareByTopicID?topicId="+ topicId +"&token=" + accessToken;
        NotificationGetTask getData = new NotificationGetTask();
        getData.delegate = this;
        getData.execute(url);
    }

    @Override
    public void response(String result) {
        if(result != null) {
            Log.i("Response", result);
        }
    }
}
