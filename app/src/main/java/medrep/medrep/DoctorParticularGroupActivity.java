package medrep.medrep;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adapter.CommonAdapter;
import com.app.adapter.DoctorGroupListViewAdapter;
import com.app.db.Notification;
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

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class DoctorParticularGroupActivity extends AppCompatActivity implements View.OnClickListener, GetResponse{

    private AlertDialog alertDialog = null;
    private AutoCompleteTextView searchConnections;
    //private ListView doctorGroupMembersListView;
    public ArrayList<Integer> selectedId = new ArrayList<>();
    private JSONArray responseArray;
    private Boolean isAdmin = false;
    private ImageView groupIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_particular_group);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Button backButton = (Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        Button removeContactButton = (Button)findViewById(R.id.removeContactButton);
        removeContactButton.setOnClickListener(this);
        Button addButton = (Button)findViewById(R.id.addButton);
        addButton.setOnClickListener(this);
        groupIcon = (ImageView)findViewById(R.id.groupIcon);

        String groupId = getIntent().getExtras().getString("groupId");
        String groupName = getIntent().getExtras().getString("groupName");
        String groupImage = getIntent().getExtras().getString("imageData");
        String shortDesc = getIntent().getExtras().getString("shortDesc");
        String longDesc = getIntent().getExtras().getString("longDesc");

        TextView name = (TextView) findViewById(R.id.groupName);
        name.setText(groupName);



        LoadImage loadImage = new LoadImage();
        loadImage.execute(groupImage);

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
                    getData.delegate = DoctorParticularGroupActivity.this;
                    getData.execute(url);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        String ip = HttpUrl.COMMONURL;
        String accessToken = SignIn.GET_ACCESS_TOKEN();
        String groupId = getIntent().getExtras().getString("groupId");
        System.out.println(accessToken);
        System.out.print(groupId);
        String url = ip + "/getGroupMembers/" + groupId + "?token=" + accessToken;
        NotificationGetTask task = new NotificationGetTask();
        task.delegate = this;
        task.execute(url);
    }


    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        Intent intent;
        String ip = HttpUrl.COMMONURL;
        String accessToken = SignIn.GET_ACCESS_TOKEN();
        String url;
        String groupId = getIntent().getExtras().getString("groupId");
        switch (viewId) {

            case R.id.backButton:
                finish();
                break;
            case R.id.addButton:
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.custom_alert_dialog_for_add_button, null);
                Button addMembersButton = (Button)dialogView.findViewById(R.id.addMembersButton);
                addMembersButton.setText("Add Members");
                addMembersButton.setOnClickListener(this);
                Button pendingMembersButton = (Button)dialogView.findViewById(R.id.pendingMembersButton);
                pendingMembersButton.setOnClickListener(this);
                Button createGroup = (Button)dialogView.findViewById(R.id.createGroup);
                createGroup.setVisibility(View.VISIBLE);
                createGroup.setText("Update Group");
                createGroup.setOnClickListener(this);
                if(!isAdmin) {
                    addMembersButton.setVisibility(View.GONE);
                    pendingMembersButton.setVisibility(View.GONE);
                    createGroup.setVisibility(View.GONE);
                }
                Button cancelButton  = (Button)dialogView.findViewById(R.id.cancelButton);
                cancelButton.setOnClickListener(this);
                Button leaveGroup = (Button)dialogView.findViewById(R.id.leaveGroup);
                leaveGroup.setVisibility(View.VISIBLE);
                leaveGroup.setOnClickListener(this);
                dialogBuilder.setView(dialogView);
                alertDialog = dialogBuilder.create();
                alertDialog.show();
                break;
            case R.id.addMembersButton:
                intent = new Intent(this, AddGroupMembersActivity.class);
                int id = Integer.parseInt(groupId);
                intent.putExtra("name", id);
                startActivity(intent);
                break;
            case R.id.pendingMembersButton:
                break;
            case R.id.createGroup:
                    if(responseArray.length() > 0) {
                        intent = new Intent(this, CreateGroupActivity.class);
                        intent.putExtra("groupId", groupId);
                        intent.putExtra("groupName", getIntent().getExtras().getString("groupName"));
                        intent.putExtra("shortDesc", getIntent().getExtras().getString("shortDesc"));
                        intent.putExtra("longDesc", getIntent().getExtras().getString("longDesc"));
                        intent.putExtra("imgData", getIntent().getExtras().getString("imageData"));
                        startActivity(intent);
                    }
                break;
            case R.id.leaveGroup:
//                JSONObject object = new JSONObject();
//                try {
//                    object.put("group_id", groupId);
//                    object.put("member_id", "");
//                    object.put("status", "EXIT");
//                    url = ip + "/exitGroup?token=7cd49a0f-da77-4dbd-b7ca-dd73743fdf98";
//                    HttpPost post = new HttpPost(object);
//                    post.delegate = this;
//                    post.execute(url);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                Toast.makeText(this, "Development in progress", Toast.LENGTH_LONG).show();
                alertDialog.cancel();
                break;
            case R.id.cancelButton:
                alertDialog.cancel();
                break;
            case R.id.removeContactButton:
                System.out.println(selectedId);
                if(selectedId.size() == 0) {
                    Utils.DISPLAY_GENERAL_DIALOG(this, "Contacts", "Please select atleast one contact.");
                } else {

                    System.out.print(accessToken);
                    if(accessToken == null) {
                    } else {
                        System.out.println(selectedId);
                        JSONObject object = new JSONObject();
                        try {
                            JSONObject resObject = responseArray.getJSONObject(0);
                            System.out.println(resObject);
                            object.put("group_id", resObject.getInt("group_id"));
                            JSONArray array = new JSONArray(selectedId);
                            object.put("memberList", array);
                            url = ip + "/removeMember?token=" + accessToken;
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

    @Override
    public void response(String result) {
        System.out.println(result);
        if(result != null) {
            try {
                Object json = new JSONTokener(result).nextValue();
                if(json instanceof JSONObject) {
                    JSONObject object = new JSONObject(result);
                        if(object.has("statusCode")) {
                            int statusCode = object.getInt("statusCode");
                            if(statusCode == 405) {
                                RefreshAccessToken token = new RefreshAccessToken(this);
                                startActivity(getIntent());
                            }
                        } else if(object.has("status")){
                            Toast.makeText(this, "Successfully removed from group", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Something went wrong try after sometime.", Toast.LENGTH_LONG).show();
                        }
                } else {
                    JSONArray array = new JSONArray(result);
                    responseArray = array;
                    if(array.length() > 0) {
                        JSONObject object = array.getJSONObject(0);
                        String groupImgData = object.getString("imageUrl");
                        String groupTitle = object.getString("group_name");
                        if(groupImgData != null) {
                            LoadImage loadImage = new LoadImage();
                            loadImage.execute(groupImgData);
//                            byte[] imageAsBytes = Base64.decode(groupImgData.getBytes(), Base64.DEFAULT);
//                            Bitmap map = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
//                            groupIcon.setImageBitmap(map);
                        }
                        TextView groupName = (TextView)findViewById(R.id.groupName);
                        groupName.setText(groupTitle);
                        JSONArray memberArray = object.getJSONArray("member");
                        System.out.println(memberArray);
                        isAdmin = memberArray.getJSONObject(0).getBoolean("is_admin");
                        ListView doctorGroupMembersListView = (ListView)findViewById(R.id.doctorGroupMembersListView);
                        CommonAdapter adapter = new CommonAdapter(this, memberArray);
                        doctorGroupMembersListView.setAdapter(adapter);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        protected Bitmap doInBackground(String... args) {
            Bitmap bitmap = null;
            System.out.println("image url: " + args[0]);
            try {
                //bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
                InputStream in = new URL(args[0]).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("bitmap image*****: " + bitmap);
            return bitmap;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        protected void onPostExecute(Bitmap bitmapImage) {
            if(bitmapImage != null){
                //BitmapDrawable bdrawable = new BitmapDrawable(context.getResources(),bitmapImage);
                groupIcon.setImageBitmap(bitmapImage);
            }
        }
    }
}
