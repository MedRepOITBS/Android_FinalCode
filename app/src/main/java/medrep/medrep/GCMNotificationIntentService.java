package medrep.medrep;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

//import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by code_artist on 21/08/15.
 */
public class GCMNotificationIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;

    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    public static final String TAG = "GCMNotificationIntentService";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String msg;
        Log.i("NOTIFICATION", String.valueOf(extras));
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                //sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
//                sendNotification("Deleted messages on server: "
//                        + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                for (int i = 0; i < 3; i++) {

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }

                }

                if (extras.get("msg") != null) {
                    msg = extras.get("msg").toString();
                    Log.i("NOTIFICATION", msg);
                } else {
                    msg = extras.toString();
                    Log.i("MESSAGE", msg);
                }

               System.out.println("dataReceived: "+msg);
                sendNotification(msg);
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {

        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, DoctorDashboard.class), 0);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    this).setSmallIcon(R.mipmap.launching_icon)
                    .setContentTitle("MedRep")
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                    .setContentText(Html.fromHtml(msg))
                    .setDefaults(Notification.DEFAULT_LIGHTS)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setDefaults(Notification.DEFAULT_SOUND);
            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
