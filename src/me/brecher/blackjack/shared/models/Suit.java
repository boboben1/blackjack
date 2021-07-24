package me.brecher.blackjack.shared.models;

public enum Suit {
    Hearts("Hearts", "H"),
    Diamonds("Diamonds", "D"),
    Spades("Spades", "S"),
    Clubs("Clubs", "C");

    final String name;
    final String resourceString;
    Suit(String name, String resourceString) {
        this.name = name;
        this.resourceString = resourceString;
    }

    public String getResourceString() {
        return this.resourceString;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
