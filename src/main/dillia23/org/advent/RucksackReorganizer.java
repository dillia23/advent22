package org.advent;

import org.advent.utils.FileFinder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class RucksackReorganizer {
    private static final String RUCKSACK_FILE_LOC = "/rucksack.txt";

    private int totalPriority;

    public RucksackReorganizer() {
        final Scanner scanner;
        try  {
            scanner = FileFinder.getScannerForFileName(RUCKSACK_FILE_LOC);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        calculateRucksacksPriorityBy3(scanner);
    }

    public int getTotalPriority() {
        return totalPriority;
    }

    // part 1
    private void calculateRucksacksPriority(final Scanner scanner) {
        while(scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            final boolean[] buffer = new boolean[256];
            for (int i = 0; i < line.length() / 2; i++) {
                buffer[line.charAt(i)] = true;
            }
            for (int i = line.length() / 2; i < line.length(); i++) {
                if (buffer[line.charAt(i)]) {
                    totalPriority += (getPriority(line.charAt(i)));
                    break;
                }
            }
        }
    }

    // part 2
    private void calculateRucksacksPriorityBy3(final Scanner scanner) {
        while(scanner.hasNextLine()) {
            // process group of 3
            int group = 1;
            final int[] buffer = new int[256];
            while (group <= 3) {
                processGroup(scanner, buffer, group);
                group++;
            }
        }
    }

    private void processGroup(final Scanner scanner, final int[] buffer, int group) {
        final String line = scanner.nextLine();
        for (int i = 0; i < line.length(); i++) {
            if (buffer[line.charAt(i)] == group - 1) {
                buffer[line.charAt(i)] = group;
            }
            if(buffer[line.charAt(i)] == 3) {
                totalPriority += getPriority(line.charAt(i));
                break;
            }
        }
    }

    private static int getPriority(final char c) {
        if (c > 'Z') {
            return c - 'a' + 1;
        } else {
            return c - 'A' + 27;
        }
    }
}
