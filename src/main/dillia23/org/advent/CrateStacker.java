package org.advent;

import org.advent.utils.FileFinder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class CrateStacker {
    private static final String CRATES_FILE_LOC = "/crates.txt";

    private final List<Stack<Character>> crates;

    public CrateStacker() {
        final Scanner scanner;
        crates = new ArrayList<>(List.of(
                new Stack<>(),
                new Stack<>(),
                new Stack<>(),
                new Stack<>(),
                new Stack<>(),
                new Stack<>(),
                new Stack<>(),
                new Stack<>(),
                new Stack<>()));
        try  {
            scanner = FileFinder.getScannerForFileName(CRATES_FILE_LOC);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        processInstructions(scanner);
    }

    public String getCrateTops() {
        final StringBuilder tops = new StringBuilder();
        for (final Stack<Character> crate: crates) {
            if (!crate.isEmpty()) {
                tops.append(crate.peek());
            }
        }

        return tops.toString();
    }

    private void processInstructions(final Scanner scanner) {
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            if (line.contains("[")) {
                // add to appropriate stacks
                addBoxesToCrates(line);
            } else if (line.startsWith("m")) {
                // follow appropriate instruction
                followInstruction(line);
            } else if (line.isBlank()) {
                reverseCrates();
            }
        }
    }

    private void addBoxesToCrates(final String line) {
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) <= 'Z' && line.charAt(i) >= 'A') {
                // add to correct crate
                final Stack<Character> crate = crates.get((i - 1) / 4);
                crate.push(line.charAt(i));
            }
        }
    }

    private void followInstruction(final String line) {
        final String[] info = line.replaceAll("[a-z]","").trim().split("\s");
        if (info.length != 5) {
            throw new IllegalStateException("there must be 3 pieces of information");
        }

        int times = Integer.parseInt(info[0]);
        final Stack<Character> from = crates.get(Integer.parseInt(info[2]) - 1);
        final Stack<Character> to = crates.get(Integer.parseInt(info[4]) - 1);
        final Stack<Character> temp = new Stack<>();
        while (!crates.isEmpty() && times > 0) {
//            part 1
//            to.push(from.pop());
            temp.push(from.pop());
            times--;
        }

        while(!temp.isEmpty()) {
            to.push(temp.pop());
        }
    }

    private void reverseCrates() {
        for(int i = 0; i < crates.size(); i++) {
            final Stack<Character> crate = crates.get(i);
            final Stack<Character> reversed = new Stack<>();
            while (!crate.isEmpty()) {
                reversed.push(crate.pop());
            }

            crates.set(i, reversed);
        }
    }
}
