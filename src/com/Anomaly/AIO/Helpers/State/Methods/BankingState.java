package com.Anomaly.AIO.Helpers.State.Methods;

import com.Anomaly.AIO.Helpers.State.Methods.GrandExchange.BuyItemsState;
import com.Anomaly.AIO.Helpers.State.State;
import com.Anomaly.AIO.Main.SettingsManager;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.depositbox.DepositBox;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.Item;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankingState implements State {
    private final AbstractScript script;
    private final BankLocation bankLocation;
    private final SettingsManager settings;
    public boolean useDepositBox = false;
    private final List<String> itemsToKeep;
    private final Map<String, Integer> itemsToWithdraw;
    private final Map<String, Integer> optionalItemsToWithdraw;
    Map<String, Integer> itemsToBuy;
    private final Player player;
    private boolean isComplete = false;
    private boolean justStarted = false;

    public BankingState(AbstractScript script, SettingsManager settings, Map<String, Integer> itemsToWithdraw,
                        Map<String, Integer> optionalItemsToWithdraw, boolean useDepositBox, boolean justStarted, String... itemsToKeep) {
        this.script = script;
        this.settings = settings;
        this.bankLocation = Bank.getClosestBankLocation();
        this.itemsToKeep = Arrays.asList(itemsToKeep);
        this.itemsToWithdraw = itemsToWithdraw;
        this.optionalItemsToWithdraw = optionalItemsToWithdraw;
        this.useDepositBox = useDepositBox;
        this.justStarted = justStarted;
        this.player = Players.getLocal();
    }

    @Override
    public int execute() {
        if (useDepositBox) {
            if (!DepositBox.isOpen()) {
                DepositBox.open();
                return Calculations.random(500, 1000);
            } else {
                depositItemsToDepositBox();
                DepositBox.close();
                isComplete = true;
                return Calculations.random(200, 300);
            }
        } else {
            if (!bankLocation.getArea(5).contains(player)) {
                Walking.walk(bankLocation.getArea(5).getRandomTile());
                return Calculations.random(2000, 3000);
            }

            if (!Bank.isOpen()) {
                Bank.open();
                return Calculations.random(500, 1000);
            }

            if(justStarted) {
                justStarted = false;
                Bank.depositAllItems();
                Bank.depositAllEquipment();
            }
            else
                depositItems();
            withdrawItems(itemsToWithdraw, true);
            Sleep.sleepUntil(() -> Inventory.containsAll(itemsToKeep), 1000);
            if(optionalItemsToWithdraw != null)
                withdrawItems(optionalItemsToWithdraw, false);

            if(itemsToBuy.size() > 0) {
                isComplete = true;
                //script.stop();
            }
            Bank.close();
        }

        isComplete = true;
        return Calculations.random(200, 300);
    }

    private void depositItems() {
        Map<String, Integer> allRequiredItems = new HashMap<>(itemsToWithdraw);
        if(optionalItemsToWithdraw != null)
            allRequiredItems.putAll(optionalItemsToWithdraw);

        List<Item> itemsToDeposit = Inventory.all().stream()
                .filter(item -> item != null && !allRequiredItems.containsKey(item.getName()))
                .toList();

        for (Item item : itemsToDeposit) {
            Logger.log("Depositing item(s): " + item);
            Bank.depositAll(item.getName());
            Sleep.sleepUntil(() -> !Inventory.contains(item.getName()), Calculations.random(100, 250));
        }
    }

    private void withdrawItems(Map<String, Integer> items, boolean isRequired) {
        itemsToBuy = new HashMap<>();
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String itemToWithdraw = entry.getKey();
            int totalAmountNeeded = entry.getValue();
            int amountInInventory = Inventory.count(itemToWithdraw);
            int amountToWithdraw = totalAmountNeeded - amountInInventory;

            if (amountToWithdraw > 0) {
                if (Bank.contains(itemToWithdraw)) {
                    Logger.log(String.format("Withdrawing %d %s", amountToWithdraw, itemToWithdraw));
                    Bank.withdraw(itemToWithdraw, amountToWithdraw);
                    Sleep.sleepUntil(() -> Inventory.count(itemToWithdraw) >= totalAmountNeeded, 1000);
                } else {
                    Logger.log("Bank does not have enough of (or is missing): " + itemToWithdraw);
                    if (isRequired) {
                        itemsToBuy.put(itemToWithdraw, totalAmountNeeded);
                    }
                    if(settings.isBuyItemsEnabled())
                        new BuyItemsState(script, settings, itemsToBuy).execute();
                }
            }
        }
    }

    private void depositItemsToDepositBox() {
        Map<String, Integer> allRequiredItems = new HashMap<>(itemsToWithdraw);
        allRequiredItems.putAll(optionalItemsToWithdraw);

        List<Item> itemsToDeposit = Inventory.all().stream()
                .filter(item -> item != null && !allRequiredItems.containsKey(item.getName()))
                .toList();

        for (Item item : itemsToDeposit) {
            Logger.log("Depositing item(s) to deposit box: " + item);
            DepositBox.depositAll(item.getName());
            Sleep.sleepUntil(() -> !Inventory.contains(item.getName()), Calculations.random(100, 250));
        }
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }
}
