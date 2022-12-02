package org.advent;

import org.advent.utils.FileFinder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.PriorityQueue;
import java.util.Scanner;

public class CalorieCounter {
    private static final String CALORIES_FILE_LOC = "/calories.txt";
    private int maxCalories;

    private final PriorityQueue<Integer> topCalories;

    public CalorieCounter() {
        topCalories = new PriorityQueue<>(4);
        final Scanner scanner;
        try  {
            scanner = FileFinder.getScannerForFileName(CALORIES_FILE_LOC);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        findMaxCalories(scanner);
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
