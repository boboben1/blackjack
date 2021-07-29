package me.brecher.blackjack.client.gui;

import com.google.inject.Inject;
import me.brecher.blackjack.client.cardresourcefinder.CardResourceFinder;
import me.brecher.blackjack.shared.models.Card;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GuiHandManagerImpl implements GuiHandManager {


    private final CardResourceFinder cardResourceFinder;


    private final List<HandData> handDatas;
    private int activeHandData;
    private boolean didSplit;
    private boolean revealed;


    @Inject
    GuiHandManagerImpl(CardResourceFinder cardResourceFinder) {
        this.cardResourceFinder = cardResourceFinder;

        this.handDatas = Collections.synchronizedList(new ArrayList<>());

        reset();
    }


    @Override
    public synchronized void addCard(int hand, Card card, int handValue, int newHandSize) {
        h(hand).addCard(card, newHandSize, handValue);

        if (!card.faceUp())
            revealed = false;
    }

    @Override
    public synchronized void addCards(int hand, List<Card> cards, int handValue, int newHandSize) {
        h(hand).addCards(cards, newHandSize, handValue);

        if (cards.stream().anyMatch(c -> !c.faceUp()))
            revealed = false;
    }

    @Override
    public synchronized void reveal() {
        synchronized (handDatas) {
            for (HandData handData : handDatas) {
                handData.reveal();
            }
        }

        revealed = true;

        updateImages();
    }

    @Override
    public synchronized void reset() {
        synchronized (handDatas) {
            this.handDatas.clear();
            this.handDatas.add(new HandData());
            this.handDatas.add(new HandData());
        }

        this.activeHandData = 0;
        this.didSplit = false;
        this.revealed = true;
    }


    private HandData h(int hand) {
        return handDatas.get(hand);
    }

//

    @Override
    public void updateImages() {
        synchronized (handDatas) {
            for (HandData handData : handDatas) {
                handData.setHandImages(cardResourceFinder::getCardImage);
            }
        }
    }

    @Override
    public void split(Card card1, Card card2, int handValue) {
        reset();

        h(0).addCard(card1, 1, handValue);
        h(1).addCard(card2, 1, handValue);

        didSplit = true;
    }

    @Override
    public void switchHand() {
        this.activeHandData = 1;
    }

    @Override
    public void forEachImage(Consumer<? super Image> op) {
        h(activeHandData).forEachImage(op);
    }

    @Override
    public boolean shouldDrawValue() {
        return revealed && h(activeHandData).getNumImages() > 0;
    }

    @Override
    public int activeHandValue() {
        return h(activeHandData).getHandValue();
    }

    @Override
    public boolean didSplit() {
        return didSplit;
    }

    @Override
    public int inactiveHandValue() {
        return h(1-activeHandData).getHandValue();
    }

    //
//
//    private List<Card> cards(int hand) {
//        return this.handDatas.get(hand).cards;
//    }
//
//    private void setCards(List<Card> cards, int hand) {
//        cards(hand).clear();
//
//        for (Card card : cards) {
//            cards(hand).add(card);
//        }
//    }
//
//    private List<Image> handImages(int hand) {
//        return this.handDatas.get(hand).handImages;
//    }
//
//    private int handValue(int hand) {
//        return this.handDatas.get(hand).handValue;
//    }
//
//    private void setHandValue(int value, int hand) {
//        this.handDatas.get(hand).handValue = value;
//    }
//
//    private int otherHandValue() {
//        return this.handDatas.get(1-activeHandData).handValue;
//    }
//
//    private int handSize(int hand) { return this.handDatas.get(hand).handsize;}
//    private void setHandSize(int handSize, int hand) { this.handDatas.get(hand).handsize = handSize;}
}
