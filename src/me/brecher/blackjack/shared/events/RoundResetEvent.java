package me.brecher.blackjack.shared.events;

import java.io.Serializable;

public class RoundResetEvent implements Serializable {
    private final String name = "ResetEvent";

    public String getName() {
        return name;
    }
}
