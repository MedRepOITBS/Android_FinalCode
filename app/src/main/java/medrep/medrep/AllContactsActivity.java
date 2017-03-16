package medrep.medrep;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adapter.CommonAdapter;
import com.app.adapter.GroupListAdapter;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AllContactsActivity extends AppCompatActivity implements View.OnClickListener, GetResponse{

    private JSONArray resultArray;
    public ArrayList<Integer> selectedId;
//    private AutoCompleteTextView searchConnections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_contacts);
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(this);
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("Contacts");
        HorizontalScrollView horizontalScroll = (HorizontalScrollView)findViewById(R.id.horizontalScroll);
        horizontalScroll.setVisibility(View.GONE);
        LinearLayout addLayout = (LinearLayout)findViewById(R.id.addLayout);
        addLayout.setVisibility(View.GONE);

        final AutoCompleteTextView searchConnections = (AutoCompleteTextView)findViewById(R.id.searchConnections);

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
                if (searchText.length() > 2 && resultArray != null && resultArray.length() > 0) {
                    JSONArray array = new JSONArray();
                    for(int i = 0; i < resultArray.length(); i++) {
                        try {
                            JSONObject object = resultArray.getJSONObject(i);
                            String firstName = object.getString("firstName");
                            String lastName = object.getString("lastName");
                            String fullName = firstName + " " + lastName;
                            Matcher m = Pattern.compile(searchText).matcher(fullName.toLowerCase());
                            if(m.find()) {
                                array.put(object);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    if(array.length() > 0) initializeListView(array);
                } else if(searchText.length() > 0) {
                    initializeListView(resultArray);
                }
            }
        });
    }

    private void initializeListView(JSONArray array) {
        System.out.print(array);

        try {
            JSONObject object = array.getJSONObject(0);
            final JSONArray groupArray = object.getJSONArray("groups");
            JSONArray contactArray = object.getJSONArray("doctor");
            System.out.println("contactArray: " + contactArray);
            ListView contactsListView = (ListView)findViewById(R.id.contactsListView);
            CommonAdapter adapter = new CommonAdapter(this, groupArray);
            contactsListView.setAdapter(adapter);
//            contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    try {
//                        JSONObject jsonObject = groupArray.getJSONObject(position);
//                        int groupId = jsonObject.getInt("group_id");
//                        selectedId.add(groupId);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
            ListView groupListView = (ListView)findViewById(R.id.groupListView);
            GroupListAdapter groupAdapter = new GroupListAdapter(this, contactArray);
            groupListView.setAdapter(groupAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String accessToken = SignIn.GET_ACCESS_TOKEN();
        String ip = HttpUrl.COMMONURL;
        String url = ip + "/getContactsAndGroups?token=" + accessToken;
        NotificationGetTask task = new NotificationGetTask();
        task.delegate = this;
        task.execute(url);
    }
    @Override
    public void onClick(View v) {

        finish();

    }

    @Override
    public void response(String result) {
        if(result != null) {
            try {
                Object json = new JSONTokener(result).nextValue();
                if(json instanceof JSONObject) {
                    JSONObject obj = new JSONObject(result);
                    if(obj.has("message")){
                        if(obj.getString("Status").equals("Error")) {
                            RefreshAccessToken token = new RefreshAccessToken(this);
                            startActivity(getIntent());
                        } else {
//                            Toast.makeText(this, "Group Deleted Successfully", Toast.LENGTH_LONG).show();
//                            startActivity(getIntent());
                        }
                    }
                } else {
                    JSONArray array = new JSONArray(result);
                    resultArray = array;
                    initializeListView(array);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Something went wrong, please try after sometime.", Toast.LENGTH_LONG).show();
        }
    }
}
