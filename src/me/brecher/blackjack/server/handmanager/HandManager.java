package me.brecher.blackjack.server.handmanager;

import me.brecher.blackjack.shared.models.Card;
import me.brecher.blackjack.shared.models.Hand;

public interface HandManager {
    Hand activeHand();

    int handValue();
    boolean hasBlackjack();
    boolean canDouble();

    void addCard(Card card);

    void reset();
}
