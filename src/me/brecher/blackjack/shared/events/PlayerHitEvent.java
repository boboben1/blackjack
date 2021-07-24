package me.brecher.blackjack.shared.events;

import java.io.Serializable;

public class PlayerHitEvent implements Serializable {
    private final String name = "PlayerHitEvent";

    public String getName() {
        return name;
    }
}
