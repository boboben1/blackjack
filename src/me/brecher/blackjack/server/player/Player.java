package me.brecher.blackjack.server.player;

import me.brecher.blackjack.shared.models.Card;
import me.brecher.blackjack.shared.models.Hand;

import java.util.List;

public interface Player {
    void beginTurn();

    void waitForTurn();

    void finishTurn();

    boolean isTakingTurn();

    void addCard(Card card);
    void addCards(List<Card> cards);

    //int handValue();

    void resetHand();

    List<Hand> getHands();

    Hand getActiveHand();

    boolean hasBlackjack();
}
