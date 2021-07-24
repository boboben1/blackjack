package me.brecher.blackjack.server.gameplay;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import me.brecher.blackjack.Game;
import me.brecher.blackjack.annotations.Dealer;
import me.brecher.blackjack.annotations.Human;
import me.brecher.blackjack.server.Server;
import me.brecher.blackjack.server.deckmanager.DeckManager;
import me.brecher.blackjack.shared.events.*;
import me.brecher.blackjack.server.handmanager.HandManager;
import me.brecher.blackjack.server.player.Player;
import me.brecher.blackjack.shared.gameplay.Gameplay;

public class GameplayImpl implements Gameplay {

    final HandManager playerHand;
    final HandManager dealerHand;
    final Thread thread;

    @Inject
    DeckManager deck;

    @Inject
    @Human
    Player humanPlayer;

    @Inject
    @Dealer
    Player dealerPlayer;
    boolean inRound;
    boolean beginNextRound;

    final AsyncEventBus asyncEventBus;


    @Inject
    public GameplayImpl(AsyncEventBus asyncEventBus, Server game) {
        this.playerHand = game.getPlayerHandManager();
        this.dealerHand = game.getDealerHandManager();

        this.asyncEventBus = asyncEventBus;


        this.inRound = false;
        this.beginNextRound = false;
        this.thread = new Thread(() -> this.run());

        this.thread.start();

        asyncEventBus.register(this);
    }

    void run() {
        while (true) {

            asyncEventBus.post(new RoundResetEvent());

            synchronized (this) {
                while (!beginNextRound) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("Gameplay thread. Interrupt while waiting for next round");
                    }
                }
            }


            synchronized (this) {
                inRound = true;
                beginNextRound = false;
            }

            this.deck.reset();

            this.asyncEventBus.post(new RoundBeganEvent());



            asyncEventBus.post(new AddCardEvent(0, this.deck.draw(true)));
            asyncEventBus.post(new AddCardEvent(0, this.deck.draw(false)));

            asyncEventBus.post(new AddCardEvent(1, this.deck.draw(true)));
            asyncEventBus.post(new AddCardEvent(1, this.deck.draw(true)));


            this.humanPlayer.beginTurn();
            this.humanPlayer.waitForTurn();

            asyncEventBus.post(new RevealCardsEvent());


            this.dealerPlayer.beginTurn();
            this.dealerPlayer.waitForTurn();

            int result = 0;
            boolean blackJack = this.playerHand.hasBlackjack();

            if (this.playerHand.handValue() > 21) {
                if (this.dealerHand.handValue() > 21) {
                    result = 2;
                } else {
                    result = 0;
                }
            } else if (this.dealerHand.handValue() > 21) {
                result = 1;
            } else if (this.playerHand.handValue() > this.dealerHand.handValue()) {
                result = 1;
            } else if (this.playerHand.handValue() < this.dealerHand.handValue()) {
                result = 0;
            } else {
                result = 2;
            }


            asyncEventBus.post(new RoundEndEvent(blackJack, result));

            synchronized (this) {
                inRound = false;
            }
        }
    }

    @Subscribe
    public synchronized void startRound(StartRoundEvent event) {
        if (!inRound) {
            beginNextRound = true;
            notify();
        }
    }

    @Override
    public synchronized boolean isPlaying() {
        return inRound;
    }


}
