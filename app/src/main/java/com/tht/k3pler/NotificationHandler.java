package com.tht.k3pler;


import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationHandler {
    private Activity activity;
    private Class mClass;
    private int ID;
    private NotificationManager notificationManager;

    public NotificationHandler(int ID, Activity activity, Class mClass){
        this.ID = ID;
        this.activity = activity;
        this.mClass = mClass;
    }
    public Intent getIntent(){
        Intent intent = new Intent(activity, mClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }
    public PendingIntent getPendingIntent(Intent intent){
        return PendingIntent.getActivity(activity, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
    }
    private NotificationManager createNotificationManager(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(String.valueOf(ID),
                    activity.getString(R.string.app_name) + String.valueOf(ID),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(activity.getString(R.string.app_name) + String.valueOf(ID));
            NotificationManager notificationManager = (NotificationManager) activity
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            return notificationManager;
        }else{
            return (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }
    public NotificationManager getNotificationManager(){
        return notificationManager;
    }
    private PendingIntent getStopPendingIntent(){
        Intent intent = getIntent();
        intent.putExtra(activity.getString(R.string.proxy_stop), true);
        return PendingIntent.getActivity(activity, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
    }
    public void notify(String messageTitle, String messageBody, Boolean isOnGoing) {
        Intent intent = getIntent();
        intent.putExtra(activity.getString(R.string.is_notification), true);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(activity, String.valueOf(ID))
                .setSmallIcon(R.mipmap.ic_launcher)
                .addAction(0, activity.getString(R.string.proxy_stop), getStopPendingIntent())
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setOngoing(isOnGoing)
                .setContentIntent(getPendingIntent(intent));
        notificationManager = createNotificationManager();
        notificationManager.notify(ID, notificationBuilder.build());
    }

}
