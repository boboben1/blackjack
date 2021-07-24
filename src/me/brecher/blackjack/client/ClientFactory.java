package me.brecher.blackjack.client;

public interface ClientFactory {
    public Client create(int port);
}
