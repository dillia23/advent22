package org.advent;

import org.advent.utils.FileFinder;
import org.advent.utils.MatrixUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TreeScanner {
    private static final String TREES_FILE_LOC = "/trees.txt";

    private final int visibleTrees;
    private final Tree[][] treeGrove;
    private final int highestScenicScore;

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
        visibleTrees = countVisibleTrees();
        calcScenicScore();
        highestScenicScore = setHighestScenicScore();
    }

    public int getVisibleTrees() {
        return visibleTrees;
    }

    public int getHighestScenicScore() {
        return highestScenicScore;
    }

    private int setHighestScenicScore() {
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
                    treeGrove[row][col] = new Tree(treeGrove[row][col].height, true, 1);
                    rHeight = treeGrove[row][col].height;
                }
            }

            // scan to the left
            int lHeight = -1;
            for (int col = treeGrove[row].length - 1; col >= 0; col--) {
                if (treeGrove[row][col].height > lHeight) {
                    treeGrove[row][col] = new Tree(treeGrove[row][col].height, true, 1);
                    lHeight = treeGrove[row][col].height;
                }
            }
        }

        for (int col = 0; col < treeGrove[0].length; col++) {
            // scan top to bottom
            int tHeight = -1;
            for (int row = 0; row < treeGrove.length; row++) {
                if (treeGrove[row][col].height > tHeight) {
                    treeGrove[row][col] = new Tree(treeGrove[row][col].height, true, 1);
                    tHeight = treeGrove[row][col].height;
                }
            }

            // scan bottom to top
            int bHeight = -1;
            for (int row = treeGrove.length - 1; row >= 0; row--) {
                if (treeGrove[row][col].height > bHeight) {
                    treeGrove[row][col] = new Tree(treeGrove[row][col].height, true, 1);
                    bHeight = treeGrove[row][col].height;
                }
            }
        }
    }

    private int countVisibleTrees() {
        int visible = 0;
        for (final Tree[] trees: treeGrove) {
            for (final Tree tree: trees) {
                if (tree.visible) {
                    visible++;
                }
            }
        }

        return visible;
    }

    private Tree[][] buildTreeGrove(final Scanner scanner) {
        final Tree[][] trees = new Tree[99][99];
        int row = 0;
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            for (int col = 0; col < line.length(); col++) {
                int height = Integer.parseInt(String.valueOf(line.charAt(col)));
                trees[row][col] = new Tree(height, false, 1);
            }
            row++;
        }

        return trees;
    }

    private void calcScenicScore() {
        for (int row = 0; row < treeGrove.length; row++) {
            for (int col = 0; col < treeGrove[row].length; col++) {
                calcScenicScore(row, col);
            }
        }
    }

    private void calcScenicScore(final int row, final int col) {
        final Tree tree = treeGrove[row][col];
        final List<Integer> scores = new ArrayList<>();
        for (int[] direction: MatrixUtils.getDirections()) {
            boolean clear = true;
            int score = 0;

            while (clear && !MatrixUtils.isMoveOutOfBounds(treeGrove, new int[]{row, col}, direction)) {
                score++;
                if (treeGrove[row + direction[0]][col + direction[1]].height < tree.height) {
                    MatrixUtils.moveSameDirection(direction);
                } else {
                    clear = false;
                }
            }
            scores.add(score);
        }
        treeGrove[row][col] = new Tree(
                tree.height,
                tree.visible,
                Math.max(tree.scenicScore, scores.stream().reduce(1, (a, b) -> a * b)));
    }

    private void calcScenicScoreVerbose(final int row, final int col) {
        final Tree tree = treeGrove[row][col];
        // check right
        boolean clearRight = true;
        int rightScore = 0;
        int colRight = col + 1;
        while(clearRight && colRight < treeGrove[row].length) {
            rightScore++;
            if(treeGrove[row][colRight].height < tree.height) {
                colRight++;
            } else {
                clearRight = false;
            }
        }

        // check left
        boolean clearLeft = true;
        int leftScore = 0;
        int colLeft = col - 1;
        while(clearLeft && colLeft >= 0) {
            leftScore++;
            if(treeGrove[row][colLeft].height < tree.height) {
                colLeft--;
            } else {
                clearLeft = false;
            }
        }

        // go down
        boolean clearDown = true;
        int downScore = 0;
        int rowDown = row + 1;
        while(clearDown && rowDown < treeGrove.length) {
            downScore++;
            if(treeGrove[rowDown][col].height < tree.height) {
                rowDown++;
            } else {
                clearDown = false;
            }
        }

        // go up
        boolean clearUp = true;
        int upScore = 0;
        int rowUp = row - 1;
        while(clearUp && rowUp >= 0) {
            upScore++;
            if(treeGrove[rowUp][col].height < tree.height) {
                rowUp--;
            } else {
                clearUp = false;
            }
        }

        final int product = rightScore * leftScore * downScore * upScore;

        // store the highest scenic score or 0 for an edge
        treeGrove[row][col] = new Tree(
                tree.height,
                tree.visible,
                Math.max(tree.scenicScore, product));
    }

    private static record Tree(int height, boolean visible, int scenicScore) {}
}
