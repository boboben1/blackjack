package me.brecher.blackjack.server.player;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import me.brecher.blackjack.Game;
import me.brecher.blackjack.server.Server;
import me.brecher.blackjack.server.deckmanager.DeckManager;
import me.brecher.blackjack.server.scoring.BetManager;
import me.brecher.blackjack.shared.events.*;
import me.brecher.blackjack.server.handmanager.HandManager;
import me.brecher.blackjack.shared.models.Card;

public class LocalPlayerImpl implements Player {
    final HandManager playerHand;
    final AsyncEventBus eventBus;
    final BetManager betManager;
    @Inject
    DeckManager deckManager;
    boolean doingTurn;


    @Inject
    public LocalPlayerImpl(AsyncEventBus eventBus, Server game, BetManager betManager) {
        this.playerHand = game.getPlayerHandManager();
        this.doingTurn = false;
        this.eventBus = eventBus;
        this.betManager = betManager;
        eventBus.register(this);
    }

    @Subscribe
    public void hit(PlayerHitEvent event) {
        addCard(this.deckManager.draw(true));
    }

    @Subscribe
    public void stand(PlayerStandEvent event) {
        finishTurn();
    }

    @Subscribe
    public void doubleUp(PlayerDoubleEvent event) {
        if (playerHand.canDouble() &&  betManager.canDouble())
        {
            this.eventBus.post(new BetDoubleEvent());

            addCard(deckManager.draw(true));
        }

    }

    @Override
    public synchronized void beginTurn() {
        this.doingTurn = true;
    }

    @Override
    public synchronized void waitForTurn() {
        while (doingTurn) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("PlayerImpl::takeTurn() interrupted");
            }
        }
    }

    @Override
    public synchronized void finishTurn() {
        if (doingTurn) {
            this.doingTurn = false;

            notify();
        }
    }

    @Override
    public synchronized boolean isTakingTurn() {
        return this.doingTurn;
    }

    @Override
    public void addCard(Card card) {
        playerHand.addCard(card);

        if (playerHand.handValue() >= 21)
            this.eventBus.post(new PlayerStandEvent());
    }

    @Override
    public int handValue() {
        return playerHand.handValue();
    }

    @Override
    public void resetHand() {
        playerHand.reset();
    }

    @Override
    public boolean hasBlackjack() {
        return playerHand.hasBlackjack();
    }
}
