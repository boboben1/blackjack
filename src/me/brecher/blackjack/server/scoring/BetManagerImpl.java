package me.brecher.blackjack.server.scoring;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import me.brecher.blackjack.shared.events.*;

public class BetManagerImpl implements BetManager {

    AsyncEventBus eventBus;
    final int minBet;
    final ScoreKeeper scoreKeeper;

    boolean justReset;

    int bet;

    boolean canChangeBet;

    @Inject
    public BetManagerImpl(AsyncEventBus eventBus, @Named("MinBet") int minBet, ScoreKeeper scoreKeeper) {
        this.eventBus = eventBus;
        this.minBet = minBet;
        this.scoreKeeper = scoreKeeper;

        this.bet = minBet;

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
    public synchronized void roundReset(RoundResetEvent event) {

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

        // PUSH
        if (event.getResult() == 3)
        {
            pay = bet;
        } //WIN
        else if (event.getResult() == 1) {
            pay = 2 * bet;
            if (event.isBlackJack())
                pay += bet * 0.5;
        }

        if (bet > scoreKeeper.getLocalPlayerMoney() + pay)
            bet = (int)scoreKeeper.getLocalPlayerMoney() + pay;

        this.eventBus.post(new MoneyChangedEvent(1, pay));

    }

    @Override
    public int getBet() {
        return bet;
    }

    @Override
    public int getMinBet() {
        return minBet;
    }
}
