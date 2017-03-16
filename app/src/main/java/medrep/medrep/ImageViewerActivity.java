package medrep.medrep;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.db.DetailedNotification;
import com.app.db.MedRepDatabaseHandler;
import com.app.fragments.DoctorNavigationDrawerFragment;
import com.app.fragments.PharmaNavigationDrawerFragment;
import com.app.util.HttpUrl;
import com.app.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by kishore on 19/11/15.
 */
public class ImageViewerActivity extends AppCompatActivity{

    public static ArrayList<Integer> DETAILED_NOTIFICATION_IDS = new ArrayList();

    public static final String IS_FROM_DOCTOR_KEY = "IsFromDoctor";
    private boolean isFromDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isFromDoctor = getIntent().getBooleanExtra(IS_FROM_DOCTOR_KEY, false);

        if(isFromDoctor){
            setContentView(R.layout.doctor_image_viewer);
        }else{
            setContentView(R.layout.pharma_image_viewer);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.notification_view_pager);
        viewPager.setAdapter(new MyPagerAdapter());

        ImageView backIV = (ImageView) findViewById(R.id.back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        /*if(isFromDoctor){
            DoctorNavigationDrawerFragment drawerFragment = (DoctorNavigationDrawerFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

            drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);
        }else{
            PharmaNavigationDrawerFragment drawerFragment = (PharmaNavigationDrawerFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

            drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);
        }*/

    }


    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return DETAILED_NOTIFICATION_IDS.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            final int detailId = DETAILED_NOTIFICATION_IDS.get(position);

            View rootView = View.inflate(ImageViewerActivity.this, R.layout.image_viewer_item, null);

            final PinchZoomImageView notificationImage = (PinchZoomImageView) rootView.findViewById(R.id.imageView);

            Bitmap bmp = null;

            if(isFromDoctor){
                bmp = MedRepDatabaseHandler.getInstance(ImageViewerActivity.this).
                        getDetailedNotificationImage(detailId);
            }else{
                bmp = MedRepDatabaseHandler.
                        getInstance(ImageViewerActivity.this).
                        getPharmaDetailedNotificationImage(detailId);
            }

            if (bmp == null && Utils.isNetworkAvailableWithOutDialog(ImageViewerActivity.this)) {
                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(Void... params) {
                        String url;
                        Bitmap bmp;

                        if(isFromDoctor){
                            url = HttpUrl.GET_NOTIFICATION_CONTENT + detailId +
                                    "?access_token=" + Utils.GET_ACCESS_TOKEN(ImageViewerActivity.this);

                            bmp = getBitmapFromURL(url);

                            if(bmp != null){
                                ContentValues values = new ContentValues();
                                values.put(DetailedNotification.COLUMN_NAMES.IMAGE_CONTENT, Utils.bitmapToBase64(bmp));

                                MedRepDatabaseHandler.getInstance(ImageViewerActivity.this).updateDetailedNotification(values, detailId);
                            }

                        }else{
                            url = HttpUrl.PHARMA_GET_NOTIFICATION_CONTENT + detailId +
                                    "?access_token=" + Utils.GET_ACCESS_TOKEN(ImageViewerActivity.this);

                            bmp = getBitmapFromURL(url);
                            if(bmp != null){
                                MedRepDatabaseHandler.getInstance(ImageViewerActivity.this).
                                        updatePharmaDetailedNotificationImage(
                                                Utils.bitmapToBase64(bmp),
                                                detailId);
                            }
                        }

                        System.out.println("url: " + url);

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
                        }

                    }


                    public Bitmap getBitmapFromURL(String src) {
                        try {
                            URL url = new URL(src);
                            HttpURLConnection connection = (HttpURLConnection) url
                                    .openConnection();
                            connection.setDoInput(true);
                            connection.connect();
                            InputStream input = connection.getInputStream();
                            Bitmap myBitmap = BitmapFactory.decodeStream(input);
                            return myBitmap;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                }.execute();
            } else {
                openImage(notificationImage, bmp);
            }

            container.addView(rootView);

            return rootView;
        }

        private void openImage(final PinchZoomImageView notificationImage, final Bitmap bitmap) {
            notificationImage.setImageBitmap(bitmap);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
