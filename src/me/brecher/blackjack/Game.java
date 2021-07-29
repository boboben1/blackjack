package me.brecher.blackjack;

import com.google.inject.Guice;
import com.google.inject.Injector;
import me.brecher.blackjack.client.Client;
import me.brecher.blackjack.client.ClientFactory;
import me.brecher.blackjack.client.ClientModule;
import me.brecher.blackjack.server.Server;
import me.brecher.blackjack.server.ServerModule;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Game {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new GameModule());

        Server server = injector.getInstance(Server.class);

        int port = server.start();

        Client client = injector.getInstance(ClientFactory.class).create(port);

        client.start();
    }
}
