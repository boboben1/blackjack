package me.brecher.blackjack.shared.events;

import java.io.Serializable;

public class PlayerDoubleEvent implements Serializable {
    private String name = "PlayerDoubleEvent";

    public String getName() {
        return name;
    }
}
