package com.Anomaly.AIO.Helpers.Requirements.Fishing;

public enum FishingEquipment {
    SMALL_NET("Small fishing net", 1),
    FISHING_ROD("Fishing rod", 1),
    FLY_FISHING_ROD("Fly fishing rod", 1),
    HARPOON("Harpoon", 1),
    LOBSTER_POT("Lobster pot", 1),
    OILY_FISHING_ROD("Oily fishing rod", 1),
    BARBARIAN_ROD("Barbarian rod", 1),
    KARAMBWAN_VESSEL("Karambwan vessel", 1),
    BIG_NET("Big net", 1),
    DRIFT_NET("Drift net", 1),
    CORMORANTS_GLOVE("Cormorant's glove", 1),
    FISHING_BAIT("Fishing bait", 5000),
    DARK_FISHING_BAIT("Dark fishing bait", 1000),
    FEATHER("Feather", 5000),
    STRIPY_FEATHER("Stripy feather", 1000),
    SANDWORMS("Sandworms", 1),
    RED_VINE_WORM("Red vine worm", 1),
    RAW_KARAMBWANJI("Raw karambwanji", 5000),
    KING_WORM("King worm", 1),
    FISH_CHUNKS("Fish chunks", 1),
    CAVE_WORM("Cave worm", 1);

    private final String displayName;
    private final Integer amount;

    FishingEquipment(String displayName, Integer amount) {
        this.displayName = displayName;
        this.amount = amount;
    }

    public String getDisplayName() {
        return displayName;
    }
    public Integer getAmount() {
        return amount;
    }
}
