package com.Anomaly.AIO.Helpers;

import com.Anomaly.AIO.Helpers.Destination.Destination;
import com.Anomaly.AIO.Task;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.interactive.Player;

public class WalkingTask implements Task {
    private final AbstractScript script;
    private final Destination destination;
    private final Player player;

    public WalkingTask(AbstractScript script, Destination destination) {
        this.script = script;
        this.destination = destination;
        player = Players.getLocal();
    }

    @Override
    public int execute() {
        if (!destination.isOnScreen()) {
            destination.walkTo(script);
            Sleep.sleepUntil(destination::isOnScreen, 5000, 600);
        }

        if (destination instanceof Area) {
            return ((Area) destination).contains(player) ? 0 : Calculations.random(100, 200);
        } else if (destination instanceof Tile) {
            return ((Tile) destination).distance(player) < 2 ? 0 : Calculations.random(100, 200);
        } else {
            script.log("Unsupported destination type.");
            return 0;
        }
    }

    @Override
    public boolean isComplete() {
        return false;
    }
}
