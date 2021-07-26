package me.brecher.blackjack.shared.events;

import java.io.Serializable;

public class GuiLoadEvent implements Serializable {
    private final String name = "GuiLoadEvent";
    private final boolean hasError;

    public GuiLoadEvent(boolean hasError) {
        this.hasError = hasError;
    }

    public boolean hasError() {
        return hasError;
    }

    public String getName() {
        return name;
    }
}
