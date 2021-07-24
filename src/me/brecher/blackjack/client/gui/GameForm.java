package me.brecher.blackjack.client.gui;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import me.brecher.blackjack.shared.events.BetUpdateEvent;
import me.brecher.blackjack.shared.events.GuiUpdateMoneyEvent;

import javax.swing.*;
import java.awt.*;


public class GameForm {
    final HandViewFactory handViewFactory;
    private JPanel panel;
    private JButton button1;
    private JButton button2;
    private JPanel actionPanel;
    private JPanel playPanel;
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

    private final AsyncEventBus eventBus;

    @Inject
    public GameForm(HandViewFactory handViewFactory, AsyncEventBus eventBus) {
        this.handViewFactory = handViewFactory;
        this.eventBus = eventBus;

        eventBus.register(this);
    }

    @Subscribe
    public void betUpdated(BetUpdateEvent event) {
        this.BetLabel.setText("" + event.getAmount());
    }

    @Subscribe
    public void guiUpdateMoney(GuiUpdateMoneyEvent event) {
        this.MoneyLabel.setText("" + event.getMoney());
    }

    @Inject
    void injectListeners(
            @Named("HitAction") AbstractAction hitAction,
            @Named("StandAction") AbstractAction standAction,
            @Named("BetResetAction") AbstractAction resetAction,
            @Named("BetAllInAction") AbstractAction allInAction,
            BetChangeFactory betChangeFactory) {
        this.button1.addActionListener(hitAction);
        this.button2.addActionListener(standAction);

        this.resetButton.addActionListener(resetAction);

        this.allIn.addActionListener(allInAction);

        this.betChangeMinus1.addActionListener(betChangeFactory.create(-1));
        this.betChangeMinus10.addActionListener(betChangeFactory.create(-10));
        this.betChangeMinus100.addActionListener(betChangeFactory.create(-100));

        this.betChangePlus1.addActionListener(betChangeFactory.create(1));
        this.betChangePlus10.addActionListener(betChangeFactory.create(10));
        this.betChangePlus100.addActionListener(betChangeFactory.create(100));
    }

    public void addToFrame(JFrame frame) {
        frame.setContentPane(this.panel);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

        this.actionPanel = new JPanel();
        this.playPanel = new JPanel();
        this.playPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 3, Color.LIGHT_GRAY));


        this.handView1 = handViewFactory.create(1);
        this.handView2 = handViewFactory.create(0);

    }
}
