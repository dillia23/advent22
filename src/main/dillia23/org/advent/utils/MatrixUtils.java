package org.advent.utils;

public class MatrixUtils {
    public static final int[][] DIRECTIONS = new int[][] {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

    public static boolean isMoveOutOfBounds(Object[][] matrix, int[] currPos, int[] move) {
        int nextRow = currPos[0] + move[0];
        int nextCol = currPos[1] + move[1];

        return nextRow < 0 || nextCol < 0 || nextRow >= matrix.length || nextCol >= matrix[0].length;
    }
}
