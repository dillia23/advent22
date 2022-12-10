package org.advent;

import org.advent.utils.FileFinder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.Set;

public class SignalStrengthChecker {
    private static final String CYCLES_FILE_LOC = "/cycles.txt";
    private static final Set<Integer> INTERVALS = Set.of(20, 60, 100, 140, 180, 220);
    private static final int RIGHT_MOST_X = 39;

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
        int count = 0;
        StringBuilder sb = new StringBuilder();
        while(scanner.hasNextLine()) {
            final String input = scanner.nextLine();
            if (input.equals("noop")) {
                cycle++;
                count++;
            } else if (input.startsWith("addx")) {
                cycle++;
                count++;

                // addx takes two cycles add we need to check the interval each cycle
                if (INTERVALS.contains(cycle + 1)) {
                    signalStrength = (cycle + 1) * register + signalStrength;
                }
                sb.append(Math.abs(count - register) < 2 ? "█" : ".");
                if (sb.length() == RIGHT_MOST_X + 1) {
                    System.out.println(sb);
                    sb = new StringBuilder();
                    count = 0;
                }

                cycle++;
                count++;
                register += getV(input);
            } else {
                throw new IllegalArgumentException("invalid command");
            }

            if (INTERVALS.contains(cycle + 1)) {
                signalStrength = (cycle + 1) * register + signalStrength;
            }
            assert cycle % 40 == count % 40;
            sb.append(Math.abs(count - register) < 2 ? "█" : ".");
            if (sb.length() - 1 == RIGHT_MOST_X) {
                System.out.println(sb);
                sb = new StringBuilder();
                count = 0;
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
