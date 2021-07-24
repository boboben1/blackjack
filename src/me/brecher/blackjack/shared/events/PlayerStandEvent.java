package me.brecher.blackjack.shared.events;

import java.io.Serializable;

public class PlayerStandEvent implements Serializable {
    private final String name = "PlayerStandEvent";

    public PlayerStandEvent() {
    }
    public String getName() {
        return name;
    }
}
