package me.brecher.blackjack.shared.events;

import java.io.Serializable;

public class BetAllInEvent implements Serializable {
    private String name = "BetAllInEvent";

    public String getName() {
        return name;
    }
}
