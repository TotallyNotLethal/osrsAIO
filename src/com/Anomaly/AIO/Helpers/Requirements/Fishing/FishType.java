package com.Anomaly.AIO.Helpers.Requirements.Fishing;

public enum FishType {
    SHRIMP("Shrimp"),
    KARAMBWANJI("Karambwanji"),
    ANCHOVIES("Anchovies"),
    GUPPY("Guppy"),
    CAVEFISH("Cavefish"),
    TETRA("Tetra"),
    FROG_SPAWN("Frog spawn"),
    MONKFISH("Monkfish"),
    SEA_TURTLE("Sea turtle"),
    MANTA_RAY("Manta ray"),
    MINNOW("Minnow"),
    MACKEREL("Mackerel"),
    COD("Cod"),
    BASS("Bass"),
    CATFISH("Catfish"),
    FISH_SHOAL("Fish shoal"),
    SARDINE("Sardine"),
    HERRING("Herring"),
    PIKE("Pike"),
    SLIMY_EEL("Slimy eel"),
    CAVE_EEL("Cave eel"),
    ANGLERFISH("Anglerfish"),
    SACRED_EEL("Sacred eel"),
    TROUT("Trout"),
    SALMON("Salmon"),
    RAINBOW_FISH("Rainbow fish"),
    LAVA_EEL("Lava eel"),
    INFERNAL_EEL("Infernal eel"),
    LEAPING_TROUT("Leaping trout"),
    LEAPING_SALMON("Leaping salmon"),
    LEAPING_STURGEON("Leaping sturgeon"),
    TUNA("Tune"),
    SWORDFISH("Swordfish"),
    SHARK("Shark"),
    LOBSTER("Lobster"),
    DARK_CRAB("Dark crab"),
    BLUEGILL("Bluegill"),
    COMMON_TENCH("Common tench"),
    MOTTLED_EEL("Mottled eel"),
    GREATER_SIREN("Greater siren"),
    KARAMBWAN("Karambwan");

    private final String displayName;

    FishType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static FishType byDisplayName(String displayName) {
        for (FishType fishType : FishType.values()) {
            if (fishType.getDisplayName().equalsIgnoreCase(displayName)) {
                return fishType;
            }
        }
        return null;
    }
}
