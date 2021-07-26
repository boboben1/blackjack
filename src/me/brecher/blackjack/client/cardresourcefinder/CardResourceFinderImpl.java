package me.brecher.blackjack.client.cardresourcefinder;

import com.google.common.eventbus.AsyncEventBus;
import com.google.inject.Inject;
import me.brecher.blackjack.shared.events.GuiLoadEvent;
import me.brecher.blackjack.shared.models.Card;
import me.brecher.blackjack.shared.models.CardNumber;
import me.brecher.blackjack.shared.models.Suit;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CardResourceFinderImpl implements CardResourceFinder {

    private final Map<Card, Image> cardImages;

    private Image cardBack;

    private boolean loaded;
    private boolean errorOccurred;


    private final AsyncEventBus asyncEventBus;

    @Inject
    public CardResourceFinderImpl(AsyncEventBus asyncEventBus) {

        this.asyncEventBus = asyncEventBus;

        this.cardImages = new HashMap<>();

        this.loaded = false;
        this.errorOccurred = false;


        new Thread(() -> {
            boolean hadError = false;
            try {
                preloadResources();
            } catch (IOException e) {
                e.printStackTrace();
                hadError = true;
            } finally {
                setLoaded(hadError);
            }
        }).start();

    }

    private synchronized void setLoaded(boolean error) {
        this.loaded = true;
        this.errorOccurred = error;

        notifyAll();

        this.asyncEventBus.post(new GuiLoadEvent(error));
    }

    public synchronized boolean waitForLoaded() {
        while (!this.loaded && !this.errorOccurred) {
            try {
                wait(0);
            } catch (InterruptedException e) {
                System.out.println("CardResourceFinderImpl::waitForLoaded Error! Interrupted.");
                Thread.currentThread().interrupt();
            }
        }

        return !this.errorOccurred;
    }

    private void preloadResources() throws IOException {
        this.cardBack = ImageIO.read(
                Objects.requireNonNull(getClass().getClassLoader().getResource("res/blue_back.png")));

        for (Suit suit : Suit.values()) {
            for (CardNumber cardNumber : CardNumber.values()) {
                Card card = new Card(cardNumber, suit, true);

                cardImages.put(card, ImageIO.read(
                        Objects.requireNonNull(getClass().getClassLoader().getResource(
                                "res/" + card.number().getResourceString() + card.suit().getResourceString() + ".png"
                        ))));
            }
        }

    }

    @Override
    public Image getCardImage(Card card) {
//
//        if (Objects.equals(card.number().getResourceString(), "") && card.faceUp())
//            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
//        try {
//
//            if (card.faceUp()) {
//                if (this.cardImages.containsKey(card)) {
//                    return this.cardImages.get(card);
//                }
//            } else {
//                return cardBack;
//            }
//
//
//            Image image = ImageIO.read(
//                    Objects.requireNonNull(getClass().getClassLoader().getResource(card.faceUp()
//                            ? "res/" + card.number().getResourceString() + card.suit().getResourceString() + ".png"
//                            : "res/blue_back.png"))
//            );
//
//            this.cardImages.put(card, image);
//
//            return image;
//
//        } catch (IOException ex) {
//            System.err.println(ex.getMessage());
//        }
//
//        return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

        waitForLoaded();

        if (card.faceUp())
            return cardImages.get(card);

        return cardBack;
    }
}
