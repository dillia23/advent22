package org.advent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.PriorityQueue;
import java.util.Scanner;

public class CalorieCounter {
    private static final String CALORIES_FILE_LOC = "/resources/calories.txt";
    private int maxCalories;

    private final PriorityQueue<Integer> topCalories;

    public CalorieCounter() {
        final File input = new File(CALORIES_FILE_LOC);
        topCalories = new PriorityQueue<>(4);
        try {
            final Scanner scanner = new Scanner(input);
            findMaxCalories(scanner);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int getMaxCalories() {
        return maxCalories;
    }

    public int getTopCalories() {
        return topCalories.stream().reduce(0, Integer::sum);
    }

    private void findMaxCalories(final Scanner scanner) {
        int max = 0;
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            try {
                max += Integer.parseInt(line);
            } catch (NumberFormatException e) {
                topCalories.add(max);
                this.maxCalories = Math.max(max, maxCalories);
                max = 0;
                cleanUpHeap();
            }
        }
    }

    private void cleanUpHeap() {
        if (topCalories.size() > 3) {
            topCalories.poll();
        }
    }
}
