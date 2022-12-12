package org.advent;

import org.advent.utils.FileFinder;
import org.advent.utils.MatrixUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class SignalFinder {
    private static final String ELEVATION_FILE_LOC = "/elevation.txt";

    private final int minSteps;
    private final char[][] elevation;
    private Coordinate start;
    private Coordinate end;

    public SignalFinder() {
        final Scanner scanner;
        try  {
            scanner = FileFinder.getScannerForFileName(ELEVATION_FILE_LOC);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        elevation = new char[41][66];
        scanElevation(scanner);
        minSteps = findMinSteps();
    }

    public int getMinSteps() {
        return minSteps;
    }

    private int findMinSteps() {
        final Set<Coordinate> visited = new HashSet<>();
        final Queue<CoordStep> q = new LinkedList<>();
        q.add(new CoordStep(start, 0));
        visited.add(start);

        while (!q.isEmpty()) {
            final CoordStep coordStep = q.remove();
            final Coordinate curr = coordStep.coordinate();
            if (curr.equals(end)) {
                return coordStep.step();
            } else {
                addNextSteps(q, visited, curr, coordStep.step);
            }
        }

        return -1;
    }

    private void addNextSteps(final Queue<CoordStep> q, final Set<Coordinate> visited, final Coordinate curr, final int step) {
        for (final int[] dir: MatrixUtils.DIRECTIONS) {
            final Coordinate next = new Coordinate(curr.row() + dir[0], curr.col() + dir[1]);
            if (!visited.contains(next)
                    && !isOutOfBounds(next)
                    && elevation[next.row()][next.col()] - elevation[curr.row()][curr.col()] <= 1) {
                q.add(new CoordStep(next, step + 1));
                visited.add(next);
            }
        }
    }

    private boolean isOutOfBounds(final Coordinate coordinate) {
        return coordinate.row() < 0 || coordinate.col() < 0
                || coordinate.row() >= elevation.length || coordinate.col() >= elevation[0].length;
    }

    private void scanElevation(final Scanner scanner) {
        int count = 0;
        while(scanner.hasNextLine()) {
            final String line = scanner.nextLine();

            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == 'S') {
                    elevation[count][i] = 'a';
                    start = new Coordinate(count, i);
                } else if (line.charAt(i) == 'E') {
                    elevation[count][i] = 'z';
                    end = new Coordinate(count, i);
                } else {
                    elevation[count][i] = line.charAt(i);
                }
            }
            count++;
        }

        if (start == null || end == null) {
            throw new IllegalArgumentException("does not have start and/or end");
        }
    }

    record Coordinate(int row,int col){}
    record CoordStep(Coordinate coordinate, int step){}
}
