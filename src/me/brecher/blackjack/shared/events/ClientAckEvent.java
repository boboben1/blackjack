package me.brecher.blackjack.shared.events;

import java.io.Serializable;

public class ClientAckEvent implements Serializable {
    private final String name = "ClientAckEvent";

    public String getName() {
        return name;
    }
}
