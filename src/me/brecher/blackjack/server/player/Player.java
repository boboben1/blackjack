package me.brecher.blackjack.server.player;

import me.brecher.blackjack.shared.models.Card;

public interface Player {
    void beginTurn();

    void waitForTurn();

    void finishTurn();

    boolean isTakingTurn();

    void addCard(Card card);

    int handValue();

    void resetHand();

    boolean hasBlackjack();
}
