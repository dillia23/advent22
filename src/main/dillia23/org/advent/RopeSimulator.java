package org.advent;

import org.advent.utils.FileFinder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class RopeSimulator {
    private static final String ROPE_FILE_LOC = "/ropes.txt";
    private static final int KNOTS = 10;

    private final int positionCount;

    public RopeSimulator() {
        final Scanner scanner;
        try  {
            scanner = FileFinder.getScannerForFileName(ROPE_FILE_LOC);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        positionCount = ropeJourney(scanner);
    }

    public int getPositionCount() {
        return positionCount;
    }

    private int ropeJourney(final Scanner scanner) {
        // 2 knots should result in 6090 positions visited
        final Coordinate origin = new Coordinate(0, 0);
        // keep an ordered array of knots with 0 as the head to 2 as the tail
        // tracks the current position of the knot
        final Coordinate[] knots = new Coordinate[KNOTS];
        // tracks where the tail has visited
        final Set<Coordinate> visited = new HashSet<>();

        // start at the origin
        Arrays.fill(knots, origin);

        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            final Instruction instruction = getInstruction(line);

            switch (instruction.direction) {
                case 'R' -> {
                    // go right
                    for (int i = 0; i < instruction.steps; i++) {
                        final Coordinate head = knots[0];
                        knots[0] = new Coordinate(head.x() + 1, head.y());
                        knots[1] = ropeLengthGreaterThan1(knots[0], knots[1]) ? head : knots[1];
                        shuffleKnots(knots, visited);
                    }
                }
                case 'L' -> {
                    // go left
                    for (int i = 0; i < instruction.steps; i++) {
                        final Coordinate head = knots[0];
                        knots[0] = new Coordinate(head.x() - 1, head.y());
                        knots[1] = ropeLengthGreaterThan1(knots[0], knots[1]) ? head : knots[1];
                        shuffleKnots(knots, visited);
                    }
                }
                case 'U' -> {
                    // go up
                    for (int i = 0; i < instruction.steps; i++) {
                        final Coordinate head = knots[0];
                        knots[0] = new Coordinate(head.x(), head.y() + 1);
                        knots[1] = ropeLengthGreaterThan1(knots[0], knots[1]) ? head : knots[1];
                        shuffleKnots(knots, visited);
                    }
                }
                case 'D' -> {
                    // go down
                    for (int i = 0; i < instruction.steps; i++) {
                        final Coordinate head = knots[0];
                        knots[0] = new Coordinate(head.x(), head.y() - 1);
                        knots[1] = ropeLengthGreaterThan1(knots[0], knots[1]) ? head : knots[1];
                        shuffleKnots(knots, visited);
                    }
                }
                default -> throw new IllegalArgumentException("invalid direction: " + instruction.direction);
            }
        }

        return visited.size();
    }

    private static void shuffleKnots(
            final Coordinate[] knots,
            final Set<Coordinate> visited) {
        for (int j = 2; j < knots.length; j++) {
            if (ropeLengthGreaterThan1(knots[j - 1], knots[j])) {
                knots[j] = moveKnot(knots[j - 1], knots[j]);
            }
        }
        visited.add(knots[knots.length - 1]);
    }

    private static Coordinate moveKnot(final Coordinate knot1, final Coordinate knot2) {
            int x = knot2.x();
            int y =  knot2.y();
            int dx = knot2.x - knot1.x;
            int dy = knot2.y - knot1.y;

            if (dx > 0) {
                x--;
            } else if (dx < 0) {
                x++;
            }
            if (dy > 0) {
                y--;
            } else if (dy < 0) {
                y++;
            }

            return new Coordinate(x, y);
    }

    private static boolean ropeLengthGreaterThan1(final Coordinate c1, final Coordinate c2) {
        return Math.abs(c1.x() - c2.x()) > 1 || Math.abs(c1.y() - c2.y()) > 1;
    }

    private static Instruction getInstruction(final String input) {
        final String[] parts = input.split(" ");
        if (parts.length != 2) {
            throw new IllegalArgumentException("instructions did not come in two parts: " + input);
        }

        return new Instruction(input.charAt(0), Integer.parseInt(parts[1]));
    }

    private record Coordinate(int x, int y){}
    private record Instruction(char direction, int steps){}
}
