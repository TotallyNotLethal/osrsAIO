package com.Anomaly.AIO.Helpers.State.Methods;

import com.Anomaly.AIO.Helpers.State.State;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.grandexchange.GrandExchange;
import org.dreambot.api.methods.grandexchange.LivePrices;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LootDropsState implements State {
    private final AbstractScript script;
    private final List<String> priorityItems;
    private final Area lootArea;
    private final Player player;
    private int lootPrice = 0;

    public LootDropsState(AbstractScript script, Area lootArea, List<String> priorityItems) {
        this.script = script;
        this.priorityItems = priorityItems;
        this.lootArea = lootArea;
        this.player = Players.getLocal();
    }

    @Override
    public int execute() {
        if(!Inventory.isFull()) {
            GroundItem priorityItem = GroundItems.closest(item -> item != null && priorityItems != null && priorityItems.contains(item.getName()));
            if (priorityItem != null && priorityItem.canReach() && lootArea.contains(priorityItem)) {
                priorityItem.interact("Take");
                Sleep.sleepUntil(() -> !priorityItem.exists(), 2000);
                lootPrice += LivePrices.get(priorityItem.getItem()) * priorityItem.getAmount();
                return Calculations.random(600, 1200);
            }

            GroundItem anyItem = GroundItems.closest(item -> item != null && item.canReach());
            if (anyItem != null && anyItem.canReach() && lootArea.contains(anyItem)) {
                anyItem.interact("Take");
                Sleep.sleepUntil(() -> !anyItem.exists(), 2000);
                lootPrice += LivePrices.get(anyItem.getItem()) * anyItem.getAmount();
                return Calculations.random(600, 1200);
            }
        }
        return Calculations.random(100, 200);
    }

    public int getLootPrices() {
        return lootPrice;
    }

    @Override
    public boolean isComplete() {
        boolean noItems = true;
        List<GroundItem> items = GroundItems.all();
        for (GroundItem drop : items)
            if(lootArea.contains(drop))
                noItems = false;
        return noItems || Inventory.isFull();
    }
}
