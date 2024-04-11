package com.Anomaly.AIO.Tasks.Skilling;

import com.Anomaly.AIO.Main;
import com.Anomaly.AIO.Task;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Player;

import java.util.Arrays;
import java.util.List;

public class AgilityTask implements Task {
    private final AbstractScript script;
    private final Player player;
    private final List<String> commonObstacleActions = Arrays.asList("Climb", "Climb-up", "Cross", "Jump", "Leap", "Hurdle", "Balance", "Jump-off"); // Added "Leap"
    private GameObject lastObstacle;
    private final Main main = new Main();
    public AgilityTask(AbstractScript script, String method, String location) {
        this.script = script;
        this.player = Players.getLocal();
        execute();
    }

    @Override
    public int execute() {
        if (player.isMoving() || player.isAnimating()) {
            return Calculations.random(500, 1000);
        }

        GameObject nextObstacle = getNextObstacle();
        if (nextObstacle != null && !nextObstacle.equals(lastObstacle)) {
            String action = determineActionForObstacle(nextObstacle);
            if (action != null) {
                nextObstacle.interact(action);
                lastObstacle = nextObstacle;
                return Calculations.random(2000, 3000);
            }
        } else {
            script.log("No more obstacles found or last obstacle repeated.");
            return 0;
        }
        return Calculations.random(100, 200);
    }

    @Override
    public boolean isComplete() {
        return false;
    }

    private GameObject getNextObstacle() {
        return GameObjects.closest(gameObject ->
                gameObject != null &&
                        !gameObject.equals(lastObstacle) &&
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