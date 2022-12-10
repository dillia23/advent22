package org.advent;

import org.advent.utils.FileFinder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class RopeSimulator {
    private static final String ROPE_FILE_LOC = "/ropes.txt";

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
        int count = 0;
        final Coordinate origin = new Coordinate(0, 0);
        final Queue<Coordinate> heads = new LinkedList<>();
        final Queue<Coordinate> tails = new LinkedList<>();
        final Set<Coordinate> visited = new HashSet<>();

        heads.add(origin);
        tails.add(origin);

        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            final Instruction instruction = getInstruction(line);

            Coordinate newHead = heads.remove();
            Coordinate newTail = tails.remove();

            switch (instruction.direction) {
                case 'R' -> {
                    // go right
                    for (int i = 0; i < instruction.steps; i++) {
                        final Coordinate nextHead = new Coordinate(newHead.x + 1, newHead.y);
                        if (ropeLengthGreaterThan1(newTail, nextHead)) {
                            newTail = newHead;
                        }
                        newHead = nextHead;
                        count += isVisited(visited, newTail);
                    }
                }
                case 'L' -> {
                    // go left
                    for (int i = 0; i < instruction.steps; i++) {
                        final Coordinate nextHead = new Coordinate(newHead.x - 1, newHead.y);
                        if (ropeLengthGreaterThan1(newTail, nextHead)) {
                            newTail = newHead;
                        }
                        newHead = nextHead;
                        count += isVisited(visited, newTail);
                    }
                }
                case 'U' -> {
                    // go up
                    for (int i = 0; i < instruction.steps; i++) {
                        final Coordinate nextHead = new Coordinate(newHead.x, newHead.y + 1);
                        if (ropeLengthGreaterThan1(newTail, nextHead)) {
                            newTail = newHead;
                        }
                        newHead = nextHead;
                        count += isVisited(visited, newTail);
                    }
                }
                case 'D' -> {
                    // go down
                    for (int i = 0; i < instruction.steps; i++) {
                        final Coordinate nextHead = new Coordinate(newHead.x, newHead.y - 1);
                        if (ropeLengthGreaterThan1(newTail, nextHead)) {
                            newTail = newHead;
                        }
                        newHead = nextHead;
                        count += isVisited(visited, newTail);
                    }
                }
                default -> throw new IllegalArgumentException("invalid direction: " + instruction.direction);
            }
            heads.add(newHead);
            tails.add(newTail);
        }

        return count;
    }

    private static boolean ropeLengthGreaterThan1(final Coordinate c1, final Coordinate c2) {
        return Math.abs(c1.x - c2.x) > 1 || Math.abs(c1.y - c2.y) > 1;
    }

    private static int isVisited(final Set<Coordinate> visited, final Coordinate coordinate) {
        if (!visited.contains(coordinate)) {
            visited.add(coordinate);
            return 1;
        }
        return 0;
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
