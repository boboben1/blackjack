package me.brecher.blackjack.shared.events;

import java.io.Serializable;

public class BetResetEvent implements Serializable {
    private final String name = "BetResetEvent";

    public BetResetEvent() {

    }

    public String getName() {
        return name;
    }
}
