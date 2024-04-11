package com.Anomaly.AIO.Helpers.Destination;

import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.wrappers.interactive.Player;

public interface Destination {
    boolean hasReached(AbstractScript script);
    void walkTo(AbstractScript script);

    boolean isOnScreen();
    boolean contains(Player player);
    Tile getTile();
}