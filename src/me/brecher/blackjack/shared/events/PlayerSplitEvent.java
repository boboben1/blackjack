package me.brecher.blackjack.shared.events;

import java.io.Serializable;

public class PlayerSplitEvent implements Serializable {
    private final String name = "PlayerSplitEvent";

    public String getName() {
        return name;
    }
}
