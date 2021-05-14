package com.example.eling.fitnessapp.calories;

public class CalorieDetails {

    private String date;
    private float totalCalories;
    private String caloriesID;

    public CalorieDetails() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(float totalCalories) {
        this.totalCalories = totalCalories;
    }

    public String getCaloriesID() {
        return caloriesID;
    }

    public void setCaloriesID(String caloriesID) {
        this.caloriesID = caloriesID;
    }
}
