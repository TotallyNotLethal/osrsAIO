package com.Anomaly.AIO.Tasks.Skilling;

import com.Anomaly.AIO.Main;
import com.Anomaly.AIO.Task;
import org.dreambot.api.ClientSettings;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.listener.ChatListener;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.Item;

import java.util.HashMap;
import java.util.Map;

public class FletchingTask implements ChatListener, Task {

    private final Map<String, Integer> requiredItems;
    private Area bankArea;

    public FletchingTask(AbstractScript script, String method, String location) {
        Player player = Players.getLocal();
        requiredItems = new HashMap<>();

        switch (method) {
            case "Shortbow" -> {
                requiredItems.put("Knife", 1);
                requiredItems.put("Logs", 27);
            }
            case "Longbow" -> {
                requiredItems.put("Knife", 1);
                requiredItems.put("Oak logs", 27);
            }
            case "Crossbow" -> {
                requiredItems.put("Knife", 1);
                requiredItems.put("Yew logs", 27);
            }
            default -> throw new IllegalArgumentException("Invalid fletching method");
        }

        switch(location) {
            case "Varrock" -> bankArea = new Area(new Tile(3250, 3420, 0), new Tile(3257, 3419, 0));
            case "Falador" -> bankArea = new Area(new Tile(2946, 3368, 0), new Tile(2943, 3369, 0));
            case "GrandExchange" -> bankArea = new Area(new Tile(3159, 3493, 0), new Tile(3170, 3484, 0));
            default -> bankArea = new Area(new Tile(0, 0, 0), new Tile(0, 0, 0));
        }
    }

    public int execute() {
        for (String item : requiredItems.keySet()) {
            if (!Inventory.contains(item)) {
                Logger.log("Missing required item: " + item);
                return -1;
            }
        }
        if (!ClientSettings.areRoofsHidden()) ClientSettings.toggleRoofs(false);
        ClientSettings.closeSettingsInterface();

        return Calculations.random(200, 700);
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    private void fletchBows() {
        if(Dialogues.canContinue()) Dialogues.spaceToContinue();

        if (Inventory.contains(requiredItems.keySet().toArray(new String[]{}))) {
            Item knife = Inventory.get("Knife");
            Item logs = Inventory.get("Logs", "Oak logs", "Yew logs");

            if (knife != null && logs != null) {
                if(!Inventory.isItemSelected() && !Players.getLocal().isMoving()) {
                    Inventory.interact(knife,"use");
                    Sleep.sleepUntil(Inventory::isItemSelected, 850);
                }

                if(Inventory.isItemSelected() && !Players.getLocal().isAnimating() && !Players.getLocal().isMoving()) {
                    Inventory.interact(logs, "Use");
                    Sleep.sleep(Calculations.random(850, 1150));
                    Sleep.sleepUntil(() -> !Players.getLocal().isAnimating() && Inventory.isItemSelected(), 15000);
                }
            }
        }
    }
}