package com.tht.k3pler;


import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotificationHandler {
    private Activity activity;
    private Class mClass;
    private int ID;

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
    public NotificationManager getNotificationManager(){
        return (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
    }
    public void notify(String messageTitle, String messageBody) {
        Intent intent = getIntent();
        intent.putExtra(Constants.NOTIFICATION_CONSTANT, true);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(activity)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(false)
                .setContentIntent(getPendingIntent(intent));
        getNotificationManager().notify(ID, notificationBuilder.build());
    }

}
