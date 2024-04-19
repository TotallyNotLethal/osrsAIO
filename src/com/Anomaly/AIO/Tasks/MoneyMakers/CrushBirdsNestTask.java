package com.Anomaly.AIO.Tasks.MoneyMakers;

import com.Anomaly.AIO.Helpers.State.Methods.BankEquipmentState;
import com.Anomaly.AIO.Helpers.State.Methods.BankingState;
import com.Anomaly.AIO.Helpers.State.Methods.GrandExchange.BuyItemsState;
import com.Anomaly.AIO.Helpers.State.Methods.GrandExchange.SellItemsState;
import com.Anomaly.AIO.Helpers.State.StateManager;
import com.Anomaly.AIO.Main.SettingsManager;
import com.Anomaly.AIO.Main.Task;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankMode;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.grandexchange.LivePrices;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.items.Item;

import java.awt.*;
import java.util.Map;

public class CrushBirdsNestTask extends Task {
    private final AbstractScript script;
    private final StateManager stateManager;
    private final SettingsManager settings;
    private int profit = 0;
    private boolean isComplete = false;

    public CrushBirdsNestTask(AbstractScript script, SettingsManager settings) {
        this.script = script;
        this.stateManager = new StateManager(script);
        this.settings = settings;
    }

    @Override
    public int execute() {
        if (stateManager.hasStates()) {
            stateManager.executeCurrentState();
            return 600; // Allow state execution before continuing
        }

        prepareStates();

        if (Inventory.contains("Pestle and mortar") && Inventory.contains("Bird nest")) {
            processNests();
        }

        return 300; // Tick delay
    }

    private void prepareStates() {

        if (!Inventory.contains("Pestle and mortar") || Inventory.count("Bird nest") < 27) {

            Bank.open();
            if (!Bank.isOpen()) {
                stateManager.addState(new BankingState(script, settings, Map.of("Pestle and mortar", 1, "Bird nest", 27), null, false, false, "Pestle and mortar", "Bird nest"));
                if(Bank.isOpen())
                    Bank.close();
            } else {
                handleBankContents();
            }
        }
    }

    private void handleBankContents() {
        Bank.open();
        Sleep.sleepUntil(Bank::isOpen, 1000);
        int nestsAvailable = Bank.count("Bird nest");
        if (nestsAvailable > 0) {
            Bank.setWithdrawMode(BankMode.ITEM);
            Bank.withdraw("Bird nest", Math.min(nestsAvailable, 27));
            Bank.withdraw("Pestle and mortar", 1);
            Bank.close();
        } else {
            if (Bank.count("Crushed nest") > 0) {
                Bank.setWithdrawMode(BankMode.NOTE);
                Bank.withdrawAll("Crushed nest");
                Bank.close();
                stateManager.addState(new SellItemsState(script, settings, "Crushed nest", Inventory.count("Crushed nest"), 7000));
                if(GrandExchange.isOpen())
                    GrandExchange.close();
            } else {
                stateManager.addState(new BuyItemsState(script, settings, "Bird nest", 270, 6900));
                if(GrandExchange.isOpen())
                    GrandExchange.close();
            }
        }
    }

    private void processNests() {
        if(Bank.isOpen())
            Bank.close();
        Sleep.sleepUntil(() -> !Bank.isOpen(), 1000);
        for (Item item : Inventory.all()) {
            if (item != null && item.getName().equalsIgnoreCase("Bird nest")) {
                item.useOn(Inventory.get("Pestle and mortar"));
                Sleep.sleep(25);
            }
            profit += 100;
        }
        stateManager.addState(new BankingState(script, settings, Map.of("Pestle and mortar", 1, "Bird nest", 27), null, false, false, "Pestle and mortar", "Bird nest")); // Bank everything after processing
    }

    @Override
    public void onPaint(Graphics g) {
        g.drawString("Profit: " + profit, 10, 290);
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }
}
