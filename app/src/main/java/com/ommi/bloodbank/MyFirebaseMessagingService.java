package com.ommi.bloodbank;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ommi.bloodbank.Activities.InboxActivity;
import com.ommi.bloodbank.Activities.MainActivity;
import com.ommi.bloodbank.Activities.NotificationActivity;

import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private String ADMIN_CHANNEL_ID = "admin_channel";
    Intent intent = null;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String id = "", con_id = "", img;
        Map<String, String> data = remoteMessage.getData();

        if (data.containsKey("id")) {
            id = data.get("id");
            String title = data.get("title");
            img = data.get("img");
            con_id = data.get("conversation_id");
            Intent intent1 = new Intent("com.broadcast.fcm.message");
            intent1.putExtra("from","service");
            sendBroadcast(intent1);
            intent = new Intent(this, InboxActivity.class);
            intent.putExtra("image", img);
            intent.putExtra("name", title);
            intent.putExtra("con_id", con_id);
            intent.putExtra("id", id);
            intent.putExtra("src","notification");
            Log.d("onMessageReceived__", "try" + remoteMessage.getData().get("title") + remoteMessage.getData().get("message"));

        } else {
            intent = new Intent(this, NotificationActivity.class);
            Intent intent1 = new Intent("com.broadcast.fro.message");
            intent1.putExtra("name","abc");
            sendBroadcast(intent1);
            intent.putExtra("temp", "temp");
            Intent intents = new Intent();
            intents.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            intents.setAction("notify");
            sendBroadcast(intents);
            Log.d("onMessageReceived__", "catch" + remoteMessage.getData().get("title") + remoteMessage.getData().get("message"));
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Random random = new Random();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.logo_app);

        Uri notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.drawable.logo_app);
        notificationBuilder.setLargeIcon(largeIcon);
        if (remoteMessage.getData().containsKey("title"))
            notificationBuilder.setContentTitle(remoteMessage.getData().get("title"));
        else
            notificationBuilder.setContentTitle(remoteMessage.getNotification().getTitle());
        if (remoteMessage.getData().containsKey("message"))
            notificationBuilder.setContentText(remoteMessage.getData().get("message"));
        else
            notificationBuilder.setContentTitle(remoteMessage.getNotification().getBody());
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(notificationSoundUri);
        notificationBuilder.setContentIntent(pendingIntent);


        //Set notification color to match your app color template
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        notificationManager.notify(1, notificationBuilder.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        Log.wtf("firebase_called", "--" + s + "--");
        PrefrenceManager.setDecviceToken(MyFirebaseMessagingService.this, s);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager) {
        CharSequence adminChannelName = "New notification";
        String adminChannelDescription = "Device to devie notification";

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }
}
