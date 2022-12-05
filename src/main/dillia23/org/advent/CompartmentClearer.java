package org.advent;

import org.advent.utils.FileFinder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class CompartmentClearer {
    private static final String COMPARTMENTS_FILE_LOC = "/compartments.txt";

    private int overlappingPairs;

    public CompartmentClearer() {
        final Scanner scanner;
        try  {
            scanner = FileFinder.getScannerForFileName(COMPARTMENTS_FILE_LOC);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        findOverlappingCleaners(scanner);
    }

    public int getOverlappingPairs() {
        return overlappingPairs;
    }

    // part 2
    private void findOverlappingCleaners(final Scanner scanner) {
        while(scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            final String[] pairs = line.split(",");
            if (pairs.length != 2) {
                throw new IllegalStateException("must have pair of 2 cleaners");
            }

            final Assignment assignment1 = getAssignment(pairs[0]);
            final Assignment assignment2 = getAssignment(pairs[1]);

            if (assignment1.start <= assignment2.start && assignment1.end >= assignment2.end) {
                overlappingPairs++;
            } else if (assignment2.start <= assignment1.start && assignment2.end >= assignment1.end) {
                overlappingPairs++;
            } else if (assignment1.start <= assignment2.start && assignment1.end >= assignment2.start) {
                overlappingPairs++;
            } else if (assignment2.start <= assignment1.start && assignment2.end >= assignment1.start) {
                overlappingPairs++;
            }
        }
    }

    private Assignment getAssignment(final String pair) {
        final String[] assignmentString = pair.split("-");
        if (assignmentString.length != 2) {
            throw new IllegalStateException("assignment must have 2 parts");
        }

        return new Assignment(Integer.parseInt(assignmentString[0]), Integer.parseInt(assignmentString[1]));
    }

    private record Assignment(int start, int end) {}
}
