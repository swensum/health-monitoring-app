package com.health.myapplication.ui.Drink;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.health.myapplication.R;

public class WaterNotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "WaterReminderChannel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("notification_message");
        String soundUri = intent.getStringExtra("notification_sound");
        showNotification(context, message, soundUri);


        Toast.makeText(context, "It's time to drink water!", Toast.LENGTH_SHORT).show();
    }

    private void showNotification(Context context, String message, String soundUri) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.download)
                .setContentTitle("Water Reminder")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(Uri.parse(soundUri));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, builder.build());
    }
}
