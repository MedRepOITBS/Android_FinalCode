package medrep.medrep;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adapter.CommonAdapter;
import com.app.adapter.DoctorGroupListViewAdapter;
import com.app.db.Notification;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoctorsMyContactActivity extends AppCompatActivity implements View.OnClickListener, GetResponse {

    private AlertDialog alertDialog = null;
    private AutoCompleteTextView searchConnections;
    private int[] buttonIds;
    private JSONArray resultArray;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_my_contact);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("Connect");
        Button connect = (Button)findViewById(R.id.connect);
        connect.setBackground(getResources().getDrawable(R.drawable.border_button_light));
        LinearLayout listLayout = (LinearLayout)findViewById(R.id.listLayout);
        listLayout.getBackground().setAlpha(120);
        ImageButton addButton = (ImageButton)findViewById(R.id.addButton);
        addButton.setOnClickListener(this);
        Button transform = (Button)findViewById(R.id.transform);
        transform.setOnClickListener(this);
        Button serve = (Button)findViewById(R.id.serve);
        serve.setOnClickListener(this);
        Button share = (Button)findViewById(R.id.share);
        share.setOnClickListener(this);
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(this);
        LinearLayout searchImageLayout = (LinearLayout)findViewById(R.id.searchImageLayout);
        searchImageLayout.setVisibility(View.VISIBLE);
        ImageButton searchButton = (ImageButton)findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);

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
                    if(array.length() > 0) initializeGridView(array);
                } else if(searchText.length() > 0) {
                    initializeGridView(resultArray);
                }
            }
        });
        initialieHorizontalView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String accessToken = SignIn.GET_ACCESS_TOKEN();
        if (accessToken != null) {
            String url = HttpUrl.COMMONURL + "/getMyContactList?token=" + accessToken;
            NotificationGetTask getTask = new NotificationGetTask();
            getTask.delegate = this;
            getTask.execute(url);
        } else {
            Intent intent = new Intent(DoctorsMyContactActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initialieHorizontalView() {
        //final ListView listViewObject = listView;
        final String names[] = {"My Connections", "Suggested Connections", "My Groups", "Suggested Groups"};
        buttonIds = new int[]{R.id.button1, R.id.button2, R.id.button3, R.id.button4};
        LinearLayout horizontalLayout = (LinearLayout)findViewById(R.id.horizontalLayout);
        horizontalLayout.setPadding(10, 0, 0, 10);
        for(int i = 0; i < names.length; i++) {
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            Button tv=new Button(this);
            //tv.setCompoundDrawablePadding(15);
            tv.setBackgroundResource(R.color.home_button);
            if(i == 0) {
                tv.setBackgroundResource(R.color.home_highted);
            }
            tv.setText(names[i]);
            tv.setId(buttonIds[i]);
            tv.setTextColor(getResources().getColor(R.color.tect_color_white));
            tv.setTextSize(10f);
            tv.setPadding(10, 0, 10, 0);
            tv.setAllCaps(false);
            tv.setLayoutParams(lparams);
            horizontalLayout.addView(tv);
            //final int j = i;
            tv.setOnClickListener(this);
        }
    }

    private void initializeGridView(final JSONArray result) {
        System.out.println("JSONArray result " + result);
        GridView contactsGridView = (GridView)findViewById(R.id.contactsGridView);

        CommonAdapter customAdapter = new CommonAdapter(this, result);
        contactsGridView.setAdapter(customAdapter);
        contactsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    JSONObject object = result.getJSONObject(position);
                    Intent intent = new Intent(DoctorsMyContactActivity.this, DoctorPostsActivity.class);
                    //int userId = object.getInt("userId");
                    String firstName = object.getString("firstName");
                    String lastName = object.getString("lastName");
                    String fullName = firstName + " " + lastName;
                    String therapeuticArea = object.getString("therapeuticArea");
                    String dPicture = object.getString("dPicture");
                    String city = object.getString("city");
                    int doctorId = object.getInt("doctorId");
                    int contactId = object.getInt("contactId");
                    intent.putExtra("dPicture", dPicture);
                    //intent.putExtra("userId", userId);
                    intent.putExtra("fullName", fullName);
                    intent.putExtra("therapeuticArea", therapeuticArea);
                    intent.putExtra("doctorId", doctorId);
                    intent.putExtra("contactId", contactId);
                    intent.putExtra("city", city);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        NotificationGetTask task = new NotificationGetTask();
        switch (viewId) {
            case R.id.onBackClick:
                navigateActivities(DoctorDashboard.class, "");
                break;
            case R.id.transform:
                navigateActivities(TransformActivity.class, "");
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.share:
                //Utils.DISPLAY_GENERAL_DIALOG(this, "Coming Soon...", "This Feature is presently under development.");
                navigateActivities(ShareActivity.class, "");
                break;
            case R.id.serve:
                navigateActivities(ServeActivity.class, "");
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.addButton:
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_alert_dialog_for_add_button, null);
                Button cancelButton = (Button) dialogView.findViewById(R.id.cancelButton);
                cancelButton.setOnClickListener(DoctorsMyContactActivity.this);
                Button addMembers = (Button)dialogView.findViewById(R.id.addMembersButton);
                addMembers.setOnClickListener(DoctorsMyContactActivity.this);
                Button pendingMembersButton = (Button)dialogView.findViewById(R.id.pendingMembersButton);
                pendingMembersButton.setOnClickListener(DoctorsMyContactActivity.this);
                dialogBuilder.setView(dialogView);
                alertDialog = dialogBuilder.create();
                alertDialog.show();
                break;
//
            case R.id.cancelButton:
                alertDialog.dismiss();

                break;
            case R.id.button2:
                    clickButton(R.id.button2);
                    navigateActivities(SuggestedContactsActivity.class, "");
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.button3:
                clickButton(R.id.button3);
                navigateActivities(DoctorMyGroupsActivty.class, "myGroups");
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.button4:
                clickButton(R.id.button4);
                navigateActivities(DoctorMyGroupsActivty.class, "mySuggestedGroups");
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.addMembersButton:
                alertDialog.dismiss();
                navigateActivities(ContactsMoreOptionsActivity.class, "addMembers");
                break;
            case R.id.pendingMembersButton:
                alertDialog.dismiss();
                navigateActivities(ContactsMoreOptionsActivity.class, "addSuggestedContacts");
                break;
            case R.id.searchButton:
                String searchText = searchConnections.getText().toString();
                String accessToken = SignIn.GET_ACCESS_TOKEN();
                if(accessToken != null) {
                    String url = HttpUrl.COMMONURL + "/getDoctorContacts/" + searchText + "?token=" + accessToken;
                    NotificationGetTask getData = new NotificationGetTask();
                    getData.delegate = DoctorsMyContactActivity.this;
                    getData.execute(url);
                } else {
                    Intent intent = new Intent(DoctorsMyContactActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                break;
        }

    }

    private void clickButton(int id) {
        for(int i = 0; i < buttonIds.length; i++) {
            if(buttonIds[i] != id) {
                findViewById(buttonIds[i]).setBackgroundResource(R.color.home_button);
            } else {
                findViewById(buttonIds[i]).setBackgroundResource(R.color.home_highted);
            }
        }
    }

    private void navigateActivities(Class object, String name) {
        Intent intent = new Intent(this, object);
       if(object == DoctorDashboard.class) {
            intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
        }
        if(!name.equals("")) {
            intent.putExtra("name", name);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
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
                    if(obj.has("status")){
                        if(obj.getString("status").equals("error")) {
                            RefreshAccessToken token = new RefreshAccessToken(this);
                            startActivity(getIntent());
                        } else if(obj.getString("status").equals("success")){
                            JSONObject object = obj.getJSONObject("result");
                            JSONArray array = object.getJSONArray("myContacts");
                            resultArray = array;
                            initializeGridView(array);
                        } else {
                            Toast.makeText(this, "Group Deleted Successfully", Toast.LENGTH_LONG).show();
                            startActivity(getIntent());
                        }
                    }
                } else {
                    JSONArray array = new JSONArray(result);
                    resultArray = array;
                    initializeGridView(array);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Something went wrong, please try after sometime.", Toast.LENGTH_LONG).show();
        }
    }
}
