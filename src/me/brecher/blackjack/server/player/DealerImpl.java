package me.brecher.blackjack.server.player;

import com.google.common.eventbus.AsyncEventBus;
import com.google.inject.Inject;
import me.brecher.blackjack.server.Server;
import me.brecher.blackjack.server.deckmanager.DeckManager;
import me.brecher.blackjack.server.handmanager.HandManager;
import me.brecher.blackjack.shared.models.Card;
import me.brecher.blackjack.server.player.ai.Actions;
import me.brecher.blackjack.server.player.ai.DealerAI;
import me.brecher.blackjack.shared.models.Hand;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DealerImpl implements Player {


    private final HandManager dealerHand;
    private final DeckManager deckManager;
    private final AsyncEventBus eventBus;
    private final ScheduledExecutorService scheduledExecutorService;

    private boolean doingTurn;

    private final DealerAI ai;

    private ScheduledFuture scheduledFuture;


    @Inject
    DealerImpl(DealerAI ai, Server game, DeckManager deckManager, AsyncEventBus eventBus, ScheduledExecutorService scheduledExecutorService) {
        this.ai = ai;
        this.doingTurn = false;
        this.dealerHand = game.getDealerHandManager();
        this.deckManager = deckManager;
        this.eventBus = eventBus;
        this.scheduledExecutorService = scheduledExecutorService;

        this.scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(this::doTurn, 0, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public synchronized void beginTurn() {
        doingTurn = true;

//        new Thread(() -> {
//            while (true) {
//                Actions action = this.ai.takeAction(dealerHand.handValue());
//
//                if (action == Actions.STAND) {
//                    break;
//                }
//
//                else if (action == Actions.HIT) {
//                    addCard(this.deckManager.draw(true));
//                }
//            }
//
//            finishTurn();
//        }).start();
    }

    private synchronized void doTurn() {
        if (doingTurn) {
            Actions action = this.ai.takeAction(dealerHand.handValue());

            if (action == Actions.STAND) {
                finishTurn();
            }
            else if (action == Actions.HIT) {
                addCard(this.deckManager.draw(true));
            }
        }
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

            notifyAll();
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

//    @Override
//    public int handValue() {
//        return dealerHand.handValue();
//    }

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

    @Override
    public List<Hand> getHands() {
        return dealerHand.getHands();
    }

    @Override
    public Hand getActiveHand() {
        return dealerHand.activeHand();
    }
}
