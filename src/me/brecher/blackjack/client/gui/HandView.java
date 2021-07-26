package me.brecher.blackjack.client.gui;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import me.brecher.blackjack.client.cardresourcefinder.CardResourceFinder;
import me.brecher.blackjack.shared.events.*;
import me.brecher.blackjack.shared.models.Card;
import me.brecher.blackjack.util.Util;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class HandView extends JPanel {

    final private CardResourceFinder cardResourceFinder;
    private List<Card> cards;
    private List<Image> handImages;
    private int playerNumber;

    private int handValue;
    private boolean revealed;

    private Font handValueFont;

    private boolean loaded;
    private boolean hadError;

    @Inject
    public HandView(@Assisted int playerNumber, CardResourceFinder cardResourceFinder, AsyncEventBus eventBus) {
        super();

        eventBus.register(this);

        this.cardResourceFinder = cardResourceFinder;

        this.playerNumber = playerNumber;

        this.revealed = true;
        this.handValue = 0;

        this.loaded = false;
        this.hadError = false;

        init();
    }



    @Subscribe
    public void addCard(GuiAddCardEvent event) {
        if (this.playerNumber == event.getPlayerID()) {
            synchronized (this.cards) {
                this.cards.add(event.getCard());
            }

            if (!event.getCard().faceUp())
                revealed = false;

            this.handValue = event.getHandValue();

            updateHandImages();
        }
    }

    @Subscribe
    public void addCards(GuiAddCardsEvent event) {
        if (this.playerNumber == event.getPlayerID()) {
            synchronized (this.cards) {
                this.cards.addAll(event.getCards());
            }

            if (event.getCards().stream().anyMatch(e -> !e.faceUp()))
                revealed = false;

            this.handValue = event.getHandValue();

            updateHandImages();
        }
    }

    private void init() {
        this.handImages = Collections.synchronizedList(new ArrayList<>());
        this.cards = Collections.synchronizedList(new ArrayList<>());
        this.handValueFont = createHandValueFont();
    }

    private Font createHandValueFont()
    {
        Map<TextAttribute, Object> fontAttributes = new HashMap<>();

        fontAttributes.put(TextAttribute.FAMILY, "TimesRoman");
        fontAttributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        fontAttributes.put(TextAttribute.SIZE, 32);

        return Font.getFont(fontAttributes);
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
        synchronized (this.cards) {
            this.cards = this.cards.stream().map(card -> card.makeFaceup(true)).collect(Collectors.toList());
        }

        this.revealed = true;
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

        Font oldfont = g.getFont();
        g.setFont(handValueFont);

        int x = 10;
        int y = -205;
        int spacing = 10;



        if (this.loaded && !this.hadError) {
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


            if (revealed && handImages.size() > 0)
            {

                g.setColor(Color.BLACK);
                g.drawString("" + handValue,10, getHeight() - 220);

            }
        }

        g.setFont(oldfont);
    }

    @Subscribe
    public synchronized void guiLoad(GuiLoadEvent event) {
        this.loaded = true;
        this.hadError = event.hasError();

        this.repaint();
    }
}
