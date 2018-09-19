package com.atta.banknoqueue.classes;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.atta.banknoqueue.R;
import com.atta.banknoqueue.TicketDetailseActivity;

/**
 * Created by mosta on 3/14/2018.
 */

public class NotificationService extends IntentService {

    private NotificationManager mNotificationManager;
    private String mMessage;
    private int mMillis;
    NotificationCompat.Builder builder;

    public NotificationService() {
        super("com.atta.banknoqueue");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // The notification message
        mMessage = intent.getStringExtra("EXTRA_MESSAGE");


        mMillis = intent.getIntExtra("EXTRA_TIMER", 20);

        String action = intent.getAction();

        if(action.equals("ACTION_NOTIFY")) {
            issueNotification(intent, mMessage);
        }
    }

    private void issueNotification(Intent intent, String mMessage) {

        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        // Constructs the Builder object.
        builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.bankbranches)
                        .setContentTitle("Bank No Queue")
                        .setContentText(mMessage)
                        .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
                /*
                 * Sets the big view "big text" style and supplies the
                 * text (the user's reminder message) that will be displayed
                 * in the detail area of the expanded notification.
                 */
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(mMessage));

        Intent resultIntent = new Intent(this, TicketDetailseActivity.class);
        resultIntent.putExtra("EXTRA_MESSAGE", mMessage );

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        //mNotificationManager.notify(101, builder.build());

        startTimer(mMillis);
    }

    private void issueNotification(NotificationCompat.Builder builder) {
        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        // Including the notification ID allows you to update the notification later on.
        mNotificationManager.notify(001, builder.build());
    }

    private void startTimer(long millis) {
        try {
            Thread.sleep(millis);

        } catch (InterruptedException e) {
            Log.d(getPackageName(), "Sleep failure");
        }
        Log.d(getPackageName(), "Timer finished.");
        issueNotification(builder);
    }
}
