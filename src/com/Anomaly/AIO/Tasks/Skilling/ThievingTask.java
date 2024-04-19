package com.Anomaly.AIO.Tasks.Skilling;

import com.Anomaly.AIO.Helpers.Items.EquipmentSets;
import com.Anomaly.AIO.Helpers.Locations.Location;
import com.Anomaly.AIO.Helpers.Locations.Spot;
import com.Anomaly.AIO.Helpers.Locations.Thieving.ThievingLocations;
import com.Anomaly.AIO.Helpers.Locations.Woodcutting.WoodcuttingLocations;
import com.Anomaly.AIO.Helpers.Requirements.Thieving.*;
import com.Anomaly.AIO.Helpers.Requirements.Thieving.ThievingEntity;
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
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ThievingTask extends Task {
    private final AbstractScript script;
    private final ThievableEntity thievableEntity;
    private final Location location;
    private final Player player;
    private final Area thievingArea;
    private final SettingsManager settings;
    private Area bankingArea;
    private boolean useDepositBox = false;
    private int completeLevel = 1;
    private int completeTime = 1;
    private boolean shadowVeil = false;
    private double shadowTimer = 0.6 * Skill.MAGIC.getLevel();
    private final Map<String, Integer> requiredItems = new HashMap<>();

    private final Map<String, Integer> optionalItems = new HashMap<>();
    private final StateManager stateManager;

    public ThievingTask(AbstractScript script, SettingsManager settings, String thievingTarget, String location, int duration, int stopLevel) {
        this.script = script;
        this.settings = settings;
        this.location = Location.byDisplayName(location);
        this.thievableEntity = getThievableEntityByName(thievingTarget);
        this.stateManager = new StateManager(script);
        this.player = Players.getLocal();
        this.optionalItems.putAll(EquipmentSets.ROGUE.getItems());
        this.optionalItems.put("Dodgy necklace", 1);
        if(Skill.THIEVING.getLevel() >= 99)
            optionalItems.put("Thieving cape", 1);
        Spot spot = ThievingLocations.getThievingSpot(this.location, this.thievableEntity);
        this.thievingArea = Objects.requireNonNull(spot).getArea();

        prepareStates();
    }

    private void prepareStates() {
        if (!thievingArea.contains(player)) {
            stateManager.addState(new WalkToState(script, Bank.getClosestBankLocation()));
            stateManager.addState(new BankingState(script, settings, requiredItems, optionalItems, false, true));
            stateManager.addState(new EquipItemsState(script, null));
            stateManager.addState(new TeleportToState(script, thievingArea));
            stateManager.addState(new WalkToState(script, thievingArea));
        }
    }

    @Override
    public int execute() {
        if (!stateManager.isComplete()) {
            stateManager.executeCurrentState();
            return Calculations.random(200, 500);
        }

        if(!Inventory.isFull() && !player.isAnimating() && !player.isMoving())
            switch (thievableEntity.getEntityType()) {
                case NPC -> interactWithNPC();
                case STALL -> interactWithStall();
                case CHEST, DOOR -> interactWithObject();
            }

        if(Inventory.isFull()){
            stateManager.addState(new WalkToState(script, Bank.getClosestBankLocation()));
            stateManager.addState(new BankingState(script, settings, requiredItems, optionalItems, useDepositBox, false));
            stateManager.addState(new WalkToState(script, thievingArea));
        }

        return Calculations.random(300, 600);
    }

    private void interactWithNPC() {
        NPC targetNPC = NPCs.closest(npc -> npc != null && npc.getName().equals(thievableEntity.getDisplayName()) && npc.canReach());
        if (targetNPC != null && !player.isInCombat() && !player.isAnimating()) {
            targetNPC.interact("Pickpocket");
            Sleep.sleepUntil(() -> !player.isAnimating(), Calculations.random(1800, 2200));
        }
    }

    private void interactWithStall() {
        GameObject stall = GameObjects.closest(gameObject -> gameObject != null &&
                gameObject.getName().equalsIgnoreCase(thievableEntity.getDisplayName()) &&
                thievingArea.contains(gameObject));
        if (stall != null && !player.isAnimating()) {
            stall.interact("Steal-from");
            Sleep.sleepUntil(() -> !stall.exists() || player.getAnimation() == -1, Calculations.random(1800, 2200));
        }
    }

    private void interactWithObject() {
        GameObject object = GameObjects.closest(gameObject -> gameObject != null &&
                gameObject.getName().equalsIgnoreCase(thievableEntity.getDisplayName()) &&
                thievingArea.contains(gameObject));
        if (object != null && !player.isAnimating()) {
            object.interact(thievableEntity.getEntityType() == ThievingEntity.DOOR ? "Pick-lock" : "Search for traps");
            Sleep.sleepUntil(() -> !object.exists() || player.getAnimation() == -1, Calculations.random(1800, 2200));
        }
    }

    public ThievableEntity getThievableEntityByName(String entityName) {
        for (ThievingNPC npc : ThievingNPC.values()) {
            if (npc.getDisplayName().equalsIgnoreCase(entityName)) {
                return npc;
            }
        }
        for (ThievingStall stall : ThievingStall.values()) {
            if (stall.getDisplayName().equalsIgnoreCase(entityName)) {
                return stall;
            }
        }
        for (ThievingChest chest : ThievingChest.values()) {
            if (chest.getDisplayName().equalsIgnoreCase(entityName)) {
                return chest;
            }
        }
        for (ThievingDoor door : ThievingDoor.values()) {
            if (door.getDisplayName().equalsIgnoreCase(entityName)) {
                return door;
            }
        }
        return null;
    }

    @Override
    public boolean isComplete() {
        return false;
    }
}
