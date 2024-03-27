package com.health.myapplication.ui.Drink;

import androidx.lifecycle.ViewModel;

public class WaterViewModel extends ViewModel {

    private int age;
    private int height;
    private int weight;

    // Default constructor
    public WaterViewModel() {
        // Initialize default values
        this.age = 25;
        this.height = 170;
        this.weight = 70;
    }

    // Getter and setter methods for age
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // Getter and setter methods for height
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    // Getter and setter methods for weight
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
