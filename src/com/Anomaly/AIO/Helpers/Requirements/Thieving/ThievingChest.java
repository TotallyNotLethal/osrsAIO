package com.Anomaly.AIO.Helpers.Requirements.Thieving;

public enum ThievingChest implements ThievableEntity {
    TEN_COIN_CHEST("10 coin chest", 13),
    NATURE_RUNE_CHEST("Nature rune chest", 28),
    ISLE_OF_SOULS_CHEST("Isle of Souls chest", 28),
    FIFTY_COIN_CHEST("50 coin chest", 43),
    STEEL_ARROWTIPS_CHEST("Steel arrowtips chest", 47),
    DORGESH_KAAN_AVERAGE_CHEST("Dorgesh-Kaan average chest", 52),
    BLOOD_RUNES_CHEST("Blood runes chest", 59),
    STONE_CHEST("Stone chest", 64),
    ARDOUGNE_CASTLE_CHEST("Ardougne Castle chest", 72),
    DORGESH_KAAN_RICH_CHEST("Dorgesh-Kaan rich chest", 78),
    ROGUES_CASTLE_CHEST("Rogues' Castle chest", 84);

    private final String displayName;
    private final int levelRequirement;

    ThievingChest(String displayName, int levelRequirement) {
        this.displayName = displayName;
        this.levelRequirement = levelRequirement;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLevelRequirement() {
        return levelRequirement;
    }

    @Override
    public boolean requiresLockpick() {
        return false;
    }

    public ThievingEntity getEntityType() {
        return ThievingEntity.CHEST;
    }

    public static ThievingChest getChestForLevel(int thievingLevel) {
        for (ThievingChest chest : values()) {
            if (thievingLevel >= chest.getLevelRequirement()) {
                return chest;
            }
        }
        return TEN_COIN_CHEST;
    }

}

