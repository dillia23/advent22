package org.advent;

import org.advent.utils.FileFinder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

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
        calculateRucksacksPriority(scanner);
    }

    public int getTotalPriority() {
        return totalPriority;
    }

    private void calculateRucksacksPriority(final Scanner scanner) {
        while(scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            final Set<Character> buffer = new HashSet<>();
            for (int i = 0; i < line.length() / 2; i++) {
                buffer.add(line.charAt(i));
            }
            for (int i = line.length() / 2; i < line.length(); i++) {
                if (buffer.contains(line.charAt(i))) {
                    totalPriority += getPriority(line.charAt(i));
                }
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

    private static char switchCase(final char c) {
        if (c > 'Z') {
            return Character.toUpperCase(c);
        } else {
            return Character.toLowerCase(c);
        }
    }
}
