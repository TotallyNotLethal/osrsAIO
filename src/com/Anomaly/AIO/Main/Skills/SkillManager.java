package com.Anomaly.AIO.Main.Skills;

import java.util.*;

public class SkillManager {
    private final Map<String, Map<String, List<String>>> skillsData = new HashMap<>();

    public SkillManager() {
        loadSkillsData();
    }

    private void loadSkillsData() {
        for (SkillData skillData : SkillData.values()) {
            skillsData.put(skillData.name(), skillData.getLocationsAndMethods());
        }
    }

    public Set<String> getSkills() {
        return skillsData.keySet();
    }

    public List<String> getLocations(String skill) {
        Map<String, List<String>> locationsAndMethods = skillsData.get(skill);
        return locationsAndMethods != null ? new ArrayList<>(locationsAndMethods.keySet()) : Collections.emptyList();
    }

    public List<String> getMethods(String skill, String location) {
        Map<String, List<String>> locationsAndMethods = skillsData.get(skill);
        if (locationsAndMethods != null) {
            List<String> methods = locationsAndMethods.get(location);
            return methods != null ? methods : Collections.emptyList();
        }
        return Collections.emptyList();
    }
}