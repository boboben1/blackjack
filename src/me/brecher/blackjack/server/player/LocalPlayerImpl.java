package me.brecher.blackjack.server.player;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import me.brecher.blackjack.Game;
import me.brecher.blackjack.server.Server;
import me.brecher.blackjack.server.deckmanager.DeckManager;
import me.brecher.blackjack.shared.events.AddCardEvent;
import me.brecher.blackjack.shared.events.PlayerHitEvent;
import me.brecher.blackjack.shared.events.PlayerStandEvent;
import me.brecher.blackjack.server.handmanager.HandManager;

public class LocalPlayerImpl implements Player {
    final HandManager playerHand;
    final AsyncEventBus eventBus;
    @Inject
    DeckManager deckManager;
    boolean doingTurn;


    @Inject
    public LocalPlayerImpl(AsyncEventBus eventBus, Server game) {
        this.playerHand = game.getPlayerHandManager();
        this.doingTurn = false;
        this.eventBus = eventBus;
        eventBus.register(this);
    }

    @Subscribe
    public void hit(PlayerHitEvent event) {
        this.eventBus.post(new AddCardEvent(1, this.deckManager.draw(true)));
    }

    @Subscribe
    public void stand(PlayerStandEvent event) {
        finishTurn();
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
}
