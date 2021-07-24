package me.brecher.blackjack.shared.models;

import java.io.Serializable;
import java.util.Objects;

public final class Card implements Serializable {
    private final CardNumber number;
    private final Suit suit;
    private final boolean faceUp;

    public Card(CardNumber number, Suit suit, boolean faceUp) {
        this.number = number;
        this.suit = suit;
        this.faceUp = faceUp;
    }

    public CardNumber number() {
        return number;
    }

    public Suit suit() {
        return suit;
    }

    public boolean faceUp() {
        return faceUp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return faceUp == card.faceUp && Objects.equals(number, card.number) && suit == card.suit;
    }

    @Override
    public String toString() {
        return "Card{" +
                "number='" + number + '\'' +
                ", suit=" + suit +
                ", faceUp=" + faceUp +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, suit, faceUp);
    }

    public int[] getValue() {
        return this.number().getValue();
    }

    public Card makeFaceup(boolean faceUp) {
        return new Card(this.number, this.suit, faceUp);
    }
}
