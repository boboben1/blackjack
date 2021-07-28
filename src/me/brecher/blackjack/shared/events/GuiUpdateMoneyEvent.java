package me.brecher.blackjack.shared.events;

import java.io.Serializable;

public class GuiUpdateMoneyEvent implements Serializable {
    private final String name = "GuiUpdateMoneyEvent";
    private final int playerID;
    private final long money;
    private final boolean showChange;
    private final long change;

    public GuiUpdateMoneyEvent(int playerID, long money, boolean showChange, long change) {
        this.playerID = playerID;
        this.money = money;
        this.showChange = showChange;
        this.change = change;
    }

    public GuiUpdateMoneyEvent(int playerID, long money) {
        this.playerID = playerID;
        this.money = money;
        this.showChange = false;
        this.change = 0;
    }

    public String getName() {
        return name;
    }

    public int getPlayerID() {
        return playerID;
    }

    public long getMoney() {
        return money;
    }

    public boolean isShowChange() {
        return showChange;
    }

    public long getChange() {
        return change;
    }
}
