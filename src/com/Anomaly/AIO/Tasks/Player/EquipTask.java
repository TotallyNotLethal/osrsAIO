package com.Anomaly.AIO.Tasks.Player;

import com.Anomaly.AIO.Main;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.items.Item;

public class EquipTask implements Main.Task {
    public EquipTask() {
        execute();
    }

    @Override
    public int execute() {
        // Iterate through inventory items
        for (Item item : Inventory.all()) {
            if (item != null && canEquip(item)) {
                item.interact(item.getActions()[0]); // Assuming the first action is "Wield" or "Wear"
                Sleep.sleepUntil(() -> !Players.getLocal().isMoving() && !Players.getLocal().isAnimating(), 5000, 600);
            }
        }
        return Calculations.random(200, 300); // Adjust timing as needed
    }

    private boolean canEquip(Item item) {
        // Check if the item has "Wield" or "Wear" options
        for (String action : item.getActions()) {
            if (action != null && (action.equalsIgnoreCase("Wield") || action.equalsIgnoreCase("Wear"))) {
                return true;
            }
        }
        return false;
    }
}