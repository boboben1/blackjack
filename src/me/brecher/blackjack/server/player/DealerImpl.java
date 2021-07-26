package me.brecher.blackjack.server.player;

import com.google.common.eventbus.AsyncEventBus;
import com.google.inject.Inject;
import me.brecher.blackjack.Game;
import me.brecher.blackjack.server.Server;
import me.brecher.blackjack.server.deckmanager.DeckManager;
import me.brecher.blackjack.shared.events.GuiAddCardEvent;
import me.brecher.blackjack.server.handmanager.HandManager;
import me.brecher.blackjack.shared.models.Card;
import me.brecher.blackjack.server.player.ai.Actions;
import me.brecher.blackjack.server.player.ai.DealerAI;

import java.util.List;

public class DealerImpl implements Player {


    final HandManager dealerHand;
    final DeckManager deckManager;
    final AsyncEventBus eventBus;

    private boolean doingTurn;

    private final DealerAI ai;

    @Inject
    DealerImpl(DealerAI ai, Server game, DeckManager deckManager, AsyncEventBus eventBus) {
        this.ai = ai;
        this.dealerHand = game.getDealerHandManager();
        this.deckManager = deckManager;
        this.eventBus = eventBus;
    }

    @Override
    public synchronized void beginTurn() {
        doingTurn = true;

        new Thread(() -> {
            while (true) {
                Actions action = this.ai.takeAction(dealerHand.handValue());

                if (action == Actions.STAND) {
                    break;
                }

                else if (action == Actions.HIT) {
                    addCard(this.deckManager.draw(true));
                }
            }

            finishTurn();
        }).start();
    }

    @Override
    public synchronized void waitForTurn() {
        while (doingTurn) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("DealerImpl::takeTurn() interrupted");
            }
        }
    }

    @Override
    public synchronized void finishTurn() {
        if (doingTurn) {
            doingTurn = false;

            notify();
        }
    }

    @Override
    public synchronized boolean isTakingTurn() {
        return doingTurn;
    }

    @Override
    public void addCard(Card card) {
        dealerHand.addCard(card);
    }

    @Override
    public int handValue() {
        return dealerHand.handValue();
    }

    @Override
    public void resetHand() {
        dealerHand.reset();
    }

    @Override
    public boolean hasBlackjack() {
        return dealerHand.hasBlackjack();
    }

    @Override
    public void addCards(List<Card> cards) {
        dealerHand.addCards(cards);

        if (dealerHand.handValue() >= 21)
        {
            finishTurn();
        }
    }
}
