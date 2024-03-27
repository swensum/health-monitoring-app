package com.health.myapplication.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.health.myapplication.ui.home.HomeFragment;
import com.health.myapplication.R;
import com.health.myapplication.service.App;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GeneralHelper {

    public static String getTodayDate() {
        Date date = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("dd MMM yyyy");
        return df.format(date);
    }

    public static void updateNotification(Context context, Service service, int step) {
        int NOTIFICATION_ID = 7837;

        Intent intent = new Intent(context, HomeFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_MUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, App.CHANNEL_ID)
                .setContentTitle("Step Counter")
                .setContentText(String.valueOf(step))
                .setTicker(String.valueOf(step))
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Step Counter"))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(String.valueOf(step)))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setProgress(500, step, false)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET);

        Notification notification = notificationBuilder.build();

        service.startForeground(NOTIFICATION_ID, notification);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public static String getCalories(int steps) {
        int calories = (int) (steps * 0.045);
        return calories + " calories";
    }

    public static String getDistanceCovered(int steps) {
        int feet = (int) (steps * 2.5);
        double distance = feet / 3.281;
        double finalDistance = Double.parseDouble(String.format("%.2f", distance));
        return finalDistance + " meters";
    }
}
