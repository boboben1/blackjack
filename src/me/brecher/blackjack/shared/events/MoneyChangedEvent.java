package me.brecher.blackjack.shared.events;

import java.io.Serializable;

public class MoneyChangedEvent implements Serializable {
    private String name = "MoneyChangedEvent";
    private int playerID;
    private int change;

    public MoneyChangedEvent(int playerID, int change) {
        this.playerID = playerID;
        this.change = change;
    }

    public String getName() {
        return name;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getChange() {
        return change;
    }
}
