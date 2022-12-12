package org.advent;

import org.advent.utils.FileFinder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class MonkeyBusinessCalculator {
    private static final String MONKEYS_FILE_LOC = "/monkeys.txt";
    private static final int ROUNDS = 20;

    private int monkeyBusinessLevel;
    private final List<Monkey> monkeys;
    private char itemName;

    public MonkeyBusinessCalculator() {
        final Scanner scanner;
        try  {
            scanner = FileFinder.getScannerForFileName(MONKEYS_FILE_LOC);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        monkeys = new ArrayList<>();
        takeInInitialMonkeySitutation(scanner);
        playKeepAway();
        calcMonkeyBusiness();
    }

    public int getMonkeyBusinessLevel() {
        return monkeyBusinessLevel;
    }

    private void calcMonkeyBusiness() {
        final PriorityQueue<Integer> monkeyBusiness = new PriorityQueue<>(Collections.reverseOrder());
        for (final Monkey monkey : monkeys) {
            monkeyBusiness.add(monkey.getHandles());
        }

        if (monkeyBusiness.size() < 2) {
            throw new IllegalStateException("too little monkey business!");
        }

        monkeyBusinessLevel = monkeyBusiness.poll() * monkeyBusiness.poll();
    }

    private void playKeepAway() {
        for (int i = 0; i < ROUNDS; i++) {
            // create list of items to remove after round ends
            final Queue<MonkeyRemove> removes = new LinkedList<>();
            for (final Monkey monkey : monkeys) {
                for (final Item item : monkey.getItems()) {
                    monkey.handled();
                    int worryLevel = item.worryLevel();
                    int newWorryLevel = monkey.doOperation(worryLevel);
                    Item updatedItem = new Item(item.name(), newWorryLevel);
                    int personalWorryLevel = testWorryLevel(updatedItem);
                    int nextMonkeyToPassTo = monkey.chooseMonkeyToPassTo(personalWorryLevel);
                    removes.add(new MonkeyRemove(item, monkey));
                    passItem(new MonkeyPass(monkeys.get(nextMonkeyToPassTo), new Item(item.name(), personalWorryLevel)));
                }
            }
            removeItems(removes);
        }
    }

    private static void removeItems(Queue<MonkeyRemove> removes) {
        while(!removes.isEmpty()) {
            MonkeyRemove remove = removes.remove();
            Monkey from = remove.from();
            from.removeItem(remove.old());
        }
    }

    private static void passItem(final MonkeyPass pass) {
        Monkey to = pass.to();
        Item item = pass.newItem();
        to.addItem(item);
    }

    private void takeInInitialMonkeySitutation(final Scanner scanner) {
        itemName = 'A';
        Monkey currMonkey = null;
        while(scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            if (line.startsWith("Monkey")) {
                currMonkey = new Monkey(Integer.parseInt(line.substring((line.length() - 2), line.length() - 1)));
                monkeys.add(currMonkey);
            } else if (!line.isBlank()) {
                buildMonkey(currMonkey, line);
            }
        }
    }

    private void buildMonkey(final Monkey monkey, final String input) {
        if (monkey == null) {
            throw new IllegalStateException("there should be no null monkeys here!");
        }
        if (input.contains("Starting")) {
            monkey.ListItems(buildItems(input));
        } else if (input.contains("Operation")) {
            monkey.ListOperation(buildOperation(input, monkey));
        } else if (input.contains("Test")) {
            monkey.ListTest(startMonkeyTestBuild(input));
        } else if (input.contains("If true")) {
            continueMonkeyTestBuild(input, monkey, true);
        } else if (input.contains("If false")) {
            continueMonkeyTestBuild(input, monkey, false);
        } else {
            throw new IllegalArgumentException("monkeys have no business here!");
        }
    }

    private List<Item> buildItems(final String input) {
        final String[] parts = input.trim().replaceAll(",", "").split(" ");
        final List<Item> items = new ArrayList<>();
        for (int i = 2; i < parts.length; i++) {
            items.add(new Item(itemName, Integer.parseInt(parts[i])));
            itemName++;
        }

        return items;
    }

    private static Operation buildOperation(final String input, final Monkey monkey) {
        // ex) Operation: new = old * 7
        final String[] parts = input.trim().split(" ");
        if (parts.length != 6) {
            throw new IllegalArgumentException("monkey operation should always be 6 parts!");
        }
        OperationType operationType = switch (parts[4]) {
            case "+" -> OperationType.ADD;
            case "*" -> OperationType.MULTIPLY;
            default -> throw new IllegalArgumentException("invalid operation type");
        };

        if (parts[5].equals("old")) {
            monkey.ListOperationSelf(new OperationSelf(operationType));
            return null;
        }

        return new Operation(operationType, Integer.parseInt(parts[5]));
    }

    private static MonkeyTest startMonkeyTestBuild(final String input) {
        // ex) Test: divisible by 19
        final String[] parts = input.trim().split(" ");
        if (parts.length != 4) {
            throw new IllegalArgumentException("monkey tests should always be 4 parts!");
        }

        final MonkeyTest monkeyTest = new MonkeyTest();
        monkeyTest.ListDivisor(Integer.parseInt(parts[3]));
        return monkeyTest;
    }

    private static void continueMonkeyTestBuild(final String input, final Monkey monkey, final boolean trueTest) {
        // ex) If true: throw to monkey 2
        final String[] parts = input.trim().split(" ");
        if (parts.length != 6) {
            throw new IllegalArgumentException("monkey test decisions should always be 6 parts!");
        }

        final MonkeyTest monkeyTest = monkey.getTest();
        final int monkeyToPassTo = Integer.parseInt(parts[5]);
        if (trueTest) {
            monkeyTest.ListTrueMonkey(monkeyToPassTo);
        } else {
            monkeyTest.ListFalseMonkey(monkeyToPassTo);
        }
    }

    private static int testWorryLevel(final Item item) {
        return item.worryLevel / 3;
    }

    static class Monkey {
        private int handles;
        private final int num;
        private List<Item> items;
        // if null use operation self
        private Operation operation;
        private OperationSelf operationSelf;
        private MonkeyTest test;

        public Monkey(final int num) {
            this.num = num;
            items = new ArrayList<>();
        }

        public int doOperation(int worryLevel) {
            OperationType operationType;
            int num;
            if (getOperation() == null) {
                OperationSelf opSelf = getOperationSelf();
                num = worryLevel;
                operationType = opSelf.operationType;
            } else {
                operationType = getOperation().operationType();
                num = getOperation().num();
            }

            switch (operationType) {
                case ADD -> {
                    return worryLevel + num;
                }
                case MULTIPLY -> {
                    return worryLevel * num;
                }
                default -> throw new IllegalStateException("invalid operation type");
            }
        }

        public void handled() {
            handles++;
        }

        public int chooseMonkeyToPassTo(int worryLevel) {
            MonkeyTest test = getTest();
            if (worryLevel % test.divisor == 0) {
                return test.trueMonkey;
            }

            return test.falseMonkey;
        }

        public int getNum() {
            return num;
        }

        public Operation getOperation() {
            return operation;
        }

        public MonkeyTest getTest() {
            return test;
        }

        public void ListTest(final MonkeyTest test) {
            this.test = test;
        }

        public void ListOperation(final Operation operation) {
            this.operation = operation;
        }

        public List<Item> getItems() {
            return items;
        }

        public void ListItems(final List<Item> items) {
            this.items = items;
        }

        public void addItem(final Item item) {
            items.add(item);
        }

        public void removeItem(final Item item) {
            if (!items.contains(item)) {
                throw new IllegalStateException("monkey does not have this item!");
            }
            items.remove(item);
        }

        public OperationSelf getOperationSelf() {
            return operationSelf;
        }

        public void ListOperationSelf(OperationSelf operationSelf) {
            this.operationSelf = operationSelf;
        }

        public int getHandles() {
            return handles;
        }
    }
    private record Item(char name, int worryLevel){}
    private record Operation(OperationType operationType, int num) {}
    private record OperationSelf(OperationType operationType) {}
    enum OperationType {
        MULTIPLY,
        ADD
    }
    static class MonkeyTest {
        private int divisor;
        // we're just going to store the index since they come in order
        private int trueMonkey;
        private int falseMonkey;

        public int getDivisor() {
            return divisor;
        }

        public void ListDivisor(int divisor) {
            this.divisor = divisor;
        }

        public int getTrueMonkey() {
            return trueMonkey;
        }

        public void ListTrueMonkey(int trueMonkey) {
            this.trueMonkey = trueMonkey;
        }

        public int getFalseMonkey() {
            return falseMonkey;
        }

        public void ListFalseMonkey(int falseMonkey) {
            this.falseMonkey = falseMonkey;
        }
    }
    private record MonkeyPass(Monkey to, Item newItem){}
    private record MonkeyRemove(Item old, Monkey from){}
}
