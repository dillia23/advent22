package org.advent.day2;

public enum RpsPlayer {
    ROCK("X"),
    PAPER("Y"),
    SCISSORS("Z");

    private final String value;

    RpsPlayer(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RpsPlayer fromString(final String possibleRps) {
        for (final RpsPlayer rpsPlayer: RpsPlayer.values()) {
            if (rpsPlayer.value.equals(possibleRps)) {
                return rpsPlayer;
            }
        }

        throw new IllegalArgumentException("not rock, paper, or scissors");
    }
}
