package me.brecher.blackjack.server.scoring;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import me.brecher.blackjack.shared.events.GuiUpdateMoneyEvent;
import me.brecher.blackjack.shared.events.MoneyChangedEvent;
import me.brecher.blackjack.shared.events.RoundResetEvent;

public class ScoreKeeperImpl implements ScoreKeeper {

    private final AsyncEventBus asyncEventBus;
    private final long minMoney;

    private final ScoreSaver<Integer, Long> scoreSaver;

    @Inject
    ScoreKeeperImpl(AsyncEventBus asyncEventBus, @Named("MinMoney") long minMoney, ScoreSaver<Integer, Long> scoreSaver) {
        this.asyncEventBus = asyncEventBus;
        this.minMoney = minMoney;

        this.scoreSaver = scoreSaver;

        asyncEventBus.register(this);
    }

    @Subscribe
    public void moneyChanged(MoneyChangedEvent event) {
        Long current_money = this.scoreSaver.getScoreForPlayer(event.getPlayerID());

        long new_money = event.getChange() + current_money;

        if (new_money < minMoney && event.isPayout()) {
            new_money = minMoney;
        }

        this.scoreSaver.putScoreForPlayer(event.getPlayerID(), new_money);

        this.asyncEventBus.post(new GuiUpdateMoneyEvent(event.getPlayerID(), new_money, true, event.getChange()));
    }

    @Subscribe
    public void roundReset(RoundResetEvent event) {
        this.asyncEventBus.post(new GuiUpdateMoneyEvent(1, this.scoreSaver.getScoreForPlayer(1)));
    }

    @Override
    public long getLocalPlayerMoney() {
        return scoreSaver.getScoreForPlayer(1);
    }
}
