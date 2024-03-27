package com.health.myapplication.ui.mon;
// CalorieViewModel.java
import androidx.lifecycle.ViewModel;

public class CalorieViewModel extends ViewModel {
    private int totalCalories = 0;

    public int getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
    }
}
