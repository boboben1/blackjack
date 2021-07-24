package me.brecher.blackjack.client.gameplay;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import me.brecher.blackjack.shared.events.RoundBeganEvent;
import me.brecher.blackjack.shared.events.RoundEndEvent;
import me.brecher.blackjack.shared.gameplay.Gameplay;


public class ClientGameplayImpl implements Gameplay {

    private boolean isPlaying;

    @Inject
    ClientGameplayImpl(AsyncEventBus asyncEventBus) {
        asyncEventBus.register(this);

        this.isPlaying = false;
    }

    @Subscribe
    public synchronized void roundBegan(RoundBeganEvent event) {
        this.isPlaying = true;
    }

    @Subscribe
    public synchronized void roundEnd(RoundEndEvent event) {
        this.isPlaying = false;
    }

    @Override
    public synchronized boolean isPlaying() {
        return isPlaying;
    }
}
