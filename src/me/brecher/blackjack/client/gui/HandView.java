package me.brecher.blackjack.client.gui;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import me.brecher.blackjack.client.cardresourcefinder.CardResourceFinder;
import me.brecher.blackjack.shared.events.GuiAddCardEvent;
import me.brecher.blackjack.shared.events.RoundBeganEvent;
import me.brecher.blackjack.shared.events.RevealCardsEvent;
import me.brecher.blackjack.shared.models.Card;
import me.brecher.blackjack.util.Util;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HandView extends JPanel {

    final private CardResourceFinder cardResourceFinder;
    List<Card> cards;
    List<Image> handImages;
    private int playerNumber;

    @Inject
    public HandView(@Assisted int playerNumber, CardResourceFinder cardResourceFinder, AsyncEventBus eventBus) {
        super();

        eventBus.register(this);

        this.cardResourceFinder = cardResourceFinder;

        this.playerNumber = playerNumber;

        init();
    }

    @Subscribe
    public void addCard(GuiAddCardEvent event) {
        if (this.playerNumber == event.getPlayerID()) {
            synchronized (this.cards) {
                this.cards.add(event.getCard());
            }

            updateHandImages();
        }
    }

    private void init() {
        this.handImages = Collections.synchronizedList(new ArrayList<>());
        this.cards = Collections.synchronizedList(new ArrayList<>());
    }

    void updateHandImages() {
        synchronized (cards) {
            handImages.clear();
            for (Card card : cards) {
                this.addHandImage(cardResourceFinder.getCardImage(card));
            }
        }

        this.repaint();
    }

    @Subscribe
    public void revealHand(RevealCardsEvent event) {
        this.cards = this.cards.stream().map(card -> card.makeFaceup(true)).collect(Collectors.toList());
        updateHandImages();
    }

    @Subscribe
    public void resetHand(RoundBeganEvent event) {
        reset();
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public void addHandImage(Image handImage) {
        synchronized (this.handImages) {
            this.handImages.add(handImage);
        }

        this.repaint();
    }

    public void reset() {
        synchronized (this.handImages) {
            this.handImages.clear();
            this.cards.clear();
        }

        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int x = 10;
        int y = -205;
        int spacing = 10;

        synchronized (this.handImages) {
            for (Image img : this.handImages) {
                // Don't hardcode this!
                g.drawImage(Util.resizedImage(img, 120, 200), x, this.getHeight() + y, this);
                x += 120 + spacing;

                if (x + 120 > this.getWidth())
                {
                    x = 40;
                    y = -150;
                }


            }
        }
    }
}