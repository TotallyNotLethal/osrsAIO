package com.Anomaly.AIO.Helpers.State.Methods;

import com.Anomaly.AIO.Helpers.State.State;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;


public class WalkToNPCState implements State {
    private NPC target;
    private final Player player = Players.getLocal();
    private boolean isComplete = false;

    public WalkToNPCState(NPC target) {
        this.target = target;
    }

    public int execute() {
        Area area = target.getTrueTile().getArea(4);
        if (!area.contains(player)) {
            Logger.log("Walking to " + target.getName());
            Walking.walk(area.getRandomTile());
        } else {
            isComplete = true;
        }
        return Calculations.random(200, 500);
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void reset() {
        isComplete = false;
    }
}
