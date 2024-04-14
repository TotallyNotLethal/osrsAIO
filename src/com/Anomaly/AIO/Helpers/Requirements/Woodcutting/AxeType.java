package com.Anomaly.AIO.Helpers.Requirements.Woodcutting;

import org.dreambot.api.methods.world.World;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.utilities.Logger;

public enum AxeType {
    BRONZE("Bronze Axe", 1, false),
    IRON("Iron Axe", 1, false),
    STEEL("Steel Axe", 5, false),
    BLACK("Black Axe", 10, false),
    MITHRIL("Mithril Axe", 20, false),
    ADAMANT("Adamant Axe", 30, false),
    RUNE("Rune Axe", 40, false),
    DRAGON("Dragon Axe", 60, true);
    //INFERNAL("Infernal Axe", 61, true, 1.85),
    //CRYSTAL("Crystal Axe", 70, true, 2.0);

    private final String displayName;
    private final int levelRequirement;
    private final boolean isMembersOnly;

    AxeType(String displayName, int levelRequirement, boolean isMembersOnly) {
        this.displayName = displayName;
        this.levelRequirement = levelRequirement;
        this.isMembersOnly = isMembersOnly;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLevelRequirement() {
        return levelRequirement;
    }

    public boolean isMembersOnly() {
        return isMembersOnly;
    }

    public static AxeType getBestAxeForLevel(int woodcuttingLevel, boolean isMember) {
        AxeType bestAxe = BRONZE;
        for (AxeType axe : values()) {
            Logger.log("Checking Axe: " + axe);
            if (woodcuttingLevel >= axe.getLevelRequirement() && Worlds.getCurrent().isMembers() == axe.isMembersOnly && (isMember || !axe.isMembersOnly()) && axe.getLevelRequirement() > bestAxe.getLevelRequirement()) {
                bestAxe = axe;
                Logger.log("Best Axe:" + bestAxe);
            }
        }
        return bestAxe;
    }
}
