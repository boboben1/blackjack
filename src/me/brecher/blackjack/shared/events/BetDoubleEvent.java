package me.brecher.blackjack.shared.events;

import java.io.Serializable;

public class BetDoubleEvent implements Serializable {
    private final String name = "BetDoubleEvent";

    public String getName() {
        return name;
    }
}
