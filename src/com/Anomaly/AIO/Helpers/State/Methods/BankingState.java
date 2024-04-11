package com.Anomaly.AIO.Helpers.State.Methods;

import com.Anomaly.AIO.Helpers.State.State;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
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
    private final List<String> itemsToKeep;
    private final Map<String, Integer> itemsToWithdraw;
    private final Map<String, Integer> optionalItemsToWithdraw;
    Map<String, Integer> itemsToBuy;
    private final Player player;
    private boolean isComplete = false;

    public BankingState(AbstractScript script, Map<String, Integer> itemsToWithdraw,
                        Map<String, Integer> optionalItemsToWithdraw, String... itemsToKeep) {
        this.script = script;
        this.bankLocation = Bank.getClosestBankLocation();
        this.itemsToKeep = Arrays.asList(itemsToKeep);
        this.itemsToWithdraw = itemsToWithdraw;
        this.optionalItemsToWithdraw = optionalItemsToWithdraw;
        this.player = Players.getLocal();
    }

    @Override
    public int execute() {
        if (!bankLocation.getArea(5).contains(player)) {
            Walking.walk(bankLocation.getArea(5).getRandomTile());
            return Calculations.random(2000, 3000);
        }

        if (!Bank.isOpen()) {
            Bank.open();
            return Calculations.random(500, 1000);
        }

        depositItems();
        withdrawItems(itemsToWithdraw, true);
        withdrawItems(optionalItemsToWithdraw, false);
        Bank.close();

        isComplete = true;
        return Calculations.random(200, 300);
    }

    private void depositItems() {
        if (Inventory.all().size() > 0) {
            List<Item> itemsToDeposit = Inventory.all().stream()
                    .filter(item -> item != null && !itemsToKeep.contains(item.getName()) && !itemsToWithdraw.containsKey(item.getName()))
                    .toList();

            for (Item item : itemsToDeposit) {
                Logger.log("Depositing item:" + item);
                Bank.deposit(item.getName(), item.getAmount());
                Sleep.sleepUntil(() -> !Inventory.contains(item.getName()), Calculations.random(100,250));
            }
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
                }
            }
        }
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }
}