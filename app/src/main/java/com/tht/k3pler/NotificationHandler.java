package com.tht.k3pler;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class NotificationHandler {
    private Context context;
    private Class mClass;
    private int ID, btnID1 = 1, btnID2 = 2;
    private NotificationManager notificationManager;
    private int pendingFlag = PendingIntent.FLAG_ONE_SHOT;

    public NotificationHandler(int ID, Context context, Class mClass){
        this.ID = ID;
        this.context = context;
        this.mClass = mClass;
    }
    public Intent getIntent(){
        Intent intent = new Intent(context, mClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }
    public PendingIntent getPendingIntent(Intent intent){
        return PendingIntent.getService(context, 0, intent,
                pendingFlag);
    }
    private NotificationManager createNotificationManager(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(String.valueOf(ID),
                    context.getString(R.string.app_name) + String.valueOf(ID),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(context.getString(R.string.app_name) + String.valueOf(ID));
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            return notificationManager;
        }else{
            return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }
    public NotificationManager getNotificationManager(){
        return notificationManager;
    }
    private PendingIntent getStopPendingIntent(){
        Intent intent = getIntent();
        intent.putExtra(context.getString(R.string.proxy_stop), true);
        return PendingIntent.getService(context, btnID1, intent,
                pendingFlag);
    }
    private PendingIntent getShowPendingIntent(){
        Intent intent = getIntent();
        intent.putExtra(context.getString(R.string.show_gui), true);
        return PendingIntent.getService(context, btnID2, intent,
                pendingFlag);
    }
    public void notify(String messageTitle, String messageBody, Boolean isOnGoing) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, String.valueOf(ID))
                .setSmallIcon(R.mipmap.ic_launcher)
                .addAction(0, context.getString(R.string.show_gui), getShowPendingIntent())
                .addAction(0, context.getString(R.string.proxy_stop), getStopPendingIntent())
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setOngoing(isOnGoing)
                .setContentIntent(getPendingIntent(getIntent()));
        notificationManager = createNotificationManager();
        notificationManager.notify(ID, notificationBuilder.build());
    }

}
