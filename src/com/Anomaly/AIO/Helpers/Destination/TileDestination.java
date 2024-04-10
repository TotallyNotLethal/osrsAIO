package com.Anomaly.AIO.Helpers.Destination;

import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.methods.walking.impl.Walking;

public abstract class TileDestination implements Destination {
    private final Tile tile;
    private final Player player;

    public TileDestination(Tile tile) {
        this.tile = tile;
        player = Players.getLocal();
    }

    @Override
    public boolean hasReached(AbstractScript script) {
        return tile.distance(player) < 2;
    }

    @Override
    public void walkTo(AbstractScript script) {
        Walking.walk(tile);
    }

    public boolean isOnScreen(AbstractScript script) {
        return tile.distance(player) <= 7; // Adjust distance as needed
    }
}