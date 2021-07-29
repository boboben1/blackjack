package me.brecher.blackjack.client.gui;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import me.brecher.blackjack.client.cardresourcefinder.CardResourceFinder;
import me.brecher.blackjack.shared.events.*;
import me.brecher.blackjack.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class HandView extends JPanel {

    final private CardResourceFinder cardResourceFinder;

    private int playerNumber;

    private Font handValueFont;

    private boolean loaded;
    private boolean hadError;

    private final GuiHandManager guiHandManager;

    @Inject
    public HandView(@Assisted int playerNumber, CardResourceFinder cardResourceFinder, AsyncEventBus eventBus, GuiHandManager guiHandManager) {
        super();

        this.guiHandManager = guiHandManager;

        reset();

        eventBus.register(this);

        this.cardResourceFinder = cardResourceFinder;

        this.playerNumber = playerNumber;

        this.loaded = false;

        this.hadError = false;

        init();
    }

    @Subscribe
    public void addCard(GuiAddCardEvent event) {
        if (this.playerNumber == event.getPlayerID()) {
            guiHandManager.addCard(event.getHand(), event.getCard(), event.getHandValue(), event.getHandSize());

            updateHandImages();
        }
    }

    @Subscribe
    public void addCards(GuiAddCardsEvent event) {
        if (this.playerNumber == event.getPlayerID()) {
            guiHandManager.addCards(event.getHand(), event.getCards(), event.getHandValue(), event.getHandSize());

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
        guiHandManager.updateImages();

        this.repaint();
    }

    @Subscribe
    public void revealHand(RevealCardsEvent event) {
        guiHandManager.reveal();

        updateHandImages();
    }

    @Subscribe
    public void guiSplit(GuiSplitEvent event) {
        if (playerNumber == event.getPlayerID()) {
            guiHandManager.split(event.getCard1(), event.getCard2(), event.getValue());

            updateHandImages();
        }
    }

    @Subscribe
    public void guiSwitch(GuiSwitchEvent event) {
        if (event.getPlayerID() == playerNumber) {
            guiHandManager.switchHand();

            updateHandImages();
        }
    }

    public void reset() {
        guiHandManager.reset();

        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Font oldfont = g.getFont();
        g.setFont(handValueFont);

        AtomicInteger x = new AtomicInteger(10);
        AtomicInteger y = new AtomicInteger(-205);
        int spacing = 10;


        int maxX = this.getWidth();

        if (this.loaded && !this.hadError) {
            guiHandManager.forEachImage(img -> {
                // Don't hardcode this!
                g.drawImage(Util.resizedImage(img, 120, 200), x.get(), this.getHeight() + y.get(), this);
                x.addAndGet(120 + spacing);

                if (x.get() + 120 > maxX)
                {
                    x.set(40);
                    y.set(-150);
                }
            });


            if (guiHandManager.shouldDrawValue())
            {
                g.setColor(Color.BLACK);
                g.drawString("" + guiHandManager.activeHandValue(),10, getHeight() - 220);

                if (guiHandManager.didSplit())
                    g.drawString("" + guiHandManager.inactiveHandValue(), 300, getHeight() - 220);
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
