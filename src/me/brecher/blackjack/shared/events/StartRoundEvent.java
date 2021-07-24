package me.brecher.blackjack.shared.events;

import java.io.Serializable;

public class StartRoundEvent implements Serializable {
    private final String name = "StartRoundEvent";

    public String getName() {
        return name;
    }
}
