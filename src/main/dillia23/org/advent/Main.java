package org.advent;

import org.advent.day2.RockPaperScissors;

public class Main {
    public static void main(String[] args) {
        // Day 8
        day8();

        // Day 9
        day9();

        // Day 10
        day10();

        // Day 11
        day11();

        // Day 12
        day12();
    }

    private static void day12() {
        System.out.println("Day 12:");
        final SignalFinder signalFinder = new SignalFinder();
        System.out.printf("Min steps to best signal: %s%n", signalFinder.getMinSteps());
        System.out.printf("***%n");
    }

    private static void day11() {
        System.out.println("Day 11:");
        final MonkeyBusinessCalculator monkeyBusinessCalculator = new MonkeyBusinessCalculator();
        System.out.printf("Level of monkey business: %s%n", monkeyBusinessCalculator.getMonkeyBusinessLevel());
        System.out.printf("***%n");
    }

    private static void day10() {
        System.out.println("Day 10:");
        final SignalStrengthChecker signalStrengthChecker = new SignalStrengthChecker();
        System.out.printf("Signal strength sum: %s%n", signalStrengthChecker.getSignalStrength());
        System.out.printf("***%n");
    }

    private static void day9() {
        System.out.println("Day 9:");
        final RopeSimulator ropeSimulator = new RopeSimulator();
        System.out.printf("Rope tail visited to {%s} positions%n", ropeSimulator.getPositionCount());
        System.out.printf("***%n");
    }

    private static void day8() {
        System.out.println("Day 8:");
        final TreeScanner treeScanner = new TreeScanner();
        System.out.printf("Total visible trees: %s%n", treeScanner.getVisibleTrees());
        System.out.printf("Highest scenic score: %s%n", treeScanner.getHighestScenicScore());
        System.out.printf("***%n");
    }

    private static void day7() {
        System.out.println("Day 7: ");
        final DirectoryScanner directoryScanner = new DirectoryScanner();
        System.out.printf("Total directory size: %s%n", directoryScanner.getTotalSize());
        System.out.printf("Small directory size to delete for update: %s%n", directoryScanner.getMinDirSize());
        System.out.printf("***%n");
    }

    private static void day6() {
        System.out.println("Day 6: ");
        final DatastreamReader datastreamReader = new DatastreamReader();
        System.out.printf("Need to process {%s} to get start-of-packet marker.%n", datastreamReader.getStartOfPacketMarkerLoc());
        System.out.printf("Need to process {%s} to get start-of-message marker.%n", datastreamReader.getStartOfMessageMarkerLoc());
        System.out.printf("***%n");
    }

    private static void day5() {
        System.out.println("Day 5: ");
        final CrateStacker crateStacker = new CrateStacker();
        System.out.printf("Final crate tops: %s%n", crateStacker.getCrateTops());
        System.out.printf("***%n");
    }

    private static void day4() {
        System.out.println("Day 4: ");
        final CompartmentClearer compartmentClearer = new CompartmentClearer();
        System.out.printf("Total overlapping pairs: %s%n", compartmentClearer.getOverlappingPairs());
        System.out.printf("***%n");
    }

    private static void day3() {
        System.out.println("Day 3: ");
        final RucksackReorganizer rucksackReorganizer = new RucksackReorganizer();
        System.out.printf("Total priority: %s%n", rucksackReorganizer.getTotalPriority());
        System.out.printf("***%n");
    }

    private static void day2() {
        System.out.println("Day 2: ");
        final RockPaperScissors rockPaperScissors = new RockPaperScissors();
        System.out.printf("Total score: %s%n", rockPaperScissors.getTotalScore());
        System.out.printf("***%n");
    }

    private static void day1() {
        System.out.println("Day 1: Count Calories");
        final CalorieCounter calorieCounter = new CalorieCounter();
        System.out.printf("Highest calories: %s%n", calorieCounter.getMaxCalories());
        final int topCalories = calorieCounter.getTopCalories();
        System.out.printf("Top 3 calories: %s%n", topCalories);
        System.out.printf("***%n");
    }
}