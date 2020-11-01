package com.aditya.walktracker.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.aditya.walktracker.ui.activity.DetailsActivity;
import com.aditya.walktracker.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class WalkingService extends Service {

    private static int NOTIF_ID = 101;
    private static String Channel_ID = "Walk Service";
    SimpleDateFormat simpleDateFormat, dateFormat;
    String startTime ,date ,endTime;

    @Nullable @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        simpleDateFormat = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
        dateFormat = new SimpleDateFormat("dd/MM/YYYY");

        if(intent.getIntExtra(getString(R.string.forWhat),0)==0){
            Date dateNow = new Date();
            startTime = simpleDateFormat.format(dateNow);
            date = dateFormat.format(dateNow);

            startForeground();
        }
        else {
            Date dateNow = new Date();
            endTime  = simpleDateFormat.format(dateNow);

            Intent i = new Intent(WalkingService.this, DetailsActivity.class);
            i.addFlags(FLAG_ACTIVITY_NEW_TASK);
            i.putExtra(getString(R.string.startKey),intent.getStringExtra(getString(R.string.startKey)));
            i.putExtra(getString(R.string.endKey),endTime);
            i.putExtra(getString(R.string.toSave),true);
            i.putExtra(getString(R.string.dateKey),intent.getStringExtra(getString(R.string.dateKey)));
            stopForeground(true);
            startActivity(i);
            stopSelf();
        }

        return START_NOT_STICKY;
    }

    private void startForeground(){ startForeground(NOTIF_ID, getMyActivityNotification(getString(R.string.start_walk))); }

    private Notification getMyActivityNotification(String text){
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){ createChannel(manager); }


        Intent i = new Intent(this, WalkingService.class);
        i.putExtra(getString(R.string.startKey),startTime);
        i.putExtra(getString(R.string.forWhat),1);
        i.putExtra(getString(R.string.dateKey),date);
        PendingIntent pendingIntent= PendingIntent.getService(this, 1,i ,PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(this,Channel_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(text)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_add, getString(R.string.finish_run),pendingIntent)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_walk_40dp)
                .build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel(NotificationManager manager){
        String channelName = "Walking Service";
        NotificationChannel notificationChannel = new NotificationChannel(Channel_ID,channelName,NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.setDescription(getString(R.string.channel_desc_walk));
        notificationChannel.setLightColor(R.color.colorAccent);
        manager.createNotificationChannel(notificationChannel);
    }
}
