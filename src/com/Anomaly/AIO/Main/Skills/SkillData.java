package com.Anomaly.AIO.Main.Skills;

import com.Anomaly.AIO.Helpers.Requirements.Fishing.FishType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum SkillData {
    WOODCUTTING(
            Map.of(
                    "Lumbridge", Arrays.asList("Tree", "Oak", "Willow", "Yew"),
                    "Draynor Village", Arrays.asList("Willow"),
                    "Varrock", Arrays.asList("Maple", "Yew"),
                    "Falador", Arrays.asList("Yew", "Magic")
            )
    ),
    FISHING(
            Map.of(
                    "Lumbridge Swamp", Arrays.asList("Shrimp", "Anchovies"),
                    "Barbarian Village", Arrays.asList("Trout", "Salmon"),
                    "Karamja", Arrays.asList("Lobster", "Swordfish", "Tuna", "Shrimp", "Anchovies", "Sardine", "Herring")
            )
    );
    // Add other skills here

    private final Map<String, List<String>> locationsAndMethods;

    SkillData(Map<String, List<String>> locationsAndMethods) {
        this.locationsAndMethods = locationsAndMethods;
    }

    public Map<String, List<String>> getLocationsAndMethods() {
        return locationsAndMethods;
    }
    }