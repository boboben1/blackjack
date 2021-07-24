package me.brecher.blackjack.client.gui;

import javax.swing.*;

public interface BetChangeFactory {
    AbstractAction create(int amount);
}
