package com.app.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.db.DetailedNotification;
import com.app.db.MedRepDatabaseHandler;
import com.app.db.Notification;
import com.app.pojo.SignIn;
import com.app.reminder.AlarmReceiver;
import com.app.util.HttpUrl;
import com.app.util.Utils;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import medrep.medrep.ImageViewerActivity;
import medrep.medrep.R;

import static com.app.util.Utils.makeRequest;

/**
 * Created by masood on 9/11/15.
 */
public class NotificationDetails extends Fragment implements View.OnClickListener {

    public static  boolean UPDATE_NOTIFICATION = false;

    public static String CURRENT_NOTIFICATION_TITLE;
    private ImageView backImageView;
    //    private LinearLayout callMedRep;
    private SignIn signIn;
    static TextView feedback;
    static TextView remindMe;
    static  TextView favouriteThis;
    static LinearLayout callMedRep;
    static ImageView CALL_MEDREP_IV;
    static ImageView fullscreen;
    static Button CALL_MEDREP_BUTTON;

    private ViewPager notificationViewPager;
    ArrayList<DetailedNotification> detailedNotifications;

    public static int CURRENT_NOTIFICATION_ID;

    public static final int OPEN_IMAGE_REQ_CODE = 100;

    private Activity activity;
    private boolean isNotificationAlreadyRead;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       /* View v= inflater.inflate(R.layout.detailed_notification, container, false);

        notificationViewPager = (ViewPager)v.findViewById(R.id.notification_view_pager);*/

        View v = inflater.inflate(R.layout.docter_f_detailed_notfication, container, false);
        if (getActivity().getIntent().getExtras() != null)
            signIn = getActivity().getIntent().getExtras().getParcelable("signin");
        backImageView = (ImageView) v.findViewById(R.id.back);

//        notificationViewPager = (ViewPager) v.findViewById(R.id.notification_view_pager);

        /*callMedRep = (LinearLayout)v.findViewById(R.id.call_rep);
        callMedRep.setOnClickListener(this);*/
        backImageView.setOnClickListener(this);
        notificationViewPager = (ViewPager) v.findViewById(R.id.notification_view_pager);
        fullscreen = (ImageView) v.findViewById(R.id.iv_full_screen);
        fullscreen.setOnClickListener(this);

        feedback = (TextView) v.findViewById(R.id.feedback);
        remindMe = (TextView) v.findViewById(R.id.remind_me);
        favouriteThis = (TextView) v.findViewById(R.id.favourite);
        callMedRep = (LinearLayout) v.findViewById(R.id.call_rep);
        CALL_MEDREP_IV= (ImageView) v.findViewById(R.id.call_medrep_iv);
        CALL_MEDREP_BUTTON = (Button) v.findViewById(R.id.call_medrep_button);

        TextView notificationTitle = (TextView) v.findViewById(R.id.notification_title);

        if(CURRENT_NOTIFICATION_TITLE != null && CURRENT_NOTIFICATION_TITLE.length() != 0){
            notificationTitle.setText(CURRENT_NOTIFICATION_TITLE);
        }

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        System.out.println("CURRENT_NOTIFICATION_ID: " + CURRENT_NOTIFICATION_ID);

        detailedNotifications =
                MedRepDatabaseHandler.getInstance(getActivity()).
                        getDetailedNotifications(CURRENT_NOTIFICATION_ID);

        isNotificationAlreadyRead = MedRepDatabaseHandler.getInstance(getActivity()).isNotificationRead(CURRENT_NOTIFICATION_ID);


        if(!isNotificationAlreadyRead){
//            UPDATE_NOTIFICATION = true;
            ContentValues values = new ContentValues();
            values.put(Notification.COLUMN_NAMES.IS_READ, true);

            MedRepDatabaseHandler.getInstance(getActivity()).updateNotification(values, CURRENT_NOTIFICATION_ID);
        }


        if (detailedNotifications != null && detailedNotifications.size() > 0) {
            System.out.println("detailedNotifications.size(): " + detailedNotifications.size());
            ScreenSlidePagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getActivity().getSupportFragmentManager());
            notificationViewPager.setAdapter(mPagerAdapter);
        } else {
            Toast.makeText(getActivity(), "No notifications found", Toast.LENGTH_SHORT).show();
            goBack();
        }

        feedback.setOnClickListener(this);

        MedRepDatabaseHandler medRepDatabaseHandler = MedRepDatabaseHandler.getInstance(getActivity());
        if(!medRepDatabaseHandler.isFeedbackGiven(CURRENT_NOTIFICATION_ID)){
            //Set different image resource here, if required.
            feedback.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.feedback, 0, 0);
        }else{
            feedback.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.feedback_active, 0, 0);
        }

        if(!medRepDatabaseHandler.isReminderAdded(CURRENT_NOTIFICATION_ID)){
            remindMe.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.remind, 0, 0);
        }else{
            remindMe.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.remind_active, 0, 0);
        }

        remindMe.setOnClickListener(this);
        favouriteThis.setOnClickListener(this);

        callMedRep.setOnClickListener(callMedRepClickListener);
        CALL_MEDREP_IV.setOnClickListener(callMedRepClickListener);
        CALL_MEDREP_BUTTON.setOnClickListener(callMedRepClickListener);

        favouriteThis.setCompoundDrawablesWithIntrinsicBounds(0, isThisNotificationFavourite() ? R.mipmap.favourite_active : R.mipmap.favourite, 0, 0);
    }




    @Override
    public void onDestroy() {

        final Activity activity = getActivity();

        super.onDestroy();

        updateNotification(activity);

    }

    private void updateNotification(final Activity activity) {
        Format formatter = new SimpleDateFormat("yyyyMMddHHmmSS");
        String viewedOn = formatter.format(Calendar.getInstance().getTime());

        Map<String, String> comment = new HashMap<String, String>();
        comment.put("notificationId", CURRENT_NOTIFICATION_ID + "");
        comment.put("favourite", isThisNotificationFavourite() ? "Y" : "N");

        if(/*!isNotificationAlreadyRead && */UPDATE_NOTIFICATION){
            comment.put("prescribe", Feedback.PRESCRIBED ? "Y" : "N");
            comment.put("rating", Feedback.RATING + "");
            comment.put("recomend", Feedback.RECOMMEND ? "Y" : "N");
        }else{
            comment.put("prescribe", null);
            comment.put("rating", null);
            comment.put("recomend", null);
        }

        comment.put("viewStatus", "Viewed");
        comment.put("viewedOn", viewedOn);
        final String json = new GsonBuilder().create().toJson(comment, Map.class);

        System.out.println("json: " + json);

        AsyncTask<Void, Void, JSONObject> async = new AsyncTask<Void, Void, JSONObject>() {

//            ProgressDialog pd;

            @Override
            protected JSONObject doInBackground(Void... params) {

                final String url = HttpUrl.BASEURL + HttpUrl.UPDATE_NOTIFICATION + Utils.GET_ACCESS_TOKEN(getActivity());
                System.out.println("URL: " + url);

                HttpResponse response = makeRequest(url, json);

                String json_string = null;
                try {
                    json_string = EntityUtils.toString(response.getEntity());
                    JSONObject temp1 = new JSONObject(json_string);
                    System.out.println("temp1: " + temp1);
                    return temp1;

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                /*pd = new ProgressDialog(getActivity());
                pd.setTitle("Updating Notification");
                pd.setMessage("Please wait for few seconds");
                pd.setCancelable(false);
                pd.show();*/
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);

                System.out.println("jsonObject: " + jsonObject);

                Log.d("Result", jsonObject + "");

                try {
                    if (jsonObject.getString("status").equalsIgnoreCase("ok") || jsonObject.getString("message").equalsIgnoreCase("success")) {
                        if(UPDATE_NOTIFICATION){
                            MedRepDatabaseHandler.getInstance(activity).markFeedbackGiven(CURRENT_NOTIFICATION_ID);
                            UPDATE_NOTIFICATION = false;

                            try{
//                                Utils.DISPLAY_GENERAL_DIALOG(activity, "Feedback", "Feedback submitted successfully");
                                Toast.makeText(activity, "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
                            }catch (WindowManager.BadTokenException e){
                                e.printStackTrace();
                            }

                        }

//                        Toast.makeText(activity, "Notification update, " + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        Log.d("UPDATE_NOTIFICATION", "Notification update, " + jsonObject.getString("message"));
                    } else {
//                        Toast.makeText(activity, "Notification update, " + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        Log.d("UPDATE_NOTIFICATION", "Notification update, " + jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Log.d("UPDATE_NOTIFICATION", UPDATE_NOTIFICATION + "");
        Log.d("isNotificationRead", isNotificationAlreadyRead + "");

        if(Utils.isNetworkAvailableWithOutDialog(getActivity()) && (UPDATE_NOTIFICATION || !isNotificationAlreadyRead))
            async.execute();

    }

    @Override
    public void onClick(View v) {
        if (v == backImageView) {
            goBack();
        }else if (v == fullscreen) {
            if(ImageViewerActivity.DETAILED_NOTIFICATION_IDS != null){
                ImageViewerActivity.DETAILED_NOTIFICATION_IDS.clear();
            }else{
                ImageViewerActivity.DETAILED_NOTIFICATION_IDS = new ArrayList<Integer>();
            }

            for(DetailedNotification not: detailedNotifications){
                ImageViewerActivity.DETAILED_NOTIFICATION_IDS.add(not.getDetailId());
            }

            Intent intent = new Intent(getActivity(), ImageViewerActivity.class);
            intent.putExtra(ImageViewerActivity.IS_FROM_DOCTOR_KEY, true);
            getActivity().startActivity(intent);
        }else if (v == feedback) {

            MedRepDatabaseHandler medRepDatabaseHandler = MedRepDatabaseHandler.getInstance(getActivity());
            if(!medRepDatabaseHandler.isFeedbackGiven(CURRENT_NOTIFICATION_ID)){
                showFeedbackDialog();
            }else{
                //Display message here, if required
//                Utils.DISPLAY_GENERAL_DIALOG(getActivity(), "Thank You", "Feedback has already been submitted for this notification.");

                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Already Submitted")
                        .setMessage("Feedback for this notification is already submitted. Do you want give feedback again?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                showFeedbackDialog();
                            }
                        })
                        .setNegativeButton("Cancel", null);
                AlertDialog alertDialog = builder.create();

                if(alertDialog != null && !alertDialog.isShowing()){
                    alertDialog.show();
                }


            }



        }
        else if (v == remindMe) {
            final Dialog dialog = new Dialog(getActivity());

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.doctor_f_remindme_dailogue);
            Button okButton = (Button) dialog.findViewById(R.id.bt_ok);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // time at which alarm will be scheduled here alarm is scheduled at 1 day from current time,
                    // we fetch  the current time in milliseconds and added 1 day time
                    // i.e. 24*60*60*1000= 86,400,000   milliseconds in a day

                    RadioButton rbDay = (RadioButton) dialog.findViewById(R.id.rb_1day);
                    RadioButton rbWeek = (RadioButton) dialog.findViewById(R.id.rb_1week);
                    RadioButton rbMonth = (RadioButton) dialog.findViewById(R.id.rb_1month);

                    int days = -1;

                    if(rbDay.isChecked()){
                        days = 1;
                    }else if(rbWeek.isChecked()){
                        days = 7;
                    }else if(rbMonth.isChecked()){
                        Calendar cal = Calendar.getInstance();
                        days = 20;
                        System.out.println(days + "&&&&&&");
                    }


                    if(days == -1){
                        dialog.dismiss();
                        return;
                    }
                    System.out.println(days + "&&&&&&");

                    //Long dateTime = Calendar.getInstance().getTimeInMillis() + (days * 24 * 60 * 60 * 1000);

                    Long dateTime = new GregorianCalendar().getTimeInMillis()+(days * 24*60*60*1000);

                    System.out.println("dateTime: " + dateTime);
                    int revdays = (int) (dateTime / (1000*60*60*24) % 7);
                    System.out.println("dateTime: " + revdays);
                    System.out.println("Calendar.getInstance().getTimeInMillis(): " + Calendar.getInstance().getTimeInMillis());


                    // create an Intent and set the class which will execute when Alarm triggers, here we have
                    // given AlarmReciever in the Intent, the onRecieve() method of this class will execute when
                    // alarm triggers
                    Intent alarmIntent = new Intent(getActivity(), AlarmReceiver.class);
                    alarmIntent.putExtra(AlarmReceiver.NOTIFICATION_ID_KEY, CURRENT_NOTIFICATION_ID);

                    // create the object
                    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);


                    PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), AlarmReceiver.REQUEST_CODE,
                            alarmIntent, PendingIntent.FLAG_ONE_SHOT);


                    //set the alarm for particular time
                    alarmManager.set(AlarmManager.RTC_WAKEUP, dateTime, pendingIntent);

                    Toast.makeText(getActivity(), "Reminder Set for " + days + "' Day/s", Toast.LENGTH_LONG).show();

                    MedRepDatabaseHandler.getInstance(getActivity()).markReminderAdded(CURRENT_NOTIFICATION_ID);
                    remindMe.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.remind_active, 0, 0);

                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        else if (v == favouriteThis) {

            System.out.println("Selected favourite this");

            if(isThisNotificationFavourite()) {
                MedRepDatabaseHandler.getInstance(getActivity()).markNotificationAsNotFavourite(CURRENT_NOTIFICATION_ID);
                favouriteThis.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.favourite, 0, 0);
            }else{
                MedRepDatabaseHandler.getInstance(getActivity()).markNotificationAsFavourite(CURRENT_NOTIFICATION_ID);
                favouriteThis.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.favourite_active, 0, 0);
            }
        }
    }

    private void showFeedbackDialog() {
        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dpctor_feedback_dialog);
        Button okButton = (Button) dialog.findViewById(R.id.bt_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Feedback.RATING = ((RatingBar) dialog.findViewById(R.id.ratingBar1)).getRating();
                     /*   Feedback.PRESCRIBED = ((RadioButton)dialog.findViewById(R.id.radio0)).isChecked();
                        Feedback.RECOMMEND = ((RadioButton)dialog.findViewById(R.id.RadioButton02)).isChecked();
*/
                Feedback.PRESCRIBED = ((RadioButton)dialog.findViewById(R.id.radio0)).isChecked();
                Feedback.RECOMMEND = ((RadioButton)dialog.findViewById(R.id.RadioButton02)).isChecked();


                UPDATE_NOTIFICATION = true;

                updateNotification(getActivity());

                dialog.dismiss();
            }
        });

        Button cancelButton = (Button) dialog.findViewById(R.id.bt_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UPDATE_NOTIFICATION = false;
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /*protected void setStatusBarIcon(boolean enabled)
    {
        Intent alarmChanged = new Intent("android.intent.action.ALARM_CHANGED");
        alarmChanged.putExtra("alarmSet", enabled);
        getActivity().sendBroadcast(alarmChanged);
    }*/


    View.OnClickListener callMedRepClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            UPDATE_NOTIFICATION = false;

            if(Utils.isNetworkAvailable(getActivity())){
                MedRepDatabaseHandler dbHelper = MedRepDatabaseHandler.getInstance(getActivity());

//                int notificationID = dbHelper.getNotificationID(detailedNotification.getDetailId());
                String appointmentTitle = dbHelper.getNotificationName(CURRENT_NOTIFICATION_ID);
                String companyName = dbHelper.getCompanyName(CURRENT_NOTIFICATION_ID);
                String therapeuticName = dbHelper.getTherapeuticName(CURRENT_NOTIFICATION_ID);


                CallMedRepFragment fragment = new CallMedRepFragment();

                fragment.setNotificationID(CURRENT_NOTIFICATION_ID);
                fragment.setAppointmentTitle(appointmentTitle);
                fragment.setCompanyName(companyName);
                fragment.setTherapeuticName(therapeuticName);

                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        }
    };

    private void goBack() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ScreenSlidePageFragment(detailedNotifications.get(position));
        }

        @Override
        public int getCount() {
            return detailedNotifications.size();
        }
    }

    class ScreenSlidePageFragment extends Fragment {
        DetailedNotification detailedNotification;

        public ScreenSlidePageFragment(DetailedNotification detailedNotification) {
            this.detailedNotification = detailedNotification;
        }

        public int getStatusBarHeight() {
            int result = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = getResources().getDimensionPixelSize(resourceId);
            }
            return result;
        }


        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(
                    R.layout.detailed_notification, container, false);


            Point size = new Point();
            getActivity().getWindowManager().getDefaultDisplay().getSize(size);
            int screenHeight = size.y;

            LinearLayout layout = (LinearLayout)rootView.findViewById(R.id.layout1);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();
            params.height = screenHeight - (getStatusBarHeight() * 3);

            final ImageView notificationImage = (ImageView) rootView.findViewById(R.id.brand_detail_image);
            ((TextView) rootView.findViewById(R.id.not_detail_text_heading)).setText(detailedNotification.getDetailTitle());
            ((TextView) rootView.findViewById(R.id.not_detail_text)).setText(detailedNotification.getDetailDesc());

            Bitmap bmp = MedRepDatabaseHandler.getInstance(getActivity()).
                    getDetailedNotificationImage(detailedNotification.getDetailId());

            if (bmp == null && Utils.isNetworkAvailableWithOutDialog(getActivity())) {
                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(Void... params) {
                        String url = HttpUrl.GET_NOTIFICATION_CONTENT + detailedNotification.getDetailId() +
                                "?access_token=" + Utils.GET_ACCESS_TOKEN(getActivity());

                        return getBitmapFromURL(url);
                    }

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected void onPostExecute(final Bitmap bitmap) {
                        super.onPostExecute(bitmap);
                        if(bitmap != null){


                            if(bitmap != null){
                                ContentValues values = new ContentValues();
                                values.put(DetailedNotification.COLUMN_NAMES.IMAGE_CONTENT, Utils.bitmapToBase64(bitmap));

                                MedRepDatabaseHandler.getInstance(getActivity()).updateDetailedNotification(values, detailedNotification.getDetailId());
                                openImage(notificationImage, bitmap);
                            }
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
                }.execute();
            } else if (bmp != null){
                openImage(notificationImage, bmp);
            }

            return rootView;
        }
//        Matrix matrix = new Matrix();
        private void openImage(final ImageView notificationImage, final Bitmap bitmap) {
            /*class ScaleListener extends ScaleGestureDetector.
                    SimpleOnScaleGestureListener {
                @Override
                public boolean onScale(ScaleGestureDetector detector) {
                    float scaleFactor = detector.getScaleFactor();
                    scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));
                    matrix.setScale(scaleFactor, scaleFactor);
                    notificationImage.setImageMatrix(matrix);
                    return true;
                }
            }

            notificationImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getActivity(), new ScaleListener());
                    scaleGestureDetector.onTouchEvent(event);
                    return true;
                }
            });*/

            notificationImage.setImageBitmap(bitmap);
            notificationImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(ImageViewerActivity.DETAILED_NOTIFICATION_IDS != null){
                        ImageViewerActivity.DETAILED_NOTIFICATION_IDS.clear();
                    }else{
                        ImageViewerActivity.DETAILED_NOTIFICATION_IDS = new ArrayList<Integer>();
                    }

                    for(DetailedNotification not: detailedNotifications){
                        ImageViewerActivity.DETAILED_NOTIFICATION_IDS.add(not.getDetailId());
                    }

                    Intent intent = new Intent(getActivity(), ImageViewerActivity.class);
                    intent.putExtra(ImageViewerActivity.IS_FROM_DOCTOR_KEY, true);
                    getActivity().startActivity(intent);

                    /*File imageFile = new File(getActivity().getExternalCacheDir().getAbsolutePath(), "." + Calendar.getInstance().getTimeInMillis());
                    if (imageFile.exists()) {
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

                    if (imageFile != null && imageFile.exists()) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse("file://" + imageFile.getAbsolutePath()), "image*//*");
                        startActivityForResult(intent, OPEN_IMAGE_REQ_CODE);
                    }*/
                }
            });
        }
    }

    private boolean isThisNotificationFavourite(){
        return  MedRepDatabaseHandler.getInstance(getActivity()).isThisNotificationFavourite(CURRENT_NOTIFICATION_ID);
    }

    private static class Feedback{
        public static float RATING = 0;
        public static boolean PRESCRIBED = false;
        public static boolean RECOMMEND = true;
    }

}
