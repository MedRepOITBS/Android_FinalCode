package pharma;

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
import android.widget.TextView;
import android.widget.Toast;

import com.app.db.DetailedNotification;
import com.app.fragments.PharmaNavigationDrawerFragment;
import com.app.util.HttpUrl;
import com.app.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import medrep.medrep.PinchZoomImageView;
import medrep.medrep.R;

/**
 * Created by kishore on 19/11/15.
 */
public class BrochureViewerActivity extends AppCompatActivity{

    public static ArrayList<DetailedNotification> notificationDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(notificationDetails != null && notificationDetails.size() > 0){
            setContentView(R.layout.pharma_image_viewer);

            ((TextView)findViewById(R.id.notification_title)).setText("Brochures");

            ViewPager viewPager = (ViewPager) findViewById(R.id.notification_view_pager);
            viewPager.setAdapter(new MyPagerAdapter(notificationDetails));

            ImageView backIV = (ImageView) findViewById(R.id.back);
            backIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }else{
            Toast.makeText(this, "No brouchers found", Toast.LENGTH_SHORT).show();
            finish();
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        PharmaNavigationDrawerFragment drawerFragment = (PharmaNavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);

    }


    private class MyPagerAdapter extends PagerAdapter {

        private ArrayList<DetailedNotification> notificationDetails;

        public MyPagerAdapter(ArrayList<DetailedNotification> notificationDetails){
            this.notificationDetails = notificationDetails;
        }


        @Override
        public int getCount() {
            return notificationDetails.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            final DetailedNotification detailedNotification = notificationDetails.get(position);

            View rootView = View.inflate(BrochureViewerActivity.this, R.layout.image_viewer_item, null);

            final PinchZoomImageView notificationImage = (PinchZoomImageView) rootView.findViewById(R.id.imageView);

            AsyncTask<Void, Void, Bitmap> task = new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... params) {
                    String url = HttpUrl.GET_NOTIFICATION_CONTENT + detailedNotification.getDetailId() +
                            "?access_token=" + Utils.GET_ACCESS_TOKEN(BrochureViewerActivity.this);

                    return getBitmapFromURL(url);
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected void onPostExecute(final Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    if (bitmap != null) {
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
                        /*BitmapFactory.Options options=new BitmapFactory.Options();// Create object of bitmapfactory's option method for further option use
                        options.inPurgeable = true;
                        options.inSampleSize=2;

                        options.inPreferredConfig = Bitmap.Config.RGB_565;

                        Bitmap myBitmap = BitmapFactory.decodeStream(input,null,options);*/

                        Bitmap myBitmap = BitmapFactory.decodeStream(input);

                        return myBitmap;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };
            if(Utils.isNetworkAvailableWithOutDialog(BrochureViewerActivity.this)){
                task.execute();
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
