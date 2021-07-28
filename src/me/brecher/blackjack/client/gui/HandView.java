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
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class HandView extends JPanel {

    // To do. Implement this in a provider.
    static class HandData
    {
        final List<Card> cards;
        final List<Image> handImages;
        int handValue;

        HandData() {
            this.cards = Collections.synchronizedList(new ArrayList<>());
            this.handImages = Collections.synchronizedList(new ArrayList<>());
            this.handValue = 0;
        }
    }

    final private CardResourceFinder cardResourceFinder;

    private final List<HandData> handDatas;
    private int activeHandData;

    private List<Card> cards() {
        return this.handDatas.get(activeHandData).cards;
    }

    private void setCards(List<Card> cards) {
        cards().clear();

        for (Card card : cards) {
            cards().add(card);
        }
    }

    private List<Image> handImages() {
        return this.handDatas.get(activeHandData).handImages;
    }

    private int handValue() {
        return this.handDatas.get(activeHandData).handValue;
    }

    private void setHandValue(int value) {
        this.handDatas.get(activeHandData).handValue = value;
    }

    private boolean didSplit;

    private int otherHandValue() {
        return this.handDatas.get(1-activeHandData).handValue;
    }

    private int playerNumber;

    private boolean revealed;

    private Font handValueFont;

    private boolean loaded;
    private boolean hadError;

    @Inject
    public HandView(@Assisted int playerNumber, CardResourceFinder cardResourceFinder, AsyncEventBus eventBus) {
        super();

        this.activeHandData = 0;
        this.handDatas = new ArrayList<>();
        reset();

        eventBus.register(this);

        this.cardResourceFinder = cardResourceFinder;

        this.playerNumber = playerNumber;

        this.revealed = true;

        this.loaded = false;
        this.hadError = false;

        init();
    }

    @Subscribe
    public void addCard(GuiAddCardEvent event) {
        if (this.playerNumber == event.getPlayerID()) {
            synchronized (this.handDatas) {
                this.cards().add(event.getCard());
            }

            if (!event.getCard().faceUp())
                revealed = false;

            this.setHandValue(event.getHandValue());

            updateHandImages();
        }
    }

    @Subscribe
    public void addCards(GuiAddCardsEvent event) {
        if (this.playerNumber == event.getPlayerID()) {
            synchronized (this.handDatas) {
                this.cards().addAll(event.getCards());
            }

            if (event.getCards().stream().anyMatch(e -> !e.faceUp()))
                revealed = false;

            this.setHandValue(event.getHandValue());

            updateHandImages();
        }
    }

    private void init() {
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
        synchronized (this.handDatas) {
            this.handImages().clear();
            for (Card card : cards()) {
                this.addHandImage(cardResourceFinder.getCardImage(card));
            }
        }

        this.repaint();
    }

    @Subscribe
    public void revealHand(RevealCardsEvent event) {
        synchronized (this.handDatas) {
            this.setCards(this.cards().stream().map(card -> card.makeFaceup(true)).collect(Collectors.toList()));
        }

        this.revealed = true;
        updateHandImages();
    }

    @Subscribe
    public void resetHand(RoundBeganEvent event) {
        reset();
    }

    @Subscribe
    public void guiSplit(GuiSplitEvent event) {

        synchronized (this.handDatas) {
            if (playerNumber == event.getPlayerID()) {
                reset();
                handDatas.add(new HandData());

                cards().add(event.getCard1());

                this.setHandValue(event.getValue());

                this.activeHandData = 1;

                cards().add(event.getCard2());

                setHandValue(event.getValue());

                this.activeHandData = 0;


                this.didSplit = true;

                updateHandImages();
            }
        }



    }

    @Subscribe
    public void guiSwitch(GuiSwitchEvent event) {
        synchronized (this.handDatas) {
            if (event.getPlayerID() == playerNumber) {
                this.activeHandData = 1;
                this.cards().add(event.getDrawn());
                updateHandImages();
            }
        }
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public void addHandImage(Image handImage) {
        synchronized (this.handDatas) {
            this.handImages().add(handImage);
        }

        this.repaint();
    }

    public void reset() {
        synchronized (this.handDatas) {
            this.handDatas.clear();
            this.handDatas.add(new HandData());
            this.activeHandData = 0;
            this.didSplit = false;
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


        int maxX = this.getWidth();

        if (this.loaded && !this.hadError) {
            synchronized (this.handDatas) {
                for (Image img : this.handImages()) {
                    // Don't hardcode this!
                    g.drawImage(Util.resizedImage(img, 120, 200), x, this.getHeight() + y, this);
                    x += 120 + spacing;

                    if (x + 120 > maxX)
                    {
                        x = 40;
                        y = -150;
                    }
                }
            }


            if (revealed && handImages().size() > 0)
            {
                g.setColor(Color.BLACK);
                g.drawString("" + handValue(),10, getHeight() - 220);

                if (didSplit)
                    g.drawString("" + otherHandValue(), 300, getHeight() - 220);
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
