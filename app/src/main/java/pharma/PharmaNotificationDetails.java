package pharma;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.db.MedRepDatabaseHandler;
import com.app.fragments.PharmaNavigationDrawerFragment;
import com.app.pharma.PharmaNotification;
import com.app.util.HttpUrl;
import com.app.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import medrep.medrep.ImageViewerActivity;
import medrep.medrep.R;


/**
 * Created by admin on 9/27/2015.
 */
public class PharmaNotificationDetails extends AppCompatActivity{


    public ArrayList<PharmaNotification.NotificationDetail> currentNotificationDetails;
    //    private static AbstractMap CURRENT_NOTIFICATION_DETAILS = ;
    public static PharmaNotification CURRENT_NOTIFICATION;

    public static final int OPEN_IMAGE_REQ_CODE = 100;
    private String contentLocation;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        System.out.println("requestCode == OPEN_IMAGE_REQ_CODE: " + (requestCode == OPEN_IMAGE_REQ_CODE));

        if(requestCode == OPEN_IMAGE_REQ_CODE){
            /*File imageFile = new File(getActivity().getExternalCacheDir().getAbsolutePath(), ".temp");
            if(imageFile != null && imageFile.exists()){
                imageFile.delete();
            }*/
            clearApplicationData(getExternalCacheDir().getAbsolutePath());
        }
    }

    public static void clearApplicationData(String path)
    {
        try {
            Runtime.getRuntime().exec(String.format("rm -rf %s", path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharma_notification_details);

        if(CURRENT_NOTIFICATION == null){
            finish();
        }

        final String notificationName = getIntent().getExtras().getString("title");
//        TextView notificationTitle = (TextView)findViewById(R.id.notification_title);
//        notificationTitle.setText(notificationName);


        currentNotificationDetails = CURRENT_NOTIFICATION.getNotificationDetails();
        for(int i = 0; i < currentNotificationDetails.size(); i++) {
            System.out.println("currentNotificationDetails" + currentNotificationDetails.get(i));
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.notification_view_pager);

        if(currentNotificationDetails.size() > 0){
            viewPager.setAdapter(new MyPagerAdapter());
        }else{
            finish();
            Toast.makeText(PharmaNotificationDetails.this, "No notifications found.", Toast.LENGTH_SHORT).show();
        }


        Button viewStatusButton = (Button) findViewById(R.id.view_status_button);

        viewStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            System.out.println(CURRENT_NOTIFICATION.getTotalSentNotification());
            System.out.println(CURRENT_NOTIFICATION.getTotalPendingNotifcation());
            System.out.println(CURRENT_NOTIFICATION.getTotalViewedNotifcation());
            System.out.println(CURRENT_NOTIFICATION.getTotalConvertedToAppointment());

            Intent intent=new Intent(PharmaNotificationDetails.this, PharmaCampainDetails.class);
            intent.putExtra(PharmaCampainDetails.CAMPAIGN_NAME_KEY, CURRENT_NOTIFICATION.getCompanyName());
            intent.putExtra("title", notificationName);
            startActivity(intent);
            }
        });

        ImageView backIV = (ImageView) findViewById(R.id.back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        PharmaNavigationDrawerFragment drawerFragment = (PharmaNavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        CURRENT_NOTIFICATION = null;
    }


    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return currentNotificationDetails.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public int getStatusBarHeight() {
            int result = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = getResources().getDimensionPixelSize(resourceId);
            }
            return result;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final PharmaNotification.NotificationDetail notificationDetail = currentNotificationDetails.get(position);

            View rootView = View.inflate(PharmaNotificationDetails.this, R.layout.detailed_notification, null);

            Point size = new Point();
            PharmaNotificationDetails.this.getWindowManager().getDefaultDisplay().getSize(size);
            int screenHeight = size.y;

            LinearLayout layout = (LinearLayout)rootView.findViewById(R.id.layout1);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();
            params.height = screenHeight - (getStatusBarHeight() * 3);

            final VideoView videoView = (VideoView) rootView.findViewById(R.id.video_view);
            final ImageView notificationImage = (ImageView) rootView.findViewById(R.id.brand_detail_image);
            ((TextView) rootView.findViewById(R.id.not_detail_text_heading)).setText(notificationDetail.getDetailTitle());
            ((TextView) rootView.findViewById(R.id.not_detail_text)).setText(notificationDetail.getDetailDesc());

            System.out.println("notificationDetail.getDetailTitle(): " + notificationDetail.getDetailTitle());
            System.out.println("notificationDetail.getDetailDesc(): " + notificationDetail.getDetailDesc());


           /* String contentName = detailedNotification.getContentName();

            String temp = (contentName != null && !contentName.trim().equals(""))?contentName:"DetailedNotification";

            DownloadImage downloadImage = new DownloadImage(getActivity(), notificationImage, temp + detailedNotification.getDetailId());
            String url = HttpUrl.GET_NOTIFICATION_CONTENT + detailedNotification.getDetailId() + "?access_token=" + Utils.GET_ACCESS_TOKEN();
            downloadImage.execute(url);*/

//            Bitmap bmp = null;
            //System.out.println("notificationDetail.getContentType() " + notificationDetail.getContentType());
            //if(notificationDetail.getContentType() == "JPG") {
                Bitmap bmp = MedRepDatabaseHandler.
                        getInstance(PharmaNotificationDetails.this).
                        getPharmaDetailedNotificationImage(notificationDetail.getDetailId());

                System.out.println("bmp" + bmp);

                if (bmp == null && Utils.isNetworkAvailableWithOutDialog(PharmaNotificationDetails.this)) {
                    new AsyncTask<Void, Void, Bitmap>() {
                        String url;
                        @Override
                        protected Bitmap doInBackground(Void... params) {
                            url = HttpUrl.PHARMA_GET_NOTIFICATION_CONTENT + notificationDetail.getDetailId() +
                                    "?access_token=" + Utils.GET_ACCESS_TOKEN(PharmaNotificationDetails.this);

                            System.out.println("url: " + url);

                            Bitmap bmp = getBitmapFromURL(url);

                            if(bmp != null){
                                MedRepDatabaseHandler.getInstance(PharmaNotificationDetails.this).
                                        updatePharmaDetailedNotificationImage(
                                                Utils.bitmapToBase64(bmp),
                                                notificationDetail.getDetailId());
                            }

                            return bmp;
                        }

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                        }

                        @Override
                        protected void onPostExecute(final Bitmap bitmap) {
                            super.onPostExecute(bitmap);
                            if(bitmap != null){
                                openImage(notificationImage, bitmap);
                            } else {
                                final ProgressDialog progressDialog = new ProgressDialog(PharmaNotificationDetails.this);
                                progressDialog.setCancelable(false);
                                progressDialog.setTitle("MedRep");
                                progressDialog.setMessage("Please wait video is loading...");
                                notificationImage.setVisibility(View.GONE);
                                videoView.setVisibility(View.VISIBLE);
                                final Uri uri = Uri.parse(contentLocation);
                                videoView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //videoView.start();
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        intent.setDataAndType(uri, "video/mp4");
                                        startActivity(intent);
                                    }
                                });
                                videoView.setVideoURI(uri);
                                videoView.requestFocus();
                                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    // Close the progress bar and play the video
                                    public void onPrepared(MediaPlayer mp) {
                                        progressDialog.dismiss();
                                        videoView.start();
                                    }
                                });

                            }

                        }


                        public Bitmap getBitmapFromURL(String src) {
                            StringBuffer response;
                            try {
                                URL url = new URL(src);
                                HttpURLConnection connection = (HttpURLConnection) url
                                        .openConnection();
                                connection.setDoInput(true);
                                connection.connect();
                                int responseCode = connection.getResponseCode();
                                if(responseCode == HttpURLConnection.HTTP_OK) {
                                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                    String inputLine;
                                    response = new StringBuffer();

                                    while ((inputLine = in.readLine()) != null) {
                                        response.append(inputLine);
                                    }
                                    in.close();
                                    System.out.println(response);
                                    JSONObject object = new JSONObject(response.toString());
                                    contentLocation = object.getString("contentLocation");
                                    if(contentLocation != null) {
                                        Bitmap image = null;
                                        char lastChar = contentLocation.charAt(contentLocation.length() - 1);
                                        System.out.println("lastChar " + lastChar);
                                        if(lastChar != 'g') {
                                            System.out.println("lastChar " + lastChar);
                                            //openVideo(contentLocation);
                                        } else {
                                            URL imageUrl = new URL(contentLocation);
                                            image = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                                        }
                                        return image;
                                    } else {
                                        return null;
                                    }

                                }
//                            InputStream input = connection.getInputStream();
//                            Bitmap myBitmap = BitmapFactory.decodeStream(input);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute();
                } else {
                    openImage(notificationImage, bmp);
                }
//            } else {
//                notificationImage.setVisibility(View.GONE);
//                final VideoView videoView = (VideoView) rootView.findViewById(R.id.video_view);
//                new AsyncTask<Void, Void, String>() {
//                    @Override
//                    protected String doInBackground(Void... params) {
//                        String url = HttpUrl.PHARMA_GET_NOTIFICATION_CONTENT + notificationDetail.getDetailId() +
//                                "?access_token=" + Utils.GET_ACCESS_TOKEN(PharmaNotificationDetails.this);
//
//                        System.out.println("url: " + url);
//
//                        return url;
//                    }
//
//                    @Override
//                    protected void onPreExecute() {
//                        super.onPreExecute();
//                    }
//
//                    @Override
//                    protected void onPostExecute(final String bitmap) {
//                        super.onPostExecute(bitmap);
//                        if(bitmap != null){
//                            Uri video = Uri.parse(notificationDetail.getContentName());
//                            videoView.setVideoURI(video);
//                        }
//
//                    }
//                }.execute();
//            }
            container.addView(rootView);

            return rootView;
        }

//        private void openVideo(String url) {
//            System.out.println("coming here to play video");
//            notificationImage.setVisibility(View.GONE);
//            videoView.setVisibility(View.VISIBLE);
//            Uri uri = Uri.parse(url);
//            videoView.setVideoURI(uri);
//        }

        private void openImage(ImageView notificationImage, final Bitmap bitmap) {

            notificationImage.setImageBitmap(bitmap);

            notificationImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(ImageViewerActivity.DETAILED_NOTIFICATION_IDS != null){
                        ImageViewerActivity.DETAILED_NOTIFICATION_IDS.clear();
                    }else{
                        ImageViewerActivity.DETAILED_NOTIFICATION_IDS = new ArrayList<Integer>();
                    }

                    for(PharmaNotification.NotificationDetail not: currentNotificationDetails){
                        ImageViewerActivity.DETAILED_NOTIFICATION_IDS.add(not.getDetailId());
                    }

                    Intent intent = new Intent(PharmaNotificationDetails.this, ImageViewerActivity.class);
                    intent.putExtra(ImageViewerActivity.IS_FROM_DOCTOR_KEY, false);
                    startActivity(intent);

                    /*File imageFile = new File(getExternalCacheDir().getAbsolutePath(), "." + Calendar.getInstance().getTimeInMillis());
                    if(imageFile.exists()){
                        imageFile.delete();
                    }
                    try {
                        imageFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    FileOutputStream out = null;
                    try {
                        out = new FileOutputStream(imageFile);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        // PNG is a lossless format, the compression factor (100) is ignored
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if(imageFile != null && imageFile.exists()){
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse("file://" + imageFile.getAbsolutePath()), "image*//*");
//                                        startActivity(intent);
                        startActivityForResult(intent, OPEN_IMAGE_REQ_CODE);
                    }*/
                }
            });

        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

    }
}
