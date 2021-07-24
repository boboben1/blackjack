package me.brecher.blackjack.shared.events;

import java.io.Serializable;

public class RoundBeganEvent implements Serializable {
    private final String name = "RoundBeginEvent";

    public String getName() {
        return name;
    }
}
