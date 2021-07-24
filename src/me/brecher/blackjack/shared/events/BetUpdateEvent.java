package me.brecher.blackjack.shared.events;

import java.io.Serializable;

public class BetUpdateEvent implements Serializable {
    private final String name = "BetUpdateEvent";
    private final int amount;

    public BetUpdateEvent(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }
}
