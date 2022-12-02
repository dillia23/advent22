package org.advent;

public class Main {
    public static void main(String[] args) {
        // Day 1
        System.out.println("Day 1: Count Calories");
        final CalorieCounter calorieCounter = new CalorieCounter();
        System.out.printf("Highest calories: %s%n", calorieCounter.getMaxCalories());
        final int topCalories = calorieCounter.getTopCalories();
        System.out.printf("Top 3 calories: %s%n", topCalories);
    }
}