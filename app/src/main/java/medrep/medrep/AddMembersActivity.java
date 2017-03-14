package medrep.medrep;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.app.adapter.CommonAdapter;
import com.app.interfaces.GetResponse;
import com.app.pojo.SignIn;
import com.app.task.NotificationGetTask;
import com.app.task.RefreshAccessToken;
import com.app.util.HttpUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class AddMembersActivity extends AppCompatActivity implements GetResponse, View.OnClickListener{

    private int[] buttonIds;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);

        String ip = HttpUrl.COMMONURL;
        String accessToken = SignIn.GET_ACCESS_TOKEN();
        NotificationGetTask task = new NotificationGetTask();
        String url = null;
        if(getIntent().getExtras().getString("name").equals("addMembers")) {
            url = ip +"/getAllContactsByCity?token="+ accessToken;
        } else if(getIntent().getExtras().getString("name").equals("addMembers")) {
            url = ip +"/fetchPendingConnections?token="+ accessToken;
        }
        task.delegate = this;
        task.execute(url);

        initialieHorizontalView();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initialieHorizontalView() {
        //final ListView listViewObject = listView;
        final String names[] = {"My Contacts", "Suggested Contacs", "My Groups", "Suggested Groups"};
        buttonIds = new int[]{R.id.button1, R.id.button2, R.id.button3, R.id.button4};
        LinearLayout horizontalLayout = (LinearLayout)findViewById(R.id.horizontalLayout);
        for(int i = 0; i < names.length; i++) {
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 51);
            Button tv=new Button(this);
            //tv.setCompoundDrawablePadding(15);
            tv.setBackgroundResource(R.color.tect_color_white);
            if(name.equals("myContacts")) {
                if(i == 0) {
                    tv.setBackgroundResource(R.color.d_gray);
                }
            } else if(name.equals("suggestedContacts")) {
                if(i == 1) {
                    tv.setBackgroundResource(R.color.d_gray);
                }
            }

            tv.setText(names[i]);
            tv.setId(buttonIds[i]);
            tv.setTextColor(getResources().getColor(R.color.register_color));
            tv.setTextSize(10f);
            tv.setPadding(10, 0, 10, 0);
            tv.setAllCaps(false);
            tv.setLayoutParams(lparams);
            horizontalLayout.addView(tv);
            //final int j = i;
            tv.setOnClickListener(this);
        }
    }

    private void initializeListView(String result) {
        ListView view = (ListView)findViewById(R.id.contactsListView);
        view.setVisibility(View.VISIBLE);
        try {
            JSONArray array = new JSONArray(result);
            CommonAdapter adapter = new CommonAdapter(this, array);
            view.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void response(String result) {
         if(result != null) {
             try {
                 Object json = new JSONTokener(result).nextValue();
                 if(json instanceof JSONObject) {
                     JSONObject object = new JSONObject(result);
                     int statusCode = object.getInt("statusCode");
                     if(statusCode == 405) {
                         RefreshAccessToken token = new RefreshAccessToken(this);
                         startActivity(getIntent());
                     }
                 } else {
                     initializeListView(result);
                 }
             } catch (JSONException e) {
                 e.printStackTrace();
             }
         } else {
            Toast.makeText(this, "Something went wrong, please try after sometime.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View v) {

    }
}
