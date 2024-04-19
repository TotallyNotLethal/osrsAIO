package com.Anomaly.AIO.Helpers.State.Methods.GrandExchange;

import com.Anomaly.AIO.Helpers.State.State;
import com.Anomaly.AIO.Main.SettingsManager;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.grandexchange.LivePrices;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.items.Item;

import java.util.HashMap;
import java.util.Map;

public class SellItemsState implements State {
    private final AbstractScript script;
    private final SettingsManager settings;
    private String itemName;
    private int quantity;
    private int customSellPrice;
    private String itemBeingSold = "";
    private Integer itemsPrice = 0;
    private Integer itemAmount = 1;
    private Map<String, Integer> itemsToSell = new HashMap<>();
    private boolean itemsSold = false;

    public SellItemsState(AbstractScript script, SettingsManager settings, Map<String, Integer> itemsToSell) {
        this.script = script;
        this.settings = settings;
        this.itemsToSell = itemsToSell;
    }

    public SellItemsState(AbstractScript script, SettingsManager settings, String itemName, int quantity, int customSellPrice) {
        this.script = script;
        this.settings = settings;
        this.itemName = itemName;
        this.quantity = quantity;
        this.itemsToSell.put(itemName, quantity);
        this.customSellPrice = customSellPrice; // Store the custom price
    }

    @Override
    public int execute() {
        if (!GrandExchange.isOpen()) {
            NPCs.closest("Grand Exchange Clerk").interact("Exchange");
            Sleep.sleepUntil(GrandExchange::isOpen, 6000);
        }

        for (Map.Entry<String, Integer> entry : itemsToSell.entrySet()) {
            if(entry != null) {
                String itemName = entry.getKey();
                itemBeingSold = itemName;
                int quantity = entry.getValue();
                itemAmount = quantity;
                Item item = Inventory.get(itemName);

                if (item == null || quantity <= 0) {
                    Logger.log("Item not found or invalid quantity: " + itemName);
                    continue;
                }

                int pricePerItem = customSellPrice > 0 ? customSellPrice : (int) (LivePrices.get(item.getID()) * 0.95);
                itemsPrice = pricePerItem;

                if (GrandExchange.getFirstOpenSlot() == -1) {
                    Logger.log("No open slots in Grand Exchange.");
                    continue;
                }

                GrandExchange.sellItem(item.getID(), quantity, pricePerItem);
                boolean itemSold = Sleep.sleepUntil(GrandExchange::isReadyToCollect, 30000);

                if (itemSold) {
                    GrandExchange.collectToBank();
                    Logger.log("Successfully sold " + quantity + " of " + itemName);
                } else {
                    Logger.log("Failed to sell " + itemName);
                }
            }
        }

        GrandExchange.close();

        itemsSold = true;
        return 0;
    }

    public String getItemBeingSold() {
        return String.format("Selling %d %s at %dgp each (%d total)", itemAmount, itemBeingSold, itemsPrice, itemAmount*itemsPrice);
    }

    @Override
    public boolean isComplete() {
        return itemsSold;
    }
}
