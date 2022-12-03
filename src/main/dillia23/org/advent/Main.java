package org.advent;

import org.advent.day2.RockPaperScissors;

public class Main {
    public static void main(String[] args) {
        // Day 1
        day1();

        // Day 2
        day2();
    }

    private static void day2() {
        System.out.println("Day 2: ");
        final RockPaperScissors rockPaperScissors = new RockPaperScissors();
        System.out.printf("Total score: %s%n", rockPaperScissors.getTotalScore());
        System.out.printf("***%n");
    }

    private static void day1() {
        System.out.println("Day 1: Count Calories");
        final CalorieCounter calorieCounter = new CalorieCounter();
        System.out.printf("Highest calories: %s%n", calorieCounter.getMaxCalories());
        final int topCalories = calorieCounter.getTopCalories();
        System.out.printf("Top 3 calories: %s%n", topCalories);
        System.out.printf("***%n");
    }
}