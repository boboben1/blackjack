package me.brecher.blackjack.shared.events;

import java.io.Serializable;

public class BaseEvent implements Serializable {
    private final String name = "BaseEvent";

    public String getName() {
        return name;
    }
}
