package medrep.medrep;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
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

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoctorMyGroupsActivty extends AppCompatActivity implements View.OnClickListener, GetResponse {
    private AlertDialog alertDialog = null;
    private AutoCompleteTextView searchConnections;
    private int[] buttonIds;
    private String name;
    private JSONArray resultArray;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_my_groups_activty);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("My Groups");
        LinearLayout listLayout = (LinearLayout)findViewById(R.id.listLayout);
        listLayout.getBackground().setAlpha(120);
        Button connect = (Button)findViewById(R.id.connect);
        connect.setBackground(getResources().getDrawable(R.drawable.border_button_light));
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(this);
        ImageButton addButton = (ImageButton)findViewById(R.id.addButton);
        addButton.setOnClickListener(this);
        Button transform = (Button)findViewById(R.id.transform);
        transform.setOnClickListener(this);
        Button serve = (Button)findViewById(R.id.serve);
        serve.setOnClickListener(this);
        Button share = (Button)findViewById(R.id.share);
        share.setOnClickListener(this);
//        LinearLayout searchImageLayout = (LinearLayout)findViewById(R.id.searchImageLayout);
//        searchImageLayout.setVisibility(View.VISIBLE);
//        ImageButton searchButton = (ImageButton)findViewById(R.id.searchButton);
//        searchButton.setOnClickListener(this);

        name = getIntent().getExtras().getString("name");
        System.out.println(name);

        searchConnections = (AutoCompleteTextView)findViewById(R.id.searchConnections);
        String names[] = {"Group 1", "Group 2", "Group 3", "Group 4"};
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
                    JSONArray array = new JSONArray();
                    for(int i = 0; i < resultArray.length(); i++) {
                        try {
                            JSONObject object = resultArray.getJSONObject(i);
                            String groupName = object.getString("group_name");
                            Matcher m = Pattern.compile(searchText).matcher(groupName.toLowerCase());
                            if(m.find()) {
                                array.put(object);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    if(array.length() > 0) initializeGroupListView(array);
                } else if(searchText.length() == 0) {
                    initializeGroupListView(resultArray);
                }
            }
        });
        initialieHorizontalView();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initialieHorizontalView() {
        //final ListView listViewObject = listView;
        final String names[] = {"My Contacts", "Suggested Contacs", "My Groups", "Suggested Groups"};
        buttonIds = new int[]{R.id.button1, R.id.button2, R.id.button3, R.id.button4};
        LinearLayout horizontalLayout = (LinearLayout)findViewById(R.id.horizontalLayout);
        for(int i = 0; i < names.length; i++) {
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            Button tv=new Button(this);
            //tv.setCompoundDrawablePadding(15);
            tv.setBackgroundResource(R.color.home_button);
            if(name.equals("myGroups")) {
                if(i == 2) {
                    tv.setBackgroundResource(R.color.home_highted);
                }
            } else if(name.equals("mySuggestedGroups")) {
                if(i == 3) {
                    tv.setBackgroundResource(R.color.home_highted);
                }
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

    @Override
    protected void onStart() {
        super.onStart();
        NotificationGetTask getData = new NotificationGetTask();
        getData.delegate = this;
        String accessToken = SignIn.GET_ACCESS_TOKEN();
        String url;
        if(accessToken != null) {
            if(name == "myGroups") {
                url = HttpUrl.COMMONURL + "/getGroups?token=" +accessToken;
            } else {
                url = HttpUrl.COMMONURL + "/getSuggestedGroups?token=" +accessToken;
            }
            getData.execute(url);
        } else {
            Intent intent = new Intent(DoctorMyGroupsActivty.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

    private void initializeGroupListView(final JSONArray resultObject) {
        ListView groupListView = (ListView)findViewById(R.id.groupListView);
        CommonAdapter customGroupAdapter = new CommonAdapter(this, resultObject);
        groupListView.setAdapter(customGroupAdapter);
        groupListView.setItemsCanFocus(true);
        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //System.out.println("navigate to next activity &&&&&&&&&");
                try {
                    JSONObject group = resultObject.getJSONObject(position);
                    System.out.println("server Data: " + group);
                    String groupId = group.getString("group_id");
                    String groupName = group.getString("group_name");
                    String longDesc = group.getString("group_long_desc");
                    String shortDesc = group.getString("group_short_desc");
                    String imageData = null;
                    if(group.getString("imageUrl") != null) {
                        imageData = group.getString("imageUrl");
                    } else {
                        imageData = String.valueOf(group.getString("group_name").charAt(0));
                    }
                    Intent intent = new Intent(DoctorMyGroupsActivty.this, DoctorParticularGroupActivity.class);
                    intent.putExtra("groupId", groupId);
                    intent.putExtra("groupName", groupName);
                    intent.putExtra("longDesc", longDesc);
                    intent.putExtra("shortDesc", shortDesc);
                    intent.putExtra("imageData", imageData);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        switch (viewId) {
            case R.id.onBackClick:
                navigateActivities(DoctorDashboard.class, "");
                break;
            case R.id.transform:
                navigateActivities(TransformActivity.class,"");
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.share:
               // Utils.DISPLAY_GENERAL_DIALOG(this, "Coming Soon...", "This Feature is presently under development.");
                navigateActivities(ShareActivity.class, "");
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
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
                Button addMembersButton = (Button)dialogView.findViewById(R.id.addMembersButton);
                addMembersButton.setText("Pending Connections");
                addMembersButton.setOnClickListener(this);
                Button pendingMembersButton = (Button)dialogView.findViewById(R.id.pendingMembersButton);
                pendingMembersButton.setText("More Connections");
                pendingMembersButton.setOnClickListener(this);
                Button cancelButton  = (Button)dialogView.findViewById(R.id.cancelButton);
                cancelButton.setOnClickListener(DoctorMyGroupsActivty.this);
                Button createGroup = (Button)dialogView.findViewById(R.id.createGroup);
                createGroup.setVisibility(View.VISIBLE);
                createGroup.setOnClickListener(DoctorMyGroupsActivty.this);
                dialogBuilder.setView(dialogView);
                alertDialog = dialogBuilder.create();
                alertDialog.show();
                break;
            case R.id.addMembersButton:
                alertDialog.dismiss();
                navigateActivities(GroupMoreActivity.class, "Pending Connections");
                break;
            case R.id.pendingMembersButton:
                alertDialog.dismiss();
                navigateActivities(GroupMoreActivity.class, "More Connections");
                break;
            case R.id.cancelButton:
                alertDialog.cancel();
                break;
            case R.id.createGroup:
                alertDialog.cancel();
                navigateActivities(CreateGroupActivity.class, "groupId");
//                Intent intent = new Intent(this, CreateGroupActivity.class);
//                startActivity(intent);
                break;
            case R.id.button1:
                clickButton(R.id.button1);
                navigateActivities(DoctorsMyContactActivity.class, "");
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.button2:
                clickButton(R.id.button2);
                navigateActivities(SuggestedContactsActivity.class, "");
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.button3:
                if(name.equals("mySuggestedGroups")) {
                    clickButton(R.id.button3);
                    navigateActivities(DoctorMyGroupsActivty.class, "myGroups");
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
                break;
            case R.id.button4:
                if(name.equals("myGroups")) {
                    clickButton(R.id.button4);
                    navigateActivities(DoctorMyGroupsActivty.class, "mySuggestedGroups");
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                }
                break;
//            case R.id.searchButton:
//                String searchText = searchConnections.getText().toString();
//                if (searchText.length() >= 2) {
//                    String accessToken = SignIn.GET_ACCESS_TOKEN();
//                    if(accessToken != null) {
//                        String url = HttpUrl.COMMONURL + "/getDoctorContacts/" + searchText + "?token=" + accessToken;
//                        NotificationGetTask getData = new NotificationGetTask();
//                        getData.delegate = DoctorMyGroupsActivty.this;
//                        getData.execute(url);
//                    } else {
//                        Intent intent = new Intent(DoctorMyGroupsActivty.this, LoginActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                    }
//
//                }
//                break;
        }
    }

    private void navigateActivities(Class object, String name) {
        Intent intent = new Intent(this, object);
        if(object == TransformActivity.class || object == DoctorDashboard.class) {
            intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
        }
        if(!name.equals("")) {
            intent.putExtra("name", name);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
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

    @Override
    public void response(String result) {
        System.out.println(result);
        if(result != null) {
            try {
                Object json = new JSONTokener(result).nextValue();
                if (json instanceof JSONObject) {
                    JSONObject obj = new JSONObject(result);
                    if(obj.has("message")){
                        if(obj.has("statusCode")) {
                            int statusCode = obj.getInt("statusCode");
                            RefreshAccessToken token = new RefreshAccessToken(this);
                        } else {
                            Toast.makeText(this, "Group Deleted Successfully", Toast.LENGTH_LONG).show();
                        }
                        startActivity(getIntent());
                    } else {
                        Toast.makeText(this, "Something went wrong, please try after some time.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    JSONArray array = new JSONArray(result);
                    if(array.length() > 0) {
                        resultArray = array;
                        initializeGroupListView(array);
                    } else {
                        TextView noGroups = (TextView)findViewById(R.id.noGroups);
                        noGroups.setVisibility(View.VISIBLE);
                        TextView navigateCreateGroup = (TextView)findViewById(R.id.navigateCreateGroup);
                        navigateCreateGroup.setVisibility(View.VISIBLE);
                        navigateCreateGroup.setText(Html.fromHtml("<a href=''> Click here to add </a>"));
                        navigateCreateGroup.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(DoctorMyGroupsActivty.this, CreateGroupActivity.class);
                                startActivity(intent);
                            }
                        });
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Something went wrong, please try after sometime.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        navigateActivities(DoctorDashboard.class, "");
    }
}
