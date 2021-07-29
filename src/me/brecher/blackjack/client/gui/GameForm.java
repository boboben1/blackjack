package me.brecher.blackjack.client.gui;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import me.brecher.blackjack.client.ClientToServerEventQueue;
import me.brecher.blackjack.shared.events.*;

import javax.swing.*;


public class GameForm {

    private JPanel panel;
    private JButton hitButton;
    private JButton standButton;
    private HandView handView1;
    private HandView handView2;
    private JLabel MoneyLabel;
    private JLabel BetLabel;
    private JButton resetButton;
    private JButton betChangePlus10;
    private JButton betChangeMinus10;
    private JButton betChangeMinus1;
    private JButton betChangePlus1;
    private JButton betChangeMinus100;
    private JButton betChangePlus100;
    private JButton allIn;
    private JButton doubleButton;
    private JLabel statusLabel;
    private JPanel actionPanel;
    private JButton splitButton;

    private final AsyncEventBus eventBus;
    private final HandViewFactory handViewFactory;
    private final ClientToServerEventQueue clientToServerEventQueue;

    private boolean loaded;
    private boolean hasError;
    private final Timer timer;

    @Inject
    public GameForm(HandViewFactory handViewFactory, AsyncEventBus eventBus, ClientToServerEventQueue clientToServerEventQueue) {
        this.handViewFactory = handViewFactory;
        this.eventBus = eventBus;
        this.clientToServerEventQueue = clientToServerEventQueue;
        this.loaded = false;

        this.timer = new Timer(1000, e -> {
            synchronized (this) {
                this.statusLabel.setText(this.statusLabel.getText() + ".");
                if (this.statusLabel.getText().length() > 13)
                    this.statusLabel.setText("Loading.");
            }
        });

        this.timer.start();

        eventBus.register(this);

        setButtonsEnabled(false);
    }


    void setButtonsEnabled(boolean enabled) {
        this.hitButton.setEnabled(enabled);
        this.standButton.setEnabled(enabled);
        this.doubleButton.setEnabled(enabled);
        this.splitButton.setEnabled(enabled);
    }

    @Subscribe
    public void betUpdated(BetUpdateEvent event) {
        this.BetLabel.setText("" + event.getAmount());
    }

    @Subscribe
    public void guiUpdateMoney(GuiUpdateMoneyEvent event) {
        this.MoneyLabel.setText("" + event.getMoney());
    }

    @Subscribe
    public void roundEnd(RoundEndEvent event) {
        switch (event.getResult()) {
            case 0:
                this.statusLabel.setText("DEALER WON!");
                break;
            case 1:
                this.statusLabel.setText("YOU WON!");
                break;
            case 2:
                this.statusLabel.setText("PUSH");
                break;
        }
    }

    @Subscribe
    public void roundBegun(RoundBeganEvent event) {
        this.statusLabel.setText("");
        this.handView1.reset();
        this.handView2.reset();

        this.clientToServerEventQueue.sendToServer(new ClientAckEvent());
    }

    @Subscribe
    public void guiLoaded(GuiLoadEvent event) {
        this.loaded = true;
        this.hasError = event.hasError();

        this.timer.stop();

        if (hasError)
            this.statusLabel.setText("An error occurred while loading assets.");
        else
        {
            this.statusLabel.setText("Press any action to deal");
            setButtonsEnabled(true);
        }

    }

    @Inject
    void injectListeners(
            @Named("HitAction") AbstractAction hitAction,
            @Named("StandAction") AbstractAction standAction,
            @Named("BetResetAction") AbstractAction resetAction,
            @Named("BetAllInAction") AbstractAction allInAction,
            @Named("DoubleAction") AbstractAction doubleAction,
            @Named("SplitAction") AbstractAction splitAction,
            BetChangeFactory betChangeFactory) {
        this.hitButton.addActionListener(hitAction);
        this.standButton.addActionListener(standAction);

        this.resetButton.addActionListener(resetAction);

        this.allIn.addActionListener(allInAction);

        this.betChangeMinus1.addActionListener(betChangeFactory.create(-1));
        this.betChangeMinus10.addActionListener(betChangeFactory.create(-10));
        this.betChangeMinus100.addActionListener(betChangeFactory.create(-100));

        this.betChangePlus1.addActionListener(betChangeFactory.create(1));
        this.betChangePlus10.addActionListener(betChangeFactory.create(10));
        this.betChangePlus100.addActionListener(betChangeFactory.create(100));

        this.doubleButton.addActionListener(doubleAction);
        this.splitButton.addActionListener(splitAction);
    }

    public void addToFrame(JFrame frame) {
        frame.setContentPane(this.panel);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        this.handView1 = handViewFactory.create(1);
        this.handView2 = handViewFactory.create(0);


    }

}
