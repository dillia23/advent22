package org.advent.day2;

public enum RpsPlayerGuide {
    LOSE("X"),
    DRAW("Y"),
    WIN("Z");

    private final String value;

    RpsPlayerGuide(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RpsPlayerGuide fromString(final String possibleRps) {
        for (final RpsPlayerGuide rpsPlayerGuide : RpsPlayerGuide.values()) {
            if (rpsPlayerGuide.value.equals(possibleRps)) {
                return rpsPlayerGuide;
            }
        }

        throw new IllegalArgumentException("not lose, draw, or win");
    }
}
