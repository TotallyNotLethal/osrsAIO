package com.Anomaly.AIO.Helpers.State.Methods;

import com.Anomaly.AIO.Helpers.State.State;
import org.dreambot.api.Client;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.world.World;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.methods.worldhopper.WorldHopper;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.AccountManager;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldSwitchState implements State {
    private final AbstractScript script;
    private boolean members;
    private final Player player;
    private int currentWorld;
    private boolean isComplete = false;

    public WorldSwitchState(AbstractScript script, boolean members) {
        this.script = script;
        this.members = members;
        this.player = Players.getLocal();
    }

    @Override
    public int execute() {
        Logger.log("Players detected nearby, attempting to switch world.");
        if (switchToWorld(members)) {
            Sleep.sleepUntil(() -> Worlds.getCurrentWorld() != currentWorld && Client.getGameState() == GameState.LOGGED_IN, 20000);
            isComplete = true;
            return 0; // Switch was successful, state completes immediately.
        } else {
            Logger.log("Failed to switch worlds.");
        }
        // No players nearby or switch failed, consider this execution step complete.
        isComplete = true;
        return 6000; // Delay before the next state checks again or moves on.
    }

    private boolean switchToWorld(boolean members) {
        List<World> suitableWorlds;
        if(members) suitableWorlds = Worlds.all(w -> w.isMembers() && !w.isPVP() && !w.isHighRisk() && !w.isSuspicious() && w.isNormal() && Skills.getTotalLevel() >= w.getMinimumLevel());
        else suitableWorlds = Worlds.all(w -> !w.isMembers() && !w.isPVP() && !w.isHighRisk() && !w.isSuspicious() && w.isNormal() && Skills.getTotalLevel() >= w.getMinimumLevel());

        if (!suitableWorlds.isEmpty()) {
            World newWorld = suitableWorlds.get(new Random().nextInt(0, suitableWorlds.size()));
            Logger.log("Switching to world: " + newWorld.getDescription());
            return WorldHopper.hopWorld(newWorld);
        }
        Logger.log("No suitable worlds found.");
        return false;
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }
}
