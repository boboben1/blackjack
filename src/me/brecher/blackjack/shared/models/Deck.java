package me.brecher.blackjack.shared.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck implements Serializable {
    final List<Card> cards;
    final int deckSize;
    int numberOfCardsRemaining;

    public Deck(Integer deckSize) {
        this.deckSize = deckSize;

        this.cards = new ArrayList<>();

        resetDeck();
    }

    public void resetDeck() {
        this.cards.clear();

        for (Suit suit : Suit.values()) {
            for (CardNumber cardNumber : CardNumber.values()) {
                for (int i = 0; i < this.deckSize; ++i) {
                    this.cards.add(new Card(cardNumber, suit, false));
                    this.numberOfCardsRemaining++;
                }
            }
        }
    }

    public List<Card> cards() {
        return cards;
    }

    public void shuffle() {
        Collections.shuffle(this.cards);
    }
}
