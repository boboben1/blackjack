package me.brecher.blackjack.server.scoring;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import me.brecher.blackjack.shared.events.*;
import me.brecher.blackjack.shared.models.RoundResult;

public class BetManagerImpl implements BetManager {

    private final AsyncEventBus eventBus;
    private final int minBet;
    private final ScoreKeeper scoreKeeper;

    private boolean justReset;

    private boolean doubled;

    private int bet;

    private boolean canChangeBet;

    @Inject
    public BetManagerImpl(AsyncEventBus eventBus, @Named("MinBet") int minBet, ScoreKeeper scoreKeeper) {
        this.eventBus = eventBus;
        this.minBet = minBet;
        this.scoreKeeper = scoreKeeper;

        this.bet = minBet;
        this.doubled = false;

        this.justReset = false;
        this.canChangeBet = true;

        eventBus.register(this);
    }

    @Subscribe
    public synchronized void betChanged(BetChangeEvent event) {

        if (!this.canChangeBet)
                return;

        if (justReset && event.getAmount() > minBet)
            bet = event.getAmount();
        else
            bet += event.getAmount();

        if (bet < minBet) {
            bet = minBet;
        }

        if (bet > scoreKeeper.getLocalPlayerMoney()) {
            bet = (int) scoreKeeper.getLocalPlayerMoney();
        }

        justReset = false;

        this.eventBus.post(new BetUpdateEvent(bet));
    }

    @Subscribe
    public synchronized void betReset(BetResetEvent event) {
        if (!canChangeBet)
            return;

        bet = minBet;

        justReset = true;

        this.eventBus.post(new BetUpdateEvent(bet));
    }

    @Subscribe
    public synchronized void betAllInAction(BetAllInEvent event) {
        if (!canChangeBet)
            return;

        bet = (int) scoreKeeper.getLocalPlayerMoney();

        this.eventBus.post(new BetUpdateEvent(bet));
    }

    @Subscribe
    public synchronized void roundReset(RoundNewRoundEvent event) {

        this.eventBus.post(new BetUpdateEvent(bet));

        this.canChangeBet = true;
    }

    @Subscribe
    public synchronized void roundBegan(RoundBeganEvent event) {
        this.canChangeBet = false;

        eventBus.post(new MoneyChangedEvent(1, -bet));
    }

    @Subscribe
    public synchronized void roundEnded(RoundEndEvent event) {
        int pay = 0;
        for (RoundResult roundResult : event.getRoundResults()) {
            int roundPay = 0;
            if (roundResult.getWinner() == 2) {
                roundPay = bet;
            } else if (roundResult.getWinner() == 1) {
                roundPay = 2 * bet;

                if (roundResult.isWithBlackjack()) {
                    roundPay += bet * 0.5f;
                }
            }

            pay += roundPay;
        }

        if (doubled)
            pay *= 2;

        doubled = false;

        if (bet > scoreKeeper.getLocalPlayerMoney() + pay)
            bet = Math.max((int)scoreKeeper.getLocalPlayerMoney() + pay, minBet);

        this.eventBus.post(new MoneyChangedEvent(1, pay, true));

    }

    @Override
    public synchronized void doubleDown() {
        if (scoreKeeper.getLocalPlayerMoney() >= bet) {
            this.eventBus.post(new MoneyChangedEvent(1, -bet, true));

            this.doubled = true;
        }
    }

    @Override
    public int getBet() {
        return bet;
    }

    @Override
    public int getMinBet() {
        return minBet;
    }

    @Override
    public synchronized boolean canDouble() {
        if (canChangeBet)
            return false;

        return bet <= scoreKeeper.getLocalPlayerMoney();
    }

    @Override
    public void split() {
        this.eventBus.post(new MoneyChangedEvent(1, -bet, true));
    }

    @Override
    public boolean canSplit() {
        if (canChangeBet)
            return false;

        return bet <= scoreKeeper.getLocalPlayerMoney();
    }
}
