package org.advent.day2;

public enum RpsOpponent {
    ROCK("A"),
    PAPER("B"),
    SCISSORS("C");

    private final String value;

    RpsOpponent(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RpsOpponent fromString(final String possibleRps) {
        for (final RpsOpponent rpsOpponent: RpsOpponent.values()) {
            if (rpsOpponent.value.equals(possibleRps)) {
                return rpsOpponent;
            }
        }

        throw new IllegalArgumentException("not rock, paper, or scissors");
    }
}
