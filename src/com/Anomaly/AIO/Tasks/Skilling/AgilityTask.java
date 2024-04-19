package com.Anomaly.AIO.Tasks.Skilling;

import com.Anomaly.AIO.Helpers.Destination.AreaDestination;
import com.Anomaly.AIO.Helpers.Destination.Destination;
import com.Anomaly.AIO.Helpers.Items.EquipmentSets;
import com.Anomaly.AIO.Helpers.Locations.AgilityLocations.AgilityEndLocations;
import com.Anomaly.AIO.Helpers.Locations.AgilityLocations.AgilityStartLocations;
import com.Anomaly.AIO.Helpers.Locations.Location;
import com.Anomaly.AIO.Helpers.Locations.Spot;
import com.Anomaly.AIO.Helpers.State.Methods.BankingState;
import com.Anomaly.AIO.Helpers.State.Methods.EquipItemsState;
import com.Anomaly.AIO.Helpers.State.Methods.TeleportToState;
import com.Anomaly.AIO.Helpers.State.Methods.WalkToState;
import com.Anomaly.AIO.Helpers.State.StateManager;
import com.Anomaly.AIO.Main.Main;
import com.Anomaly.AIO.Main.SettingsManager;
import com.Anomaly.AIO.Main.Task;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.input.Camera;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.launcher.Settings;

import java.awt.*;
import java.util.*;
import java.util.List;

public class AgilityTask extends Task {
    private final AbstractScript script;
    private final StateManager stateManager;
    private final SettingsManager settings;
    private final Player player;
    private final Map<String, Integer> requiredItems = new HashMap<>();

    private final Map<String, Integer> optionalItems = new HashMap<>();
    private Location location;
    private int completeLevel = 1;
    private int completeTime = 1;

    long startTime = System.currentTimeMillis();
    private Location method;
    private Area agilityArea;
    private final List<String> commonObstacleActions = Arrays.asList(
            "Climb", "Climb-up", "Cross", "Jump-up", "Jump",
            "Leap", "Hurdle", "Balance", "Jump-off", "Climb-down",
            "Walk-across", "Climb-over", "Climb-under", "Walk-on",
            "Swing-across", "Squeeze-through", "Jump-over", "Swing-on",
            "Step-across", "Balance-across", "Vault", "Jump-across",
            "Ride","Slide-down", "Teeth-grip", "Jump-in");
    private GameObject lastObstacle;
    private List<GameObject> encounteredObstacles = new ArrayList<>();
    private final Main main = new Main();
    public AgilityTask(AbstractScript script, SettingsManager settings, String location, String method, int duration, int stopLevel) {
        this.script = script;
        this.stateManager = new StateManager(script);
        this.settings = settings;
        this.location = Location.byDisplayName(location);
        this.method = Location.byDisplayName(method);
        this.player = Players.getLocal();

        if(Location.byDisplayName(location) != Location.WILDERNESS)
            this.optionalItems.putAll(EquipmentSets.GRACEFUL.getItems());

        Spot spot = AgilityStartLocations.getAgilitySpot(this.method, this.location);

        if (spot != null) {
            this.agilityArea = spot.getArea();
        } else {
            script.log("No agility spot found for location: " + this.location + " and spot: " + this.method);
        }

        this.agilityArea = Objects.requireNonNull(spot).getArea();

        prepareStates();
    }

    private void prepareStates() {
        if (!agilityArea.contains(player)) {
            stateManager.addState(new WalkToState(script, Bank.getClosestBankLocation()));
            stateManager.addState(new BankingState(script, settings, requiredItems, optionalItems, false, true));
            stateManager.addState(new EquipItemsState(script, null));
            stateManager.addState(new TeleportToState(script, agilityArea));
            stateManager.addState(new WalkToState(script, agilityArea));
            Camera.setZoom(Camera.getMaxZoom()); Camera.rotateTo(2045, 383);
        }
    }
public boolean finished = false;
    @Override
    public int execute() {
        if(!finished) {
            if (!stateManager.isComplete()) {
                stateManager.executeCurrentState();
                return Calculations.random(200, 500);
            }

            if (isInEndLocation()) {
                finished = true;
                Logger.log("Player is in the end location, walking back to start...");
                stateManager.addState(new WalkToState(script, agilityArea));
                stateManager.executeCurrentState();
                encounteredObstacles.clear();
                finished = false;

                return Calculations.random(1000, 2000);
            }

            if (player.isMoving() || player.isAnimating()) {
                return Calculations.random(1000, 2000);
            }

            GroundItem markOfGrace = GroundItems.closest("Mark of Grace");
            if (markOfGrace != null && (markOfGrace.canReach() || markOfGrace.distance() < 3) ) {
                markOfGrace.interact("Take");
                Sleep.sleepUntil(() -> !markOfGrace.exists(), Calculations.random(4000, 6000));
                return Calculations.random(1000, 2000);
            }

            GameObject nextObstacle = getNextObstacle();
            if (nextObstacle != null && !encounteredObstacles.contains(nextObstacle)) {
                String action = determineActionForObstacle(nextObstacle);
                Logger.log("Found obstacle: " + nextObstacle);
                if (action != null) {
                    if (!player.isAnimating() && !player.isMoving()) {

                        if (nextObstacle.distance() > 8 && !nextObstacle.isOnScreen()) {
                            Walking.walk(nextObstacle.getTile().getRandomized(4));
                            Sleep.sleep(200);
                            Sleep.sleepUntil(() -> !player.isAnimating() && !player.isAnimating(), 3000);
                        }

                        nextObstacle.interact(action);
                        encounteredObstacles.add(nextObstacle);
                        lastObstacle = nextObstacle;
                        Sleep.sleepUntil(() -> !player.isAnimating() && !player.isMoving(), 10000);

                        return Calculations.random(2000, 3000);
                    }
                }
            } else {
                script.log("No more obstacles found retrying.");
                encounteredObstacles.clear();

                return Calculations.random(200, 500);
            }
        }
        return Calculations.random(1000, 1500);
    }

    @Override
    public boolean isComplete() {
        return false;//completeLevel > Skill.AGILITY.getLevel() || System.currentTimeMillis() - startTime > completeTime;
    }

    private boolean isInEndLocation() {
        Spot endSpot = AgilityEndLocations.getAgilitySpot(this.method, this.location);
        return endSpot != null && endSpot.getArea().contains(player);
    }

    private GameObject getNextObstacle() {
        return GameObjects.closest(gameObject ->
                gameObject != null && (gameObject.canReach() || isObstacleReachable(gameObject)) &&
                        !encounteredObstacles.contains(gameObject) && !gameObject.getName().equalsIgnoreCase("Ladder") && !gameObject.getName().equalsIgnoreCase("Stairs") && !gameObject.getName().equalsIgnoreCase("Staircase") && !gameObject.getName().equalsIgnoreCase("Curtain") && !gameObject.getName().equalsIgnoreCase("Trapdoor") &&
                        Arrays.stream(gameObject.getActions()).anyMatch(commonObstacleActions::contains)

        );
    }

    private boolean isObstacleReachable(GameObject obstacle) {
        Area surroundingArea = obstacle.getSurroundingArea(3);
        for (Tile tile : surroundingArea.getTiles()) {
            if (tile.canReach()) {
                return true;
            }
        }
        return false;
    }

    private String determineActionForObstacle(GameObject obstacle) {
        for (String action : commonObstacleActions) {
            if (Arrays.asList(obstacle.getActions()).contains(action)) {
                return action;
            }
        }
        return null;
    }
}