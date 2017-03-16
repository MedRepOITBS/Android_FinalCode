package medrep.medrep;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adapter.CommonAdapter;
import com.app.interfaces.GetResponse;
import com.app.pojo.SignIn;
import com.app.task.HttpPost;
import com.app.task.NotificationGetTask;
import com.app.util.HttpUrl;
import com.app.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroupMoreActivity extends AppCompatActivity implements GetResponse, View.OnClickListener{

    private String name;
    public ArrayList<Integer> selectedId = new ArrayList<>();
    private AutoCompleteTextView searchConnections;
    private JSONArray responseArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_more_options);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        HorizontalScrollView horizontalScroll = (HorizontalScrollView)findViewById(R.id.horizontalScroll);
        horizontalScroll.setVisibility(View.GONE);
        LinearLayout addLayout = (LinearLayout)findViewById(R.id.addLayout);
        addLayout.setVisibility(View.GONE);
        Button backButton = (Button)findViewById(R.id.onBackClick);
        backButton.setOnClickListener(this);
        name = getIntent().getExtras().getString("name");
        TextView title = (TextView)findViewById(R.id.title);
        title.setText(name);
        Button addContactButton = (Button)findViewById(R.id.addContactButton);
        addContactButton.setOnClickListener(this);
        addContactButton.setText("Remove");

        searchConnections = (AutoCompleteTextView)findViewById(R.id.searchConnections);
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
                    if(accessToken != null) {
                        String url = HttpUrl.COMMONURL + "/getDoctorContacts/" + searchText + "?token=" + accessToken;
                        NotificationGetTask getData = new NotificationGetTask();
                        getData.delegate = GroupMoreActivity.this;
                        getData.execute(url);
                    } else {
                        Intent intent = new Intent(GroupMoreActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.print(name);
        String ip = HttpUrl.COMMONURL;
        String accessToken = SignIn.GET_ACCESS_TOKEN();
        String url = null;
        if(name.equals("Pending Connections")) {
            url = ip +"/getPendingGroup?token="+ accessToken;
        } else if(name.equals("More Connections")){
            url = ip +"/fetchPendingConnections?token="+ accessToken;;
        }
        NotificationGetTask task = new NotificationGetTask();
        task.delegate = this;
        task.execute(url);
    }

    private void initializeListView(String result) {
        try {
            final JSONArray array = new JSONArray(result);
            responseArray = array;
            CommonAdapter adapter = new CommonAdapter(this, array);
            ListView contactsListView = (ListView)findViewById(R.id.contactsListView);
            contactsListView.setAdapter(adapter);
            contactsListView.setItemsCanFocus(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void response(String result) {

        System.out.print(result);

        if(result != null) {
            try {
                JSONArray array = new JSONArray(result);
                JSONObject object = array.getJSONObject(0);
                Boolean message = object.has("message");
                if(message) {
                    Toast.makeText(this, "Successfully removed from group", Toast.LENGTH_LONG).show();
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
        int value = v.getId();
        switch (value) {
            case R.id.onBackClick:
                finish();
                break;
            case R.id.addContactButton:
                System.out.println(selectedId);
                if(selectedId.size() == 0) {
                    Utils.DISPLAY_GENERAL_DIALOG(this, "Contacts", "Please select atleast one contact.");
                } else {
                    String ip = HttpUrl.COMMONURL;
                    String accessToken = SignIn.GET_ACCESS_TOKEN();
                    System.out.print(accessToken);
                    if(accessToken == null) {
                        Intent intent = new Intent(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        System.out.println(selectedId);
                        JSONObject object = new JSONObject();
                        try {
                            JSONObject resObject = responseArray.getJSONObject(0);
                            System.out.println(resObject);
                            object.put("group_id", resObject.getInt("group_id"));
                            JSONArray array = new JSONArray(selectedId);
                            object.put("memberList", array);
                            String url = ip + "/removeMember?token=" + accessToken;
                            HttpPost post = new HttpPost(object);
                            post.delegate = this;
                            post.execute(url);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }
}
