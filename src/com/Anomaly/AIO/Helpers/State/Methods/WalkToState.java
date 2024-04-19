package com.Anomaly.AIO.Helpers.State.Methods;

import com.Anomaly.AIO.Helpers.Destination.Destination;
import com.Anomaly.AIO.Helpers.State.State;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.walking.path.impl.GlobalPath;
import org.dreambot.api.methods.walking.pathfinding.impl.web.WebFinder;
import org.dreambot.api.methods.walking.web.node.AbstractWebNode;
import org.dreambot.api.methods.walking.web.node.CustomWebPath;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.BoundaryObject;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Player;

import java.util.ArrayList;
import java.util.List;

public class WalkToState implements State {
        private final AbstractScript script;
        private final Destination destination;
        private Area area;
        private final Tile tile;
        private final Player player;
        private CustomWebPath customPath;

        public WalkToState(AbstractScript script, CustomWebPath customPath) {
            this.script = script;
            this.customPath = customPath;
            this.destination = null;
            this.tile = customPath.getEnd().getTile();
            this.player = Players.getLocal();
            this.area = customPath.getNodes().get(customPath.getNodes().size() - 1).getTile().getArea(2);
        }

        public WalkToState(AbstractScript script, Destination destination) {
            this.script = script;
            this.destination = destination;
            this.area = destination.getTile().getArea(2);
            this.tile = destination.getTile();
            this.player = Players.getLocal();
        }

        public WalkToState(AbstractScript script, BankLocation bankLocation) {
            this.script = script;
            this.destination = null;
            this.area = bankLocation.getArea(2);
            this.tile = area.getCenter();
            this.player = Players.getLocal();
        }

        public WalkToState(AbstractScript script, Area area) {
            this.script = script;
            this.destination = null;
            this.area = area;
            this.tile = area.getCenter();
            this.player = Players.getLocal();
        }

        public WalkToState(AbstractScript script, Tile tile) {
            this.script = script;
            this.destination = null;
            this.area = tile.getArea(2);
            this.tile = tile;
            this.player = Players.getLocal();
        }

    @Override
    public int execute() {
        if (!area.contains(player)) {
            handlePathObstacles(tile);
            Logger.log("Walking to: " + tile);
            Walking.walk(tile);
            Sleep.sleepUntil(() -> area.contains(player), 3500);
        }
        return area.contains(player) ? 0 : Calculations.random(2000, 3500);
    }

    private void handlePathObstacles(Tile destination) {
        GlobalPath<AbstractWebNode> path = WebFinder.getWebFinder().calculate(player.getTile(), destination);

        if (path == null) {
            Logger.log("No path found to " + destination + ". Unable to proceed.");
            area = player.getSurroundingArea(5);
            return;
        }

        for (AbstractWebNode node : path) {
            List<Tile> surroundingTiles = getSurroundingTiles(node.getTile(), 3);
            for (Tile tile : surroundingTiles) {
                GameObject web = GameObjects.closest(gameObject -> gameObject instanceof BoundaryObject &&
                        gameObject.getName().equals("Web") &&
                        gameObject.getTile().equals(tile));
                if (web != null && !hasSlashedWebNearby(web)) {
                    Walking.walk(web.getTile());
                    Sleep.sleepUntil(() -> !player.isMoving(), 3000);
                    if(web.distance() < 2) {
                        web.interact("Slash");
                        Sleep.sleepUntil(() -> !web.exists(), 3000);
                    }
                    handlePathObstacles(destination);
                    return;
                }
            }
        }
    }

    private boolean hasSlashedWebNearby(GameObject web) {
        List<Tile> nearbyTiles = getSurroundingTiles(web.getTile(), 1);
        for (Tile nearbyTile : nearbyTiles) {
            GameObject nearbyWeb = GameObjects.closest(gameObject -> gameObject instanceof BoundaryObject &&
                    gameObject.getName().equals("Slashed web") &&
                    gameObject.getTile().equals(nearbyTile));
            if (nearbyWeb != null) {
                return true;
            }
        }
        return false;
    }

    private List<Tile> getSurroundingTiles(Tile center, int radius) {
        List<Tile> tiles = new ArrayList<>();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                if (dx != 0 || dy != 0) {
                    tiles.add(new Tile(center.getX() + dx, center.getY() + dy, center.getZ()));
                }
            }
        }
        return tiles;
    }

        @Override
        public boolean isComplete() {
            return area.contains(player);
        }
    }