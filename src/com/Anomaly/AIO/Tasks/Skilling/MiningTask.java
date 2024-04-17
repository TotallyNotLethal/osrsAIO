package com.Anomaly.AIO.Tasks.Skilling;

import com.Anomaly.AIO.Helpers.Items.EquipmentSets;
import com.Anomaly.AIO.Helpers.Locations.Location;
import com.Anomaly.AIO.Helpers.Locations.Spot;
import com.Anomaly.AIO.Helpers.Locations.Mining.MiningLocations;
import com.Anomaly.AIO.Helpers.Requirements.Mining.OreType;
import com.Anomaly.AIO.Helpers.Requirements.Mining.PickaxeType;
import com.Anomaly.AIO.Helpers.State.Methods.BankingState;
import com.Anomaly.AIO.Helpers.State.Methods.EquipItemsState;
import com.Anomaly.AIO.Helpers.State.Methods.TeleportToState;
import com.Anomaly.AIO.Helpers.State.Methods.WalkToState;
import com.Anomaly.AIO.Helpers.State.StateManager;
import com.Anomaly.AIO.Main.Task;
import org.dreambot.api.Client;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Player;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MiningTask extends Task {
    private final AbstractScript script;
    private final OreType oreType;
    private final Location location;
    private final Area miningArea;
    private Area bankingArea;
    private final Map<String, Integer> requiredItems;
    private final Map<String, Integer> optionalItems;
    private final StateManager stateManager;
    private final Player player;
    private Integer miningLevel = 1;
    private Boolean isMember = false;
    private int completeLevel = 1;
    private int completeTime = 1;
    private boolean useDepositBox;

    public MiningTask(AbstractScript script, String treeType, String location, int duration, int stopLevel) {
        this.script = script;
        this.oreType = OreType.byDisplayName(treeType);
        this.location = Location.byDisplayName(location);
        this.bankingArea = Bank.getClosestBankLocation().getArea(3);
        this.stateManager = new StateManager(script);
        this.requiredItems = new HashMap<>();
        this.optionalItems = new HashMap<>();
        this.player = Players.getLocal();
        this.miningLevel = Skills.getRealLevel(Skill.MINING);
        this.isMember = Client.isMembers();

        Spot spot = MiningLocations.getMiningSpot(this.location, this.oreType);
        this.miningArea = Objects.requireNonNull(spot).getArea();

        assert this.location != null;
        setupMiningTask(this.location);
        prepareStates();
    }

    private void setupMiningTask(Location location) {
        requiredItems.put(PickaxeType.getBestPickaxeForLevel(miningLevel, isMember).getDisplayName(), 1);
        optionalItems.putAll(EquipmentSets.PROSPECTOR.getItems());
        if(miningLevel >= 99)
            optionalItems.put("Mining Cape", 1);
        /*switch (location) {
            case PORT_SARIM -> {
                bankingArea = new Area(3044, 3237, 3050, 3233);
                useDepositBox = true;
            }
            //default -> throw new IllegalArgumentException("Invalid mining location");
        }*/

    }

    private void prepareStates() {
        if (!hasAllRequiredItems(requiredItems) || Inventory.isFull()) {
            stateManager.addState(new WalkToState(script, Bank.getClosestBankLocation()));
            stateManager.addState(new BankingState(script, requiredItems, optionalItems, false, true));
            stateManager.addState(new EquipItemsState(script, null));
        }
        stateManager.addState(new TeleportToState(script, miningArea));
        stateManager.addState(new WalkToState(script, miningArea));
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
        } else {
            if (Inventory.isFull()) {
                stateManager.addState(new WalkToState(script, bankingArea));
                stateManager.addState(new BankingState(script, requiredItems, optionalItems, useDepositBox, false));
                stateManager.addState(new WalkToState(script, miningArea));
            } else {
                GameObject ore = GameObjects.closest(gameObject -> gameObject != null &&
                        gameObject.getName().contains(oreType.getDisplayName()) &&
                        miningArea.contains(gameObject));
                if (ore != null && !player.isAnimating() && ore.interact("Mine")) {
                    Sleep.sleepUntil(() -> !player.isAnimating() || Inventory.isFull(), Calculations.random(10000, 15000));
                }
            }
        }
        return Calculations.random(200, 1200);
    }

    @Override
    public boolean isComplete() {
        return false;
    }
}
