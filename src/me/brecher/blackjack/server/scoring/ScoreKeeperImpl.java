package me.brecher.blackjack.server.scoring;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import me.brecher.blackjack.shared.events.GuiUpdateMoneyEvent;
import me.brecher.blackjack.shared.events.MoneyChangedEvent;
import me.brecher.blackjack.shared.events.RoundResetEvent;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ScoreKeeperImpl implements ScoreKeeper {

    AsyncEventBus asyncEventBus;
    private final long minMoney;

//
//    Map<Integer, Long> playerValues;
//
    private final ScoreSaver<Integer, Long> scoreSaver;

    @Inject
    ScoreKeeperImpl(AsyncEventBus asyncEventBus, @Named("MinMoney") long minMoney, ScoreSaver<Integer, Long> scoreSaver) {
        this.asyncEventBus = asyncEventBus;
        this.minMoney = minMoney;

        this.scoreSaver = scoreSaver;

        asyncEventBus.register(this);

//
//        if (scoreSaver.saveExists()) {
//            this.playerValues = scoreSaver.load();
//        }
//
//        if (this.playerValues == null) {
//            this.playerValues = new HashMap<>();
//            this.playerValues.put(0, Long.MAX_VALUE);
//            this.playerValues.put(1, 1000L);
//
//            this.scoreSaver.save(this.playerValues);
//        }
    }

    @Subscribe
    public void moneyChanged(MoneyChangedEvent event) {
        Long current_money = this.scoreSaver.getScoreForPlayer(event.getPlayerID());

        Long new_money = event.getChange() + current_money;

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
