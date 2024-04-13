package com.Anomaly.AIO.Helpers.Requirements.Thieving;

public enum ThievingDoor implements ThievableEntity {
    DOOR_TO_10_COIN_CHEST("Door to 10 Coin Chest", 1, false),
    HAM_JAIL_DOOR("H.A.M. Jail Door", 1,  false),
    HAM_TRAPDOOR("H.A.M. Trapdoor", 1, false),
    DOOR_TO_ROSS_HOUSE("Door to Ross's House", 13, false),
    DOOR_TO_NATURE_RUNE_CHEST("Door to Nature Rune Chest", 16, false),
    MAGIC_AXE_HUT_DOOR("Magic Axe Hut Door", 23, true),
    ARDOUGNE_SEWERS_GATE("Ardougne Sewers Gate", 31, false),
    PIRATES_HIDEOUT_DOOR("Pirates' Hideout Door", 39, true),
    CHAOS_DRUID_TOWER_DOOR("Chaos Druid Tower Door", 46, false),
    UNDERGROUND_PASS_GATE("Underground Pass Gate", 50, false),
    ANCIENT_GATE("Ancient Gate", 50, true),
    GRUBBY_DOOR("Grubby Door", 57, false),
    PALADIN_DOOR("Paladin Door", 61, false),
    YANILLE_DUNGEON_DOOR("Yanille Dungeon Door", 82, true);

    private final String displayName;
    private final int levelRequirement;
    private final boolean requiresLockpick;

    ThievingDoor(String displayName, int levelRequirement, boolean requiresLockpick) {
        this.displayName = displayName;
        this.levelRequirement = levelRequirement;
        this.requiresLockpick = requiresLockpick;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLevelRequirement() {
        return levelRequirement;
    }

    public boolean requiresLockpick() {
        return requiresLockpick;
    }

    public ThievingEntity getEntityType() {
        return ThievingEntity.DOOR;
    }

    public static ThievingDoor getDoorForLevel(int thievingLevel) {
        for (ThievingDoor door : values()) {
            if (thievingLevel >= door.getLevelRequirement()) {
                return door;
            }
        }
        return DOOR_TO_10_COIN_CHEST;
    }
}

