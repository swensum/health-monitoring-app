package com.health.myapplication.ui.home;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.health.myapplication.R; // Replace with the correct import for your R class
import com.health.myapplication.callback.stepsCallback;
import com.health.myapplication.helper.GeneralHelper;
import com.health.myapplication.service.StepDetectorService;
import com.health.myapplication.service.StepDetectorService.subscribe;
import com.health.myapplication.ui.mon.CalorieViewModel;

public class HomeFragment extends Fragment implements stepsCallback {
    private TextView stepsTextView;
    private TextView caloriesTextView;
    private TextView distanceTextView;
    private ProgressBar caloriesProgressBar;
    private CalorieViewModel calorieViewModel;
    private TextView totalCaloriesTextView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        calorieViewModel = new ViewModelProvider(requireActivity()).get(CalorieViewModel.class);
        stepsTextView = view.findViewById(R.id.STEPS);
        caloriesTextView = view.findViewById(R.id.CALORIES);
        distanceTextView = view.findViewById(R.id.DISTANCE);
        caloriesProgressBar = view.findViewById(R.id.caloriesProgressBar);
        totalCaloriesTextView = view.findViewById(R.id.totalCaloriesTextView);

        // Start the service based on API level
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService();
        } else {
            startLegacyService();
        }

        subscribe.register(this);

        return view;
    }

    @Override
    public void subscribeSteps(int steps) {
        stepsTextView.setText(String.valueOf(steps));
        String caloriesString = GeneralHelper.getCalories(steps);

        caloriesTextView.setText(caloriesString);
        distanceTextView.setText(GeneralHelper.getDistanceCovered(steps));

        int totalCaloriesFromDiet = getTotalCaloriesFromDietMonitoring();
        totalCaloriesTextView.setText("Total Calories: " + totalCaloriesFromDiet);


        int calories = extractNumericValue(caloriesString);
        caloriesProgressBar.setMax(totalCaloriesFromDiet);
        caloriesProgressBar.setProgress(calories);
        if (calories == totalCaloriesFromDiet) {
            playAlarmSound();
        }
    }
    private int extractNumericValue(String input) {

        String numericPart = input.replaceAll("[^0-9]", "");

        try {

            return Integer.parseInt(numericPart);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void playAlarmSound() {
        try {

            MediaPlayer mediaPlayer = MediaPlayer.create(requireContext(), R.raw.aalarm);
            mediaPlayer.setLooping(false);


            mediaPlayer.start();


            mediaPlayer.setOnCompletionListener(mediaPlayer1 -> mediaPlayer1.release());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void startForegroundService() {
        Intent intent = new Intent(requireContext(), StepDetectorService.class);
        requireContext().startForegroundService(intent);
    }


    private void startLegacyService() {
        Intent intent = new Intent(requireContext(), StepDetectorService.class);
        requireContext().startService(intent);
    }
    private int getTotalCaloriesFromDietMonitoring() {

        if (calorieViewModel != null) {
            return calorieViewModel.getTotalCalories();
        } else {

            return 0;
        }
    }

}
