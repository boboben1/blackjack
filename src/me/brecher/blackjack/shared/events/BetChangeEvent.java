package me.brecher.blackjack.shared.events;

import java.io.Serializable;

public class BetChangeEvent implements Serializable {
    private String name = "BetChangeEvent";
    private int amount;

    public BetChangeEvent(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }
}
