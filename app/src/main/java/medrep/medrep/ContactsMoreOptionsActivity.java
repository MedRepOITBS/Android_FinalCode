package medrep.medrep;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
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
import com.app.task.RefreshAccessToken;
import com.app.util.HttpUrl;
import com.app.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class ContactsMoreOptionsActivity extends AppCompatActivity implements GetResponse, View.OnClickListener{

    private String name;
    public ArrayList<Integer> selectedId = new ArrayList<>();
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
        Button backButton = (Button)findViewById(R.id.onBackClick);
        backButton.setOnClickListener(this);

        name = getIntent().getExtras().getString("name");
        TextView title = (TextView)findViewById(R.id.title);
        if(name.equals("addSuggestedContacts")) {
            title.setText("Pending Contacts");
        } else {
            title.setText("Add Contacts");
        }
        Button addContactButton = (Button)findViewById(R.id.addContactButton);
        addContactButton.setOnClickListener(this);

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
                    if (accessToken != null) {
                        String url = HttpUrl.COMMONURL + "/getDoctorContacts/" + searchText + "?token=" + accessToken;
                        NotificationGetTask getData = new NotificationGetTask();
                        getData.delegate = ContactsMoreOptionsActivity.this;
                        getData.execute(url);
                    } else {
                        Intent intent = new Intent(ContactsMoreOptionsActivity.this, LoginActivity.class);
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
        if(accessToken != null) {
            if(name.equals("addMembers")) {
                url = ip +"/getAllContactsByCity?token="+ accessToken;
            } else if(name.equals("addSuggestedContacts")){
                url = ip +"/fetchPendingConnections?token="+ accessToken;;
            }
            NotificationGetTask task = new NotificationGetTask();
            task.delegate = this;
            task.execute(url);
        } else {
            Intent intent = new Intent(ContactsMoreOptionsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    private void initializeListView(String result) {
        try {
            final JSONArray array = new JSONArray(result);

            CommonAdapter adapter = new CommonAdapter(this,array);
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
        //JSONArray array = null;
        if(result != null) {
            try {
                Object json = new JSONTokener(result).nextValue();
                if(json instanceof JSONObject) {
                    JSONObject object = new JSONObject(result);
                    String status = object.getString("status");
                    if(object.has("statusCode")) {
                        int statusCode = object.getInt("statusCode");
                        if(statusCode == 405) {
                            RefreshAccessToken token = new RefreshAccessToken(this);
                            startActivity(getIntent());
                        }
                    } else if(status.equals("OK")){
                        Toast.makeText(ContactsMoreOptionsActivity.this, "Successfully added", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this, DoctorsMyContactActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(ContactsMoreOptionsActivity.this, "Something went wrong, please try after some time.", Toast.LENGTH_LONG).show();
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
        int value = v.getId();
        switch (value) {
            case R.id.onBackClick:
                finish();
                break;
            case R.id.addContactButton:
                if(selectedId.size() == 0) {
                    Utils.DISPLAY_GENERAL_DIALOG(this, "Contacts", "Please select atleast one contact.");
                } else {
                    String ip = HttpUrl.COMMONURL;
                    String accessToken = SignIn.GET_ACCESS_TOKEN();

                    if(accessToken == null) {
                        Intent intent = new Intent(this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        System.out.println(selectedId);
                        String url = ip + "/addContacts?token=" + accessToken;
                        JSONObject post_dict =  new JSONObject();;
                        try {
                            //post_dict = new JSONObject();
                            //post_dict.put("connIdList" , map);
                            JSONArray array = new JSONArray(selectedId);
                            post_dict.put("connIdList", array);
                            System.out.println(post_dict);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        HttpPost post = new HttpPost(post_dict);
                        post.delegate = this;
                        post.execute(url);
                    }
                }
                break;
        }
    }
}
