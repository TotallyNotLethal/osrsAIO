package com.Anomaly.AIO.Helpers.Requirements.Woodcutting;

public enum TreeType {
    TREE("Tree", 1, false),
    DYING("Dying tree", 1, false),
    DEAD("Dead tree", 1, false),
    EVERGREEN("Evergreen tree", 1, false),
    JUNGLE("Jungle tree", 1, true),
    ACHEY("Achey tree", 1, true),
    OAK("Oak Tree", 15, false),
    WILLOW("Willow Tree", 30, false),
    TEAK("Teak Tree", 35, true),
    HOLLOW("Hollow Tree", 45, true),
    MAPLE("Maple Tree", 45, false),
    MAHOGANY("Mahogany Tree", 50, true),
    ARTIC("Arctic pine tree", 54, true),
    YEW("Yew Tree", 60, false),
    BLISTERWOOD("Blisterwood tree", 62, true),
    MAGIC("Magic Tree", 75, false),
    ELDER("Elder Tree", 90, true),
    REDWOOD("Redwood Tree", 90, true),
    APPLE("Apple tree", 1, true),
    BANANA("Banana tree", 1, true),
    ORANGE("Orange tree", 1, true),
    CURRY("Curry tree", 1, true),
    PINEAPPLE("Pineapple tree", 1, true),
    PAPAYA("Papaya tree", 1, true),
    PALM("Palm tree", 1, true),
    CALQUAT("Calquat tree", 1, true),
    DRAGONFRUIT("Dragonfruit tree", 1, true),
    SPIRIT("Spirit tree", 1, true);

    private final String displayName;
    private final int requiredLevel;
    private final boolean isMembersOnly;

    TreeType(String displayName, int requiredLevel, boolean isMembersOnly) {
        this.displayName = displayName;
        this.requiredLevel = requiredLevel;
        this.isMembersOnly = isMembersOnly;
    }

    public static TreeType byDisplayName(String displayName) {
        for (TreeType treeType : TreeType.values()) {
            if (treeType.getDisplayName().equalsIgnoreCase(displayName)) {
                return treeType;
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
