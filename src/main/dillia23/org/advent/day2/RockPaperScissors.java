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
            final RpsPlayer player = RpsPlayer.fromString(s[1]);
            scoreRound(opponent, player);
        }
    }

    private void scoreRound(final RpsOpponent opponent, final RpsPlayer player) {
        switch (player) {
            case ROCK -> {
                totalScore += ROCK;
                if (opponent == RpsOpponent.SCISSORS) {
                    totalScore += WON;
                } else if (opponent == RpsOpponent.ROCK) {
                    totalScore += DRAW;
                } else {
                    totalScore += LOST;
                }
            }
            case PAPER -> {
                totalScore += PAPER;
                if (opponent == RpsOpponent.ROCK) {
                    totalScore += WON;
                } else if (opponent == RpsOpponent.PAPER) {
                    totalScore += DRAW;
                } else {
                    totalScore += LOST;
                }
            }
            case SCISSORS -> {
                totalScore += SCISSORS;
                if (opponent == RpsOpponent.PAPER) {
                    totalScore += WON;
                } else if (opponent == RpsOpponent.SCISSORS) {
                    totalScore += DRAW;
                } else {
                    totalScore += LOST;
                }
            }
            default -> throw new IllegalStateException("must be rock, paper, or scissors");
        }
    }
}
