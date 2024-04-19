package com.Anomaly.AIO.Helpers.TickManagement;

import com.Anomaly.AIO.Helpers.State.State;
import com.Anomaly.AIO.Main.SettingsManager;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TickManagerState implements State {
    private int currentTick;

    public void setBoss(Boss boss) {
        this.boss = boss;
    }

    private Boss boss;
    private final Player player;
    private AbstractScript script;
    private static final int FUTURE_TICKS_COUNT = 16;
    private List<String> futureTickActions;
    private boolean complete = false;

    public TickManagerState(AbstractScript script, SettingsManager settings, Boss boss) {
        this.currentTick = 0;
        this.boss = boss;
        this.player = Players.getLocal();
        this.script = script;
        this.futureTickActions = new ArrayList<>(Collections.nCopies(FUTURE_TICKS_COUNT, ""));
        updateFutureActions();
    }

    @Override
    public int execute() {
        updateTick();
        monitorBossAttacks();
        return 0;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    private void updateTick() {
        Sleep.sleep(600);
        currentTick++;
        updateFutureActions();
        executeBossAction();
    }

    private void monitorBossAttacks() {
        if(boss.bossNPC != null) {
            int animation = boss.bossNPC.getAnimation();
            if (animation == 8147 || animation == 4410) {
                Logger.log("Sarachnis is attacking: " + (animation == 8147 ? "Melee" : "Ranged"));
                // Reset the tick counter to synchronize with the boss's attack pattern
                currentTick = boss.getAttackTickRate() - 1; // Set to -1 because it will increment immediately after this check
                updateFutureActions();
            }
        }
    }


    private void updateFutureActions() {
        futureTickActions.clear();
        for (int i = 1; i <= FUTURE_TICKS_COUNT; i++) {
            // Predict boss action only on ticks where an attack is possible
            if ((currentTick + i) % boss.getAttackTickRate() == 0) {
                futureTickActions.add(boss.getNextAction(currentTick + i).getDescription());
            } else {
                // If it's not an attack tick, add idle
                futureTickActions.add("Likely Idle");
            }
        }
    }



    public void planPlayerMove(Tile destination) {
        int moveTicks = calculateMoveTicks(player.getTile(), destination);
        futureTickActions.set(0, "Player moving to " + destination + " in " + moveTicks + " ticks");
        for (int i = 1; i <= moveTicks; i++) {
            if (i < FUTURE_TICKS_COUNT) {
                futureTickActions.set(i, boss.predictReaction(currentTick + i, player.getTile(), i));
            }
        }
    }

    public void planBossMove(NPC bossNPC, Tile destination) {
        int moveTicks = calculateMoveTicks(boss.bossNPC.getTile(), destination);
        futureTickActions.set(0, "Boss moving to " + destination + " in " + moveTicks + " ticks");
        for (int i = 1; i <= moveTicks; i++) {
            if (i < FUTURE_TICKS_COUNT) {
                futureTickActions.set(i, boss.predictReaction(currentTick, player.getTile(), i));
            }
        }
    }

    private int calculateMoveTicks(Tile start, Tile end) {
        double distance = start.distance(end);
        boolean isRunning = Walking.isRunEnabled();
        int tilesPerTick = isRunning ? 2 : 1;
        return (int) Math.ceil(distance / tilesPerTick);
    }

    private void executeBossAction() {
        BossAction action = boss.getNextAction(currentTick);
        if (action != null) {
            Logger.log("Tick: " + currentTick + " - Executing action: " + action.getDescription());
        }
    }

    public List<String> getFutureActions() {
        return futureTickActions;
    }

    public void endState() {
        complete = true;
    }
}
