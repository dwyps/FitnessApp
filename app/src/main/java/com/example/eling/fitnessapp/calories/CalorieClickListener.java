package com.example.eling.fitnessapp.calories;

public interface CalorieClickListener {

    /**
     * @param calorieDetails
     */
    default void onClick(CalorieDetails calorieDetails) {

    }

    void onLongClick(CalorieDetails calorieDetails);

}
