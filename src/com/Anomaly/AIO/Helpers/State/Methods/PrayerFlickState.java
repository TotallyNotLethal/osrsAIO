package com.Anomaly.AIO.Helpers.State.Methods;

import com.Anomaly.AIO.Helpers.Animations.NPCAttack;
import com.Anomaly.AIO.Helpers.State.State;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.prayer.Prayer;
import org.dreambot.api.methods.prayer.Prayers;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;

public class PrayerFlickState implements State {
    private final AbstractScript script;
    private final String targetNpcName;
    private NPC npcTarget;
    private boolean activatedPrayer;
    private boolean disablePrayer;

    public void setRequiredPrayer(Prayer requiredPrayer) {
        this.requiredPrayer = requiredPrayer;
    }

    private Prayer requiredPrayer;
    private final Player player;

    public PrayerFlickState(AbstractScript script, String targetNpcName) {
        this.script = script;
        this.player = Players.getLocal();
        this.targetNpcName = targetNpcName;
        this.npcTarget = determineTarget();
    }

    public PrayerFlickState(AbstractScript script, boolean disablePrayer) {
        this.script = script;
        this.player = Players.getLocal();
        this.targetNpcName = "";
        this.disablePrayer = disablePrayer;
        this.npcTarget = determineTarget();
    }

    private NPC determineTarget() {
        //if (targetNpcName != null && !targetNpcName.isEmpty()) {
        //    return NPCs.closest(npc -> npc.getName().equalsIgnoreCase(targetNpcName) && npc.isInCombat() && npc.isInteracting(player));
        //}
        return NPCs.closest(npc -> npc.getName().equalsIgnoreCase(targetNpcName));
    }

    @Override
    public int execute() {
        if(disablePrayer || npcTarget == null) {
            disablePrayers();
            disablePrayer = false;
            return 0;
        }

        if (npcTarget == null) {
            npcTarget = determineTarget();
            /*if (npcTarget == null) {
                if (activatedPrayer) {
                    Prayers.toggle(true, Prayers.getActive()[0]);
                    activatedPrayer = false;
                }
                return 0;
            }*/
        }

        NPCAttack npcAnimation = NPCAttack.findByNpcName(npcTarget.getName());
        if (npcAnimation != null) {
            int currentAnimation = npcTarget.getAnimation();
            requiredPrayer = npcAnimation.getRequiredPrayer(currentAnimation);
            if (requiredPrayer != null && !Prayers.isActive(requiredPrayer)) {
                Logger.log("Toggling: " + requiredPrayer.toString());
                Prayers.toggle(true, requiredPrayer);
                Sleep.sleepUntil(() -> Prayers.isActive(requiredPrayer), 1000);
                activatedPrayer = true;
            }
        }
        return 0;
    }

    public boolean disablePrayers() {
        for (Prayer prayer : Prayers.getActive())
            Prayers.toggle(false, prayer);
        return Prayers.getActive().length == 0;
    }

    @Override
    public boolean isComplete() {
        return true;
    }
}
