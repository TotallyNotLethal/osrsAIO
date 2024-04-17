package com.Anomaly.AIO.Helpers.State.Methods;

import com.Anomaly.AIO.Helpers.Locations.Teleports.Accessories.TeleportAccessory;
import com.Anomaly.AIO.Helpers.Locations.Teleports.Tablets.TeleportTablet;
import com.Anomaly.AIO.Helpers.Locations.Teleports.TeleportLocation;
import com.Anomaly.AIO.Helpers.State.State;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.wrappers.widgets.WidgetChild;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TeleportToState implements State {
    private final AbstractScript script;
    private final Area destinationArea;
    private Player player;
    private Area closestArea;
    private boolean hadError;

    public TeleportToState(AbstractScript script, Area destinationArea) {
        this.script = script;
        this.destinationArea = destinationArea;
        this.player = Players.getLocal();
    }
    TeleportLocation closestTeleportLocation;
    @Override
    public int execute() {
        try {
            Tile destinationCenter = destinationArea.getCenter();
            double shortestDistance = Double.MAX_VALUE;
             closestTeleportLocation = null;
            String teleportType = null;
            TeleportTablet closestTablet = null;
            TeleportAccessory closestAccessory = null;

            for (TeleportTablet teleport : TeleportTablet.values()) {
                Area teleportArea = teleport.getLocation().getArea();
                if (teleportArea.contains(destinationCenter)) {
                    closestTablet = teleport;
                    closestTeleportLocation = teleport.getLocation();
                    teleportType = "Tablet";
                    break;
                }
                double distance = destinationCenter.distance(teleportArea.getCenter());
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    closestTablet = teleport;
                    closestTeleportLocation = teleport.getLocation();
                    teleportType = "Tablet";
                }
            }

            for (TeleportAccessory teleport : TeleportAccessory.values()) {
                Area teleportArea = teleport.getLocation().getArea();
                if (teleportArea.contains(destinationCenter)) {
                    closestAccessory = teleport;
                    closestTeleportLocation = teleport.getLocation();
                    teleportType = "Accessory";
                    break;
                }
                double distance = destinationCenter.distance(teleportArea.getCenter());
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    closestAccessory = teleport;
                    closestTeleportLocation = teleport.getLocation();
                    teleportType = "Accessory";
                }
            }

            if (closestTeleportLocation != null && !closestTeleportLocation.getArea().contains(player)) {
                Logger.log("Teleporting using " + (teleportType.equals("Tablet") ? "tablet: " : "accessory: ") + closestTeleportLocation.getName());
                boolean hasTeleportItem = false;

                if(teleportType.equals("Tablet") && Inventory.contains(closestTablet.getLocation().getName()))
                    hasTeleportItem = true;
                else if(teleportType.equals("Accessory") && Inventory.contains(closestAccessory.getAccessoryName()))
                    hasTeleportItem = true;

                if(hasTeleportItem)
                {
                    if(teleportType.equals("Tablet"))
                    {
                        Inventory.get(closestTablet.getLocation().getName()).interact("Break");
                        TeleportLocation finalClosestTeleportLocation = closestTeleportLocation;
                        Sleep.sleepUntil(() -> finalClosestTeleportLocation.getArea().contains(player), 5000);
                        return Calculations.random(1000,2000);
                    }

                    /*else if(teleportType.equals("Accessory"))
                    {
                        TeleportLocation finalClosestTeleportLocation = closestTeleportLocation;
                        Sleep.sleepUntil(() -> finalClosestTeleportLocation.getArea().contains(player), 5000);
                    }*/

                }

                if(!Bank.isOpen())
                    Bank.open();
                Sleep.sleepUntil(Bank::isOpen, 1000);

                if(Bank.isOpen()) {
                    if (teleportType.equals("Tablet")) {
                        Logger.log("Withdrawing: " + closestTablet.getLocation().getName());
                        Bank.withdraw(closestTablet.getLocation().getName(),1);
                        Bank.close();
                        Inventory.get(closestTablet.getLocation().getName()).interact("Break");
                    } else {

                        Item accessoryItem = null;

                        for (Item item : Bank.all()) {
                            if (item != null && item.isValid() && !item.isPlaceholder() && item.getName().contains(closestAccessory.getAccessoryName())) {
                                accessoryItem = item;
                                break;
                            }
                        }
                        assert accessoryItem != null;
                        Logger.log("Withdrawing: " + accessoryItem.getName());
                        Bank.withdraw(accessoryItem.getName(), 1);
                        Logger.log("Withdrew " + accessoryItem.getName());
                        Bank.close();

                        Inventory.get(accessoryItem.getName()).interact("Rub");
                        Sleep.sleepUntil(Dialogues::inDialogue, 1000);
                        if (accessoryItem.getName().contains("Skills") || accessoryItem.getName().contains("Xeric")) {
                            WidgetChild widgetChild = Widgets.getWidgetChild(187, 3);
                            if (widgetChild != null) {
                                TeleportAccessory finalClosestAccessory = closestAccessory;
                                Optional<WidgetChild> option = Arrays.stream(widgetChild.getChildren())
                                        .filter(child -> child != null && child.getText().contains(finalClosestAccessory.getLocation().getName()))
                                        .findFirst();

                                if (option.isPresent()) {
                                    option.get().interact();
                                    Logger.log("Clicked on the option for " + closestAccessory.getLocation().getName());
                                } else {
                                    Logger.log("No option found matching " + closestAccessory.getLocation().getName());
                                }
                            } else {
                                Logger.log("Skills Widget not found or no children available.");
                            }
                        }else {
                            Sleep.sleepUntil(Dialogues::inDialogue, 3000);
                            if (Dialogues.inDialogue()) {
                                Logger.log(Dialogues.getOptions());
                                Logger.log("Looking for: " + closestAccessory.getLocation().getName());
                                Dialogues.chooseFirstOptionContaining(closestAccessory.getLocation().getName().substring(0,4));
                            }
                            Sleep.sleep(5000);
                        }
                    }
                }

                TeleportLocation finalClosestTeleportLocation = closestTeleportLocation;
                Sleep.sleepUntil(() -> finalClosestTeleportLocation.getArea().contains(player), 5000);
            } else {
                Logger.log("No suitable teleport method found or already at location.");
            }
        }catch(Exception ex) {
            Logger.log("Error teleporting, walking instead.. lame");
            hadError = true;
            return Calculations.random(1000, 2000);
        }

        return Calculations.random(1000, 2000);
    }

    @Override
    public boolean isComplete() {
        return closestTeleportLocation.getArea().getTile().getArea(20).contains(player) || hadError;
    }
}
