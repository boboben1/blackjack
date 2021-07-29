package me.brecher.blackjack.server.gameplay;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import me.brecher.blackjack.annotations.Dealer;
import me.brecher.blackjack.annotations.Human;
import me.brecher.blackjack.server.Server;
import me.brecher.blackjack.server.deckmanager.DeckManager;
import me.brecher.blackjack.shared.events.*;
import me.brecher.blackjack.server.player.Player;
import me.brecher.blackjack.shared.gameplay.Gameplay;
import me.brecher.blackjack.shared.models.Hand;
import me.brecher.blackjack.shared.models.RoundResult;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

public class GameplayImpl implements Gameplay {
    final Thread thread;

    @Inject
    DeckManager deck;

    @Inject
    @Human
    Player player;

    @Inject
    @Dealer
    Player dealer;



    boolean inRound;
    boolean beginNextRound;

    private final AsyncEventBus asyncEventBus;
    private final ScheduledExecutorService scheduledExecutorService;

    private final Server game;
    private final ClientSync clientSync;


    @Inject
    public GameplayImpl(AsyncEventBus asyncEventBus, Server game, ScheduledExecutorService scheduledExecutorService, ClientSync clientSync) {
        this.asyncEventBus = asyncEventBus;
        this.scheduledExecutorService = scheduledExecutorService;
        this.game = game;
        this.clientSync = clientSync;


        this.inRound = false;
        this.beginNextRound = false;
        this.thread = new Thread(this::run);

        this.thread.start();

        asyncEventBus.register(this);
    }

    private synchronized void waitForNextRound() {
        while (!beginNextRound) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Gameplay thread. Interrupt while waiting for next round");
            }
        }

        beginNextRound = false;
    }


    void run() {

        clientSync.syncClient();

        asyncEventBus.post(new BetResetEvent());

        while (!Thread.currentThread().isInterrupted()) {


            asyncEventBus.post(new RoundNewRoundEvent());


            waitForNextRound();

            synchronized (this) {
                inRound = true;
            }

            this.deck.reset();

            this.asyncEventBus.post(new RoundBeganEvent());

            clientSync.syncClient();

            player.resetHand();
            dealer.resetHand();

            dealer.addCards(Arrays.asList(this.deck.draw(true), this.deck.draw(false)));
            player.addCards(Arrays.asList(this.deck.draw(true), this.deck.draw(true)));


            if (!this.dealer.hasBlackjack())
            {
                this.player.beginTurn();
                this.player.waitForTurn();
            }

            asyncEventBus.post(new RevealCardsEvent());

            if (!this.player.hasBlackjack())
            {
                this.dealer.beginTurn();
                this.dealer.waitForTurn();
            }



//            int winner;
//            boolean blackJack = this.player.hasBlackjack();
//
//            if (this.player.hasBlackjack())
//            {
//                if (this.dealer.hasBlackjack()) {
//                    winner = 2;
//                }
//                else {
//                    winner = 1;
//                }
//            } else if (this.dealer.hasBlackjack()) {
//                winner = 0;
//            }
//            else if (this.player.handValue() > 21) {
//                if (this.dealer.handValue() > 21) {
//                    winner = 2;
//                } else {
//                    winner = 0;
//                }
//            } else if (this.dealer.handValue() > 21) {
//                winner = 1;
//            } else if (this.player.handValue() > this.dealer.handValue()) {
//                winner = 1;
//            } else if (this.player.handValue() < this.dealer.handValue()) {
//                winner = 0;
//            } else {
//                winner = 2;
//            }

            List<RoundResult> roundResults = player.getHands().stream()
                    .map(h -> Hand.checkWinner(dealer.getActiveHand(), h)).collect(Collectors.toList());

            System.out.println(roundResults);

            asyncEventBus.post(new RoundEndEvent(roundResults));

            synchronized (this) {
                inRound = false;
            }

        }
    }

    @Subscribe
    public synchronized void startRound(StartRoundEvent event) {
        if (!inRound) {
            beginNextRound = true;
            notifyAll();
        }
    }

    @Override
    public synchronized boolean isPlaying() {
        return inRound;
    }


}
