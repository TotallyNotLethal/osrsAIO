package com.Anomaly.AIO.Helpers.GrandExchange;

import com.Anomaly.AIO.Helpers.State.Methods.BankingState;
import com.Anomaly.AIO.Main.Task;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.grandexchange.LivePrices;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.Player;

import java.util.Map;

public class BuyItems {
    private final Map<String, Integer> itemsToBuy;
    public boolean itemsBought;
    private final Player player;
    private final Task script;

    public BuyItems(Task task, Map<String, Integer> itemsToBuy) {
        this.itemsToBuy = itemsToBuy;
        itemsBought = execute();
        player = Players.getLocal();
        this.script = task;
    }

    public boolean execute() {
        if (!GrandExchange.isOpen()) {
            NPCs.closest("Grand Exchange Clerk").interact("Exchange");
            Sleep.sleepUntil(GrandExchange::isOpen, 6000, 600);
        }

        for (Map.Entry<String, Integer> item : itemsToBuy.entrySet()) {
            String itemName = item.getKey();
            int quantity = item.getValue();

            int pricePerItem = LivePrices.get(itemName);

            pricePerItem = (int)(pricePerItem * 1.21);

            if (!buyItem(itemName, quantity, pricePerItem)) {
                Logger.log("Failed to buy: " + itemName);
                return false;
            }
            Sleep.sleep(1000, 2000);
        }

        new BankingState(null, itemsToBuy, null, false);

        return true;
    }

    private boolean buyItem(String itemName, int quantity, int pricePerItem) {
        if (GrandExchange.getFirstOpenSlot() == -1) {
            Logger.log("No open slots in Grand Exchange.");
            return false;
        }

        GrandExchange.buyItem(itemName, quantity, pricePerItem);
        boolean itemBought = Sleep.sleepUntil(GrandExchange::isReadyToCollect, 30000);

        if (!itemBought) {
            Logger.log("Failed to buy " + itemName);
            return false;
        }
        GrandExchange.collectToBank();
        return true;
    }
}