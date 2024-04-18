package com.Anomaly.AIO.Helpers.State.Methods;

import com.Anomaly.AIO.Helpers.State.State;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.combat.Combat;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.prayer.Prayers;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.GroundItem;
import org.dreambot.api.wrappers.items.Item;

import java.util.List;

public class InCombatState implements State {
    private final AbstractScript script;
    private final Player player;
    private NPC target;
    private List<NPC> targets;

    public InCombatState(AbstractScript script, NPC target) {
        this.script = script;
        this.target = target;
        this.targets = null;
        player = Players.getLocal();
    }

    public void setTargets(List<NPC> targets) {
        this.targets = targets;
    }
    public void setTarget(NPC target) {
        this.target = target;
    }

    @Override
    public int execute() {
        if (targets != null && !targets.isEmpty()) {
            for (NPC n : targets) {
                if (n != null && n.exists() && n.getHealthPercent() > 0) {
                    if(!player.isInteracting(n))
                        n.interact("Attack");
                    Sleep.sleepUntil(() -> player.isInteracting(n), 600);
                    target = n;
                    break;
                }
            }
        } else if (target != null && target.exists() && target.getHealthPercent() > 0) {
            if(!player.isInteracting(target))
                target.interact("Attack");
            Sleep.sleepUntil(() -> player.isInteracting(target), 600);
        } else {
            return 0;
        }
        return 0;
    }

    @Override
    public boolean isComplete() {
        if(target != null && target.exists()) {
            if(targets != null)
                targets.clear();
            return !target.exists() || player.isInteracting(target);
        }
        else return true;
    }
}