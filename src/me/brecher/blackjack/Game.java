package me.brecher.blackjack;

import com.google.inject.Guice;
import com.google.inject.Injector;
import me.brecher.blackjack.client.Client;
import me.brecher.blackjack.client.ClientFactory;
import me.brecher.blackjack.client.ClientModule;
import me.brecher.blackjack.server.Server;
import me.brecher.blackjack.server.ServerModule;

import java.lang.ref.Cleaner;

public class Game {

    public static void main(String[] args) {
        Injector serverInjector = Guice.createInjector(new ServerModule());

        Server server = serverInjector.getInstance(Server.class);


        int port = server.start();

        Injector clientInjector = Guice.createInjector(new ClientModule());

        Client client = clientInjector.getInstance(ClientFactory.class).create(port);

        client.start();
    }
}
