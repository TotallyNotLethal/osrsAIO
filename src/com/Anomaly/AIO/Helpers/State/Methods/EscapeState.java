package com.Anomaly.AIO.Helpers.State.Methods;

import com.Anomaly.AIO.Helpers.State.State;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.Player;

public class EscapeState implements State {
    private final AbstractScript script;
    private final Player player;
    private final Area currentArea;
    boolean hasHealingItems, hasPrayerPots;

    public EscapeState(AbstractScript script, Area currentArea) {
        this.script = script;
        this.currentArea = currentArea;
        this.player = Players.getLocal();
    }

    @Override
    public int execute() {
        int health = Skills.getBoostedLevel(Skill.HITPOINTS);
        int prayer = Skills.getBoostedLevel(Skill.PRAYER);
        hasHealingItems = Inventory.contains(item -> item != null && item.hasAction("Eat"));
        hasPrayerPots = Inventory.contains(item -> item != null && item.getName().contains("Prayer potion"));

        if ((prayer == 0 && !hasPrayerPots) || (health < 70 && !hasHealingItems)) {
                Inventory.get(item -> item != null && item.getName().contains("teleport")).interact("Break");
                Sleep.sleep(6000);
            return 600;
        }
        return 60;
    }

    @Override
    public boolean isComplete() {
        return true;
    }
}
