package com.Anomaly.AIO.Tasks.Combat.Bossing;

import com.Anomaly.AIO.Helpers.Locations.Teleports.Accessories.TeleportAccessory;
import com.Anomaly.AIO.Helpers.State.Methods.*;
import com.Anomaly.AIO.Helpers.State.StateManager;
import com.Anomaly.AIO.Main.SettingsManager;
import com.Anomaly.AIO.Main.Task;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.input.Camera;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.prayer.Prayers;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.web.node.CustomWebPath;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.Item;

import java.awt.*;
import java.util.*;
import java.util.List;

public class SarachnisTask extends Task {
    private final AbstractScript script;
    private final StateManager stateManager;
    private final SettingsManager settings;
    private boolean isComplete;
    private final Map<String, Integer> requiredItems = new HashMap<>();
    private final Map<String, Integer> optionalItems = new HashMap<>();
    private NPC sarachnis;
    private int trips = 0;
    private boolean statesCreated;
    private InCombatState combatState;
    private LootDropsState lootDropsState;
    private PrayerFlickState prayerFlickState;
    private RecoverState recoverState;
    private EscapeState escapeState;
    private boolean sarachnisDefeated;
    private final Player player;
    private int lootPrice = 0;
    private final Area sarachnisLadder = new Area(1696, 3579, 1708, 3569);

    public SarachnisTask(AbstractScript script, SettingsManager settings) {
        this.script = script;
        this.stateManager = new StateManager(script);
        this.settings = settings;
        sarachnisDefeated = false;
        player = Players.getLocal();
        requiredItems.put("Super combat potion(4)", 1);
        requiredItems.put("Prayer potion(4)", 2);
        requiredItems.put("Monkfish", 23);
        if(this.settings.isUsePOHEnabled())
            requiredItems.put("Teleport to house", 1);
        else
            requiredItems.put("Varrock teleport", 1);
        Camera.setZoom(Camera.getMaxZoom()); Camera.rotateTo(2045, 383);
        getToLair();
    }

    Area gate = new Area(1840, 9912, 1848, 9920);
    Area sixtySevenNinty = new Area(1839, 9931, 1844, 9926);
    Area sarachnisLair = new Area(1831, 9911, 1852, 9892);
    Tile[] path = {
            new Tile(1847, 9920, 0),
            new Tile(1847, 9919, 0),
            new Tile(1847, 9918, 0),
            new Tile(1847, 9917, 0),
            new Tile(1847, 9916, 0),
            new Tile(1846, 9916, 0),
            new Tile(1845, 9915, 0),
            new Tile(1845, 9914, 0),
            new Tile(1844, 9914, 0),
            new Tile(1843, 9914, 0),
            new Tile(1842, 9914, 0),
            new Tile(1842, 9913, 0),
            new Tile(1842, 9913, 0),
            new Tile(1842, 9912, 0)
    };

    CustomWebPath customPath = new CustomWebPath(path);

    private void getToLair() {
        switch(getCurrentArea())
        {
            case 0 -> {
                Logger.log("Starting from scratch!");
                stateManager.addState(new PrayerFlickState(script, true));

                Logger.log(String.format("POH enabled: %s | In POH: %s", settings.isUsePOHEnabled(), settings.isInPlayerOwnedHouse()));
                if(settings.isUsePOHEnabled() && settings.isInPlayerOwnedHouse())
                {
                    GameObject pool = GameObjects.closest(gameObject -> gameObject.getName().contains(" pool") && gameObject.hasAction("Drink"));
                    if (pool != null) {
                        pool.interact("Drink");
                        Sleep.sleepUntil(() -> player.getHealthPercent() == 100, 5000);
                    }

                    GameObject portal = GameObjects.closest(gameObject -> gameObject.getName().contains("Portal")  && gameObject.hasAction("Grand Exchange"));
                    if (portal != null) {
                        portal.interact("Grand Exchange");
                        Sleep.sleepUntil(() -> !settings.isInPlayerOwnedHouse(), 5000);
                    }
                }


                stateManager.addState(new WalkToState(script, Bank.getClosestBankLocation()));
                stateManager.addState(new BankingState(script, settings, requiredItems, null, false, false));
                int missing = 0;
                for (String i : requiredItems.keySet()) {
                    if (Inventory.contains(i)) {
                        missing++;
                    }
                }
                if(missing >= 1 && missing < requiredItems.size())
                    stateManager.addState(new BankingState(script, settings, requiredItems, null, false, false));
                else if (!stateManager.hasStates()) isComplete = true;

                if(!isComplete) {
                    stateManager.addState(new TeleportToState(script, TeleportAccessory.XERICS_GLADE.getLocation().getArea()));
                    stateManager.addState(new WalkToState(script, sarachnisLadder.getCenter()));
                }
            }
            case 1 -> {
                customPath.connectStartToClosestNode();
                customPath.attachToWeb();
                GameObjects.closest("Ladder").interact("Climb-down");
                Sleep.sleep(3000);
                stateManager.addState(new WalkToState(script, customPath.getStart().getTile()));
            }
            case 2 -> {
                //Prayers.toggle(true, Prayer.PROTECT_FROM_MISSILES);
                GameObjects.closest("Thick Web").interact("Quick-enter");
                Sleep.sleep(600);
                Sleep.sleepUntil(() -> sarachnisLair.contains(player), 6000);
            }
            case 3 -> {
            }

            default -> throw new IllegalStateException("Unexpected value: " + player.getTile());
        }
    }

    public int getCurrentArea() {
            if (sarachnisLair.contains(player))
                return 3;
            else if (gate.contains(player))
                return 2;
            else if (sarachnisLadder.contains(player))
                return 1;
            else return 0;
    }

    private void fightingStates() {
        if(sarachnisLair.contains(player)) {
            sarachnis = NPCs.closest(n -> n != null && n.getName().equals("Sarachnis"));

            if (sarachnis != null && !sarachnis.exists()) { // If Sarachnis no longer exists, it is defeated
                if (!sarachnisDefeated) { // If we haven't already registered the defeat
                    trips++; // Increment the trips counter
                    sarachnisDefeated = true; // Set the flag to prevent re-counting
                    Logger.log("Sarachnis defeated! Total kills: " + trips);
                }
            } else {
                sarachnisDefeated = false; // Reset the flag when Sarachnis is present
            }

            List<NPC> spawns = NPCs.all(n -> n != null && n.getName().contains("Spawn"));
            spawns.sort((s1, s2) -> {
                int id1 = s1.getID(), id2 = s2.getID();
                return Integer.compare(id2, id1);
            });
                combatState = new InCombatState(script, sarachnis);
                lootDropsState = new LootDropsState(script, sarachnisLair, Arrays.asList("Sarachnis cudgel", "Giant egg sac"));
                prayerFlickState = new PrayerFlickState(script, "Sarachnis");
                recoverState = new RecoverState(script, 70);
                escapeState = new EscapeState(script, player.getTile().getArea(15));

                statesCreated = true;


            if(!spawns.isEmpty())
                combatState.setTargets(spawns);
            else combatState.setTarget(sarachnis);
            if(player.getHealthPercent() < 70 || Skills.getBoostedLevel(Skill.PRAYER) < 30)
                stateManager.addState(recoverState);
            if(sarachnis != null && sarachnis.exists() && sarachnisLair.contains(player)) {
                if (sarachnis != null && sarachnis.exists() && sarachnis.distance() <= 4.7 && sarachnis.isInteracting(player) && !sarachnis.isMoving()) {
                    if (!Prayers.isActive(Prayer.PROTECT_FROM_MELEE)) {
                        Prayers.toggle(true, Prayer.PROTECT_FROM_MELEE);
                        prayerFlickState.setRequiredPrayer(Prayer.PROTECT_FROM_MELEE);
                        Sleep.sleepUntil(() -> Prayers.isActive(Prayer.PROTECT_FROM_MELEE), 1200);
                    }
                } else stateManager.addState(prayerFlickState);
            }
            if(player.getHealthPercent() < 70 && sarachnisLair.contains(player)) {
                stateManager.addState(escapeState);
            }
            if(sarachnis != null && sarachnis.exists() && sarachnisLair.contains(player))
                stateManager.addState(combatState);
            if(sarachnis == null && sarachnisLair.contains(player) && !Inventory.isFull())
                stateManager.addState(lootDropsState);
        }
    }

    @Override
    public int execute() {
        if (stateManager.isComplete()) {
            getToLair();
            if (!stateManager.hasStates() || sarachnis == null || !sarachnis.exists()) {
                fightingStates();
            }
        }

        if (stateManager.hasStates()) {
            stateManager.executeCurrentState();
        } else {
            Logger.log("No states left to execute. Checking conditions...");
        }
        if(lootDropsState != null && lootDropsState.getLootPrices() != 0)
            lootPrice += lootDropsState.getLootPrices();

        return 300;
    }

    @Override
    public void onPaint(Graphics g) {
        super.onPaint(g);
        g.setColor(Color.WHITE);
        //g.drawString("Current State: " + stateManager.currentState(), 10, 275);
        g.drawString("Sarachnis Health: " + (sarachnis != null ? sarachnis.getHealthPercent() + "%" : "N/A"), 10, 275);
        g.drawString("Sarachnis Distance: " + (sarachnis != null ? sarachnis.distance() : "N/A"), 10, 290);

        g.drawString(trips == 0 ? String.format("Coins earned: %d | Avg per Trip(%d)", lootPrice, lootPrice) : String.format("Coins earned: %d | Avg per Trip(%d)", lootPrice, lootPrice/ trips), 10, 305);
        g.drawString("Trips completed: " + trips, 10, 320);
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }
}
