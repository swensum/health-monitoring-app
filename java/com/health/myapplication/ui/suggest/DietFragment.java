package com.health.myapplication.ui.suggest;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.health.myapplication.R;
import com.health.myapplication.ui.BMI.BMIFragment;
import com.health.myapplication.ui.BMI.SharedViewModel;

import java.util.ArrayList;
import java.util.List;

public class DietFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    private TextView bmiValueTextView;
    private TextView bmiResultTextView;

    private Button suggestButton;
    private LinearLayout resultContainer;
    private View view;

    private DietDatabaseHelper dbHelper;
    private SQLiteDatabase database;

    private BMIFragment bmiFragment;

    public DietFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diet, container, false);

        bmiValueTextView = view.findViewById(R.id.bmiValueTextView);
        bmiResultTextView = view.findViewById(R.id.bmiResultTextView);

        suggestButton = view.findViewById(R.id.suggestButton);
        resultContainer = view.findViewById(R.id.resultContainer);

        dbHelper = new DietDatabaseHelper(getContext());
        database = dbHelper.getReadableDatabase();

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        sharedViewModel.getBmiResult().observe(getViewLifecycleOwner(), bmiResult -> {
            int colonIndex = bmiResult.indexOf(":");
            if (colonIndex != -1 && colonIndex + 2 < bmiResult.length()) {
                String bmiValueString = bmiResult.substring(colonIndex + 2);
                double bmiValue = Double.parseDouble(bmiValueString);

                bmiResultTextView.setText("BMI Result: " + getBMIResult(bmiValue));
                bmiValueTextView.setText(bmiResult);
            } else {
                Log.e("DietFragment", "Invalid BMI result format: " + bmiResult);
            }
        });

        bmiFragment = new BMIFragment();

        suggestButton.setOnClickListener(v -> {
            String bmiResult = bmiValueTextView.getText().toString();
            Log.d("DietFragment", "BMI Result: " + bmiResult);
            suggestFoodItems(bmiResult);
        });

        return view;
    }

    private void suggestFoodItems(String bmiResult) {
        Log.d("DietFragment", "Before querying database for BMI: " + bmiResult);


        if (isValidBMI(bmiResult)) {

            List<FoodItem> suggestedItems = getFoodItemsByBMIResult(bmiResult);

            Log.d("DietFragment", "After querying database. Number of suggested items: " + suggestedItems.size());
            updateUI(suggestedItems);
        } else {

            Log.d("DietFragment", "Invalid BMI value. No suggested items.");

        }
    }

    private boolean isValidBMI(String bmiResult) {
        try {
            double bmiValue = Double.parseDouble(bmiResult.substring(bmiResult.indexOf(": ") + 1));

            return bmiValue >= 10.0 && bmiValue <= 50.0;
        } catch (NumberFormatException e) {

            return false;
        }
    }
    private String getBMIResult(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 24.9) {
            return "Normal Weight";
        } else if (bmi < 29.9) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    private DietDatabaseHelper.WeightCategory getWeightCategory(double bmi) {
        DietDatabaseHelper.WeightCategory weightCategory = null;

        if (bmi < 18.5) {
            weightCategory = DietDatabaseHelper.WeightCategory.UNDERWEIGHT;
        } else if (bmi < 24.9) {
            weightCategory = DietDatabaseHelper.WeightCategory.NORMAL;
        } else if (bmi < 29.9) {
            weightCategory = DietDatabaseHelper.WeightCategory.OVERWEIGHT;
        } else {
            weightCategory = DietDatabaseHelper.WeightCategory.OBESE;
        }

        Log.d("DietFragment", "Selected Weight Category: " + weightCategory.name());
        return weightCategory;
    }

    private List<FoodItem> getFoodItemsByBMIResult(String bmiResult) {
        int colonIndex = bmiResult.indexOf(":");
        if (colonIndex != -1 && colonIndex + 2 < bmiResult.length()) {
            double bmi = Double.parseDouble(bmiResult.substring(colonIndex + 2));
            DietDatabaseHelper.WeightCategory weightCategory = getWeightCategory(bmi);
            Log.d("DietFragment", "Selected Weight Category: " + weightCategory.name());

            List<FoodItem> suggestedItems = new ArrayList<>();
            String[] projection = {
                    DietDatabaseHelper.COLUMN_ID,
                    DietDatabaseHelper.COLUMN_NAME,
                    DietDatabaseHelper.COLUMN_DESCRIPTION,
                    DietDatabaseHelper.COLUMN_IMAGE_RESOURCE_ID,
                    DietDatabaseHelper.COLUMN_WEIGHT_CATEGORY
            };
            String selection = DietDatabaseHelper.COLUMN_WEIGHT_CATEGORY + " = ?";
            String[] selectionArgs = {weightCategory.name()};
            Cursor cursor = null;

            try {
                cursor = database.query(
                        DietDatabaseHelper.TABLE_FOODS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );

                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(DietDatabaseHelper.COLUMN_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(DietDatabaseHelper.COLUMN_NAME));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow(DietDatabaseHelper.COLUMN_DESCRIPTION));
                    int imageResourceId = cursor.getInt(cursor.getColumnIndexOrThrow(DietDatabaseHelper.COLUMN_IMAGE_RESOURCE_ID));

                    Log.d("DietFragment", "Retrieved from database: ID=" + id + ", Name=" + name + ", Description=" + description + ", ImageResourceID=" + imageResourceId);

                    FoodItem foodItem = new FoodItem();
                    foodItem.setId(id);
                    foodItem.setName(name);
                    foodItem.setDescription(description);
                    foodItem.setImageResourceId(imageResourceId);

                    suggestedItems.add(foodItem);
                }
                Log.d("DietFragment", "Number of suggested items: " + suggestedItems.size());
            } catch (Exception e) {
                Log.e("DietFragment", "Error querying database: " + e.getMessage());
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            return suggestedItems;
        } else {
            Log.e("DietFragment", "Invalid BMI result format: " + bmiResult);
            return new ArrayList<>(); // Return an empty list in case of invalid format
        }
    }

    private void updateUI(List<FoodItem> suggestedItems) {
        resultContainer.removeAllViews();

        for (FoodItem foodItem : suggestedItems) {
            LinearLayout resultLayout = new LinearLayout(getContext());
            resultLayout.setOrientation(LinearLayout.VERTICAL);
            resultLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView nameTextView = new TextView(getContext());
            nameTextView.setText("Name: " + foodItem.getName());
            nameTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);

            TextView descriptionTextView = new TextView(getContext());
            descriptionTextView.setText("Description: " + foodItem.getDescription());
            descriptionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            ImageView imageView = new ImageView(getContext());
            int imageSize = getResources().getDimensionPixelSize(R.dimen.image_size);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(imageSize, imageSize));
            imageView.setImageResource(foodItem.getImageResourceId());

            resultLayout.addView(nameTextView);
            resultLayout.addView(descriptionTextView);
            resultLayout.addView(imageView);

            resultContainer.addView(resultLayout);
        }
    }
}
