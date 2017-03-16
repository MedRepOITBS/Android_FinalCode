package pharma;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

import com.app.util.HttpUrl;
import com.app.util.JSONParser;
import com.app.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import pharma.model.DoctorNotificationStats;

/**
 * Created by kishore on 17/11/15.
 */
public class GetDoctorNotificationStatsAsync extends AsyncTask<Integer, Void, DoctorNotificationStats> {

    ProgressDialog pd;
    Activity activity;

    public GetDoctorNotificationStatsAsync(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected DoctorNotificationStats doInBackground(Integer... params) {
        String url = HttpUrl.PHARMA_GET_DOCTOR_NOTIFICATION_STATS + params[0] + "?access_token=" + Utils.GET_ACCESS_TOKEN(activity);

        System.out.println("url: " + url);

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = parser.getJSON_Response(url, true);

        DoctorNotificationStats notificationStats = null;
        try {
            notificationStats = (DoctorNotificationStats) parser.jsonParser(jsonObject, DoctorNotificationStats.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return notificationStats;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd = new ProgressDialog(activity);
        pd.setTitle("Requesting Feedback");
        pd.setMessage("Please wait, while we loading feedback for this notification.");
        pd.setCancelable(false);
        pd.show();
    }

    @Override
    protected void onPostExecute(DoctorNotificationStats doctorNotificationStats) {
        super.onPostExecute(doctorNotificationStats);

        if(pd != null && pd.isShowing()){
            pd.dismiss();
        }

        if(doctorNotificationStats !=null){

            PharmaDoctorNotificationStatsActivity.DOCTOR_NOTIFICATION_STATS = doctorNotificationStats;
            Intent intent = new Intent(activity, PharmaDoctorNotificationStatsActivity.class);
            activity.startActivity(intent);

        }else{
            Utils.DISPLAY_GENERAL_DIALOG(activity, "No Feedback", "Feedback data for this notification is not available.");
        }
    }
}
