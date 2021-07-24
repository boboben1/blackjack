package me.brecher.blackjack.server.handmanager;

public interface HandManagerFactory {
    HandManager create(int playerNumber);
}
