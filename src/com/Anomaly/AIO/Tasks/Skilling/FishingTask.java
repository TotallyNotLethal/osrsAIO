package com.Anomaly.AIO.Tasks.Skilling;

import com.Anomaly.AIO.Helpers.Interactions.FishingInteractions;
import com.Anomaly.AIO.Helpers.Items.EquipmentSets;
import com.Anomaly.AIO.Helpers.Locations.Fishing.FishingLocations;
import com.Anomaly.AIO.Helpers.Locations.Spot;
import com.Anomaly.AIO.Helpers.Locations.Location;
import com.Anomaly.AIO.Helpers.Requirements.Fishing.FishType;
import com.Anomaly.AIO.Helpers.Requirements.Fishing.FishingEquipment;
import com.Anomaly.AIO.Helpers.Requirements.Fishing.FishingRequirements;
import com.Anomaly.AIO.Helpers.State.Methods.BankingState;
import com.Anomaly.AIO.Helpers.State.Methods.EquipItemsState;
import com.Anomaly.AIO.Helpers.State.Methods.TeleportToState;
import com.Anomaly.AIO.Helpers.State.Methods.WalkToState;
import com.Anomaly.AIO.Helpers.State.StateManager;
import com.Anomaly.AIO.Main.SettingsManager;
import com.Anomaly.AIO.Main.Task;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FishingTask extends Task {
    private final AbstractScript script;
    private final SettingsManager settings;
    String method, interaction;
    Location location;
    FishType fishType;
    private final Player player;
    private Area fishingArea;
    private Area bankingArea;
    private boolean useDepositBox = false;
    //private int fishingSpotId;
    private final Map<String, Integer> requiredItems;

    private final Map<String, Integer> optionalItems;
    private final StateManager stateManager;

    public FishingTask(AbstractScript script, SettingsManager settings, String method, String location, int duration, int stopLevel) {
        this.script = script;
        this.method = method;
        this.settings = settings;
        this.location = Location.byDisplayName(location);
        this.fishType = FishType.byDisplayName(method);
        this.stateManager = new StateManager(script);
        requiredItems = new HashMap<>();
        optionalItems = new HashMap<>();
        this.player = Players.getLocal();

        setupFishingTask(fishType);
        prepareStates();
    }

    private void setupFishingTask(FishType fishType) {

        FishingEquipment[] requiredEquipment = FishingRequirements.fishToEquipmentMap.get(fishType);
        if (requiredEquipment != null) {
            for (FishingEquipment equipment : requiredEquipment) {
                requiredItems.put(equipment.getDisplayName(), equipment.getAmount());
            }
        }
        Spot spot = FishingLocations.getFishingSpot(location, fishType);
        this.fishingArea = Objects.requireNonNull(spot).getArea();
        bankingArea = Bank.getClosestBankLocation().getArea(2);
        //optionalItems.putAll(EquipmentSets.GRACEFUL.getItems());

        switch (location) {
            case KARAMJA -> {
                bankingArea = new Area(3044, 3237, 3050, 3233);
                useDepositBox = true;
                requiredItems.put("Coins", 600);
            }
            //default -> throw new IllegalArgumentException("Invalid fishing location");
        }
    }

    private boolean shouldUseDepositBox() {
        for (Map.Entry<String, Integer> entry : requiredItems.entrySet()) {
            String itemName = entry.getKey();
            int requiredQuantity = entry.getValue();
            if (Inventory.count(itemName) < requiredQuantity) {
                return false;
            }
        }
        return true;
    }

    private void prepareStates() {
        //StateUtil.goToBank(requiredItems, optionalItems);
        if (!hasAllRequiredItems(requiredItems) || Inventory.isFull()) {
            if(!Bank.getClosestBankLocation().getArea(4).contains(player))
                stateManager.addState(new WalkToState(script, bankingArea));

            stateManager.addState(new BankingState(script, settings, requiredItems, optionalItems, useDepositBox && shouldUseDepositBox(), true));

            stateManager.addState(new EquipItemsState(script, null));
        }
        stateManager.addState(new TeleportToState(script, fishingArea));
        stateManager.addState(new WalkToState(script, fishingArea));

        interaction = FishingInteractions.getAction(Objects.requireNonNull(FishType.byDisplayName(method)));
        //stateManager.addState(new FishingState(script, fishingArea, fishingSpotId, interaction));
    }

    private boolean hasAllRequiredItems(Map<String, Integer> requiredItems) {
        for (Map.Entry<String, Integer> entry : requiredItems.entrySet()) {
            String itemName = entry.getKey();
            int requiredQuantity = entry.getValue();
            if (Inventory.count(itemName) < requiredQuantity) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int execute() {
        if (!stateManager.isComplete()) {
            stateManager.executeCurrentState();
        }
        else {

            for (String item : requiredItems.keySet()) {
                if (!Inventory.contains(item)) {
                    script.log("Missing required item: " + item);
                    //stateManager.addState(new BankingState(script, requiredItems, optionalItems, useDepositBox));

                    return -1;//Calculations.random(200,700);
                }
            }

            NPC fishingSpot = NPCs.closest(npc -> npc != null && npc.hasAction(interaction));
            if (fishingSpot != null && !Inventory.isFull() && !(player.getAnimation() == 618) && fishingSpot.interact(interaction) ) {
                Sleep.sleep(2000);
                Sleep.sleepUntil(() -> !fishingSpot.exists() || Inventory.isFull() || !player.isAnimating(), Calculations.random(200,500));
            }

            if(Inventory.isFull()){
                boolean shouldUseDepositBox = shouldUseDepositBox();
                Area walkToArea = shouldUseDepositBox ? new Area(3044, 3237, 3050, 3233) : Bank.getClosestBankLocation().getArea(5);
                stateManager.addState(new WalkToState(script, walkToArea));
                stateManager.addState(new BankingState(script, settings, requiredItems, optionalItems, useDepositBox && shouldUseDepositBox(), false));
                stateManager.addState(new WalkToState(script, fishingArea));
                useDepositBox = shouldUseDepositBox();
            }
        }
        return Calculations.random(1000, 2000);
    }

    @Override
    public boolean isComplete() {
        return false;
    }
}
