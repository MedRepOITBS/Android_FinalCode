package pharma;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.fragments.PharmaNavigationDrawerFragment;
import com.app.util.HttpUrl;
import com.app.util.JSONParser;
import com.app.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import medrep.medrep.R;
import pharma.model.PharmaRepProfile;

/**
 * Created by samara on 25/10/2015.
 */
public class PharmaRepDetails extends AppCompatActivity {

    public static final String REP_ID_KEY = "RepId";
    private int repId;
    private String name;
    private String emailId;
    private String mobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharma_rep_details);

        final int repID = getIntent().getExtras().getInt(REP_ID_KEY, -1);

        System.out.println("RepID: " + repID);

        if(repID == -1){
            Toast.makeText(this, "Invalid profile.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            JSONArray array = TeamActivity.jsonArray;
            try {
                JSONObject object = (JSONObject) array.get(repID);
                repId = object.getInt("repId");
                name = object.getString("displayName");
                emailId = object.getString("emailId");
                mobileNo = object.getString("mobileNo");

                TextView header_text=(TextView)findViewById(R.id.back_tv);
                header_text.setText(name);

                ((TextView)findViewById(R.id.tv_empid)).setText(repId + "");
                ((TextView)findViewById(R.id.tv_empname)).setText(name);
                ((TextView)findViewById(R.id.tv_email_name)).setText(emailId);
                ((TextView)findViewById(R.id.tv_mobile_name)).setText(mobileNo);
                new DownloadImageTask((ImageView) findViewById(R.id.rep_profile_pic))
                        .execute(object.getString("dPicture"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        GetPharmaRepProfile repProfile = new GetPharmaRepProfile();
//        repProfile.context = PharmaRepDetails.this;
//        repProfile.execute(repID);

        findViewById(R.id.back_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        findViewById(R.id.btn_view_appointments).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PharmaRepDetails.this, PharmaRepAppointmentsActivity.class);
                intent.putExtra(REP_ID_KEY, repId);
                startActivity(intent);
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
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

    public void setImage(String image) {
        URL url = null;
        try {
            url = new URL(image);
            Bitmap bmp  = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            ((ImageView) findViewById(R.id.rep_profile_pic)).setImageBitmap(bmp);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class GetPharmaRepProfile extends AsyncTask<Integer, Void, PharmaRepProfile>{
        private Bitmap bmp;
        public Context context;

//        public GetPharmaRepProfile(Context context) {
//            this.context = context;
//        }
        @Override
        protected PharmaRepProfile doInBackground(Integer... params) {
            String url = HttpUrl.PHARMA_GET_PHARMA_REP_PROFILE + params[0] + "?access_token=" + Utils.GET_ACCESS_TOKEN(PharmaRepDetails.this);

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = parser.getJSON_Response(url, true);
            try {
                JSONObject profile = jsonObject.getJSONObject("profilePicture");
                String imageUrl = profile.getString("imageUrl");
                URL url1 = new URL(imageUrl);
                bmp  = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            PharmaRepProfile repProfile = null;
            try {
                repProfile = (PharmaRepProfile) parser.jsonParser(jsonObject, PharmaRepProfile.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return repProfile;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(PharmaRepProfile pharmaRepProfile) {
            super.onPostExecute(pharmaRepProfile);
            //System.out.println("*************" + pharmaRepProfile.getProfilePicture().getData());

            String name = pharmaRepProfile.getFirstName() + pharmaRepProfile.getLastName();

            if(name == null || name.trim().length() == 0){
                name = "Unknown";
            }

            TextView header_text=(TextView)findViewById(R.id.back_tv);
            header_text.setText(pharmaRepProfile.getRoleName());


//            ((TextView)findViewById(R.id.tv_empid)).setText(pharmaRepProfile.getRepId() + "");
//            ((TextView)findViewById(R.id.tv_empname)).setText(name);
//            ((TextView)findViewById(R.id.tv_email_name)).setText(pharmaRepProfile.getEmailId());
//            ((TextView)findViewById(R.id.tv_mobile_name)).setText(pharmaRepProfile.getMobileNo());

            //if(pharmaRepProfile.getProfilePicture()!=null) {
                //String profilePicData = pharmaRepProfile.getProfilePicture().getData();
                    //((PharmaRepDetails)context).setImage(imageUrl);
//            ((ImageView) findViewById(R.id.rep_profile_pic)).setImageBitmap(bmp);
            //}

        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


}
