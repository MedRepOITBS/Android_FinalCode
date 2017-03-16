package pharma;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.db.MedRepDatabaseHandler;
import com.app.fragments.PharmaNavigationDrawerFragment;
import com.app.pharma.PharmaNotification;
import com.app.pojo.SignIn;
import com.app.util.HttpUrl;
import com.app.util.JSONParser;
import com.app.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import medrep.medrep.R;

/**
 * Created by admin on 9/26/2015.
 */
public class PharmaProductCompaign extends AppCompatActivity implements View.OnClickListener {


    PharmaProductCompaign _activity;
    ArrayList<PharmaNotification> pharmaNotifications;

    ImageView trackCompaingn;
    ImageView pcompanyname;
    LinearLayout track_new_product;

    //ImageView pcompanyname;
//    LinearLayout visibilitylayout;
    LinearLayout seeAllNotifications;

    //l_seeallnotifi
    //layout_visibilty

    LinearLayout   l_track_new_product;
    LinearLayout linearlayout_fourtabs;

//    ImageView iv_downarraow;

    ListView notificationsList;

    /*LinearLayout paracetamollayout;
    LinearLayout dololayout;
    LinearLayout novomixlayout;
    LinearLayout stamilolayout;*/

    TextView title;


    //todo:need to do visibile and gone when user click on last tab in the screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pharma_productcompaing);

        Utils.SET_PHARMA_COMPANY_LOGO(this, (ImageView) findViewById(R.id.p_company_name));

        _activity = this;

        title=(TextView)findViewById(R.id.back_tv);
        title.setText("Product Campaigns");

//        visibilitylayout=(LinearLayout)findViewById(R.id.layout_notifications);
        seeAllNotifications=(LinearLayout)findViewById(R.id.l_seeallnotifi);
        l_track_new_product=(LinearLayout)findViewById(R.id.l_track_new_product);

        notificationsList = (ListView) findViewById(R.id.notifications_list);

        notificationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(id);
                String name = ((TextView)view.findViewById(R.id.notification_title)).getText().toString();

                PharmaNotification pharmaNotification = (PharmaNotification) parent.getItemAtPosition(position);

                pharmaNotification.setNotificationDetails(
                        MedRepDatabaseHandler.getInstance(PharmaProductCompaign.this).
                                getPharmaNotificationDetails(pharmaNotification.getNotificationId()));

                PharmaNotificationDetails.CURRENT_NOTIFICATION = pharmaNotification;

                Intent intent = new Intent(PharmaProductCompaign.this, PharmaNotificationDetails.class);
                //JSONObject object = (JSONObject) jsonArray.get(position);
                intent.putExtra("title", name);
                startActivity(intent);
            }
        });

        //l_track_new_product
        //linearlayout_fourtabs

        /*iv_downarraow=(ImageView)findViewById(R.id.iv_downarraow);*/
        linearlayout_fourtabs=(LinearLayout)findViewById(R.id.linearlayout_fourtabs);



        /*paracetamollayout=(LinearLayout)findViewById(R.id.l_paracetamol);
        dololayout=(LinearLayout)findViewById(R.id.l_dolo);
        novomixlayout=(LinearLayout)findViewById(R.id.l_novomix);
        stamilolayout=(LinearLayout)findViewById(R.id.l_stailo);*/



//        visibilitylayout.setOnClickListener(_activity);
        seeAllNotifications.setOnClickListener(_activity);
        l_track_new_product.setOnClickListener(_activity);
//        linearlayout_fourtabs.setOnClickListener(_activity);
//        iv_downarraow.setOnClickListener(_activity);


        /*paracetamollayout.setOnClickListener(_activity);
        dololayout.setOnClickListener(_activity);
        novomixlayout.setOnClickListener(_activity);
        stamilolayout.setOnClickListener(_activity);*/

        ImageView backIV = (ImageView)findViewById(R.id.back);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                /*if(linearlayout_fourtabs.getVisibility() == View.GONE){
                    notificationsList.setVisibility(View.GONE);
                    linearlayout_fourtabs.setVisibility(View.VISIBLE);
                }else{
                    finish();
                }*/
            }
        });


        displayAllNotifications();

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);

        PharmaNavigationDrawerFragment drawerFragment = (PharmaNavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar, R.id.fragment_navigation_drawer);
    }

    @Override
    public void onClick(View v) {

        System.out.println("On click event");

        if(v.getId()== R.id.l_track_new_product){
            if (notificationsList.getVisibility()==View.GONE) {
                displayAllNotifications();
            }
        }

        /*if(v.getId()== R.id.iv_downarraow){
            if (visibilitylayout.getVisibility()==View.VISIBLE){
                visibilitylayout.setVisibility(View.GONE);
                linearlayout_fourtabs.setVisibility(View.VISIBLE);
            }
        }*/

        /*if (v.getId()== R.id.l_paracetamol){
            Intent intent=new Intent(_activity, PharmaCampainDetails.class);
            intent.putExtra(Constants.mParacetomolTitle,Constants.mParacetomolTitleValue);
            startActivity(intent);
        }

        if (v.getId()== R.id.l_dolo){
            Intent intent=new Intent(_activity, PharmaCampainDetails.class);
            intent.putExtra(Constants.mParacetomolTitle,Constants.mdoloTitleValue);
            startActivity(intent);
        }

        if (v.getId()== R.id.l_novomix){
            Intent intent=new Intent(_activity, PharmaCampainDetails.class);
            intent.putExtra(Constants.mParacetomolTitle,Constants.mnovomixTitleValue);
            startActivity(intent);
        }
        if (v.getId()== R.id.l_stailo){
            Intent intent=new Intent(_activity, PharmaCampainDetails.class);
            intent.putExtra(Constants.mParacetomolTitle,Constants.mPStamloTitleValue);
            startActivity(intent);
        }*/
    }

    private void displayAllNotifications() {
            notificationsList.setVisibility(View.VISIBLE);
            linearlayout_fourtabs.setVisibility(View.GONE);

            if(Utils.isNetworkAvailableWithOutDialog(PharmaProductCompaign.this)){
//                    String url = HttpUrl.PHARMA_GET_MY_NOTIFICATIONS /*+*/ /*start_date*//*"20150101?access_token="*/;

                NotificationsAsync notificationsAsync = new NotificationsAsync(PharmaProductCompaign.this);
                notificationsAsync.execute();
            }else{
                displayNotificaitons();
            }
    }

    private class NotificationsAsync extends AsyncTask<String, Integer, ArrayList<PharmaNotification>> {

        private final Activity activity;

        public NotificationsAsync(Activity activity){
            this.activity = activity;
        }

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.setTitle(R.string.pharma_info_dialog_title);
            progressDialog.setMessage(activity.getString(R.string.pharma_info_dialog_msg));
            if(!progressDialog.isShowing()){progressDialog.show();}
        }

        @Override
        protected ArrayList<PharmaNotification> doInBackground(String... params) {
//            String lastUpdatedDate = "20150101";

            String lastUpdatedDate = MedRepDatabaseHandler.getInstance(PharmaProductCompaign.this).getPharmaNotificationsLatestUpdatedDate() + "";

            String url = HttpUrl.PHARMA_GET_MY_NOTIFICATIONS + lastUpdatedDate + "?access_token=" + SignIn.GET_ACCESS_TOKEN();
            return getNotification(url);
        }

        private ArrayList<PharmaNotification> getNotification(String url){

            System.out.println("Url: " + url);

            JSONParser parser = new JSONParser();
            JSONArray jsonArray = parser.getJSON_Response(url);

            pharmaNotifications = new ArrayList<>();

            parser.jsonParser(jsonArray, PharmaNotification.class, pharmaNotifications);

            MedRepDatabaseHandler.getInstance(activity).addPharmaNotifications(pharmaNotifications);

            for(PharmaNotification pharmaNotification: pharmaNotifications){
                MedRepDatabaseHandler.getInstance(activity).addPharmaDetailedNotifications(pharmaNotification.getNotificationDetails());
            }

            return pharmaNotifications;
        }


        @Override
        protected void onPostExecute(ArrayList<PharmaNotification> pharmaNotifications) {
            super.onPostExecute(pharmaNotifications);

            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            progressDialog = null;

            //Set adapter here
            displayNotificaitons();
        }
    }

    private void displayNotificaitons() {
        ArrayList<PharmaNotification> pharmaNotifications = MedRepDatabaseHandler.getInstance(PharmaProductCompaign.this).getPharmaNotifications();

        if(pharmaNotifications != null && pharmaNotifications.size() > 0){
            NotificationsAdapter notificationsAdapter = new NotificationsAdapter(pharmaNotifications);
            notificationsList.setAdapter(notificationsAdapter);
        }else{

            notificationsList.setVisibility(View.GONE);
            linearlayout_fourtabs.setVisibility(View.VISIBLE);

            Utils.DISPLAY_GENERAL_DIALOG(PharmaProductCompaign.this,
                    "No Notifications Found",
                    "No notifications are available at this time. " +
                            "Please try again later.");
        }
    }

    private class NotificationsAdapter extends BaseAdapter{

        private ArrayList<PharmaNotification> pharmaNotifications;

        public NotificationsAdapter(ArrayList<PharmaNotification> pharmaNotifications) {
            this.pharmaNotifications = pharmaNotifications;
        }

        @Override
        public int getCount() {
            return pharmaNotifications.size();
        }

        @Override
        public PharmaNotification getItem(int position) {
            return pharmaNotifications.get(position);
        }

        @Override
        public long getItemId(int position) {
            return pharmaNotifications.get(position).getNotificationId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null){
                LayoutInflater inflater = LayoutInflater.from(PharmaProductCompaign.this);
                convertView = inflater.inflate(R.layout.row_notification, parent, false);
                holder = new ViewHolder();
                holder.titleTextView = (TextView)convertView.findViewById(R.id.notification_title);
                holder.descTextView = (TextView)convertView.findViewById(R.id.notification_desc);
                holder.ticketID_TextView = (TextView)convertView.findViewById(R.id.ticket_id);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            PharmaNotification pharmaNotification = pharmaNotifications.get(position);

            String title = pharmaNotification.getNotificationName();
//            System.out.println("title " + title);
            String description = pharmaNotification.getNotificationDesc();

            title = (title != null && title.trim().length() !=0)?title.trim():"No Title";
            description = (description != null && description.trim().length() !=0)?description.trim():"No Description";


            holder.titleTextView.setText(title);
            holder.descTextView.setText(description);
            holder.ticketID_TextView.setText(title.charAt(0) + "");
            return convertView;
        }


        class ViewHolder{
            TextView titleTextView;
            TextView descTextView;
            TextView ticketID_TextView;
            ImageView rightArrowImageView;
        }
    }
}
