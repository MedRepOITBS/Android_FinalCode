package com.app.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.app.db.MedRepDatabaseHandler;

import medrep.medrep.DoctorDashboard;
import medrep.medrep.R;
import medrep.medrep.SplashScreen;

/**
 * Created by kishore on 2/10/15.
 */
public class AlarmReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_ID_KEY = "NotificationID";

    public static final int REQUEST_CODE = 100;

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("Received broadcast");

        int  id = intent.getIntExtra(NOTIFICATION_ID_KEY, -1);

        Toast.makeText(context, "Notification ID: " + id, Toast.LENGTH_SHORT).show();

        String msg = /*"MedRep Reminder"*/MedRepDatabaseHandler.getInstance(context).getNotificationName(id);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo))
                        .setSmallIcon(R.mipmap.logo)
                        .setContentTitle("MedRep Reminder")
                        .setContentText((msg == null || msg.trim().length() == 0)?"Notification reminder.":msg);

        Intent resultIntent = new Intent(context, DoctorDashboard.class);
        resultIntent.putExtra(NOTIFICATION_ID_KEY, id);
        resultIntent.putExtra("StartNotifications", true);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        mBuilder.setContentIntent(resultPendingIntent);

        Notification notification = mBuilder.build();

        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;

        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(id, notification);

        /*Notification n = new Notification(R.mipmap.ic_launcher, msg,
                System.currentTimeMillis());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(), 0);

        n.setLatestEventInfo(context, "MedRep Reminder", msg, pendingIntent);
        n.defaults |= Notification.DEFAULT_VIBRATE;
//        n.sound = Uri.parse(RemindMe.getRingtone());
        n.defaults |= Notification.DEFAULT_SOUND;
        n.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager nm = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify((int)id, n);*/
    }

}