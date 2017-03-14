package pharma;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.db.Company;
import com.app.db.DoctorProfile;
import com.app.fragments.PharmaNavigationDrawerFragment;
import com.app.util.HttpUrl;
import com.app.util.JSONParser;
import com.app.util.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

import medrep.medrep.R;
import pharma.model.CompanyDoctor;

/**
 * Created by kishore on 1/11/15.
 */
public class PharmaCompanyDoctors extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharma_converted_appiontment);

        ((TextView)findViewById(R.id.back_tv)).setText("Activities");

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(Utils.isNetworkAvailable(this)){
            CompanyDoctorsAsync companyDoctorsAsync = new CompanyDoctorsAsync();
            companyDoctorsAsync.execute();
        }else{
            Toast.makeText(this, "Lost network connectivity.", Toast.LENGTH_SHORT).show();
            finish();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        PharmaNavigationDrawerFragment drawerFragment = (PharmaNavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);
    }


    public class CompanyDoctorsAsync extends AsyncTask<Void, Integer, ArrayList<CompanyDoctor>> {
        ProgressDialog pd;

        @Override
        protected ArrayList<CompanyDoctor> doInBackground(Void... params) {

            String url = HttpUrl.PHARMA_GET_COMPANY_DOCTORS + Utils.GET_ACCESS_TOKEN(PharmaCompanyDoctors.this);

            System.out.println("PHARMA_GET_COMPANY_DOCTORS: " + url);

            JSONParser parser = new JSONParser();
            JSONArray jsonArray = parser.getJSON_Response(url);

            ArrayList<CompanyDoctor> companyDoctors = new ArrayList<>();

            parser.jsonParser(jsonArray, CompanyDoctor.class, companyDoctors);
/*

              for(CompanyDoctor companyDoctor: companyDoctors){
                    url = HttpUrl.PHARMA_GET_DOCTOR_PROFILE + companyDoctor.getDoctorId() +
                        "?access_token=" +
                        Utils.GET_ACCESS_TOKEN(PharmaCompanyDoctors.this);
                parser = new JSONParser();
                JSONObject jsonObject = parser.getJSON_Response(url, true);

                DoctorProfile doctorProfile = null;
                try {
                    doctorProfile = (DoctorProfile) parser.jsonParser(jsonObject, DoctorProfile.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                companyDoctor.setProfilePicture(doctorProfile.getProfilePicture());

                */
/*Bitmap bmp = null;

                String bitmapData = doctorProfile.getProfilePicture().getData();

                if (bitmapData != null && bitmapData.trim().length() > 0) {
//                bmp = Utils.decodeBase64(bitmapData);
                    return decodeSampledBitmapFromResource(bitmapData, 45, 45);
                }*//*

            }
*/

            return companyDoctors;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(PharmaCompanyDoctors.this);
            pd.setTitle("Getting Doctors");
            pd.setMessage("Please wait, while we retrieve your company doctors.");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(ArrayList<CompanyDoctor> companyDoctors) {
            super.onPostExecute(companyDoctors);

            if(pd != null && pd.isShowing()){
                pd.dismiss();
            }

            if(companyDoctors == null || companyDoctors.size() == 0){
                Toast.makeText(PharmaCompanyDoctors.this, "No Appointments Found.", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                //Set appointments adapter
                ListView appointmentsLV = (ListView) findViewById(R.id.converted_appointments_lv);
                appointmentsLV.setAdapter(new DoctorsAdapter(PharmaCompanyDoctors.this, companyDoctors));

                appointmentsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        CompanyDoctor companyDoctor = (CompanyDoctor)parent.getItemAtPosition(position);
                        PharmaActivityScoreDetails.DOCTOR_PROFILE = null;

                        System.out.println("Kishore Doctor ID: " + companyDoctor.getDoctorId());

//                        companyDoctor.setDoctorId(49);
                        PharmaActivityScoreDetails.COMPANY_DOCTOR = companyDoctor;

                        Intent intent=new Intent(PharmaCompanyDoctors.this, PharmaActivityScoreDetails.class);
                        intent.putExtra(PharmaActivityScoreDetails.DISABLE_COMPETITIVE_ANALYSIS_KEY, true);
                        startActivity(intent);
                    }
                });
            }

        }
    }

    private class DoctorsAdapter extends BaseAdapter {
        Handler threadHandler=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                final ProfilePic profilePic= (ProfilePic) msg.obj;

                String bitmapData = "";

                if(profilePic != null && profilePic.profile != null){
                    bitmapData = profilePic.profile.getData();
                }


                if (bitmapData != null && bitmapData.trim().length() > 0) {
//                bmp = Utils.decodeBase64(bitmapData);
                    final Bitmap bmp = decodeSampledBitmapFromResource(bitmapData, 45, 45);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            profilePic.viewHolder.pharmaProfilePic.setImageBitmap(bmp);

                            Log.d("TEST "," Doctor :"+profilePic.profile.getLoginId());
                        }
                    });
                   // profilePic.viewHolder.pharmaProfilePic.setImageBitmap(bmp);
                }


            }
        } ;

        Activity activity;

        /**
         * Appointments
         */
        ArrayList<CompanyDoctor> companyDoctors;

        public DoctorsAdapter(Activity activity, ArrayList<CompanyDoctor> companyDoctors) {
            this.activity = activity;
            this.companyDoctors = companyDoctors;
        }

        @Override
        public int getCount() {
            return companyDoctors.size();
        }

        @Override
        public CompanyDoctor getItem(int position) {
            return companyDoctors.get(position);
        }

        @Override
        public long getItemId(int position) {
            return companyDoctors.get(position).getDoctorId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(activity);
                convertView = inflater.inflate(R.layout.pharma_doctor_list_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.titleTV = (TextView) convertView.findViewById(R.id.title_tv);
                viewHolder.timeTV = (TextView) convertView.findViewById(R.id.tv_timing);
                viewHolder.locationTV = (TextView) convertView.findViewById(R.id.location_tv);
                viewHolder.pharmaProfilePic = (ImageView) convertView.findViewById(R.id.pharma_profilepic);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            try {

                final ImageView finalImage=viewHolder.pharmaProfilePic;
                final CompanyDoctor companyDoctor = companyDoctors.get(position);

                //There is no date time available from server. That's why date =time value is being initialized to null.
                String datetime = null;

                System.out.println(datetime != null && datetime.trim().length() > 0);

                if(datetime != null && datetime.trim().length() > 0){
                    int hour = Integer.parseInt(datetime.substring(8, 10));
                    int minute = Integer.parseInt(datetime.substring(10, 12));

                    int year = Integer.parseInt(datetime.substring(0, 4));
                    int month = Integer.parseInt(datetime.substring(4, 6));
                    int date = Integer.parseInt(datetime.substring(6, 8));

                    String time =hour + ":" + minute + (hour < 12?"am":"pm");

                    String mon = Utils.formatMonth(month).substring(0,3).toUpperCase().toString();
                    String timing = mon+" "+date+" "+time;
                    System.out.println("timing: " + timing);
                    viewHolder.timeTV.setText(timing);
                }else{
                    viewHolder.timeTV.setVisibility(View.INVISIBLE);
                }

                String title = companyDoctor.getFirstName() + " " + companyDoctor.getLastName();

                if(title != null && title.trim().length() > 0){
                    viewHolder.titleTV.setText(title.trim());
                }else{
                    viewHolder.titleTV.setText("No Title");
                }

                System.out.println("companyDoctor.getTherapeuticName(): " + companyDoctor.getTherapeuticName());
                //To-Do this is pending from server side, even doctor id is being returned as null.
                System.out.println("companyDoctor.getTotalActivityScore() ");

                ArrayList<Company.Location> locations = companyDoctor.getLocations();

                if(locations != null && locations.size() >0){
                    String locationStr = "";
                    for(Company.Location location: locations){
                        locationStr = (locationStr.length() == 0)?locationStr + location.getCity():locationStr + "\n" + location.getCity();
                    }
                    viewHolder.locationTV.setText(locationStr);
                }

//                final ViewHolder finalViewHolder = viewHolder;


                Log.d("TEST befor TTTTTTTTTT","TTTTTTTTTTT"+companyDoctors.size());
                final ViewHolder finalViewHolder = viewHolder;

                new Thread()
                {
                    @Override
                    public void run() {
                        super.run();

                        String url = HttpUrl.PHARMA_GET_DOCTOR_PROFILE + companyDoctor.getDoctorId() +
                                "?access_token=" +
                                Utils.GET_ACCESS_TOKEN(PharmaCompanyDoctors.this);

//                        Log.d("TEST after TTTTTTTTTT","SSSSSSSSSSS"+ companyDoctor.getDoctorId() );
                        JSONParser parser = new JSONParser();
                        JSONObject jsonObject = parser.getJSON_Response(url, true);

                        DoctorProfile doctorProfile = null;
                        try {
                            doctorProfile = (DoctorProfile) parser.jsonParser(jsonObject, DoctorProfile.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        companyDoctor.setProfilePicture(doctorProfile.getProfilePicture());


                        ProfilePic profilePic=new ProfilePic();

                        profilePic.viewHolder= finalViewHolder;
                        profilePic.profile=doctorProfile.getProfilePicture();
                        Message mesage=new Message();
                        mesage.obj=profilePic;

                        threadHandler.dispatchMessage(mesage);
                    }
                }.start();

             /*   String bitmapData = companyDoctor.getProfilePicture().getData();

                if (bitmapData != null && bitmapData.trim().length() > 0) {
//                bmp = Utils.decodeBase64(bitmapData);
                    Bitmap bmp = decodeSampledBitmapFromResource(bitmapData, 45, 45);
                    viewHolder.pharmaProfilePic.setImageBitmap(bmp);
                }*/


                if(Utils.isNetworkAvailableWithOutDialog(activity) && companyDoctor.getDoctorId() > 0){
                   /*ImageLoader imageLoaderTask = new ImageLoader(activity, viewHolder.pharmaProfilePic);
                   imageLoaderTask.execute(companyDoctor.getDoctorId());*/
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
            return convertView;
        }

       public class ViewHolder {
            TextView titleTV, timeTV, locationTV;
            ImageView pharmaProfilePic;
        }

    }

    public class ProfilePic{
        DoctorProfile.ProfilePicture profile;
        DoctorsAdapter.ViewHolder viewHolder=null;
    }

    private class ImageLoader extends AsyncTask<Integer, Void, Bitmap>/* task = new AsyncTask<Integer, Void, Bitmap>()*/ {

//        ImageView picIV;
        Activity activity;
        private final WeakReference<ImageView> imageViewReference;

        public ImageLoader(Activity activity, ImageView picIV){
            this.activity = activity;
//            this.picIV = picIV;
            imageViewReference = new WeakReference<ImageView>(picIV);
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {

            String url = HttpUrl.PHARMA_GET_DOCTOR_PROFILE + params[0] +
                    "?access_token=" +
                    Utils.GET_ACCESS_TOKEN(activity);
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = parser.getJSON_Response(url, true);

            DoctorProfile doctorProfile = null;
            try {
                doctorProfile = (DoctorProfile) parser.jsonParser(jsonObject, DoctorProfile.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Bitmap bmp = null;

            DoctorProfile.ProfilePicture profilePicture = doctorProfile.getProfilePicture();

            String pictureData = (profilePicture == null)?null:profilePicture.getData();

            if (pictureData != null && pictureData.trim().length() > 0) {
//                bmp = Utils.decodeBase64(bitmapData);
                return decodeSampledBitmapFromResource(pictureData, 45, 45);
            }

            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if (bitmap != null) {
                /*picIV.setImageBitmap(bitmap);*/
                if (imageViewReference != null) {
                    ImageView imageView = imageViewReference.get();
                    if (imageView != null) {
                        imageView.setImageBitmap(bitmap);
                    }
                }
            }
        }

    }

    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromResource(String inputData,
                                                  int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        byte[] decodedByte = Base64.decode(inputData, 0);
        BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length, options);
    }
}
