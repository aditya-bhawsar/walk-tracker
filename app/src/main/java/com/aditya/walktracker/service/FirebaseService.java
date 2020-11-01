package com.aditya.walktracker.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.aditya.walktracker.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class FirebaseService extends FirebaseMessagingService {

    String channelId= "cloud_messaging";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId  = new Random().nextInt();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel(manager);
        }

        Notification notification =new  NotificationCompat.Builder(this, channelId)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("message"))
                .setSmallIcon(R.drawable.ic_walk_40dp)
                .setAutoCancel(true)
                .build();

        manager.notify(notificationId, notification);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel(NotificationManager manager){
        String channelName = "Cloud Messages";
        NotificationChannel notificationChannel = new NotificationChannel(channelId,channelName,NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setDescription(getString(R.string.channel_desc_fcm));
        notificationChannel.setLightColor(R.color.colorAccent);

        manager.createNotificationChannel(notificationChannel);
    }
}
