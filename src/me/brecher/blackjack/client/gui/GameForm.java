package me.brecher.blackjack.client.gui;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import me.brecher.blackjack.client.ClientToServerEventQueue;
import me.brecher.blackjack.client.cardresourcefinder.CardResourceFinder;
import me.brecher.blackjack.shared.events.*;
import me.brecher.blackjack.shared.models.RoundResult;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Locale;


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
    private final CardResourceFinder cardResourceFinder;

    private boolean loaded;
    private boolean hasError;
    private final Timer timer;


    @Inject
    public GameForm(HandViewFactory handViewFactory, AsyncEventBus eventBus, ClientToServerEventQueue clientToServerEventQueue, CardResourceFinder cardResourceFinder) {
        this.handViewFactory = handViewFactory;
        this.eventBus = eventBus;
        this.clientToServerEventQueue = clientToServerEventQueue;
        this.cardResourceFinder = cardResourceFinder;
        this.loaded = false;

        $$$setupUI$$$();
        this.timer = new Timer(1000, this::loadingTimer);

        this.timer.start();

        eventBus.register(this);

        setButtonsEnabled(false);
    }


    private void loadingTimer(ActionEvent e) {
        synchronized (this) {
            this.statusLabel.setText(this.statusLabel.getText() + ".");
            if (this.statusLabel.getText().length() > 13)
                this.statusLabel.setText("Loading.");

            if (cardResourceFinder.hadError()) {
                this.timer.stop();
                this.statusLabel.setText("An error occurred. \nMissing resources or classpath set incorrectly?");
            }
        }
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

        List<RoundResult> roundResults = event.getRoundResults();

        StringBuilder text = new StringBuilder();

        boolean multipleHands = roundResults.size() > 1;

        for (int i = 0; i < roundResults.size(); ++i) {
            if (multipleHands) {
                if (i > 0) {
                    text.append(" | ");
                }
                text.append("Hand ").append(i).append(" ");
            }

            switch (roundResults.get(i).getWinner()) {
                case 0:
                    text.append("DEALER WON!");
                    break;
                case 1:
                    text.append("YOU WON!");
                    break;
                case 2:
                    text.append("PUSH");
                    break;
            }
        }

        this.statusLabel.setText(text.toString());
    }

    @Subscribe
    public void roundBegun(RoundBeganEvent event) {
        this.statusLabel.setText(" ");
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
        else {
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

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panel = new JPanel();
        panel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(9, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(handView1, new com.intellij.uiDesigner.core.GridConstraints(6, 0, 3, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(600, 250), new Dimension(1000, 351), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(24, 351), null, 0, false));
        resetButton = new JButton();
        resetButton.setText("Reset");
        panel1.add(resetButton, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        betChangeMinus1 = new JButton();
        betChangeMinus1.setText("-");
        panel2.add(betChangeMinus1, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        betChangePlus1 = new JButton();
        betChangePlus1.setText("+");
        panel2.add(betChangePlus1, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        betChangeMinus10 = new JButton();
        betChangeMinus10.setText("-10");
        panel2.add(betChangeMinus10, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        betChangePlus10 = new JButton();
        betChangePlus10.setText("+10");
        panel2.add(betChangePlus10, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        betChangeMinus100 = new JButton();
        betChangeMinus100.setText("-100");
        panel2.add(betChangeMinus100, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        betChangePlus100 = new JButton();
        betChangePlus100.setText("+100");
        panel2.add(betChangePlus100, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        allIn = new JButton();
        allIn.setText("ALL IN");
        panel1.add(allIn, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel.add(panel3, new com.intellij.uiDesigner.core.GridConstraints(8, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_SOUTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("$");
        panel3.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(6, 18), null, 0, false));
        MoneyLabel = new JLabel();
        MoneyLabel.setText("MoneyLabel");
        panel3.add(MoneyLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(65, 18), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Bet");
        panel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(7, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        BetLabel = new JLabel();
        BetLabel.setText("BetLabel");
        panel.add(BetLabel, new com.intellij.uiDesigner.core.GridConstraints(7, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        handView2.setAlignmentX(0.5f);
        panel.add(handView2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 4, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(600, 250), new Dimension(1000, 250), null, 0, false));
        actionPanel = new JPanel();
        actionPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        actionPanel.setEnabled(true);
        panel.add(actionPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 5, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        standButton = new JButton();
        standButton.setText("Stand");
        actionPanel.add(standButton, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        actionPanel.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        actionPanel.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        doubleButton = new JButton();
        doubleButton.setText("Double");
        actionPanel.add(doubleButton, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        hitButton = new JButton();
        hitButton.setText("Hit");
        hitButton.setVerticalAlignment(0);
        actionPanel.add(hitButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        splitButton = new JButton();
        splitButton.setText("Split");
        actionPanel.add(splitButton, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        panel.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        statusLabel = new JLabel();
        Font statusLabelFont = this.$$$getFont$$$(null, -1, 40, statusLabel.getFont());
        if (statusLabelFont != null) statusLabel.setFont(statusLabelFont);
        statusLabel.setText("Loading.");
        panel.add(statusLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 4, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }
}
