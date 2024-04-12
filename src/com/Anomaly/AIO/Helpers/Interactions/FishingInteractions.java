package com.Anomaly.AIO.Helpers.Interactions;

import com.Anomaly.AIO.Helpers.Requirements.Fishing.FishType;
import com.Anomaly.AIO.Helpers.Requirements.Fishing.FishingEquipment;
import com.Anomaly.AIO.Helpers.Requirements.Fishing.FishingRequirements;
import org.dreambot.api.methods.Calculations;
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
        String action = FishingInteractions.getAction(fishType);

        NPC fishingSpot = NPCs.closest(npc -> npc != null && npc.hasAction(action));
        if (fishingSpot != null && Inventory.contains(i -> i != null && i.getName().contains(fishType.name()))) {
            fishingSpot.interact(action);
            Sleep.sleepUntil(() -> !Players.getLocal().isAnimating(), Calculations.random(10000, 15000));
            return true;
        }
        return false;
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