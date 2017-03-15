package medrep.medrep;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.interfaces.GetResponse;
import com.app.pojo.SignIn;
import com.app.task.NotificationGetTask;
import com.app.util.HttpUrl;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;

import static android.R.attr.data;

public class MyShareActivity extends AppCompatActivity implements GetResponse, View.OnClickListener{
    private ListView myShareListView;
    private JSONArray array;
    private ImageLoader imageLoader;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_share);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        TextView title = (TextView)findViewById(R.id.title);
        title.setText("My Share");
        HorizontalScrollView horizontalScroll = (HorizontalScrollView)findViewById(R.id.horizontalScroll);
        horizontalScroll.setVisibility(View.GONE);
        Button share = (Button)findViewById(R.id.share);
        share.setBackground(getResources().getDrawable(R.drawable.border_button_light));
        Button transform = (Button)findViewById(R.id.transform);
        transform.setOnClickListener(this);
        Button connect = (Button)findViewById(R.id.connect);
        connect.setOnClickListener(this);
        Button serve = (Button)findViewById(R.id.serve);
        serve.setOnClickListener(this);
        Button onBackClick = (Button)findViewById(R.id.onBackClick);
        onBackClick.setOnClickListener(this);
        Button globalShare = (Button)findViewById(R.id.global_share);
        globalShare.setOnClickListener(this);
        LinearLayout addLayout = (LinearLayout)findViewById(R.id.addLayout);
        addLayout.setVisibility(View.GONE);
        getShareData();


        myShareListView = (ListView)findViewById(R.id.my_share_list_view);
        imageLoader = ImageLoader.getInstance();

    }

    private void getShareData() {
        String url = HttpUrl.COMMONURL + "/getShare?token=" + SignIn.GET_ACCESS_TOKEN();
        NotificationGetTask notificationGetTask = new NotificationGetTask();
        notificationGetTask.delegate = this;
        notificationGetTask.execute(url);
    }

    private void sendDataToAdapter() {
        ListAdapter listAdapter = new ListAdapter();
        myShareListView.setAdapter(listAdapter);
    }

    @Override
    public void response(String result) {
        System.out.println("my share" + result);
        try {
            Object json = new JSONTokener(result).nextValue();
            if(json instanceof JSONArray) {
                JSONArray array = new JSONArray(result);
                this.array = array;
                sendDataToAdapter();
            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
            case R.id.global_share:
                navigateActivities(ShareActivity.class, "");
                break;
        }
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

    private class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return array.length();
        }

        @Override
        public Object getItem(int position) {
            try {
                return array.get(position);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_share_list_view, null);
            try {
                JSONObject object = (JSONObject) array.get(position);
                String doctorName = object.getString("doctor_Name");
                String shortDesc = object.getString("title_desc");
                String displayPicture = object.getString("displayPicture");
                //Uri uri = Uri.parse(displayPicture);
                ImageView sharedPersonImage = (ImageView)convertView.findViewById(R.id.shared_person_image);
                ImageAsync async = new ImageAsync(sharedPersonImage);
                async.execute(displayPicture);
                //sharedPersonImage.setImageBitmap(bmp);
                TextView personName = (TextView)convertView.findViewById(R.id.person_name);
                personName.setText(doctorName);
                TextView sharedMessage = (TextView)convertView.findViewById(R.id.shared_message);
                if(shortDesc != null) {
                    sharedMessage.setText(shortDesc);
                } else {
                    sharedMessage.setText("");
                }

                TextView sharedMessageDate = (TextView)convertView.findViewById(R.id.shared_message_date);
                long postedOn = object.getLong("posted_on");
                String dateTime = getDate(postedOn);
                sharedMessageDate.setText(dateTime);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return convertView;
        }

        private String getDate(long time) {
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(time);
            String date = DateFormat.format("dd-MM-yyyy HH:mm:ss", cal).toString();
            return date;
        }

        private class ImageAsync extends AsyncTask<String, Void, Bitmap> {
            ImageView view;

            private ImageAsync (ImageView view) {
                this.view = view;
            }

            @Override
            protected Bitmap doInBackground(String... params) {
                URL url = null;
                try {
                    url = new URL(params[0]);
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    return bmp;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bmp) {
                view.setImageBitmap(bmp);
            }
        }
    }
}
