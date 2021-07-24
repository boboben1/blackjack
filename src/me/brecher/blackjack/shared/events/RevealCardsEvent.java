package me.brecher.blackjack.shared.events;

import java.io.Serializable;

public class RevealCardsEvent implements Serializable {
    private final String name = "RevealCardsEvent";

    public String getName() {
        return name;
    }
}
