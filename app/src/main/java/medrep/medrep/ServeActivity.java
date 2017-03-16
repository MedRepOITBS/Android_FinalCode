package medrep.medrep;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.util.Utils;

import java.util.Timer;
import java.util.TimerTask;

public class ServeActivity extends AppCompatActivity implements View.OnClickListener{

    private Boolean changeValue = true;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serve);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("Serve");
        LinearLayout listLayout = (LinearLayout)findViewById(R.id.listLayout);
        listLayout.getBackground().setAlpha(120);
        Button serve = (Button)findViewById(R.id.serve);
        serve.setBackground(getResources().getDrawable(R.drawable.border_button_light));

//        final LinearLayout parentLinearLayout = (LinearLayout)findViewById(R.id.parentLinearLayout);
//        final TransitionDrawable transition = (TransitionDrawable) parentLinearLayout.getBackground();
//        transition.setCrossFadeEnabled(true);
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        if(changeValue) {
//                            transition.startTransition(10000);
//                            changeValue = false;
//                        } else {
//                            //parentLinearLayout.setBackground(getResources().getDrawable(R.drawable.background_transition));
//                            changeValue = true;
//                            transition.reverseTransition(10000);
//                        }
//                    }
//                });
//
//            }
//        };
//        Timer timer = new Timer();
//        timer.schedule(timerTask, 5000, 10000);

        HorizontalScrollView horizontalScroll = (HorizontalScrollView)findViewById(R.id.horizontalScroll);
        horizontalScroll.setVisibility(View.GONE);
        LinearLayout searchLayout = (LinearLayout)findViewById(R.id.searchLayout);
        searchLayout.setVisibility(View.GONE);

        Button transform = (Button)findViewById(R.id.transform);
        transform.setOnClickListener(this);
        Button connect = (Button)findViewById(R.id.connect);
        connect.setOnClickListener(this);
        Button share = (Button)findViewById(R.id.share);
        share.setOnClickListener(this);
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewObject = v.getId();
        switch (viewObject) {
            case R.id.transform:
                navigateActivities(TransformActivity.class, "");
                break;
            case R.id.connect:
                navigateActivities(DoctorsMyContactActivity.class, "myContacts");
                break;
            case R.id.share:
                //Utils.DISPLAY_GENERAL_DIALOG(this, "Coming Soon...", "This Feature is presently under development.");
                navigateActivities(ShareActivity.class, "");
                break;
            case R.id.onBackClick:
                navigateActivities(DoctorDashboard.class, "");
                break;
        }
    }

    private void navigateActivities(Class object, String name) {
        Intent intent = new Intent(this, object);
        //if(object == TransformActivity.class || object == DoctorDashboard.class) {
        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
        //}
        if(!name.equals("")) {
            intent.putExtra("name", name);
        }

        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }
}
