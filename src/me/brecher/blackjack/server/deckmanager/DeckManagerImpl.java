package me.brecher.blackjack.server.deckmanager;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import me.brecher.blackjack.shared.models.Card;
import me.brecher.blackjack.shared.models.Deck;

public class DeckManagerImpl implements DeckManager {

    final Deck deck;

    final int deckSize;

    @Inject
    public DeckManagerImpl(@Named("DeckSize") int deckSize) {
        this.deckSize = deckSize;
        this.deck = new Deck(deckSize);
    }


    @Override
    public Card draw(boolean faceUp) {
        return this.deck.cards().remove(0).makeFaceup(faceUp);
    }

    @Override
    public void shuffle() {
        this.deck.shuffle();
    }

    @Override
    public void reset() {
        this.deck.resetDeck();
        this.deck.shuffle();
    }
}
