package com.Anomaly.AIO.Helpers.State.Methods.GrandExchange;

import com.Anomaly.AIO.Helpers.State.State;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.grandexchange.LivePrices;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;

import java.util.Map;

public class BuyItemsState implements State {
    private final AbstractScript script;
    private final Map<String, Integer> itemsToBuy;
    private boolean itemsBought = false;

    public BuyItemsState(AbstractScript script, Map<String, Integer> itemsToBuy) {
        this.script = script;
        this.itemsToBuy = itemsToBuy;
    }

    @Override
    public int execute() {
        if (!GrandExchange.isOpen()) {
            NPCs.closest("Grand Exchange Clerk").interact("Exchange");
            Sleep.sleepUntil(GrandExchange::isOpen, 6000);
        }

        for (Map.Entry<String, Integer> item : itemsToBuy.entrySet()) {
            if (!buyItem(item.getKey(), item.getValue())) {
                Logger.log("Failed to buy: " + item.getKey());
                return 1000;
            }
        }

        itemsBought = true;
        return 0;
    }

    private boolean buyItem(String itemName, int quantity) {
        int pricePerItem = (int)(LivePrices.get(itemName) * 1.21);  // 21% above market price

        if (GrandExchange.getFirstOpenSlot() == -1) {
            Logger.log("No open slots in Grand Exchange.");
            return false;
        }

        GrandExchange.buyItem(itemName, quantity, pricePerItem);
        boolean itemBought = Sleep.sleepUntil(GrandExchange::isReadyToCollect, 30000);

        if (itemBought) {
            GrandExchange.collectToBank();
        } else {
            Logger.log("Failed to buy " + itemName);
        }
        return itemBought;
    }

    @Override
    public boolean isComplete() {
        return itemsBought;
    }
}
