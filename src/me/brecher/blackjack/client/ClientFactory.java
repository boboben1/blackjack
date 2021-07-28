package me.brecher.blackjack.client;

public interface ClientFactory {
    Client create(int port);
}
