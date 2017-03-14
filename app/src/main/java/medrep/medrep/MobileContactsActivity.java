package medrep.medrep;

import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileContactsActivity extends AppCompatActivity implements View.OnClickListener, GetResponse{

    public ArrayList<String> selectedContacts = new ArrayList<>();
    private JSONArray searchedContacts;
    private AutoCompleteTextView searchConnections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_more_options);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        HorizontalScrollView horizontalScroll = (HorizontalScrollView)findViewById(R.id.horizontalScroll);
        horizontalScroll.setVisibility(View.GONE);
        LinearLayout addLayout = (LinearLayout)findViewById(R.id.addLayout);
        addLayout.setVisibility(View.GONE);
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("Contacts");
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(this);
        Button addContactButton = (Button)findViewById(R.id.addContactButton);
        addContactButton.setOnClickListener(this);
        addContactButton.setText("Send Message");
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
                Log.i("COUNT", searchedContacts.length()+"");
                String searchText = searchConnections.getText().toString();
                JSONArray array = new JSONArray();
                if (searchText.length() >= 2) {
                    for(int i = 0; i < searchedContacts.length(); i++) {
                        try {
                            JSONObject object = searchedContacts.getJSONObject(i);
                            String name = object.getString("name");
                            String phoneNumber = object.getString("number");
                            Matcher m = Pattern.compile(searchText).matcher(name.toLowerCase());
                            if(m.find()) {
                                JSONObject selectedobject = new JSONObject();
                                selectedobject.put("name", name);
                                selectedobject.put("number", phoneNumber);
                                array.put(object);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    initializeListView(array);

                } else if(searchText.length() == 0) {
                    initializeListView(searchedContacts);
                }
            }
        });

        //initializeListView();
        getPhoneNumbers();
    }

    private void initializeListView(JSONArray array) {
        //JSONArray array = getPhoneNumbers();
        ListView listView = (ListView)findViewById(R.id.contactsListView);
        CommonAdapter adapter = new CommonAdapter(this, array);
        listView.setAdapter(adapter);
    }

    private void getPhoneNumbers() {
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
//        HashMap<String, String> map = new HashMap<>();

        JSONArray array = new JSONArray();
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            try {
                JSONObject object = new JSONObject();
                object.put("name", name);
                object.put("number", phoneNumber);
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        phones.close();
        searchedContacts = array;
        initializeListView(searchedContacts);
        //return array;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.onBackClick:
                Intent intent = new Intent(this, DoctorDashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.addContactButton:
                if(selectedContacts.size() > 0) {
                    String ip = HttpUrl.COMMONURL;
                    String accessToken = SignIn.GET_ACCESS_TOKEN();
                    String url = ip + "/inviteContacts?token=" + accessToken;
                    JSONObject object = new JSONObject();
                    JSONArray array = new JSONArray(selectedContacts);
                    try {
                        object.put("toMobileContactList", array);
                        object.put("message", "TEST");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    HttpPost post = new HttpPost(object);
                    post.delegate = this;
                    post.execute(url);
                } else {
                    Toast.makeText(this, "Please select atleast one contact to send sms.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void response(String result) {
        System.out.println(result);
        try {
            JSONObject object = new JSONObject(result);
            String status= (String) object.get("message");
            if(status.equals("Success")) {
                Toast.makeText(this, "Sms sent Successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Something went wrong, please try after some time", Toast.LENGTH_LONG).show();
            }
            Intent intent = new Intent(this, DoctorDashboard.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
