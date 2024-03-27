package com.health.myapplication.ui.food;


public class Food {
    private int id;
    private String name;
    private int calorie;



    public Food() {

    }

    public Food(String name, int calorie) {
        this.name = name;
        this.calorie = calorie;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    @Override
    public String toString() {
        return name + " - " + calorie + " calories";
    }

    public int getCalories() {
        return 0;
    }
}
