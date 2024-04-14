package com.Anomaly.AIO.Helpers.Requirements.Mining;

import org.dreambot.api.methods.world.World;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.utilities.Logger;

public enum PickaxeType {
    BRONZE("Bronze Pickaxe", 1, false),
    IRON("Iron Pickaxe", 1, false),
    STEEL("Steel Pickaxe", 5, false),
    BLACK("Black Pickaxe", 10, false),
    MITHRIL("Mithril Pickaxe", 20, false),
    ADAMANT("Adamant Pickaxe", 30, false),
    RUNE("Rune Pickaxe", 40, false),
    DRAGON("Dragon Pickaxe", 60, true);
    //INFERNAL("Infernal Pickaxe", 61, true, 1.85),
    //CRYSTAL("Crystal Pickaxe", 70, true, 2.0);

    private final String displayName;
    private final int levelRequirement;
    private final boolean isMembersOnly;

    PickaxeType(String displayName, int levelRequirement, boolean isMembersOnly) {
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

    public static PickaxeType getBestPickaxeForLevel(int miningLevel, boolean isMember) {
        PickaxeType bestPickaxe = BRONZE;
        for (PickaxeType pickaxe : values()) {
            Logger.log("Checking Pickaxe: " + pickaxe);
            if (miningLevel >= pickaxe.getLevelRequirement() && Worlds.getCurrent().isMembers() == pickaxe.isMembersOnly && (isMember || !pickaxe.isMembersOnly()) && pickaxe.getLevelRequirement() > bestPickaxe.getLevelRequirement()) {
                bestPickaxe = pickaxe;
                Logger.log("Best Pickaxe:" + bestPickaxe);
            }
        }
        return bestPickaxe;
    }
}