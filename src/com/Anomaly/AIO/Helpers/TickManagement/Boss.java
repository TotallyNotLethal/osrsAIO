package com.Anomaly.AIO.Helpers.TickManagement;

import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.wrappers.interactive.NPC;

import java.util.ArrayList;
import java.util.List;

public abstract class Boss {
    protected String name;
    protected int health;
    protected int attackTickRate;
    protected int distanceFromPlayer;

    public void setBossNPC(NPC bossNPC) {
        this.bossNPC = bossNPC;
    }

    protected NPC bossNPC;
    protected List<BossAction> actions;

    public Boss(NPC bossNPC, String name, int health, int attackTickRate) {
        this.bossNPC = bossNPC;
        this.name = name;
        this.health = health;
        this.attackTickRate = attackTickRate;
        this.actions = new ArrayList<>();
    }

    public abstract BossAction getNextAction(int currentTick);
    public abstract String predictReaction(int currentTick, Tile playerPosition, int ticksAfter);

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getAttackTickRate() {
        return attackTickRate;
    }
}