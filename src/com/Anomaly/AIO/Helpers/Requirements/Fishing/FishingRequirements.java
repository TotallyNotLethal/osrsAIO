package com.Anomaly.AIO.Helpers.Requirements.Fishing;

import java.util.EnumMap;

public class FishingRequirements {
    private static final EnumMap<FishType, FishingEquipment[]> fishToEquipmentMap = new EnumMap<>(FishType.class);

    static {
        fishToEquipmentMap.put(FishType.SHRIMP, new FishingEquipment[]{FishingEquipment.SMALL_NET});
        fishToEquipmentMap.put(FishType.KARAMBWANJI, new FishingEquipment[]{FishingEquipment.SMALL_NET});
        fishToEquipmentMap.put(FishType.ANCHOVIES, new FishingEquipment[]{FishingEquipment.SMALL_NET});
        fishToEquipmentMap.put(FishType.TUNA, new FishingEquipment[]{FishingEquipment.HARPOON});
        fishToEquipmentMap.put(FishType.LOBSTER, new FishingEquipment[]{FishingEquipment.LOBSTER_POT});
        fishToEquipmentMap.put(FishType.SWORDFISH, new FishingEquipment[]{FishingEquipment.HARPOON});
        fishToEquipmentMap.put(FishType.SHARK, new FishingEquipment[]{FishingEquipment.HARPOON});
        fishToEquipmentMap.put(FishType.MACKEREL, new FishingEquipment[]{FishingEquipment.BIG_NET});
        fishToEquipmentMap.put(FishType.TROUT, new FishingEquipment[]{FishingEquipment.FLY_FISHING_ROD});
        fishToEquipmentMap.put(FishType.SALMON, new FishingEquipment[]{FishingEquipment.FLY_FISHING_ROD});
        fishToEquipmentMap.put(FishType.PIKE, new FishingEquipment[]{FishingEquipment.FISHING_ROD});
        fishToEquipmentMap.put(FishType.MONKFISH, new FishingEquipment[]{FishingEquipment.SMALL_NET});
        fishToEquipmentMap.put(FishType.SEA_TURTLE, new FishingEquipment[]{FishingEquipment.BIG_NET});
        fishToEquipmentMap.put(FishType.MANTA_RAY, new FishingEquipment[]{FishingEquipment.BIG_NET});
        fishToEquipmentMap.put(FishType.LEAPING_TROUT, new FishingEquipment[]{FishingEquipment.BARBARIAN_ROD});
        fishToEquipmentMap.put(FishType.LEAPING_SALMON, new FishingEquipment[]{FishingEquipment.BARBARIAN_ROD});
        fishToEquipmentMap.put(FishType.LEAPING_STURGEON, new FishingEquipment[]{FishingEquipment.BARBARIAN_ROD});
        fishToEquipmentMap.put(FishType.MINNOW, new FishingEquipment[]{FishingEquipment.SMALL_NET});
    }

    public static FishingEquipment[] getRequiredEquipment(FishType fishType) {
        return fishToEquipmentMap.getOrDefault(fishType, new FishingEquipment[]{});
    }
}

