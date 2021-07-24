package me.brecher.blackjack.server.deckmanager;

import me.brecher.blackjack.shared.models.Card;

public interface DeckManager {
    Card draw(boolean faceUp);

    void shuffle();

    void reset();
}
