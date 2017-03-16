package pharma;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.db.DetailedNotification;
import com.app.db.Notification;
import com.app.fragments.PharmaNavigationDrawerFragment;
import com.app.interfaces.GetResponse;
import com.app.task.NotificationGetTask;
import com.app.util.HttpUrl;
import com.app.util.JSONParser;
import com.app.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import medrep.medrep.R;

import static medrep.medrep.R.styleable.Toolbar;

/**
 * Created by kishore on 10/11/15.
 */
public class PharmaBrochureActivity extends AppCompatActivity implements GetResponse {

    public static final int OPEN_IMAGE_REQ_CODE = 100;

    public static final String NOTIFICATION_ID_KEY = "NotificationID";
    private int notificationID = -1;
    private ImageView brandDetailImage;
    private VideoView brandDetailVideo;
    private TextView notDetailTextHeading;
    private TextView notDetailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pharma_brochure);

        brandDetailImage = (ImageView) findViewById(R.id.brand_detail_image);
        brandDetailVideo = (VideoView)findViewById(R.id.brand_detail_video);
        notDetailTextHeading = (TextView)findViewById(R.id.not_detail_text_heading);
        notDetailText = (TextView)findViewById(R.id.not_detail_text);

        ((TextView)findViewById(R.id.back_tv)).setText("Brochure");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        notificationID = getIntent().getIntExtra(NOTIFICATION_ID_KEY, -1);
        NotificationGetTask notificationGetTask = new NotificationGetTask();
        notificationGetTask.delegate = this;
        String url = HttpUrl.PHARMA_GET_NOTIFICATION_BY_ID + notificationID + "?access_token=" + Utils.GET_ACCESS_TOKEN(PharmaBrochureActivity.this);
        notificationGetTask.execute(url);

        //viewBrochure();

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        PharmaNavigationDrawerFragment drawerFragment = (PharmaNavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);
    }

    @Override
    public void response(String result) {
        System.out.println(result);
        if(!result.isEmpty()) {
            try {
                JSONObject object = new JSONObject(result);
                JSONArray notifcationObject = object.getJSONArray("notificationDetails");
                JSONObject notificationDetails = (JSONObject) notifcationObject.get(0);
                String contentType = notificationDetails.getString("contentType");
                String detailTitle = notificationDetails.getString("detailTitle");
                String detailDesc = notificationDetails.getString("detailDesc");
                notDetailText.setText(detailDesc);
                notDetailTextHeading.setText(detailTitle);
                final String contentName = notificationDetails.getString("contentName");
                if(contentType.equals("JPG")) {
                    AsyncTask<String, Void, Bitmap> asyncTask = new AsyncTask<String, Void, Bitmap>() {
                        @Override
                        protected Bitmap doInBackground(String... params) {
                            URL url = null;
                            String response = null;
                            try {
                                url = new URL(contentName);
                                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                return image;
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Bitmap res) {
//                            byte [] encodeByte=Base64.decode(res, Base64.DEFAULT);
//                            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//                            System.out.println(bitmap);
                            brandDetailImage.setImageBitmap(res);
                        }
                    };
                    asyncTask.execute();
                } else {
                    Uri uri = Uri.parse(contentName);
                    brandDetailVideo.setVideoURI(uri);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
//
//    private void viewBrochure() {
//
//        AsyncTask<Void, Void, Notification> async = new AsyncTask<Void, Void, Notification>() {
//
//            ProgressDialog pd;
//
//            @Override
//            protected Notification doInBackground(Void... params) {
//
//                String url = HttpUrl.PHARMA_GET_NOTIFICATION_BY_ID +
//                        notificationID +
//                        "?access_token=" + Utils.GET_ACCESS_TOKEN(PharmaBrochureActivity.this);
//
//                JSONParser parser = new JSONParser();
//                JSONObject jsonObject = parser.getJSON_Response(url, true);
//
//                Notification notification = null;
//                try {
//                    notification = (Notification) parser.jsonParser(jsonObject, Notification.class);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                return notification;
//            }
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//
//                pd = new ProgressDialog(PharmaBrochureActivity.this);
//                pd.setTitle("Retrieving Brochure");
//                pd.setMessage("Please wait while retrieving brochure.");
//                pd.setCancelable(false);
//                pd.show();
//
//            }
//
//            @Override
//            protected void onPostExecute(Notification notification) {
//                super.onPostExecute(notification);
//
//                if(pd != null && pd.isShowing()){
//                    pd.dismiss();
//                }
//
//                if(notification == null){
//                   Toast.makeText(PharmaBrochureActivity.this,
//                    "No brochure found.", Toast.LENGTH_SHORT).show();
//                    finish();
//                    return;
//                }
//
//                final ViewPager pager = (ViewPager) findViewById(R.id.notification_view_pager);
//                pager.setAdapter(new BrochureAdapter(notification));
//            }
//        };
//
//        if(Utils.isNetworkAvailableWithOutDialog(PharmaBrochureActivity.this) && notificationID != -1){
//            async.execute();
//        }else{
//            Toast.makeText(PharmaBrochureActivity.this,
//                    "Could not retrieve brochure. " +
//                            "Please connect to internet and try again.", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//
//    }
//
//    private class BrochureAdapter extends PagerAdapter {
//        private ArrayList<DetailedNotification> notificationDetails;
//        Notification notification;
//
//        public int getStatusBarHeight() {
//            int result = 0;
//            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//            if (resourceId > 0) {
//                result = getResources().getDimensionPixelSize(resourceId);
//            }
//            return result;
//        }
//
//        public BrochureAdapter(Notification notification){
//            this.notification = notification;
//            notificationDetails = notification.getNotificationDetails();
//        }
//
//        @Override
//        public int getCount() {
//            return notificationDetails.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, final int position) {
//
//            final DetailedNotification detailedNotification = notificationDetails.get(position);
//
//            View rootView = View.inflate(PharmaBrochureActivity.this, R.layout.detailed_notification, null);
//
//            Point size = new Point();
//            PharmaBrochureActivity.this.getWindowManager().getDefaultDisplay().getSize(size);
//            int screenHeight = size.y;
//
//            LinearLayout layout = (LinearLayout)rootView.findViewById(R.id.layout1);
//
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();
//            params.height = screenHeight - (getStatusBarHeight() * 3);
//
//            final ImageView notificationImage = (ImageView) rootView.findViewById(R.id.brand_detail_image);
//            ((TextView) rootView.findViewById(R.id.not_detail_text_heading)).setText(detailedNotification.getDetailTitle());
//            ((TextView) rootView.findViewById(R.id.not_detail_text)).setText(detailedNotification.getDetailDesc());
//
//
//            AsyncTask<Void, Void, Bitmap> task = new AsyncTask<Void, Void, Bitmap>() {
//                @Override
//                protected Bitmap doInBackground(Void... params) {
//                    String url = HttpUrl.GET_NOTIFICATION_CONTENT + detailedNotification.getDetailId() +
//                            "?access_token=" + Utils.GET_ACCESS_TOKEN(PharmaBrochureActivity.this);
//
//                    return getBitmapFromURL(url);
//                }
//
//                @Override
//                protected void onPreExecute() {
//                    super.onPreExecute();
//                }
//
//                @Override
//                protected void onPostExecute(final Bitmap bitmap) {
//                    super.onPostExecute(bitmap);
//                    if (bitmap != null) {
//                        openImage(notificationImage, bitmap);
//                    }
//                }
//
//                public Bitmap getBitmapFromURL(String src) {
//                    try {
//                        URL url = new URL(src);
//                        HttpURLConnection connection = (HttpURLConnection) url
//                                .openConnection();
//                        connection.setDoInput(true);
//                        connection.connect();
//                        InputStream input = connection.getInputStream();
//                        /*BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
//                        options.inPurgeable = true;
//                        options.inSampleSize=2;
//
//                        options.inPreferredConfig = Bitmap.Config.RGB_565;
//
//                        Bitmap myBitmap = BitmapFactory.decodeStream(input,null,options);*/
//
//                        Bitmap myBitmap = BitmapFactory.decodeStream(input);
//
//                        return myBitmap;
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        return null;
//                    }
//                }
//            };
//            if(Utils.isNetworkAvailableWithOutDialog(PharmaBrochureActivity.this)){
//                task.execute();
//            }
//
//            container.addView(rootView);
//
//            return rootView;
//        }
//
//        private void openImage(ImageView notificationImage, final Bitmap bitmap) {
//
//            notificationImage.setImageBitmap(bitmap);
//
//            notificationImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    /*File imageFile = new File(getExternalCacheDir().getAbsolutePath(), "." + Calendar.getInstance().getTimeInMillis());
//                    if (imageFile.exists()) {
//                        imageFile.delete();
//                    }
//                    try {
//                        imageFile.createNewFile();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    FileOutputStream out = null;
//                    try {
//                        out = new FileOutputStream(imageFile);
//                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
//                        // PNG is a lossless format, the compression factor (100) is ignored
//                        out.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    if (imageFile != null && imageFile.exists()) {
//                        Intent intent = new Intent();
//                        intent.setAction(Intent.ACTION_VIEW);
//                        intent.setDataAndType(Uri.parse("file://" + imageFile.getAbsolutePath()), "image*//*");
//                        startActivityForResult(intent, OPEN_IMAGE_REQ_CODE);
//                    }*/
//
//                    BrochureViewerActivity.notificationDetails = notificationDetails;
//
//                    Intent intent = new Intent(PharmaBrochureActivity.this, BrochureViewerActivity.class);
//                    startActivity(intent);
//
//                }
//            });
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView((ScrollView)object);
//        }
//    }
}
