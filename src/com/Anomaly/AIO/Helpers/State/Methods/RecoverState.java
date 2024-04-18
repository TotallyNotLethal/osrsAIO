package com.Anomaly.AIO.Helpers.State.Methods;

import com.Anomaly.AIO.Helpers.State.State;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.Item;

public class RecoverState implements State {
    private final AbstractScript script;
    private final int healthThreshold;
    private final Player player;
    private boolean noFood, noPrayPot;

    public RecoverState(AbstractScript script, int healthThreshold) {
        this.script = script;
        this.healthThreshold = healthThreshold;
        player = Players.getLocal();
    }

    @Override
    public int execute() {
        if (player.getHealthPercent() <= healthThreshold) {
            Item food = Inventory.get(item -> item != null && item.isValid() && item.hasAction("Eat"));
            if (food != null) {
                food.interact("Eat");
            } else noFood = true;
        }

        if(Skills.getBoostedLevel(Skill.STRENGTH) <= Skills.getRealLevel(Skill.STRENGTH)+3) {
            Item combatPot = Inventory.get(i -> i != null && i.isValid() && i.getName().contains("Super combat"));
            if (combatPot != null) {
                combatPot.interact("Drink");
            }
        }


        if(Skills.getBoostedLevel(Skill.PRAYER) <= Skills.getRealLevel(Skill.PRAYER)/2) {
            Item prayerPot = Inventory.get(i -> i != null && i.isValid() && i.getName().contains("Prayer pot"));
            if (prayerPot != null) {
                prayerPot.interact("Drink");
            } else noPrayPot = true;
        }

        return 0;
    }

    @Override
    public boolean isComplete() {
        return Players.getLocal().getHealthPercent() > healthThreshold || noFood || noPrayPot;
    }
}
