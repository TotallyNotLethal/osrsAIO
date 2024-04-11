package com.Anomaly.AIO.Tasks.Player;

import com.Anomaly.AIO.Main;
import com.Anomaly.AIO.Task;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.items.Item;

public class EquipTask implements Task {
    public EquipTask() {
        execute();
    }

    @Override
    public int execute() {
        for (Item item : Inventory.all()) {
            if (item != null && canEquip(item)) {
                item.interact(item.getActions()[0]);
                Sleep.sleepUntil(() -> !Players.getLocal().isMoving() && !Players.getLocal().isAnimating(), 5000, 600);
            }
        }
        return Calculations.random(200, 300);
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    private boolean canEquip(Item item) {
        for (String action : item.getActions()) {
            if (action != null && (action.equalsIgnoreCase("Wield") || action.equalsIgnoreCase("Wear"))) {
                return true;
            }
        }
        return false;
    }
}