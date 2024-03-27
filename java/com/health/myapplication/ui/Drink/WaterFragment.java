package com.health.myapplication.ui.Drink;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.health.myapplication.R;

import java.util.concurrent.TimeUnit;

public class WaterFragment extends Fragment {

    private TextView ageTextView;
    private TextView heightTextView;
    private TextView weightTextView;
    private Button editButton;
    private EditText editAge;
    private EditText editHeight;
    private EditText editWeight;
    private TextView waterScoreTextView;
    private EditText reminderTimeEditText;

    private int age;
    private int height;
    private int weight;

    private static final String CHANNEL_ID = "WaterReminderChannel";

    public WaterFragment() {
    }

    public WaterFragment(int age, int height, int weight) {
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_water, container, false);

        ageTextView = rootView.findViewById(R.id.ageTextView);
        heightTextView = rootView.findViewById(R.id.heightTextView);
        weightTextView = rootView.findViewById(R.id.weightTextView);
        editButton = rootView.findViewById(R.id.editButton);
        editAge = rootView.findViewById(R.id.editAge);
        editHeight = rootView.findViewById(R.id.editHeight);
        editWeight = rootView.findViewById(R.id.editWeight);
        waterScoreTextView = rootView.findViewById(R.id.waterScoreTextView);
        reminderTimeEditText = rootView.findViewById(R.id.reminderTimeEditText);

        ageTextView.setText("Age: " + age);
        heightTextView.setText("Height: " + height + " cm");
        weightTextView.setText("Weight: " + weight + " kg");

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleEditVisibility();
            }
        });

        Button calculateButton = rootView.findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateWaterIntake();
            }
        });

        return rootView;
    }

    private void toggleEditVisibility() {
        int visibility = editAge.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE;
        editAge.setVisibility(visibility);
        editHeight.setVisibility(visibility);
        editWeight.setVisibility(visibility);

        if (visibility == View.VISIBLE) {
            editAge.setText(ageTextView.getText().toString().replace("Age: ", ""));
            editHeight.setText(heightTextView.getText().toString().replace("Height: ", "").replace(" cm", ""));
            editWeight.setText(weightTextView.getText().toString().replace("Weight: ", "").replace(" kg", ""));
        }
    }

    private void calculateWaterIntake() {
        String ageStr = editAge.getText().toString();
        String heightStr = editHeight.getText().toString();
        String weightStr = editWeight.getText().toString();
        String reminderTimeStr = reminderTimeEditText.getText().toString();

        if (TextUtils.isEmpty(ageStr) || TextUtils.isEmpty(heightStr) || TextUtils.isEmpty(weightStr) || TextUtils.isEmpty(reminderTimeStr)) {
            waterScoreTextView.setText("Please enter valid values");
            return;
        }

        int age = Integer.parseInt(ageStr);
        int height = Integer.parseInt(heightStr);
        int weight = Integer.parseInt(weightStr);
        long reminderTimeMillis = TimeUnit.MINUTES.toMillis(Long.parseLong(reminderTimeStr));
        double bmr;

        boolean isMale = true;
        if (isMale) {
            bmr = 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age);
        } else {
            bmr = 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age);
        }

        double waterIntake = 30 * weight;

        waterScoreTextView.setText("Water Intake Score: " + waterIntake + " milliliters");

        Uri defaultNotificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        scheduleNotification(reminderTimeMillis, defaultNotificationSound);
    }

    private void scheduleNotification(long delayMillis, Uri notificationSound) {
        long futureInMillis = System.currentTimeMillis() + delayMillis;

        Intent notificationIntent = new Intent(getContext(), WaterNotificationReceiver.class);
        notificationIntent.putExtra("notification_message", "It's time to drink water!");
        notificationIntent.putExtra("notification_sound", notificationSound.toString());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getContext(),
                0,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE
        );

        createNotificationChannel();

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
            }


            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.download)
                    .setContentTitle("Water Reminder")
                    .setContentText("It's time to drink water!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setSound(notificationSound);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
            notificationManager.notify(0, builder.build());


        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Water Reminder Channel";
            String description = "Channel for Water Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
