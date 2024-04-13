package com.Anomaly.AIO.Helpers.Requirements.Thieving;

public enum ThievingNPC implements ThievableEntity {
    MAN("Man", 1),
    WOMAN("Woman", 1),
    FARMER("Farmer", 10),
    HAM_MEMBER("H.A.M. Member", 15),
    WARRIOR("Warrior", 25),
    AL_KHARID_WARRIOR("Al Kharid warrior", 25),
    VILLAGER("Villager", 30),
    ROGUE("Rogue", 32),
    CAVE_GOBLIN("Cave goblin", 36),
    MASTER_FARMER("Master Farmer", 38),
    GUARD("Guard", 40),
    FREMENNIK_CITIZEN("Fremennik Citizen", 45),
    BEARDED_POLLNIVNIAN_BANDIT("Bearded Pollnivnian Bandit", 45),
    WEALTHY_CITIZEN("Wealthy citizen", 50),
    DESERT_BANDIT("Desert Bandit", 53),
    KNIGHT_OF_ARDOUGNE("Knight of Ardougne", 55),
    POLLNIVNIAN_BANDIT("Pollnivnian Bandit", 55),
    YANILLE_WATCHMAN("Yanille Watchman", 65),
    MENAPHITE_THUG("Menaphite Thug", 65),
    PALADIN("Paladin", 70),
    GNOME("Gnome", 75),
    HERO("Hero", 80),
    VYRE("Vyrewatch", 82),
    ELF("Elf", 85),
    TZHAAR_HUR("TzHaar-Hur", 90);

    private final String displayName;
    private final int levelRequirement;

    ThievingNPC(String displayName, int levelRequirement) {
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
        return ThievingEntity.NPC;
    }

    public static ThievingNPC getNPCForLevel(int thievingLevel) {
        for (ThievingNPC npc : values()) {
            if (thievingLevel >= npc.getLevelRequirement()) {
                return npc;
            }
        }
        return MAN;
    }
}
