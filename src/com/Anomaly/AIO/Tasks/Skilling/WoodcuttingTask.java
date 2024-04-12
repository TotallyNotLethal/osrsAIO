package com.Anomaly.AIO.Tasks.Skilling;

import com.Anomaly.AIO.Helpers.Locations.Fishing.FishingLocations;
import com.Anomaly.AIO.Helpers.Locations.Location;
import com.Anomaly.AIO.Helpers.Locations.Spot;
import com.Anomaly.AIO.Helpers.Locations.Woodcutting.WoodcuttingLocations;
import com.Anomaly.AIO.Helpers.Requirements.Woodcutting.AxeType;
import com.Anomaly.AIO.Helpers.Requirements.Woodcutting.TreeType;
import com.Anomaly.AIO.Helpers.State.Methods.BankingState;
import com.Anomaly.AIO.Helpers.State.Methods.WalkToState;
import com.Anomaly.AIO.Helpers.State.StateManager;
import com.Anomaly.AIO.Main.Task;
import org.dreambot.api.Client;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.AccountManager;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WoodcuttingTask implements Task {
    private final AbstractScript script;
    private final TreeType treeType;
    private final Location location;
    private final Area woodcuttingArea;
    private final Area bankingArea;
    private final Map<String, Integer> requiredItems;
    private final Map<String, Integer> optionalItems;
    private final StateManager stateManager;
    private final Player player;
    private Integer woodcuttingLevel = 1;
    private Boolean isMember = false;
    private boolean useDepositBox;

    public WoodcuttingTask(AbstractScript script, String treeType, String location) {
        this.script = script;
        this.treeType = TreeType.valueOf(treeType.toUpperCase());
        this.location = Location.valueOf(location.toUpperCase());
        this.bankingArea = Bank.getClosestBankLocation().getArea(3);
        this.stateManager = new StateManager(script);
        this.requiredItems = new HashMap<>();
        this.optionalItems = new HashMap<>();
        this.player = Players.getLocal();
        this.woodcuttingLevel = Skills.getRealLevel(Skill.WOODCUTTING);
        this.isMember = Client.isMembers();

        Spot spot = WoodcuttingLocations.getWoodcuttingSpot(this.location, this.treeType);
        this.woodcuttingArea = Objects.requireNonNull(spot).getArea();

        setupWoodcuttingTask();
        prepareStates();
    }

    private void setupWoodcuttingTask() {
        requiredItems.put(AxeType.getBestAxeForLevel(woodcuttingLevel, isMember).getDisplayName(), 1);
    }

    private void prepareStates() {
        if (!hasAllRequiredItems(requiredItems)) {
            stateManager.addState(new BankingState(script, requiredItems, optionalItems, useDepositBox));
        }
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
                stateManager.addState(new BankingState(script, requiredItems, optionalItems, useDepositBox));
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
