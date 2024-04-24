package com.Anomaly.AIO.Main.Skills;

import org.dreambot.api.methods.skills.Skill;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SkillManager {

    public SkillManager() {
    }

    public List<String> getSkills() {
        List<String> skillNames = new ArrayList<>();
        for (Skill skill : Skill.values()) {
            skillNames.add(skill.name());
        }
        return skillNames;
    }

    public List<String> getLocations(String skillName) {
        GUISkill skill = GUISkill.valueOf(skillName.toUpperCase());
        List<String> locations = new ArrayList<>();
        for (SkillLocation location : skill.getSkillData().getSkillLocations()) {
            locations.add(location.getLocation());
        }
        return locations;
    }

    public List<String> getMethods(String skillName, String location) {
        GUISkill skill = GUISkill.valueOf(skillName);
        for (SkillLocation loc : skill.getSkillData().getSkillLocations()) {
            if (loc.getLocation().equals(location)) {
                return loc.getMethods();
            }
        }
        return Collections.emptyList();
    }
}
