package me.brecher.blackjack.client.cardresourcefinder;

import com.google.common.eventbus.AsyncEventBus;
import com.google.inject.Inject;
import me.brecher.blackjack.shared.events.GuiLoadEvent;
import me.brecher.blackjack.shared.models.Card;
import me.brecher.blackjack.shared.models.CardNumber;
import me.brecher.blackjack.shared.models.Suit;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;

public class CardResourceFinderImpl implements CardResourceFinder {

    private final Map<Card, Image> cardImages;

    private Image cardBack;

    private boolean loaded;
    private boolean errorOccurred;


    private final AsyncEventBus asyncEventBus;

    @Inject
    public CardResourceFinderImpl(AsyncEventBus asyncEventBus, ScheduledExecutorService scheduledExecutorService) {

        this.asyncEventBus = asyncEventBus;

        this.cardImages = new HashMap<>();

        this.loaded = false;
        this.errorOccurred = false;


        scheduledExecutorService.execute(() -> {
            boolean hadError = false;
            try {
                preloadResources();
            } catch (Exception e) {
                e.printStackTrace();
                hadError = true;
            } finally {
                setLoaded(hadError);
            }
        });

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

    private void preloadResources() throws IOException, NullPointerException {
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
        waitForLoaded();

        if (card.faceUp())
            return cardImages.get(card);

        return cardBack;
    }

    @Override
    public synchronized boolean hadError() {
        return errorOccurred;
    }
}
