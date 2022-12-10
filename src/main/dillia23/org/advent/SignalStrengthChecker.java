package org.advent;

import org.advent.utils.FileFinder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class SignalStrengthChecker {
    private static final String CYCLES_FILE_LOC = "/cycles.txt";
    private static final Set<Integer> INTERVALS = Set.of(20, 60, 100, 140, 180, 220);

    private final int signalStrengthAtIntervals;

    public SignalStrengthChecker() {
        final Scanner scanner;
        try  {
            scanner = FileFinder.getScannerForFileName(CYCLES_FILE_LOC);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        signalStrengthAtIntervals = calcSignalStrength(scanner);
    }

    public int getSignalStrength() {
        return signalStrengthAtIntervals;
    }

    private int calcSignalStrength(final Scanner scanner) {
        int cycle = 0;
        int register = 1;
        int signalStrength = 0;
        while(scanner.hasNextLine()) {
            final String input = scanner.nextLine();

            if (input.equals("noop")) {
                cycle++;
            } else if (input.startsWith("addx")) {
                cycle++;
                // addx takes two cycles add we need to check the interval each cycle
                if (INTERVALS.contains(cycle + 1)) {
                    signalStrength = (cycle + 1) * register + signalStrength;
                }
                cycle++;
                int v = getV(input);
                register += v;
            } else {
                throw new IllegalArgumentException("invalid command");
            }
            if (INTERVALS.contains(cycle + 1)) {
                signalStrength = (cycle + 1) * register + signalStrength;
            }
        }

        return signalStrength;
    }

    private static int getV(final String input) {
        final String[] parts = input.split(" ");
        if (parts.length != 2) {
            throw new IllegalArgumentException("expected 2 parts to addX");
        }

        return Integer.parseInt(parts[1]);
    }
}
