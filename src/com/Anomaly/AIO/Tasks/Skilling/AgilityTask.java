package com.Anomaly.AIO.Tasks.Skilling;

import com.Anomaly.AIO.Helpers.Items.EquipmentSets;
import com.Anomaly.AIO.Helpers.Locations.AgilityLocations.AgilityLocations;
import com.Anomaly.AIO.Helpers.Locations.Location;
import com.Anomaly.AIO.Helpers.Locations.Spot;
import com.Anomaly.AIO.Helpers.Locations.Thieving.ThievingLocations;
import com.Anomaly.AIO.Helpers.State.Methods.BankingState;
import com.Anomaly.AIO.Helpers.State.Methods.EquipItemsState;
import com.Anomaly.AIO.Helpers.State.Methods.WalkToState;
import com.Anomaly.AIO.Helpers.State.StateManager;
import com.Anomaly.AIO.Helpers.State.StateUtil;
import com.Anomaly.AIO.Main.Main;
import com.Anomaly.AIO.Main.Task;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.GroundItem;

import java.util.*;

public class AgilityTask implements Task {
    private final AbstractScript script;
    private final StateManager stateManager;
    private final Player player;
    private final Map<String, Integer> requiredItems = new HashMap<>();

    private final Map<String, Integer> optionalItems = new HashMap<>();
    private Location location;
    private Location method;
    private Area agilityArea;
    private final List<String> commonObstacleActions = Arrays.asList("Climb", "Climb-up", "Cross", "Jump-up", "Jump", "Leap", "Hurdle", "Balance", "Jump-off", "Climb-down");
    private GameObject lastObstacle;
    private List<GameObject> encounteredObstacles = new ArrayList<>();
    private final Main main = new Main();
    public AgilityTask(AbstractScript script, String location, String method) {
        this.script = script;
        this.stateManager = new StateManager(script);
        this.location = Location.byDisplayName(location);
        this.method = Location.byDisplayName(method);
        this.player = Players.getLocal();
        this.optionalItems.putAll(EquipmentSets.GRACEFUL.getItems());

        Spot spot = AgilityLocations.getAgilitySpot(this.method, this.location);

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
            stateManager.addState(new BankingState(script, requiredItems, optionalItems, false));
            stateManager.addState(new EquipItemsState(script, null));
            stateManager.addState(new WalkToState(script, agilityArea));
        }
    }

    @Override
    public int execute() {
        if (!stateManager.isComplete()) {
            stateManager.executeCurrentState();
            return Calculations.random(200, 500);
        }

        if (player.isMoving() || player.isAnimating()) {
            return Calculations.random(5000, 6000);
        }

        GroundItem markOfGrace = GroundItems.closest("Mark of Grace");
        if (markOfGrace != null && markOfGrace.canReach()) {
            markOfGrace.interact("Take");
            Sleep.sleepUntil(() -> !markOfGrace.exists(), Calculations.random(4000, 6000));
            return Calculations.random(3000, 5000);
        }

        GameObject nextObstacle = getNextObstacle();
        if (nextObstacle != null && !encounteredObstacles.contains(nextObstacle)) {
            String action = determineActionForObstacle(nextObstacle);
            if (action != null) {
                if(!player.isAnimating() && !player.isMoving()) {
                    nextObstacle.interact(action);
                    encounteredObstacles.add(nextObstacle);
                    lastObstacle = nextObstacle;


                    if (action.equals("Jump-off") || action.equals("Climb-down")) {
                        stateManager.addState(new WalkToState(script, agilityArea));
                        encounteredObstacles.clear();
                    }
                    Sleep.sleepUntil(() -> !player.isAnimating() && !player.isMoving(), 10000);
                    return Calculations.random(4000, 6000);
                }
            }
        } else {
            script.log("No more obstacles found or last obstacle repeated.");
            return 0;
        }
        return Calculations.random(1000, 2500);
    }


    @Override
    public boolean isComplete() {
        return false;
    }

    private GameObject getNextObstacle() {
        return GameObjects.closest(gameObject ->
                gameObject != null && gameObject.canReach() &&
                        !encounteredObstacles.contains(gameObject) &&
                        Arrays.stream(gameObject.getActions()).anyMatch(commonObstacleActions::contains) &&
                        gameObject.isOnScreen()
        );
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