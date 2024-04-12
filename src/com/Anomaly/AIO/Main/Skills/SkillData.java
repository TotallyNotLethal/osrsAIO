package com.Anomaly.AIO.Main.Skills;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public enum SkillData {
    WOODCUTTING(
            Map.of(
                    "Lumbridge", Arrays.asList("Tree", "Oak Tree"),
                    "Draynor Village", Arrays.asList("Willow Tree")
            )
    ),
    FISHING(
            Map.of(
                    "Lumbridge Swamp", Arrays.asList("Shrimp", "Anchovies"),
                    "Barbarian Village", Arrays.asList("Trout", "Salmon")
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