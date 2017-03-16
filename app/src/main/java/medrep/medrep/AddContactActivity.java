package medrep.medrep;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

public class AddContactActivity extends AppCompatActivity implements View.OnClickListener, GetResponse{

    private AutoCompleteTextView searchConnections;
    private int[] buttonIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //findViewById(R.id.menuButton).setOnClickListener(this);
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(this);

        searchConnections = (AutoCompleteTextView)findViewById(R.id.searchConnections);
        String names[] = {"John Deo", "Joseph", "Paul Waker", "Pavan Kumar"};
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
                    String url = HttpUrl.COMMONURL + "/contactSearch/" + searchText + "?token=" + accessToken;
                    NotificationGetTask getData = new NotificationGetTask();
                    getData.delegate = AddContactActivity.this;
                    getData.execute(url);
                }
            }
        });

        Button tranform = (Button)findViewById(R.id.transform);
        tranform.setOnClickListener(this);
        Button serve = (Button)findViewById(R.id.serve);
        serve.setOnClickListener(this);
        Button share = (Button)findViewById(R.id.share);
        share.setOnClickListener(this);
        //initializeListView();
        initialieHorizontalView();
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void initialieHorizontalView() {
        //final ListView listViewObject = listView;
        final String names[] = {"My Contacts", "Suggested Contacs", "My Groups", "Suggested Groups"};
        buttonIds = new int[]{R.id.button1, R.id.button2, R.id.button3, R.id.button4};
        LinearLayout horizontalLayout = (LinearLayout)findViewById(R.id.horizontalLayout);
        for(int i = 0; i < names.length; i++) {
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 51);
            Button tv=new Button(this);
            //tv.setCompoundDrawablePadding(15);
            tv.setBackgroundResource(R.color.tect_color_white);
            if(i == 0) {
                tv.setBackgroundResource(R.color.d_gray);
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

    private void initializeListView(){
        ListView showContacts = (ListView)findViewById(R.id.showContacts);
        DoctorGroupListViewAdapter customAdapter = new DoctorGroupListViewAdapter(this, "addContact");
        showContacts.setAdapter(customAdapter);
    }

    @Override
    public void onClick(View v) {
        int value = v.getId();
        switch (value) {
//            case R.id.menuButton:
//                onMenuClick();
//                break;
            case R.id.transform:
                navigateActivities(TransformActivity.class, "");
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                break;
            case R.id.serve:
                navigateActivities(ServeActivity.class, "");
                break;
            case R.id.share:
                Utils.DISPLAY_GENERAL_DIALOG(this, "Coming Soon...", "This Feature is presently under development.");
                break;
            case R.id.onBackClick:
                navigateActivities(DoctorDashboard.class, "");
                break;
            case R.id.button2:
                clickButton(R.id.button2);
                navigateActivities(DoctorsMyContactActivity.class, "myContacts");
                break;
            case R.id.button3:
                clickButton(R.id.button3);
                navigateActivities(DoctorMyGroupsActivty.class, "myGroups");
                break;
            case R.id.button4:
                clickButton(R.id.button4);
                navigateActivities(DoctorMyGroupsActivty.class, "mySuggestedGroups");
                break;
        }
    }

    private void clickButton(int id) {
        for(int i = 0; i < buttonIds.length; i++) {
            if(buttonIds[i] != id) {
                findViewById(buttonIds[i]).setBackgroundResource(R.color.tect_color_white);
            } else {
                findViewById(buttonIds[i]).setBackgroundResource(R.color.d_gray);
            }
        }
    }

    private void navigateActivities(Class object, String name) {
        Intent intent = new Intent(this, object);
        //if(object == DoctorDashboard.class) {
        if(!name.equals(name)) {
            intent.putExtra("name", name);
        }
            intent.setFlags( Intent.FLAG_ACTIVITY_SINGLE_TOP );
        //}

        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        navigateActivities(DoctorDashboard.class, "");
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
                    JSONArray array = new JSONArray(result);
                    CommonAdapter adapter = new CommonAdapter(this, array);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Something went wrong, please try after sometime.", Toast.LENGTH_LONG).show();
        }
    }

//    private void onMenuClick() {
//        RelativeLayout parent = (RelativeLayout)findViewById(R.id.dashboard);
//        final View child = getLayoutInflater().inflate(R.layout.menu_layout, null);
//        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in);
//        child.setAnimation(fadeInAnimation);
//        LinearLayout transparentArea = (LinearLayout)child.findViewById(R.id.transparentArea);
//
//        transparentArea.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                child.setVisibility(View.GONE);
//            }
//        });
//        ListView list_slidermenu = (ListView)child.findViewById(R.id.list_slidermenu);
//        DoctorGroupListViewAdapter adapter = new DoctorGroupListViewAdapter(this, "slideMenuActivity");
//        list_slidermenu.setAdapter(adapter);
//        parent.addView(child);
//    }
}
