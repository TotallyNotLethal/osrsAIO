package com.Anomaly.AIO.Helpers.State.Methods;

import com.Anomaly.AIO.Helpers.Destination.Destination;
import com.Anomaly.AIO.Helpers.State.State;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.Player;

    public class WalkToState implements State {
        private final AbstractScript script;
        private final Destination destination;
        private final Area area;
        private final Tile tile;
        private final Player player;

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
            this.area = tile.getArea(10);
            this.tile = tile;
            this.player = Players.getLocal();
        }

        @Override
        public int execute() {
            if(destination!=null) {
                if (!destination.contains(player)) {
                    Logger.log("Walking to: " + tile);
                    Walking.walk(destination.getTile());
                    Sleep.sleepUntil(() -> destination.contains(player), 3500);
                }
                return destination.contains(player) ? 0 : 1000;
            }
            Logger.log("Walking to: " + tile);
            Walking.walk(tile);
            Sleep.sleepUntil(() -> !player.isAnimating(), Calculations.random(2000,3500));

            return area.contains(player) ? 0 : Calculations.random(200,700);
        }

        @Override
        public boolean isComplete() {
            return area.contains(player);
        }
    }