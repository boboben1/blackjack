package me.brecher.blackjack.shared.events;

import java.io.Serializable;

public class RoundNewRoundEvent implements Serializable {
    private final String name = "ResetEvent";

    public String getName() {
        return name;
    }
}
