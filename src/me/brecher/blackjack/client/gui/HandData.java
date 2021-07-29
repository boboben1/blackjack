package me.brecher.blackjack.client.gui;

import me.brecher.blackjack.shared.models.Card;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HandData {
    private final List<Card> cards;
    private final List<Image> handImages;
    private int handValue;

    // Used to fix ordering issues.
    private int lastHandSize;

    HandData() {
        this.cards = Collections.synchronizedList(new ArrayList<>());
        this.handImages = Collections.synchronizedList(new ArrayList<>());
        this.handValue = 0;
        this.lastHandSize = 0;
    }

    public int getNumImages() {
        return handImages.size();
    }

    public int getHandValue() {
        return handValue;
    }

    public void addCard(Card card, int newHandSize, int newHandValue) {
        cards.add(card);

        if (lastHandSize < newHandSize) {
            handValue = newHandValue;
            lastHandSize = newHandSize;
        }
    }

    public void addCards(List<Card> newCards, int newHandSize, int newHandValue) {
        cards.addAll(newCards);

        if (lastHandSize < newHandSize) {
            handValue = newHandValue;
            lastHandSize = newHandSize;
        }
    }

    public void setHandImages(Function<Card, Image> mapImages) {
        this.handImages.clear();

        List<Image> newImages = this.cards.stream().map(mapImages).collect(Collectors.toList());

        this.handImages.addAll(newImages);
    }

    public void reveal() {
        List<Card> newCards = cards.stream().map(card -> card.makeFaceup(true)).collect(Collectors.toList());

        cards.clear();
        cards.addAll(newCards);
    }

    public void forEachImage(Consumer<? super Image> op) {
        synchronized (handImages) {
            handImages.forEach(op);
        }
    }
}
