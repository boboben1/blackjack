package me.brecher.blackjack.shared.models;

import java.util.Arrays;
import java.util.Optional;

public enum CardNumber {
    ACE("Ace", "A", new int[]{1, 11}),
    TWO("2", "2", 2),
    THREE("3", "3", 3),
    FOUR("4", "4", 4),
    FIVE("5", "5", 5),
    SIX("6", "6", 6),
    SEVEN("7", "7", 7),
    EIGHT("8", "8", 8),
    NINE("9", "9", 9),
    TEN("10", "10", 10),
    JACK("Jack", "J", 10),
    QUEEN("Queen", "Q", 10),
    KING("King", "K", 10);

    final String displayName;
    final String resourceString;
    final int[] value;

    CardNumber(@SuppressWarnings("SameParameterValue") String displayName, @SuppressWarnings("SameParameterValue") String resourceString, int[] value) {
        this.displayName = displayName;
        this.resourceString = resourceString;
        this.value = value;
    }
    CardNumber(String displayName, String resourceString, int value) {
        this.displayName = displayName;
        this.resourceString = resourceString;
        this.value = new int[]{value};
    }

    public static CardNumber fromResourceString(String resourceString) {

        Optional<CardNumber> match = Arrays.stream(CardNumber.values()).filter(c -> c.resourceString.equals(resourceString))
                .findFirst();

        return match.orElse(null);
    }

    @Override
    public String toString() {
        return this.displayName;
    }

    public String getResourceString() {
        return resourceString;
    }

    public int[] getValue() {
        return value;
    }
}
