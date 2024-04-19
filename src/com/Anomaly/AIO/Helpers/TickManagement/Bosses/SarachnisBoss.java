package com.Anomaly.AIO.Helpers.TickManagement.Bosses;

import com.Anomaly.AIO.Helpers.TickManagement.Boss;
import com.Anomaly.AIO.Helpers.TickManagement.BossAction;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;

public class SarachnisBoss extends Boss {
    private int attackCounter = 0;
    private int stickyWebCounter = 0;
    private final double healthThresholdOne = 0.66 * this.health;
    private final double healthThresholdTwo = 0.33 * this.health;

    public void setSarachnisNPC(NPC sarachnisNPC) {
        this.sarachnisNPC = sarachnisNPC;
    }

    private NPC sarachnisNPC;
    private final Player player;

    public SarachnisBoss(NPC sarachnisNPC) {
        super(sarachnisNPC, "Sarachnis", 400, 4);
        this.sarachnisNPC = sarachnisNPC;
        this.player = Players.getLocal();
    }

    @Override
    public BossAction getNextAction(int currentTick) {
        if (currentTick % attackTickRate == 0) { // Only attack every 4 ticks
            attackCounter++;
            if (attackCounter % 4 == 0) { // On every 4th attack, it's a special attack
                return new BossAction("Sticky Web and Move Away");
            }
            if (this.health <= healthThresholdOne && this.health > healthThresholdTwo) {
                return new BossAction("Summon Spawns at 66%");
            } else if (this.health <= healthThresholdTwo) {
                return new BossAction("Summon Spawns at 33%");
            }
            if (sarachnisNPC != null && sarachnisNPC.getTile().distance(player.getTile()) > 4) {
                return new BossAction("Ranged Attack - Heal 10");
            } else {
                return new BossAction("Melee Attack - Heal 5");
            }
        }
        return new BossAction("Idle"); // If it's not a tick where Sarachnis can attack, return an Idle action
    }


    @Override
    public String predictReaction(int currentTick, Tile playerPosition, int ticksAfter) {
        int futureTick = currentTick + ticksAfter;
        if (futureTick % attackTickRate == 0) {
            double distance = getPosition().distance(playerPosition);
            if (distance > 4) {
                return "Likely Ranged Attack";
            } else {
                return "Likely Melee Attack";
            }
        }
        return "Likely Idle";
    }

    public Tile getPosition() {
        return sarachnisNPC != null ? sarachnisNPC.getTile() : new Tile(0, 0);
    }
}
