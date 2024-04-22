package com.Anomaly.AIO.Tasks.Combat.Bossing;

import com.Anomaly.AIO.Helpers.Items.EquipmentSets;
import com.Anomaly.AIO.Helpers.Locations.Teleports.Accessories.TeleportAccessory;
import com.Anomaly.AIO.Helpers.Locations.Teleports.Tablets.TeleportTablet;
import com.Anomaly.AIO.Helpers.Locations.Teleports.TeleportLocation;
import com.Anomaly.AIO.Helpers.State.Methods.*;
import com.Anomaly.AIO.Helpers.State.StateManager;
import com.Anomaly.AIO.Main.SettingsManager;
import com.Anomaly.AIO.Main.Task;
import org.dreambot.api.ClientSettings;
import org.dreambot.api.input.mouse.destination.impl.MiniMapTileDestination;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.hint.HintArrow;
import org.dreambot.api.methods.hint.HintArrowType;
import org.dreambot.api.methods.input.Camera;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.prayer.Prayers;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;

import java.awt.*;
import java.util.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

public class GiantMoleTask extends Task {
    private final AbstractScript script;
    private final StateManager stateManager;
    private final SettingsManager settings;
    private final Area moleLair = new Area(1791, 5247, 1727, 5124); // Adjusted for actual lair coordinates
    private final Area faladorPark = new Area(2980, 3391, 3026, 3367);
    private final Area moleSpawn = new Area(1755, 5188, 1765, 5179);
    private Map<String, Integer> requiredItems = new HashMap<>();
    private Map<String, Integer> optionalItems = new HashMap<>();
    private boolean isComplete = false;

    private int kills = 0;
    private boolean giantMoleDefeated;
    private NPC mole;
    private final Player player;

    public GiantMoleTask(AbstractScript script, SettingsManager settings) {
        this.script = script;
        this.stateManager = new StateManager(script);
        this.settings = settings;
        this.player = Players.getLocal();

        optionalItems.put("Dwarven rock cake", 1);
        optionalItems.put("Fire cape", 1);
        optionalItems.put("Berserker ring(i)", 1);
        optionalItems.put("Falador shield 3", 1);
        requiredItems.put("Prayer potion(4)", 6);
        requiredItems.put("Stamina potion(4)", 4);
        optionalItems.put("Super combat potion(4)", 1);
        requiredItems.put("Spade", 1);
        requiredItems.put(settings.isUsePOHEnabled() ? "Teleport to house" : "Varrock teleport", 1);
        optionalItems.putAll(EquipmentSets.DHAROKS.getItems());

        stateManager.clearStates();
        prepareForMole();
    }

    private void prepareForMole() {
        if (!faladorPark.contains(player) || !Inventory.containsAll(requiredItems.keySet())) {
            stateManager.addState(new BankingState(script, settings, requiredItems, optionalItems, false, false));
            //stateManager.addState(new EquipItemsState(script, null));
            stateManager.addState(new TeleportToState(script, faladorPark));

            stateManager.addState(new WalkToState(script, faladorPark));
        }
    }

    @Override
    public int execute() {
        if (stateManager.getCurrentState() != null) {
            stateManager.executeCurrentState();
        } else {

            if (faladorPark.contains(player)) {
                Tile toDigAt = GameObjects.closest("Mole hill").getTile();
                if (!player.isMoving() && !player.getTile().equals(toDigAt)) {
                    Walking.walk(toDigAt);
                } else Inventory.get("Spade").interact("Dig");
            }

            if (moleLair.contains(player)) {
                while (player.getHealthPercent() >= 2) {
                    Item rockCake = Inventory.get("Dwarven rock cake");
                    if (rockCake != null) {
                        rockCake.interact("Guzzle");
                        Sleep.sleep(Calculations.random(90, 110));
                    }
                }
                while (Skills.getBoostedLevel(Skill.PRAYER) < 15) {
                    Item prayerPot = Inventory.get(i -> i.getName().contains("Prayer pot"));
                    if (prayerPot != null) {
                        prayerPot.interact("Drink");
                        Sleep.sleep(Calculations.random(80, 120));
                    } else {
                        Logger.log("No Prayer potions left in inventory.");
                        stateManager.addState(new EscapeState(script, moleLair));
                        break;
                    }
                }

                Item superCombat = Inventory.get(i -> i.getName().contains("Super combat"));
                if (superCombat != null) {
                    superCombat.interact("Drink");
                    Sleep.sleep(Calculations.random(80, 120));
                } else {
                    Logger.log("No Super combat left in inventory.");
                }


                if (Walking.getRunEnergy() < 20) {
                    Item staminaPot = Inventory.get(i -> i.getName().contains("Stamina pot"));
                    if (staminaPot != null) {
                        staminaPot.interact("Drink");
                    }
                }


                mole = NPCs.closest(n -> n != null && n.getName().equals("Giant Mole"));
                if (mole != null && mole.distance() < 8) {
                    engageMole();
                } else {
                    if (!giantMoleDefeated && wasMoleKilled()) {
                        giantMoleDefeated = true;
                        kills++;
                        Sleep.sleepUntil(() -> GroundItems.closest(drop -> drop != null && drop.exists()).distance() < 8, 10000);
                        stateManager.addState(new LootDropsState(script, moleLair, null, "Big bones"));
                    } else {
                        giantMoleDefeated = false;
                        if (HintArrow.exists())
                            searchForMole();
                    }
                }
            }

            if (moleLair.contains(player) && mole == null) {
                if (Prayers.isActive(Prayer.PROTECT_FROM_MELEE)) {
                    Prayers.toggle(false, Prayer.PROTECT_FROM_MELEE);
                }
                if (!HintArrow.exists())
                    if (Walking.shouldWalk(6))
                        Walking.walk(moleSpawn);

            }

            if (TeleportAccessory.GRAND_EXCHANGE.getLocation().getArea().contains(player)) {
                prepareForMole();
            }
        }
        return 600;
    }

    private boolean wasMoleKilled() {
        if(HintArrow.exists())
            return false;
        return NPCs.closest(n -> n != null && n.getName().equals("Giant Mole")) == null;
    }

    private void searchForMole() {
        if(Walking.shouldWalk(7))
            Walking.walk(HintArrow.getTile());
    }

    private void engageMole() {
        if (!Prayers.isActive(Prayer.PROTECT_FROM_MELEE)) {
            Prayers.toggle(true, Prayer.PROTECT_FROM_MELEE);
        }
        Sleep.sleepUntil(() -> Prayers.isActive(Prayer.PROTECT_FROM_MELEE), 5000);

        if (!player.isInteracting(mole) && Prayers.isActive(Prayer.PROTECT_FROM_MELEE))
            mole.interact("Attack");
    }

    @Override
    public void onPaint(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("Mole kills: " + kills, 10, 290);
        g.drawString("Mole health: " + (mole != null ? mole.getHealthPercent() + "%" : "Unknown"), 10, 305);
        g.drawString("Mole distance: " +(mole != null ? mole.distance() : "Unknown"), 10, 320);
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }
}