package me.brecher.blackjack.shared.events;

import java.io.Serializable;

public class MoneyChangedEvent implements Serializable {
    private String name = "MoneyChangedEvent";
    private int playerID;
    private int change;
    private boolean payout;


    public MoneyChangedEvent(int playerID, int change) {
        this.playerID = playerID;
        this.change = change;
        this.payout = false;
    }
    public MoneyChangedEvent(int playerID, int change, boolean payout) {
        this.playerID = playerID;
        this.change = change;
        this.payout = payout;
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

    public boolean isPayout() {
        return payout;
    }
}
