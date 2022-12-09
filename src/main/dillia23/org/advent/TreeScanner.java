package org.advent;

import org.advent.utils.FileFinder;
import org.advent.utils.MatrixUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class TreeScanner {
    private static final String TREES_FILE_LOC = "/trees.txt";

    private int visibleTrees;
    private final Tree[][] treeGrove;

    public TreeScanner() {
        final Scanner scanner;
        // todo be more clever and make this dynamic from the input
        try  {
            scanner = FileFinder.getScannerForFileName(TREES_FILE_LOC);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        treeGrove = buildTreeGrove(scanner);
        scanTrees();
        countVisibleTreez();
    }

    public int getVisibleTrees() {
        return visibleTrees;
    }

    public int getHighestScenicScore() {
        int maxScenicScore = -1;
        for (final Tree[] trees: treeGrove) {
            for (final Tree tree: trees) {
                maxScenicScore = Math.max(maxScenicScore, tree.scenicScore);
            }
        }

        return maxScenicScore;
    }

    private void scanTrees() {
        for (int row = 0; row < treeGrove.length; row++) {
            // scan to the right
            int rHeight = -1;
            for (int col = 0; col < treeGrove[row].length; col++) {
                if (treeGrove[row][col].height > rHeight) {
                    treeGrove[row][col] = new Tree(treeGrove[row][col].height, true, -1);
                    rHeight = treeGrove[row][col].height;
                }
            }

            // scan to the left
            int lHeight = -1;
            for (int col = treeGrove[row].length - 1; col >= 0; col--) {
                if (treeGrove[row][col].height > lHeight) {
                    treeGrove[row][col] = new Tree(treeGrove[row][col].height, true, -1);
                    lHeight = treeGrove[row][col].height;
                }
            }
        }

        for (int col = 0; col < treeGrove[0].length; col++) {
            // scan top to bottom
            int tHeight = -1;
            for (int row = 0; row < treeGrove.length; row++) {
                if (treeGrove[row][col].height > tHeight) {
                    treeGrove[row][col] = new Tree(treeGrove[row][col].height, true, -1);
                    tHeight = treeGrove[row][col].height;
                }
            }

            // scan bottom to top
            int bHeight = -1;
            for (int row = treeGrove.length - 1; row >= 0; row--) {
                if (treeGrove[row][col].height > bHeight) {
                    treeGrove[row][col] = new Tree(treeGrove[row][col].height, true, -1);
                    bHeight = treeGrove[row][col].height;
                }
            }
        }
    }

    private void countVisibleTreez() {
        for (int row = 0; row < treeGrove.length; row++) {
            for (int col = 0; col < treeGrove[row].length; col++) {
                final Tree tree = treeGrove[row][col];
                if (tree.visible) {
                    visibleTrees++;
                }
            }
        }
    }

    private Tree[][] buildTreeGrove(final Scanner scanner) {
        final Tree[][] trees = new Tree[99][99];
        int row = 0;
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            for (int col = 0; col < line.length(); col++) {
                int height = Integer.parseInt(String.valueOf(line.charAt(col)));
                trees[row][col] = new Tree(height, false, -1);
            }
            row++;
        }

        return trees;
    }

    private void countVisibleTrees() {
        int dir = 0;
        int records = treeGrove.length * treeGrove[0].length;
        int left = 0;
        int right = treeGrove.length - 1;
        int top = 0;
        int bottom = treeGrove[0].length - 1;
        while (records > 0) {
            // go right
            if (dir % 4 == 0) {
                for (int col = left; col <= right; col++) {
                    records--;
                    visibleTrees += countVisibleTrees(top, col);
                }
                top++;
            }
            // go down
            else if (dir % 4 == 1) {
                for (int row = top; row <= bottom; row++) {
                    records--;
                    visibleTrees += countVisibleTrees(row, right);
                }
                right--;
            }
            // go left
            else if (dir % 4 == 2) {
                for (int col = right; col >= left; col--) {
                    records--;
                    visibleTrees += countVisibleTrees(bottom, col);
                }
                left++;
            }
            // go up
            else {
                for (int row = bottom; row >= top; row--) {
                    records--;
                    visibleTrees += countVisibleTrees(row, left);
                }
                bottom--;
            }

            dir++;
        }
    }

    private void countVisibleTreesReverse() {
        int dir = 0;
        int records = treeGrove.length * treeGrove[0].length;
        int left = 0;
        int right = treeGrove.length - 1;
        int top = 0;
        int bottom = treeGrove[0].length - 1;
        while (records > 0) {
            // go down
            if (dir % 4 == 0) {
                for (int row = top; row <= bottom; row++) {
                    records--;
                    visibleTrees += countVisibleTrees(row, right);
                }
                right--;
            }
            // go right
            if (dir % 4 == 1) {
                for (int col = left; col <= right; col++) {
                    records--;
                    visibleTrees += countVisibleTrees(top, col);
                }
                top++;
            }
            // go up
            if(dir % 4 == 2) {
                for (int row = bottom; row >= top; row--) {
                    records--;
                    visibleTrees += countVisibleTrees(row, left);
                }
                bottom--;
            }
            // go left
            if (dir % 4 == 3) {
                for (int col = right; col >= left; col--) {
                    records--;
                    visibleTrees += countVisibleTrees(bottom, col);
                }
                left++;
            }

            dir++;
        }
    }

    private int countVisibleTrees(final int row, final int col) {
        final Tree tree = treeGrove[row][col];
        // only check outside directions
        for (final int[] direction: MatrixUtils.DIRECTIONS) {
            // check if out of bounds
            if (MatrixUtils.isMoveOutOfBounds(treeGrove, new int[] {row, col}, direction)) {
                treeGrove[row][col] = new Tree(tree.height, true, -1);
                return 1;
            }
            final Tree move = treeGrove[row + direction[0]][col + direction[1]];
            // if tree is visible next direction and taller curr direction
            if (move.visible && tree.height > move.height) {
                treeGrove[row][col] = new Tree(tree.height, true, -1);
                return 1;
            }
            // need an update case
            // if a tree was already visited but now an edge is actually visible
        }

        return 0;
    }

    private static record Tree(int height, boolean visible, int scenicScore) {}
}
