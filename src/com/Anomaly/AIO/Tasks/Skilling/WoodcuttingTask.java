package com.Anomaly.AIO.Tasks.Skilling;

import com.Anomaly.AIO.Helpers.Locations.Location;
import com.Anomaly.AIO.Helpers.Locations.Spot;
import com.Anomaly.AIO.Helpers.Locations.Woodcutting.WoodcuttingLocations;
import com.Anomaly.AIO.Helpers.Requirements.Woodcutting.AxeType;
import com.Anomaly.AIO.Helpers.Requirements.Woodcutting.TreeType;
import com.Anomaly.AIO.Helpers.State.Methods.*;
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

public class WoodcuttingTask extends Task {
    private final AbstractScript script;
    private final TreeType treeType;
    private final Location location;
    private final Area woodcuttingArea;
    private Area bankingArea;
    private final Map<String, Integer> requiredItems;
    private final Map<String, Integer> optionalItems;
    private final StateManager stateManager;
    private final Player player;
    private Integer woodcuttingLevel = 1;
    private Boolean isMember = false;
    private boolean useDepositBox;
    private int completeLevel = 1;
    private int completeTime = 1;

    public WoodcuttingTask(AbstractScript script, String treeType, String location, int duration, int stopLevel) {
        this.script = script;
        this.treeType = TreeType.byDisplayName(treeType);
        this.location = Location.byDisplayName(location);
        this.bankingArea = Bank.getClosestBankLocation().getArea(3);
        this.stateManager = new StateManager(script);
        this.requiredItems = new HashMap<>();
        this.optionalItems = new HashMap<>();
        this.player = Players.getLocal();
        this.woodcuttingLevel = Skills.getRealLevel(Skill.WOODCUTTING);
        this.isMember = Client.isMembers();

        Spot spot = WoodcuttingLocations.getWoodcuttingSpot(this.location, this.treeType);
        this.woodcuttingArea = Objects.requireNonNull(spot).getArea();

        assert this.location != null;
        setupWoodcuttingTask(this.location);
        prepareStates();
    }

    private void setupWoodcuttingTask(Location location) {
        //requiredItems.put(AxeType.getBestAxeForLevel(woodcuttingLevel, isMember).getDisplayName(), 1);

        switch (location) {
            case PORT_SARIM -> {
                bankingArea = new Area(3044, 3237, 3050, 3233);
                useDepositBox = true;
            }
            //default -> throw new IllegalArgumentException("Invalid woodcutting location");
        }
    }

    private void prepareStates() {
        if (!hasAllRequiredItems(requiredItems) || Inventory.isFull()) {

            stateManager.addState(new WalkToState(script, Bank.getClosestBankLocation()));
            stateManager.addState(new BankingState(script, requiredItems, optionalItems, false, true));
            stateManager.addState(new EquipItemsState(script, null));
        }
        stateManager.addState(new TeleportToState(script, woodcuttingArea));
        stateManager.addState(new WalkToState(script, woodcuttingArea));
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
                stateManager.addState(new BankingState(script, requiredItems, optionalItems, useDepositBox, true));
            } else {
                GameObject tree = GameObjects.closest(gameObject -> gameObject != null &&
                        gameObject.getName().equalsIgnoreCase(treeType.getDisplayName()) &&
                        woodcuttingArea.contains(gameObject));
                if (tree != null && tree.interact("Chop down")) {
                    Sleep.sleepUntil(() -> !Players.getLocal().isAnimating() || Inventory.isFull(), Calculations.random(10000, 15000));
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
