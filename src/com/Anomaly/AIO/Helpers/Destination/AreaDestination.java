package com.Anomaly.AIO.Helpers.Destination;

import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.wrappers.interactive.Player;

public abstract class AreaDestination implements Destination {
    private final Area area;
    private final Player player;

    public AreaDestination(Area area) {
        this.area = area;
        player = Players.getLocal();
    }

    @Override
    public boolean hasReached(AbstractScript script) {
        return area.contains(player);
    }

    @Override
    public void walkTo(AbstractScript script) {
        Walking.walk(area.getCenter());
    }

    public boolean isOnScreen(AbstractScript script) {
        return area.contains(player) && area.getCenter().distance(player) <= 7; // Approximation
    }
}