package com.Anomaly.AIO.Helpers.Interactions;

import com.Anomaly.AIO.Helpers.Requirements.Fishing.FishType;
import com.Anomaly.AIO.Helpers.Requirements.Fishing.FishingEquipment;
import com.Anomaly.AIO.Helpers.Requirements.Fishing.FishingRequirements;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.Item;

public class FishingInteractions {
    private final AbstractScript script;
    private final FishType fishType;

    public FishingInteractions(AbstractScript script, FishType fishType) {
        this.script = script;
        this.fishType = fishType;
    }

    public boolean performFishing() {
        FishingEquipment[] requiredEquipments = FishingRequirements.getRequiredEquipment(fishType);

        for (FishingEquipment equipment : requiredEquipments) {
            Item item = Inventory.get(i -> i != null && i.getName().contains(equipment.name()));
            if (item != null) {
                NPC fishingSpot = NPCs.closest(npc -> npc != null && npc.hasAction(getAction(equipment)));
                if (fishingSpot != null) {
                    fishingSpot.interact(getAction(equipment));
                    Sleep.sleepUntil(() -> !Players.getLocal().isAnimating(), 5000);
                    return true;
                }
            }
        }
        return false;
    }

    public static String getAction(FishingEquipment equipment) {
        return switch (equipment) {
            case SMALL_NET, BIG_NET -> "Net";
            case FISHING_ROD, BARBARIAN_ROD -> "Bait";
            case FLY_FISHING_ROD -> "Lure";
            case HARPOON -> "Harpoon";
            case LOBSTER_POT -> "Cage";
            case KARAMBWAN_VESSEL -> "Vessel";
            case DRIFT_NET -> "Use-Drift Net";
            //case FISHING_BAIT -> "Bait";
            //case FEATHER -> "Lure";
            //case SANDWORMS -> "Bait";
            //case DARK_FISHING_BAIT -> "Bait";
            //case STRIPY_FEATHER -> "Lure";
            default -> "Fish";
        };
    }

    public static String getAction(FishType fish) {
        return switch (fish) {
            case SHRIMP, KARAMBWANJI, ANCHOVIES, GUPPY, CAVEFISH, TETRA, MONKFISH, MINNOW, MACKEREL, FISH_SHOAL -> "Net";
            case SARDINE, HERRING, PIKE, SLIMY_EEL, CAVE_EEL, ANGLERFISH, SACRED_EEL, LAVA_EEL, INFERNAL_EEL -> "Bait";
            case TROUT, SALMON, RAINBOW_FISH -> "Lure";
            case LEAPING_TROUT, LEAPING_SALMON, LEAPING_STURGEON -> "Rod";
            case TUNA, SWORDFISH, SHARK -> "Harpoon";
            case LOBSTER, DARK_CRAB -> "Cage";
            case BLUEGILL, COMMON_TENCH, MOTTLED_EEL, GREATER_SIREN -> "Fishing spot";
            case KARAMBWAN -> "Fish";
            default -> "Fish";
        };
    }
}