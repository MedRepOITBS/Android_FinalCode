package medrep.medrep;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adapter.CommonAdapter;
import com.app.adapter.DoctorGroupListViewAdapter;
import com.app.interfaces.GetResponse;
import com.app.pojo.SignIn;
import com.app.task.HttpPost;
import com.app.task.NotificationGetTask;
import com.app.util.HttpUrl;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.app.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener, GetResponse{

    public ImageView previewImage;
    private JSONArray array;
    private static int REQUEST_CAMERA = 0;
    private static int SELECT_FILE = 1;
    private Dialog dialog;
    private String base64;
    private Button ok;
    private String loginTimeStamp;
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("Share");
//        Button share = (Button)findViewById(R.id.share);
//        share.setBackground(getResources().getDrawable(R.drawable.border_button_light));
        HorizontalScrollView horizontalScroll = (HorizontalScrollView)findViewById(R.id.horizontalScroll);
        horizontalScroll.setVisibility(View.GONE);
        LinearLayout addLayout = (LinearLayout)findViewById(R.id.addLayout);
        addLayout.setBackgroundColor(getResources().getColor(R.color.l_blue));
        ImageButton addButton = (ImageButton)findViewById(R.id.addButton);
        //addButton.setBackgroundColor(getResources().getColor(R.color.l_blue));
        addButton.setBackgroundResource(R.drawable.edit);
        addButton.setOnClickListener(this);
        Button globalShare = (Button)findViewById(R.id.global_share);
        globalShare.setOnClickListener(this);
//        Button transform = (Button)findViewById(R.id.transform);
//        transform.setOnClickListener(this);
//        Button connect = (Button)findViewById(R.id.connect);
//        connect.setOnClickListener(this);
//        Button serve = (Button)findViewById(R.id.serve);
//        serve.setOnClickListener(this);
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setVisibility(View.GONE);
//        onBackClick.setOnClickListener(this);
//        ListView doctorPostListView = (ListView)findViewById(R.id.doctorPostListView);
//        doctorPostListView.getBackground().setAlpha(120);
//        DoctorGroupListViewAdapter adapter = new DoctorGroupListViewAdapter(this, "doctorPostActivity");
//        doctorPostListView.setAdapter(adapter);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == SELECT_FILE)
//                onSelectFromGalleryResult(data);
//            else if (requestCode == REQUEST_CAMERA)
//                onCaptureImageResult(data);
//        }
//    }

    @Override
    public void onClick(View v) {
        int viewObject = v.getId();
        switch (viewObject) {
            case R.id.global_share:
                finish();
                break;
            case R.id.addButton:
                dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(R.layout.custom_alert_view_new_post);
//                dialog.setTitle("New Post");
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setAttributes(lp);
                ImageButton camera = (ImageButton)dialog.findViewById(R.id.camera);
                camera.setOnClickListener(this);
                ImageButton gallery = (ImageButton)dialog.findViewById(R.id.gallery);
                gallery.setOnClickListener(this);
                Button cancel = (Button)dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                final EditText postMessage = (EditText)dialog.findViewById(R.id.postMessage);
                postMessage.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(count > 0) {
                            ok.setEnabled(true);
                            ok.setBackgroundColor(getResources().getColor(R.color.home_button));
                        } else {
                            ok.setEnabled(false);
                            ok.setBackgroundColor(getResources().getColor(R.color.d_gray));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                ok = (Button)dialog.findViewById(R.id.ok);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            System.out.println("coming here after clicking ok button");
                            JSONObject object = new JSONObject();
                            object.put("detail_desc", postMessage.getText().toString());
                            object.put("title_desc", postMessage.getText().toString());
                            object.put("short_desc", postMessage.getText().toString());
                            JSONObject postData = new JSONObject();
                            postData.put("postType", 6);
                            if(base64 != null) {
                                postData.put("fileData", base64);
                                postData.put("fileName", "Image " +loginTimeStamp);
                            } else {
//                                postData.put("fileData", "");
//                                postData.put("fileName", "");
                            }

                            object.put("postMessage", postData);
                            System.out.println("object" + object);
                            HttpPost post = new HttpPost(object);
                            post.delegate = ShareActivity.this;
                            String url = HttpUrl.COMMONURL + "/postShare?token=" + SignIn.GET_ACCESS_TOKEN();
                            post.execute(url);
                            dialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                if(dialog != null) {
                    dialog.show();
                }
                break;

            case R.id.camera:
                cameraIntent();
                break;
            case R.id.gallery:
                galleryIntent();
                break;
//            case R.id.transform:
//                navigateActivities(TransformActivity.class, "");
//                break;
//            case R.id.connect:
//                navigateActivities(DoctorsMyContactActivity.class, "");
//                break;
//            case R.id.serve:
//                //Utils.DISPLAY_GENERAL_DIALOG(this, "Coming Soon...", "This Feature is presently under development.");
//                navigateActivities(ServeActivity.class, "");
//                break;
//            case R.id.onBackClick:
//                //navigateActivities(DoctorDashboard.class, "");
//                finish();
//                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        System.out.println("current date:" + timeStamp);
        String ip = HttpUrl.COMMONURL;
        String accessToken = SignIn.GET_ACCESS_TOKEN();
        SharedPreferences singInPref = getSharedPreferences(SplashScreen.SIGNIN_PREF, MODE_PRIVATE);
        loginTimeStamp = singInPref.getString("timeStamp", "");
        String url = ip + "/myWall?token=" + accessToken + "&timeStamp=" + loginTimeStamp;
        NotificationGetTask getData = new NotificationGetTask();
        getData.delegate = this;
        getData.execute(url);
    }

    private void navigateActivities(Class object, String name) {
        Intent intent = new Intent(this, object);
        //if(object == TransformActivity.class || object == DoctorDashboard.class) {
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //}
        if(!name.equals("")) {
            intent.putExtra("name", name);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    private void bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        ImageView previewImage = (ImageView)dialog.findViewById(R.id.previewImage);
        BitmapDrawable ob;
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    //ok.setBackgroundColor(getResources().getColor(R.color.home_button));
                    Uri selectedImage = imageReturnedIntent.getData();
                    Toast.makeText(this, "coming here", Toast.LENGTH_LONG).show();

                    if(selectedImage != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            //myBase64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 30);
                            //System.out.print(myBase64Image);
                            //ob = new BitmapDrawable(getResources(), bitmap);
                            //ImageView previewImage = (ImageView)dialog.findViewById(R.id.previewImage);
                            previewImage.setImageBitmap(bitmap);
                            bitmapToBase64(bitmap);
                            //BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), ob);
                            //previewImage.setBackground(ob);
                            //imageView.setBackgroundDrawable(ob);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(this, "Please select image", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    if(selectedImage != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                           // ob = new BitmapDrawable(getResources(), bitmap);
                            previewImage.setImageBitmap(bitmap);
                            bitmapToBase64(bitmap);
                            //System.out.print(myBase64Image);
                            //imageView.setBackgroundDrawable(ob);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(this, "Please select image", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void initializeArray(final JSONArray resultArray) {
        ListView doctorPostListView = (ListView)findViewById(R.id.doctorPostListView);
        CommonAdapter adapter = new CommonAdapter(this, resultArray);
        doctorPostListView.setAdapter(adapter);
        doctorPostListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject object = array.getJSONObject(position);
                    int topicId = object.getInt("topic_id");
                    Intent intent = new Intent(ShareActivity.this, ShareParticularActivity.class);
                    intent.putExtra("topicId", topicId);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void response(String result) {
        System.out.print("result for share:"+ result);
        if(result != null) {
            try {
                Object json = new JSONTokener(result).nextValue();
                if(json instanceof JSONArray) {
//                    array = new JSONArray(result);
//                    initializeArray(array);
                } else {
                    JSONObject object = new JSONObject(result);
                    String status = object.getString("status");
                    if(status.equals("success")){
                        if(object.has("result")) {
                            array = (JSONArray)object.get("result");
                            initializeArray(array);
                        } else {
                            Toast.makeText(ShareActivity.this, "Posted Successfully.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(ShareActivity.this, "Something went wrong. Please try after sometime", Toast.LENGTH_LONG).show();
                        finish();
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
