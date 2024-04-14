package com.Anomaly.AIO.Helpers.Requirements.Mining;

public enum OreType {
    RUNE_ESSENCE("Rune essence", 1, false),
    COPPER("Copper", 1, false),
    TIN("Tin", 1, false),
    CLAY("Clay rocks", 1, false),
    SANDSTONE("Sandstone", 1, false),
    IRON("Iron", 15, false),
    SILVER("Silver", 20, true),
    COAL("Coal", 30, false),
    GOLD("Gold", 40, false),
    GRANITE("Granite", 45, false),
    MITHRIL("Mithril", 55, false),
    BLURITE("Blurite", 65, true),
    ADAMANTITE("Adamantite", 70, false),
    CONCENTRATED_GOLD("Concentrated gold rocks", 77, true),
    NECRITE("Necrite", 80, true),
    RUNITE("Runite", 85, false),
    CONCENTRATED_COAL("Concentrated coal rocks", 82, true),
    AMETHYST("Amethyst", 92, true),
    BANE("Bane", 90, true),
    LUMINITE("Luminite", 99, true);

    private final String displayName;
    private final int requiredLevel;
    private final boolean isMembersOnly;

    OreType(String displayName, int requiredLevel, boolean isMembersOnly) {
        this.displayName = displayName;
        this.requiredLevel = requiredLevel;
        this.isMembersOnly = isMembersOnly;
    }

    public static OreType byDisplayName(String displayName) {
        for (OreType oreType : OreType.values()) {
            if (oreType.getDisplayName().equalsIgnoreCase(displayName)) {
                return oreType;
            }
        }
        return null;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public boolean isMembersOnly() {
        return isMembersOnly;
    }
}