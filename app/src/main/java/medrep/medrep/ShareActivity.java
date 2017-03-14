package medrep.medrep;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.adapter.CommonAdapter;
import com.app.adapter.DoctorGroupListViewAdapter;
import com.app.interfaces.GetResponse;
import com.app.pojo.SignIn;
import com.app.task.NotificationGetTask;
import com.app.util.HttpUrl;

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
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("Share");
        Button share = (Button)findViewById(R.id.share);
        share.setBackground(getResources().getDrawable(R.drawable.border_button_light));
        HorizontalScrollView horizontalScroll = (HorizontalScrollView)findViewById(R.id.horizontalScroll);
        horizontalScroll.setVisibility(View.GONE);
        LinearLayout searchLayout = (LinearLayout)findViewById(R.id.searchLayout);
        searchLayout.setVisibility(View.GONE);
        Button transform = (Button)findViewById(R.id.transform);
        transform.setOnClickListener(this);
        Button connect = (Button)findViewById(R.id.connect);
        connect.setOnClickListener(this);
        Button serve = (Button)findViewById(R.id.serve);
        serve.setOnClickListener(this);
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(this);
//        ListView doctorPostListView = (ListView)findViewById(R.id.doctorPostListView);
//        doctorPostListView.getBackground().setAlpha(120);
//        DoctorGroupListViewAdapter adapter = new DoctorGroupListViewAdapter(this, "doctorPostActivity");
//        doctorPostListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        int viewObject = v.getId();
        switch (viewObject) {
            case R.id.transform:
                navigateActivities(TransformActivity.class, "");
                break;
            case R.id.connect:
                navigateActivities(DoctorsMyContactActivity.class, "");
                break;
            case R.id.serve:
                //Utils.DISPLAY_GENERAL_DIALOG(this, "Coming Soon...", "This Feature is presently under development.");
                navigateActivities(ServeActivity.class, "");
                break;
            case R.id.onBackClick:
                //navigateActivities(DoctorDashboard.class, "");
                finish();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        System.out.println("current date:" + timeStamp);
        String ip = HttpUrl.COMMONURL;
        String accessToken = SignIn.GET_ACCESS_TOKEN();
        String url = ip + "/getShare?token=" + accessToken;
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        BitmapDrawable ob;
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    Toast.makeText(this, "coming here", Toast.LENGTH_LONG).show();

                    if(selectedImage != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            //myBase64Image = encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 30);
                            //System.out.print(myBase64Image);
                            ob = new BitmapDrawable(getResources(), bitmap);
                            //BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), ob);
                            previewImage.setBackground(ob);
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
                            ob = new BitmapDrawable(getResources(), bitmap);

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
        //System.out.print("result for share:"+ result);
        if(result != null) {
            try {
                Object json = new JSONTokener(result).nextValue();
                if(json instanceof JSONArray) {
                    array = new JSONArray(result);
                    initializeArray(array);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
