package me.brecher.blackjack.server;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import me.brecher.blackjack.shared.events.*;

public class ServerEventRouter {
    private final AsyncEventBus asyncEventBus;
    private final ServerToClientEventQueue serverToClientEventQueue;


    @Inject
    ServerEventRouter(AsyncEventBus asyncEventBus, ServerToClientEventQueue serverToClientEventQueue) {
        this.asyncEventBus = asyncEventBus;
        this.serverToClientEventQueue = serverToClientEventQueue;

        asyncEventBus.register(this);
    }

    @Subscribe
    public void guiAddCard(GuiAddCardEvent event) {
        this.serverToClientEventQueue.sendToClient(event);
    }

    @Subscribe
    public void startRoundEvent(StartRoundEvent event) {
        this.serverToClientEventQueue.sendToClient(event);
    }

    @Subscribe
    public void roundBegan(RoundBeganEvent event) {
        this.serverToClientEventQueue.sendToClient(event);
    }

    @Subscribe
    public void roundEnd(RoundEndEvent event) {
        this.serverToClientEventQueue.sendToClient(event);
    }

    @Subscribe
    public void reveal(RevealCardsEvent event) {
        this.serverToClientEventQueue.sendToClient(event);
    }

    @Subscribe
    public void resetEvent(RoundNewRoundEvent event) {
        this.serverToClientEventQueue.sendToClient(event);
    }

    @Subscribe
    public void guiUpdateMoney(GuiUpdateMoneyEvent event) {
        this.serverToClientEventQueue.sendToClient(event);
    }

    @Subscribe
    public void betUpdate(BetUpdateEvent event) {
        this.serverToClientEventQueue.sendToClient(event);
    }

    @Subscribe
    public void guiAddCards(GuiAddCardsEvent event) {
        this.serverToClientEventQueue.sendToClient(event);
    }

    @Subscribe
    public void guiSplit(GuiSplitEvent event) {
        this.serverToClientEventQueue.sendToClient(event);
    }

    @Subscribe
    public void guiSwitch(GuiSwitchEvent event) {
        this.serverToClientEventQueue.sendToClient(event);
    }
}
