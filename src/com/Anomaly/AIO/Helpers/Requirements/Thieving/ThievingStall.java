package com.Anomaly.AIO.Helpers.Requirements.Thieving;

public enum ThievingStall implements ThievableEntity {
    VEGETABLE_STALL("Vegetable Stall", 2),
    BAKERY_STALL("Bakery Stall", 5),
    CRAFTING_STALL("Crafting Stall", 5),
    MONKEY_FOOD_STALL("Monkey Food Stall", 5),
    MONKEY_GENERAL_STALL("Monkey General Stall", 5),
    TEA_STALL("Tea Stall", 5),
    SILK_STALL("Silk Stall", 20),
    WINE_STALL("Wine Stall", 22),
    FRUIT_STALL("Fruit Stall", 25),
    SEED_STALL("Seed Stall", 27),
    FUR_STALL("Fur Stall", 35),
    FISH_STALL("Fish Stall", 42),
    CROSSBOW_STALL("Crossbow Stall", 49),
    SILVER_STALL("Silver Stall", 50),
    SPICE_STALL("Spice Stall", 65),
    MAGIC_STALL("Magic Stall", 65),
    SCIMITAR_STALL("Scimitar Stall", 65),
    GEM_STALL("Gem Stall", 75),
    ORE_STALL("Ore Stall", 82);

    private final String displayName;
    private final int levelRequirement;

    ThievingStall(String displayName, int levelRequirement) {
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
        return ThievingEntity.STALL;
    }

    public static ThievingStall getStallForLevel(int thievingLevel) {
        for (ThievingStall stall : values()) {
            if (thievingLevel >= stall.getLevelRequirement()) {
                return stall;
            }
        }
        return VEGETABLE_STALL;
    }
}

