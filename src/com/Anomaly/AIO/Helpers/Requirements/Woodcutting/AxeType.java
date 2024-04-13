package com.Anomaly.AIO.Helpers.Requirements.Woodcutting;

import org.dreambot.api.methods.world.World;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.utilities.Logger;

public enum AxeType {
    BRONZE("Bronze Axe", 1, false, 1.0),
    IRON("Iron Axe", 1, false, 1.1),
    STEEL("Steel Axe", 5, false, 1.2),
    BLACK("Black Axe", 10, false, 1.25),
    MITHRIL("Mithril Axe", 20, false, 1.3),
    ADAMANT("Adamant Axe", 30, false, 1.4),
    RUNE("Rune Axe", 40, false, 1.5),
    DRAGON("Dragon Axe", 60, true, 1.7);
    //INFERNAL("Infernal Axe", 61, true, 1.85),
    //CRYSTAL("Crystal Axe", 70, true, 2.0);

    private final String displayName;
    private final int levelRequirement;
    private final boolean isMembersOnly;
    private final double efficiency;

    AxeType(String displayName, int levelRequirement, boolean isMembersOnly, double efficiency) {
        this.displayName = displayName;
        this.levelRequirement = levelRequirement;
        this.isMembersOnly = isMembersOnly;
        this.efficiency = efficiency;
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

    public double getEfficiency() {
        return efficiency;
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
