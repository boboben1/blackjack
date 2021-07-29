package me.brecher.blackjack.client.gui;

import me.brecher.blackjack.shared.models.Card;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public interface GuiHandManager {
    void addCard(int hand, Card card, int handValue, int newHandSize);
    void addCards(int hand, List<Card> cards, int handValue, int newHandSize);
    void reveal();

    void split(Card card1, Card card2, int handValue);
    void switchHand();

    void updateImages();

    void reset();

    void forEachImage(Consumer<? super Image> op);


    boolean shouldDrawValue();
    int activeHandValue();
    boolean didSplit();
    int inactiveHandValue();
}
