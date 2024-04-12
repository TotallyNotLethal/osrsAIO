package com.Anomaly.AIO.Tasks.Skilling;

import com.Anomaly.AIO.Helpers.Interactions.FishingInteractions;
import com.Anomaly.AIO.Helpers.Items.EquipmentSets;
import com.Anomaly.AIO.Helpers.Requirements.Fishing.FishType;
import com.Anomaly.AIO.Helpers.Requirements.Fishing.FishingEquipment;
import com.Anomaly.AIO.Helpers.Requirements.Fishing.FishingRequirements;
import com.Anomaly.AIO.Helpers.State.Methods.BankingState;
import com.Anomaly.AIO.Helpers.State.Methods.EquipItemsState;
import com.Anomaly.AIO.Helpers.State.Methods.WalkToState;
import com.Anomaly.AIO.Helpers.State.StateManager;
import com.Anomaly.AIO.Main.Task;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.depositbox.DepositBox;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;

import java.util.HashMap;
import java.util.Map;

public class FishingTask implements Task {
    private final AbstractScript script;
    String method, location, interaction;
    private final Player player;
    private Area fishingArea;
    private Area bankingArea;
    private boolean useDepositBox = false;
    //private int fishingSpotId;
    private final Map<String, Integer> requiredItems;

    private final Map<String, Integer> optionalItems;
    private StateManager stateManager;

    public FishingTask(AbstractScript script, String method, String location) {
        this.script = script;
        this.method = method;
        this.location = location;
        this.stateManager = new StateManager(script);
        requiredItems = new HashMap<>();
        optionalItems = new HashMap<>();
        this.player = Players.getLocal();

        setupFishingTask(method);
        prepareStates();
    }

    private void setupFishingTask(String method) {

        FishType fishType = FishType.valueOf(method.toUpperCase());

        FishingEquipment[] requiredEquipment = FishingRequirements.fishToEquipmentMap.get(fishType);
        if (requiredEquipment != null) {
            for (FishingEquipment equipment : requiredEquipment) {
                requiredItems.put(equipment.getDisplayName(), 1);
            }
        }

        switch (method) {
            case "Shrimp" -> {
                fishingArea = new Area(3236, 3157, 3250, 3140);
                bankingArea = Bank.getClosestBankLocation().getArea(2);
                optionalItems.putAll(EquipmentSets.GRACEFUL.getItems());
                //optionalItems.put("Coins", 500);// <- Example how to add items also
            }
            case "Trout", "Salmon" -> {
                fishingArea = new Area(3100, 3425, 3107, 3435);
                bankingArea = Bank.getClosestBankLocation().getArea(2);
                requiredItems.put("Feather", 5000);
                optionalItems.putAll(EquipmentSets.GRACEFUL.getItems());
            }
            case "Lobster" -> {
                fishingArea = new Area(2922, 3182, 2926, 3175);
                bankingArea = new Area(3044, 3237, 3050, 3233);
                useDepositBox = true;
                requiredItems.put("Coins", 360);
                optionalItems.putAll(EquipmentSets.GRACEFUL.getItems());
            }
            default -> throw new IllegalArgumentException("Invalid fishing method");
        }
    }

    private void prepareStates() {
        //StateUtil.goToBank(requiredItems, optionalItems);
        if (!hasAllRequiredItems(requiredItems) || Inventory.isFull()) {
            if(!Bank.getClosestBankLocation().getArea(4).contains(player))
                stateManager.addState(new WalkToState(script, bankingArea));

            stateManager.addState(new BankingState(script, requiredItems, optionalItems, false));

            stateManager.addState(new EquipItemsState(script, null));
        }

        stateManager.addState(new WalkToState(script, fishingArea));

        interaction = FishingInteractions.getAction(FishType.valueOf(method.toUpperCase()));
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
                    return -1;
                }
            }

            NPC fishingSpot = NPCs.closest(npc -> npc != null && npc.hasAction(interaction));
            if (fishingSpot != null && fishingSpot.interact(interaction)) {
                Sleep.sleepUntil(() -> !fishingSpot.exists() || Inventory.isFull(), Calculations.random(10000,15000));
            }

            if(Inventory.isFull()){
            stateManager.addState(new WalkToState(script, bankingArea));
            stateManager.addState(new BankingState(script, requiredItems, optionalItems, useDepositBox));
            stateManager.addState(new WalkToState(script, fishingArea));
            }
        }
        return Calculations.random(1000, 2000);
    }

    @Override
    public boolean isComplete() {
        return false;
    }
}
