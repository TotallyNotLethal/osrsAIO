package com.Anomaly.AIO.Tasks.Skilling;

import com.Anomaly.AIO.Main;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.Player;

public class FiremakingTask implements Main.Task {
    private final AbstractScript script;
    private final Area firemakingArea = new Area(3205, 3224, 3206, 3214);;
    private final String[] requiredItems;
    private final Player player;

    public FiremakingTask(AbstractScript script, String method, String location) {
        this.script = script;
        player = Players.getLocal();

        switch (method) {
            case "Logs" -> {
                requiredItems = new String[]{"Tinderbox", "Logs"};
            }
            case "Oak Logs" -> {
                requiredItems = new String[]{"Tinderbox", "Oak Logs"};
            }
            case "Willow Logs" -> {
                requiredItems = new String[]{"Tinderbox", "Willow Logs"};
            }
            default -> throw new IllegalArgumentException("Invalid fishing method");
        }
    }

    @Override
    public int execute() {
        for (String item : requiredItems) {
            if (!Inventory.contains(item)) {
                script.log("Missing required item: " + item);
                return -1;
            }
        }

        if (!firemakingArea.contains(Players.getLocal())) {
            Walking.walk(firemakingArea.getRandomTile());
            return Calculations.random(4000, 7000);
        }
        //assert GameObjects.closest("Fire") != null;
        //if (player.getTile() != GameObjects.closest("Fire").getTile()) {
            Inventory.get("Tinderbox").useOn(requiredItems[1]);
            Sleep.sleepUntil(() -> !Players.getLocal().isAnimating(), 15000);
        //}

        return Calculations.random(1000, 2000);
    }
}
