package com.Anomaly.AIO.Tasks.Skilling;

import com.Anomaly.AIO.Helpers.Locations.Firemaking.FiremakingLocations;
import com.Anomaly.AIO.Main.Task;
import org.dreambot.api.ClientSettings;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.listener.ChatListener;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.message.Message;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class FiremakingTask extends Task implements ChatListener {

    private final Map<String, Integer> requiredItems;

    private static Tile currentFmStartTile;


    public FiremakingTask(AbstractScript script, String method, String location, int duration, int stopLevel) {
        Player player = Players.getLocal();
        requiredItems = new HashMap<>();


        switch (method) {
            case "Logs" -> {
                requiredItems.put("Tinderbox", 1);
                requiredItems.put("Logs", 27);
            }
            case "Oak Logs" -> {
                requiredItems.put("Tinderbox", 1);
                requiredItems.put("Oak logs", 27);
            }
            case "Willow Logs" -> {
                requiredItems.put("Tinderbox", 1);
                requiredItems.put("Willow logs", 27);
            }
            default -> throw new IllegalArgumentException("Invalid firemaking method");
        }
    }

    public boolean isFireUnderPlayer() {
        GameObject fire = GameObjects.closest("Fire");
        if (fire != null && fire.getTile().equals(Players.getLocal().getTile())
                && !Players.getLocal().isAnimating() && !Players.getLocal().isMoving()) {
            Logger.log("Fire is under player.");
            Sleep.sleep(1000);
            if (fire != null && fire.getTile().equals(Players.getLocal().getTile())
                    && !Players.getLocal().isAnimating() && !Players.getLocal().isMoving()) {
                Logger.log("Fire is under player.");
                currentFmStartTile = null;
            }
            return true;
        } else {
            Logger.log("No fire under player.");
            return false;
        }
    }

    private static Tile selectFmStartTile() {
        if (currentFmStartTile == null || Players.getLocal().getY() != currentFmStartTile.getY() && !Inventory.isFull()) {
            int tileChoice = Calculations.random(1, 5);
            switch (tileChoice) {
                case 1 -> currentFmStartTile = FiremakingLocations.fmstart1;
                case 2 -> currentFmStartTile = FiremakingLocations.fmstart2;
                case 3 -> currentFmStartTile = FiremakingLocations.fmstart3;
                case 4 -> currentFmStartTile = FiremakingLocations.fmstart4;
            }
        }
        return currentFmStartTile;
    }

    public void onMessage(Message message) {
        if (message.getMessage().contains("You can't light")) {
            currentFmStartTile = null;
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
        if (!FiremakingLocations.FIRE_SPACE.contains(Players.getLocal())) {
            Walking.walk(FiremakingLocations.FIRE_SPACE.getCenter());
            Sleep.sleepUntil(() -> FiremakingLocations.FIRE_SPACE.contains(Players.getLocal()), 5000);
        }

        /*if (!hasLogs() || !hasTinderbox()) {
            if (!Bank.isOpen() && FiremakingLocations.FIRE_SPACE.contains(Players.getLocal())) {
                Bank.open();
            }
            */

        if (Bank.isOpen()) {
            currentFmStartTile = null;
        } else
            burnLogs();
        return Calculations.random(200, 700);
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    private void burnLogs() {
        Tile fmTile = selectFmStartTile();
        if(Dialogues.canContinue()) Dialogues.spaceToContinue();
        if (fmTile.getY() != Players.getLocal().getY()) {
            Walking.walk(fmTile);
            Sleep.sleepUntil(() -> fmTile.distance() == 0, 5000);
        }
        if(isFireUnderPlayer()) {
            Walking.walk(fmTile);
            Sleep.sleepUntil(() -> fmTile.getY() == Players.getLocal().getY(), 5000);

        }
        if (Inventory.contains(requiredItems) && !isFireUnderPlayer()) {
            Item tinderbox = Inventory.get("Tinderbox");
            Item logs = Inventory.get("Logs", "Oak logs", "Willow logs", "Maple logs");

            if (tinderbox != null && logs != null) {
                if(!Inventory.isItemSelected() && !Players.getLocal().isMoving()) {
                    Inventory.interact(tinderbox,"use");
                    Sleep.sleepUntil(Inventory::isItemSelected, 850);
                }
                if(!Inventory.isItemSelected()) {
                    Inventory.interact(tinderbox,"use");
                    Sleep.sleepUntil(Inventory::isItemSelected, 850);
                }
                if(Inventory.isItemSelected() && !Players.getLocal().isAnimating() && !Players.getLocal().isMoving()) {
                    Inventory.interact(logs, "Use");
                    Sleep.sleep(Calculations.random(850, 1150));
                    Sleep.sleepUntil(() -> !Players.getLocal().isAnimating() && Inventory.isItemSelected(), 15000); //possibly remove ifitemselected

                }

            }
        }
    }

}
