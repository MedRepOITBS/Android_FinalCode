package medrep.medrep;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.ArrayList;

public class AddGroupMembersActivity extends AppCompatActivity implements GetResponse, View.OnClickListener{

    public ArrayList selectedId = new ArrayList();
    private AutoCompleteTextView searchConnections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_more_options);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        int groupId = getIntent().getExtras().getInt("name");
        System.out.println(groupId);
        HorizontalScrollView horizontalScroll = (HorizontalScrollView)findViewById(R.id.horizontalScroll);
        horizontalScroll.setVisibility(View.GONE);
        LinearLayout addLayout = (LinearLayout)findViewById(R.id.addLayout);
        addLayout.setVisibility(View.GONE);
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("Members");
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(this);
        Button addContactButton = (Button)findViewById(R.id.addContactButton);
        addContactButton.setOnClickListener(this);
    }

    private void initializeListView(String result) {

        try {
            JSONArray array = new JSONArray(result);
            if(array.length() == 0) {
                findViewById(R.id.addContactButton).setVisibility(View.GONE);
            } else {
                ListView listView = (ListView)findViewById(R.id.contactsListView);
                CommonAdapter adapter = new CommonAdapter(this, array);
                listView.setAdapter(adapter);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String ip = HttpUrl.COMMONURL;
        String accessToken = SignIn.GET_ACCESS_TOKEN();
        String url = ip + "/getAllContactsByCity?token=" + accessToken;
        System.out.println(url);
        System.out.print("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        NotificationGetTask task = new NotificationGetTask();
        task.delegate = this;
        task.execute(url);

        searchConnections = (AutoCompleteTextView)findViewById(R.id.searchConnections);
        String names[] = {"John", "John Deo", "Paul Waker", "John Paul", "Pavan Kumar"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        searchConnections.setAdapter(adapter);
        searchConnections.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = searchConnections.getText().toString();
                if (searchText.length() >= 2) {
                    String accessToken = SignIn.GET_ACCESS_TOKEN();

                    String url = HttpUrl.COMMONURL + "/getDoctorContacts/" + searchText + "?token=" + accessToken;
                    NotificationGetTask getData = new NotificationGetTask();
                    getData.delegate = AddGroupMembersActivity.this;
                    getData.execute(url);
                }
            }
        });
    }

    @Override
    public void response(String result) {
        System.out.println(result);
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

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.addContactButton:
                break;
            case R.id.onBackClick:
                finish();
                break;
        }
    }
}
