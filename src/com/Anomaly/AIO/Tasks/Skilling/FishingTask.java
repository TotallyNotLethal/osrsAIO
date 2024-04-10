package com.Anomaly.AIO.Tasks.Skilling;

import com.Anomaly.AIO.Helpers.Items.EquipmentSets;
import com.Anomaly.AIO.Tasks.Banking.BankTask;
import com.Anomaly.AIO.Main;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;

import java.util.HashMap;
import java.util.Map;

public class FishingTask implements Main.Task {
    private final AbstractScript script;
    private final Area fishingArea;
    private final int fishingSpotId;
    private final Map<String, Integer> requiredItems;
    private final Main mainScript = new Main();

    public FishingTask(AbstractScript script, String method, String location) {
        this.script = script;
        requiredItems = new HashMap<>();
        Map<String, Integer> optionalItems;

        switch (method) {
            case "Shrimps" -> {
                fishingArea = new Area(3243, 3150, 3253, 3140);
                fishingSpotId = 1526;
                requiredItems.put("Small fishing net", 1);
                optionalItems = EquipmentSets.GRACEFUL.getItems();
                //optionalItems.put("Coins", 500); <- Example how to add items also
            }
            case "Trout", "Salmon" -> {
                fishingArea = new Area(3100, 3425, 3107, 3435);
                fishingSpotId = 1527;
                requiredItems.put("Fly fishing rod", 1);
                requiredItems.put("Feather", 5000);
                optionalItems = EquipmentSets.GRACEFUL.getItems();
            }
            default -> throw new IllegalArgumentException("Invalid fishing method");
        }
        new BankTask(this, requiredItems, optionalItems);
    }

    @Override
    public int execute() {
        for (String item : requiredItems.keySet()) {
                if (!Inventory.contains(item)) {
                    script.log("Missing required item: " + item);
                    return -1;
            }
        }

        if (!fishingArea.contains(Players.getLocal())) {
            Walking.walk(fishingArea.getRandomTile());
            return Calculations.random(4000, 7000);
        }
        NPC fishingSpot = NPCs.closest(fishingSpotId);
        if (fishingSpot != null && fishingSpot.interact("Fish")) {
            Sleep.sleepUntil(() -> !Players.getLocal().isAnimating(), 15000);
        }
        mainScript.taskRunning = false;
        return Calculations.random(1000, 2000);
    }
}
