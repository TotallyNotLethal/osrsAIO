package com.Anomaly.AIO.Main.Skills;

import java.util.ArrayList;
import java.util.List;

public class SkillData {
    private List<SkillLocation> skillLocations;

    public SkillData() {
        this.skillLocations = new ArrayList<>();
    }

    public void addSkillLocation(String location, List<String> methods) {
        skillLocations.add(new SkillLocation(location, methods));
    }

    public List<SkillLocation> getSkillLocations() {
        return skillLocations;
    }
}