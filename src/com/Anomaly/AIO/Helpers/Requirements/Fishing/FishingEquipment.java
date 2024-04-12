package com.Anomaly.AIO.Helpers.Requirements.Fishing;

public enum FishingEquipment {
    SMALL_NET("Small fishing net"),
    FISHING_ROD("Fishing rod"),
    FLY_FISHING_ROD("Fly fishing rod"),
    HARPOON("Harpoon"),
    LOBSTER_POT("Lobster pot"),
    OILY_FISHING_ROD("Oily fishing rod"),
    BARBARIAN_ROD("Barbarian rod"),
    KARAMBWAN_VESSEL("Karambwan vessel"),
    BIG_NET("Big net"),
    DRIFT_NET("Drift net"),
    CORMORANTS_GLOVE("Cormorant's glove");

    private final String displayName;

    FishingEquipment(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
