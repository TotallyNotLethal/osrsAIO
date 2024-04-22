package com.Anomaly.AIO.Tasks.MoneyMakers;

import com.Anomaly.AIO.Helpers.State.Methods.BankingState;
import com.Anomaly.AIO.Helpers.State.Methods.EquipItemsState;
import com.Anomaly.AIO.Main.SettingsManager;
import com.Anomaly.AIO.Main.Task;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankMode;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.grandexchange.LivePrices;
import org.dreambot.api.methods.magic.Magic;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.items.Item;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class HighAlchTask extends Task {
    private final AbstractScript script;
    private boolean isComplete = false;
    private int profit = 0;
    private Map<String, Integer> itemsToAlch = new HashMap<>();

    public HighAlchTask(AbstractScript script, SettingsManager settings) {
        this.script = script;
        prepareAlchList();
    }

    private void prepareAlchList() {
        if (!Bank.isOpen()) {
            Bank.open();
        }
        for (Item item : Bank.all()) {
            if (item != null && item.isTradable() && item.getHighAlchValue() > 0 && item.getHighAlchValue() > (item.getLivePrice() + LivePrices.get("Nature rune")) && !item.getName().contains(" rune") && !item.getName().contains("potion")  && !item.getName().contains("Coin") && !item.getName().contains("arrow") && !item.getName().contains("bolt")) {
                itemsToAlch.put(item.getName(), Bank.count(item.getName()));
            }
        }
        Bank.close();
        Logger.log("Items prepared for alching: " + itemsToAlch.size());
    }

    @Override
    public int execute() {
        if (itemsToAlch.isEmpty()) {
            isComplete = true;
            return 600;
        }

        if (!Equipment.contains("Staff of fire")) {
            if (!Bank.isOpen()) Bank.open();
            Bank.withdraw("Nature rune", Bank.count("Nature rune"));
            Bank.withdraw("Staff of fire", 1);
            Bank.close();
            new EquipItemsState(script, null).execute();
        }
        if (Inventory.onlyContains("Nature rune")) {
            withdrawItemsForAlching();
            return 600;
        }

        if (Magic.canCast(Normal.HIGH_LEVEL_ALCHEMY) && !Inventory.isEmpty()) {
            Magic.castSpell(Normal.HIGH_LEVEL_ALCHEMY);
            Sleep.sleep(600, 1200);
            Inventory.all().forEach(item -> {
                if (item != null && itemsToAlch.containsKey(item.getName())) {
                    int countBefore = Inventory.count(item.getName());
                    item.interact("Cast");
                    Sleep.sleep(3000, 3600);
                    profit += item.getHighAlchValue() - LivePrices.get("Nature rune");
                    Logger.log("Alched " + item.getName() + " for profit: " + (item.getHighAlchValue() - (LivePrices.get(item) + LivePrices.get("Nature rune"))) + "gp.");
                    int countAfter = Inventory.count(item.getName());
                    if (countAfter < countBefore) {
                        itemsToAlch.put(item.getName(), itemsToAlch.get(item.getName()) - (countBefore - countAfter));
                        if (itemsToAlch.get(item.getName()) <= 0) {
                            itemsToAlch.remove(item.getName());
                        }
                    }
                }
            });
        }
        return 300; // Loop delay
    }

    private void withdrawItemsForAlching() {
        if (!Bank.isOpen()) Bank.open();
        Bank.setWithdrawMode(BankMode.NOTE);
        for (Map.Entry<String, Integer> entry : itemsToAlch.entrySet()) {
            if(!Inventory.isFull()) {
                Bank.withdraw(entry.getKey(), entry.getValue());
                Sleep.sleep(50); // Short sleep between withdrawals
            }
        }
        Bank.close();
    }

    @Override
    public void onPaint(Graphics g) {
        g.drawString("Profit: " + profit, 10, 290);
        g.drawString("Items left: " + itemsToAlch.size(), 10, 305);
    }

    @Override
    public boolean isComplete() {
        return isComplete || itemsToAlch.isEmpty();
    }
}
