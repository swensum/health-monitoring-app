package com.health.myapplication.ui.BMI;

import  android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.health.myapplication.R;
import androidx.lifecycle.ViewModelProvider;

public class BMIFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    public BMIFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bmi, container, false);
        EditText weightEditText = view.findViewById(R.id.editTextWeight);
        EditText heightEditText = view.findViewById(R.id.editTextHeight);
        Button calculateButton = view.findViewById(R.id.buttonCalculate);
        TextView bmiResultTextView = view.findViewById(R.id.textViewBMI);
        TextView healthConditionTextView = view.findViewById(R.id.healthConditionTextView);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        calculateButton.setOnClickListener(v -> calculateBMI(weightEditText.getText().toString(), heightEditText.getText().toString(),
                bmiResultTextView, healthConditionTextView));

        return view;
    }

     void calculateBMI(String weightStr, String heightStr, TextView bmiResultTextView, TextView healthConditionTextView) {
        if (!weightStr.isEmpty() && !heightStr.isEmpty()) {
            double weight = Double.parseDouble(weightStr);
            double height = Double.parseDouble(heightStr) / 100.0;


            double bmiValue = weight / (height * height);


            bmiResultTextView.setText(String.format("BMI: %.2f", bmiValue));


            String healthCondition = determineHealthCondition(bmiValue);
            healthConditionTextView.setText("BMI INDEX: " + healthCondition);
            sharedViewModel.setBmiResult(String.format("BMI: %.2f", bmiValue));
        } else {

            bmiResultTextView.setText("Please enter weight and height.");
            healthConditionTextView.setText("");
        }

    }


    private String determineHealthCondition(double bmiValue) {

        if (bmiValue < 18.5) {
            return "Underweight";
        } else if (bmiValue >= 18.5 && bmiValue < 25) {
            return "Normal Weight";
        } else if (bmiValue >= 25 && bmiValue < 30) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }
}


