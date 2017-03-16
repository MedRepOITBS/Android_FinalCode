package medrep.medrep;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adapter.DoctorGroupListViewAdapter;
import com.app.interfaces.GetResponse;
import com.app.pojo.SignIn;
import com.app.task.NotificationGetTask;
import com.app.task.RefreshAccessToken;
import com.app.util.HttpUrl;
import com.app.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class NoContactActivity extends AppCompatActivity implements View.OnClickListener, GetResponse{

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_contact);
        ImageButton addButton = (ImageButton)findViewById(R.id.addButton);
        addButton.setOnClickListener(this);
        Button addContact = (Button)findViewById(R.id.addContact);
        addContact.setOnClickListener(this);
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(this);
        HorizontalScrollView horizontalScroll = (HorizontalScrollView)findViewById(R.id.horizontalScroll);
        horizontalScroll.setVisibility(View.GONE);
        LinearLayout searchLayout = (LinearLayout)findViewById(R.id.searchLayout);
        searchLayout.setVisibility(View.GONE);
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("No Contacts");
        Button connect = (Button)findViewById(R.id.connect);
        connect.setBackground(getResources().getDrawable(R.drawable.border_button_light));
//        Button suggestedContacsButton = (Button)findViewById(R.id.suggestedContacsButton);
//        suggestedContacsButton.setOnClickListener(this);
        ImageButton groupButton = (ImageButton)findViewById(R.id.groupButton);
        groupButton.setOnClickListener(this);
        Button transform = (Button)findViewById(R.id.transform);
        transform.setOnClickListener(this);
        Button share = (Button)findViewById(R.id.share);
        share.setOnClickListener(this);
        Button serve = (Button)findViewById(R.id.serve);
        serve.setOnClickListener(this);

        NotificationGetTask task = new NotificationGetTask();
        task.delegate = this;
        String ip = HttpUrl.COMMONURL;
        String accessToken = SignIn.GET_ACCESS_TOKEN();
        String url = ip + "/getMyContactList?token=" + accessToken;
        task.execute(url);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//    }

    @Override
    public void onClick(View v) {
        int value = v.getId();
        Intent intent;
        switch (value) {
            case R.id.addButton:
            case R.id.addContact:
                navigateActivities(AddContactActivity.class, "");
                break;
            case R.id.onBackClick:
                //finish();
                navigateActivities(DoctorDashboard.class, "");
                break;
            case R.id.groupButton:
                navigateActivities(DoctorMyGroupsActivty.class, "myGroups");
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.transform:
                navigateActivities(TransformActivity.class, "");
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.share:
                navigateActivities(ShareActivity.class, "");
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.serve:
                //Utils.DISPLAY_GENERAL_DIALOG(this, "Coming Soon...", "This Feature is presently under development.");
                navigateActivities(ServeActivity.class, "");
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
        }
    }

    private void navigateActivities(Class object, String name) {
        Intent intent = new Intent(this, object);
       // if(object == TransformActivity.class) {
            intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
        intent.putExtra("name", name);
       // }

        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        navigateActivities(DoctorDashboard.class, "");
    }

    @Override
    public void response(String result) {
        if(result != null) {
            try {
                Object json = new JSONTokener(result).nextValue();
                if(json instanceof JSONObject) {
                    JSONObject obj = new JSONObject(result);
                    if(obj.getString("status").equals("error")) {
                        RefreshAccessToken token = new RefreshAccessToken(this);
                        startActivity(getIntent());
                    } else {
                        Toast.makeText(this, "Group Deleted Successfully", Toast.LENGTH_LONG).show();
                        startActivity(getIntent());
                    }
                } else {
                    JSONArray array = new JSONArray(result);
                    if(array.length() > 0) {
                        Intent intent = new Intent(this, DoctorsMyContactActivity.class);
                        intent.putExtra("name", "myContacts");
                        intent.putExtra("result", result);
                        startActivity(intent);
                    }
                    System.out.println(result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Something went wrong, please try after sometime.", Toast.LENGTH_LONG).show();
        }
    }
}
