package com.Anomaly.AIO.Helpers.Requirements.Fishing;

import java.util.EnumMap;

public class FishingRequirements {
    private static final EnumMap<FishType, FishingEquipment[]> fishToEquipmentMap = new EnumMap<>(FishType.class);
    private static final EnumMap<FishType, FishRequirement> fishToRequirementMap = new EnumMap<>(FishType.class);

    static {
        fishToEquipmentMap.put(FishType.SHRIMP, new FishingEquipment[]{FishingEquipment.SMALL_NET});
        fishToEquipmentMap.put(FishType.KARAMBWANJI, new FishingEquipment[]{FishingEquipment.SMALL_NET});
        fishToEquipmentMap.put(FishType.GUPPY, new FishingEquipment[]{FishingEquipment.SMALL_NET});
        fishToEquipmentMap.put(FishType.ANCHOVIES, new FishingEquipment[]{FishingEquipment.SMALL_NET});
        fishToEquipmentMap.put(FishType.CAVEFISH, new FishingEquipment[]{FishingEquipment.SMALL_NET});
        fishToEquipmentMap.put(FishType.TETRA, new FishingEquipment[]{FishingEquipment.SMALL_NET});
        fishToEquipmentMap.put(FishType.FROG_SPAWN, new FishingEquipment[]{FishingEquipment.SMALL_NET});
        fishToEquipmentMap.put(FishType.MONKFISH, new FishingEquipment[]{FishingEquipment.SMALL_NET});
        fishToEquipmentMap.put(FishType.MINNOW, new FishingEquipment[]{FishingEquipment.SMALL_NET});
        fishToEquipmentMap.put(FishType.MACKEREL, new FishingEquipment[]{FishingEquipment.BIG_NET});
        fishToEquipmentMap.put(FishType.COD, new FishingEquipment[]{FishingEquipment.BIG_NET});
        fishToEquipmentMap.put(FishType.BASS, new FishingEquipment[]{FishingEquipment.BIG_NET});
        fishToEquipmentMap.put(FishType.CATFISH, new FishingEquipment[]{FishingEquipment.BIG_NET});
        fishToEquipmentMap.put(FishType.SEA_TURTLE, new FishingEquipment[]{FishingEquipment.BIG_NET});
        fishToEquipmentMap.put(FishType.FISH_SHOAL, new FishingEquipment[]{FishingEquipment.DRIFT_NET});
        fishToEquipmentMap.put(FishType.MANTA_RAY, new FishingEquipment[]{FishingEquipment.DRIFT_NET});
        fishToEquipmentMap.put(FishType.SARDINE, new FishingEquipment[]{FishingEquipment.FISHING_ROD});
        fishToEquipmentMap.put(FishType.HERRING, new FishingEquipment[]{FishingEquipment.FISHING_ROD});
        fishToEquipmentMap.put(FishType.PIKE, new FishingEquipment[]{FishingEquipment.FISHING_ROD});
        fishToEquipmentMap.put(FishType.SLIMY_EEL, new FishingEquipment[]{FishingEquipment.FISHING_ROD});
        fishToEquipmentMap.put(FishType.CAVE_EEL, new FishingEquipment[]{FishingEquipment.FISHING_ROD});
        fishToEquipmentMap.put(FishType.LAVA_EEL, new FishingEquipment[]{FishingEquipment.FISHING_ROD});
        fishToEquipmentMap.put(FishType.SACRED_EEL, new FishingEquipment[]{FishingEquipment.FISHING_ROD});
        fishToEquipmentMap.put(FishType.ANGLERFISH, new FishingEquipment[]{FishingEquipment.FISHING_ROD});
        fishToEquipmentMap.put(FishType.TROUT, new FishingEquipment[]{FishingEquipment.FLY_FISHING_ROD});
        fishToEquipmentMap.put(FishType.SALMON, new FishingEquipment[]{FishingEquipment.FLY_FISHING_ROD});
        fishToEquipmentMap.put(FishType.RAINBOW_FISH, new FishingEquipment[]{FishingEquipment.FLY_FISHING_ROD});
        fishToEquipmentMap.put(FishType.LEAPING_TROUT, new FishingEquipment[]{FishingEquipment.BARBARIAN_ROD});
        fishToEquipmentMap.put(FishType.LEAPING_SALMON, new FishingEquipment[]{FishingEquipment.BARBARIAN_ROD});
        fishToEquipmentMap.put(FishType.LEAPING_STURGEON, new FishingEquipment[]{FishingEquipment.BARBARIAN_ROD});
        fishToEquipmentMap.put(FishType.INFERNAL_EEL, new FishingEquipment[]{FishingEquipment.OILY_FISHING_ROD});
        fishToEquipmentMap.put(FishType.LOBSTER, new FishingEquipment[]{FishingEquipment.LOBSTER_POT});
        fishToEquipmentMap.put(FishType.DARK_CRAB, new FishingEquipment[]{FishingEquipment.LOBSTER_POT});
        fishToEquipmentMap.put(FishType.KARAMBWAN, new FishingEquipment[]{FishingEquipment.KARAMBWAN_VESSEL});
        fishToEquipmentMap.put(FishType.TUNA, new FishingEquipment[]{FishingEquipment.HARPOON});
        fishToEquipmentMap.put(FishType.SWORDFISH, new FishingEquipment[]{FishingEquipment.HARPOON});
        fishToEquipmentMap.put(FishType.SHARK, new FishingEquipment[]{FishingEquipment.HARPOON});
        fishToEquipmentMap.put(FishType.BLUEGILL, new FishingEquipment[]{FishingEquipment.CORMORANTS_GLOVE});
        fishToEquipmentMap.put(FishType.MOTTLED_EEL, new FishingEquipment[]{FishingEquipment.CORMORANTS_GLOVE});
        fishToEquipmentMap.put(FishType.COMMON_TENCH, new FishingEquipment[]{FishingEquipment.CORMORANTS_GLOVE});
        fishToEquipmentMap.put(FishType.GREATER_SIREN, new FishingEquipment[]{FishingEquipment.CORMORANTS_GLOVE});

        fishToRequirementMap.put(FishType.SHRIMP, new FishRequirement(1, false));
        fishToRequirementMap.put(FishType.KARAMBWANJI, new FishRequirement(5, true));
        fishToRequirementMap.put(FishType.SARDINE, new FishRequirement(5, false));
        fishToRequirementMap.put(FishType.GUPPY, new FishRequirement(7, false));
        fishToRequirementMap.put(FishType.HERRING, new FishRequirement(10, false));
        fishToRequirementMap.put(FishType.ANCHOVIES, new FishRequirement(15, false));
        fishToRequirementMap.put(FishType.MACKEREL, new FishRequirement(16, true));
        fishToRequirementMap.put(FishType.CAVEFISH, new FishRequirement(20, true));
        fishToRequirementMap.put(FishType.TROUT, new FishRequirement(20, false));
        fishToRequirementMap.put(FishType.COD, new FishRequirement(23, true));
        fishToRequirementMap.put(FishType.PIKE, new FishRequirement(25, false));
        fishToRequirementMap.put(FishType.SLIMY_EEL, new FishRequirement(28, true));
        fishToRequirementMap.put(FishType.SALMON, new FishRequirement(30, false));
        fishToRequirementMap.put(FishType.TETRA, new FishRequirement(33, false));
        fishToRequirementMap.put(FishType.FROG_SPAWN, new FishRequirement(33, true));
        fishToRequirementMap.put(FishType.TUNA, new FishRequirement(35, false));
        fishToRequirementMap.put(FishType.CAVE_EEL, new FishRequirement(38, true));
        fishToRequirementMap.put(FishType.RAINBOW_FISH, new FishRequirement(38, false));
        fishToRequirementMap.put(FishType.LOBSTER, new FishRequirement(40, false));
        fishToRequirementMap.put(FishType.BLUEGILL, new FishRequirement(43, true));
        fishToRequirementMap.put(FishType.BASS, new FishRequirement(46, true));
        fishToRequirementMap.put(FishType.CATFISH, new FishRequirement(46, true));
        fishToRequirementMap.put(FishType.FISH_SHOAL, new FishRequirement(47, true));
        fishToRequirementMap.put(FishType.LEAPING_TROUT, new FishRequirement(48, true));
        fishToRequirementMap.put(FishType.SWORDFISH, new FishRequirement(50, false));
        fishToRequirementMap.put(FishType.LAVA_EEL, new FishRequirement(53, true));
        fishToRequirementMap.put(FishType.COMMON_TENCH, new FishRequirement(56, true));
        fishToRequirementMap.put(FishType.LEAPING_SALMON, new FishRequirement(58, true));
        fishToRequirementMap.put(FishType.MONKFISH, new FishRequirement(62, true));
        fishToRequirementMap.put(FishType.KARAMBWAN, new FishRequirement(65, true));
        fishToRequirementMap.put(FishType.LEAPING_STURGEON, new FishRequirement(70, true));
        fishToRequirementMap.put(FishType.MOTTLED_EEL, new FishRequirement(73, true));
        fishToRequirementMap.put(FishType.SHARK, new FishRequirement(76, true));
        fishToRequirementMap.put(FishType.SEA_TURTLE, new FishRequirement(79, true));
        fishToRequirementMap.put(FishType.INFERNAL_EEL, new FishRequirement(80, true));
        fishToRequirementMap.put(FishType.MINNOW, new FishRequirement(82, true));
        fishToRequirementMap.put(FishType.ANGLERFISH, new FishRequirement(82, true));
        fishToRequirementMap.put(FishType.DARK_CRAB, new FishRequirement(85, true));
        fishToRequirementMap.put(FishType.SACRED_EEL, new FishRequirement(87, true));
        fishToRequirementMap.put(FishType.MANTA_RAY, new FishRequirement(90, true));
        fishToRequirementMap.put(FishType.GREATER_SIREN, new FishRequirement(91, true));
    }

    public static FishingEquipment[] getRequiredEquipment(FishType fishType) {
        return fishToEquipmentMap.getOrDefault(fishType, new FishingEquipment[]{});
    }

    public static FishRequirement getFishRequirement(FishType fishType) {
        return fishToRequirementMap.getOrDefault(fishType, new FishRequirement(1, false));
    }
}

