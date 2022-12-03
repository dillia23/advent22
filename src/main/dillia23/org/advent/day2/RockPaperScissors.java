package org.advent.day2;

import org.advent.utils.FileFinder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class RockPaperScissors {
    private static final String RPS_FILE_LOC = "/rps.txt";
    private static final int ROCK = 1;
    private static final int PAPER = 2;
    private static final int SCISSORS = 3;
    private static final int LOST = 0;
    private static final int DRAW = 3;
    private static final int WON = 6;

    private int totalScore;

    public RockPaperScissors() {
        final Scanner scanner;
        try  {
            scanner = FileFinder.getScannerForFileName(RPS_FILE_LOC);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        scoreGame(scanner);
    }

    public int getTotalScore() {
        return totalScore;
    }

    private void scoreGame(final Scanner scanner) {
        while(scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            final String[] s = line.split(" ");
            if (s.length != 2) {
                throw new IllegalStateException("input invalid");
            }
            final RpsOpponent opponent = RpsOpponent.fromString(s[0]);
            final RpsPlayerGuide player = RpsPlayerGuide.fromString(s[1]);
            scoreRound(opponent, player);
        }
    }

    private void scoreRound(final RpsOpponent opponent, final RpsPlayerGuide player) {
        switch (player) {
            case LOSE -> {
                // unnecessary but nice for accounting
                totalScore += LOST;
                if (opponent == RpsOpponent.SCISSORS) {
                    totalScore += PAPER;
                } else if (opponent == RpsOpponent.ROCK) {
                    totalScore += SCISSORS;
                } else {
                    totalScore += ROCK;
                }
            }
            case DRAW -> {
                totalScore += DRAW;
                if (opponent == RpsOpponent.ROCK) {
                    totalScore += ROCK;
                } else if (opponent == RpsOpponent.PAPER) {
                    totalScore += PAPER;
                } else {
                    totalScore += SCISSORS;
                }
            }
            case WIN -> {
                totalScore += WON;
                if (opponent == RpsOpponent.PAPER) {
                    totalScore += SCISSORS;
                } else if (opponent == RpsOpponent.SCISSORS) {
                    totalScore += ROCK;
                } else {
                    totalScore += PAPER;
                }
            }
            default -> throw new IllegalStateException("must be lost, draw, or won");
        }
    }
}
