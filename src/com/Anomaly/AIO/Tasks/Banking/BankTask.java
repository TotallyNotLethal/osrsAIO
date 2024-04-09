package com.Anomaly.AIO.Tasks.Banking;

import com.Anomaly.AIO.Main;
import com.Anomaly.AIO.Tasks.Player.EquipTask;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.Item;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BankTask implements Main.Task {
    private final BankLocation bankLocation;
    private final List<String> itemsToKeep;
    private final Map<String, Integer> itemsToWithdraw;
    private final Map<String, Integer> optionalItemsToWithdraw;
    private final Player player;

    public BankTask(Main.Task task, Map<String, Integer> itemsToWithdraw, Map<String, Integer> optionalItemsToWithdraw, String... itemsToKeep) {
        this.bankLocation = Bank.getClosestBankLocation();
        this.itemsToKeep = Arrays.asList(itemsToKeep);
        this.itemsToWithdraw = itemsToWithdraw;
        this.optionalItemsToWithdraw = optionalItemsToWithdraw;
        this.player = Players.getLocal();
        this.execute();
        new EquipTask().execute();
        task.execute();
    }

    @Override
    public int execute() {
        if (!bankLocation.getArea(4).contains(player)) {
            Walking.walk(bankLocation.getArea(4).getRandomTile());
            Sleep.sleepUntil(() -> bankLocation.getArea(4).contains(player), Calculations.random(5000, 8000));
        }

        if (!Bank.isOpen()) {
            Bank.open();
            Sleep.sleepUntil(Bank::isOpen, 5000);
        }
        if (Bank.isOpen()) {
            if (Inventory.all().size() > 0) {
                List<Item> itemsToDeposit = Inventory.all().stream()
                        .filter(item -> item != null && !itemsToKeep.contains(item.getName()) && !itemsToWithdraw.containsKey(item.getName()))
                        .toList();

                for (Item item : itemsToDeposit) {
                    Logger.log("Depositing item:" + item);
                    Bank.deposit(item.getName(), item.getAmount());
                    Sleep.sleepUntil(() -> !Inventory.contains(item.getName()), 3000);
                }
            }

            withdrawItems(itemsToWithdraw, true);
            withdrawItems(optionalItemsToWithdraw, false);

            Bank.close();
            Sleep.sleepUntil(() -> !Bank.isOpen(), 3000);
        }

        return Calculations.random(200, 300);
    }

    private void withdrawItems(Map<String, Integer> items, boolean isRequired) {
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String itemToWithdraw = entry.getKey();
            int totalAmountNeeded = entry.getValue();
            int amountInInventory = Inventory.count(itemToWithdraw);
            int amountToWithdraw = totalAmountNeeded - amountInInventory;

            if (amountToWithdraw > 0) {
                if (Bank.contains(itemToWithdraw)) {
                    Logger.log(String.format("Withdrawing %d %s", amountToWithdraw, itemToWithdraw));
                    Bank.withdraw(itemToWithdraw, amountToWithdraw);
                    Sleep.sleepUntil(() -> Inventory.count(itemToWithdraw) >= totalAmountNeeded, 3000);
                } else {
                    Logger.log("Bank does not have enough of (or is missing): " + itemToWithdraw);
                    if (isRequired) {
                        // Fail the task for required items
                        return;
                    }
                }
            }
        }
    }
}