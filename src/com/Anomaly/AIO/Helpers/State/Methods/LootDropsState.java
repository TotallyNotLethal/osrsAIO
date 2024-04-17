package com.Anomaly.AIO.Helpers.State.Methods;

import com.Anomaly.AIO.Helpers.State.State;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;

import java.util.List;
import java.util.stream.Collectors;

public class LootDropsState implements State {
    private final AbstractScript script;
    private final List<String> priorityItems;
    private final Player player;

    public LootDropsState(AbstractScript script, List<String> priorityItems) {
        this.script = script;
        this.priorityItems = priorityItems;
        this.player = Players.getLocal();
    }

    @Override
    public int execute() {
        GroundItem priorityItem = GroundItems.closest(item -> item != null && priorityItems != null && priorityItems.contains(item.getName()));
        if (priorityItem != null && priorityItem.canReach() && priorityItem.distance() < 9) {
            priorityItem.interact("Take");
            Logger.log("Picking up priority item: " + priorityItem.getName());
            Sleep.sleepUntil(() -> !priorityItem.exists(), 1000);
            return Calculations.random(400, 800);
        }

        GroundItem anyItem = GroundItems.closest(item -> item != null && item.canReach());
        if (anyItem != null && anyItem.canReach() && anyItem.distance() < 9) {
            anyItem.interact("Take");
            Logger.log("Picking up item: " + anyItem.getName());
            Sleep.sleepUntil(() -> !anyItem.exists(), 1000);
            return Calculations.random(400, 800);
        }

        return Calculations.random(100, 200);
    }

    @Override
    public boolean isComplete() {
        boolean noItems = true;
        List<GroundItem> items = GroundItems.all();
        for (GroundItem drop : items)
            if(drop.distance() < 8)
                noItems = false;
        return noItems;
    }
}