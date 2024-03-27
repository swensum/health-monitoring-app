package com.health.myapplication.ui.mon;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.health.myapplication.R;
import com.health.myapplication.ui.food.DatabaseHelper;
import com.health.myapplication.ui.food.Food;

import java.util.ArrayList;
import java.util.List;

public class MonitoringFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private EditText searchEditText;
    private Button searchButton;
    private ListView resultsListView;
    private Button plusButton;
    private ListView selectedItemsListView;
    private TextView totalCaloriesTextView;

    private List<Food> searchResults;
    private List<Food> selectedItems;
    private int selectedItemPosition = -1;
    private CalorieViewModel calorieViewModel;
    private FuzzyStringMatcher fuzzyStringMatcher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        dbHelper = new DatabaseHelper(requireContext());
        fuzzyStringMatcher = new FuzzyStringMatcher();
        View rootView = inflater.inflate(R.layout.fragment_monitoring, container, false);
        searchEditText = rootView.findViewById(R.id.searchEditText);

        searchButton = rootView.findViewById(R.id.searchButton);
        resultsListView = rootView.findViewById(R.id.resultsListView);
        plusButton = rootView.findViewById(R.id.plusButton);
        selectedItemsListView = rootView.findViewById(R.id.selectedItemsListView);
        totalCaloriesTextView = rootView.findViewById(R.id.totalCaloriesTextView);
        CardView burnCaloriesCardView = rootView.findViewById(R.id.burnCaloriesCardView);
        Button burnCaloriesButton = rootView.findViewById(R.id.burnCaloriesButton);
        calorieViewModel = new ViewModelProvider(requireActivity()).get(CalorieViewModel.class);
        burnCaloriesButton.setOnClickListener(view -> navigateToHomeFragment());
        searchResults = new ArrayList<>();
        selectedItems = new ArrayList<>();


        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                searchFoods(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        plusButton.setOnClickListener(view -> {
            if (selectedItemPosition >= 0 && selectedItemPosition < searchResults.size()) {
                Food selectedFood = searchResults.get(selectedItemPosition);
                selectedItems.add(selectedFood);

                updateSelectedItemsListView();

                selectedItemPosition = -1;

                calculateAndShowTotalCalories();

                plusButton.setVisibility(View.INVISIBLE);
                burnCaloriesButton.setVisibility(View.VISIBLE);
                burnCaloriesCardView.setVisibility(View.VISIBLE);
            }
        });

        resultsListView.setOnItemClickListener((adapterView, view, position, id) -> {
            if (!searchResults.isEmpty() && position < searchResults.size()) {

                selectedItemPosition = position;


                plusButton.setVisibility(View.VISIBLE);
            }
        });

        return rootView;
    }

    private void searchFoods(String query) {
        List<String> foodNames = dbHelper.getAllFoodNames();


        String mostSimilarFoodName = fuzzyStringMatcher.findMostSimilarString(foodNames, query);


        searchResults = dbHelper.searchFoods(mostSimilarFoodName);

        if (searchResults.isEmpty()) {

            resultsListView.setVisibility(View.GONE);
        } else {

            resultsListView.setVisibility(View.VISIBLE);

            ArrayAdapter<Food> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, searchResults);
            resultsListView.setAdapter(adapter);
        }
    }

    private void updateSelectedItemsListView() {
        ArrayAdapter<Food> selectedItemsAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, selectedItems);
        selectedItemsListView.setAdapter(selectedItemsAdapter);
    }


    private void calculateAndShowTotalCalories() {
        int totalCalories = 0;
        for (Food selectedFood : selectedItems) {
            totalCalories += selectedFood.getCalorie();
        }

        totalCaloriesTextView.setText("Calorie Intake : " + totalCalories);
        calorieViewModel.setTotalCalories(totalCalories);;
    }
    private void navigateToHomeFragment() {

        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
        navController.navigate(R.id.action_monitoring_to_home);
    }

    @Override
    public void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }

}
