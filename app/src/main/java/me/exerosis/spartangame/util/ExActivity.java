package me.exerosis.spartangame.util;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;

/**
 * Created by The Exerosis on 7/20/2015.
 */
public class ExActivity extends Activity {
    private static int notificationID = 0;
    public <T> T getByID(int id, Class<T> type){
       return (T) findViewById(id);
    }
    public <T> T getSystemService(String context, Class<T> type){
        return (T) getSystemService(context);
    }


    public void showNotification(Notification.Builder notificationBuilder ){
        Intent resultIntent = new Intent(this, this.getClass());

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(this.getClass());
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = getSystemService(Context.NOTIFICATION_SERVICE, NotificationManager.class);

        notificationManager.notify(notificationID++, notificationBuilder.build());
    }
}
