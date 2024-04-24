package com.Anomaly.AIO.Main.Skills;

import java.util.List;
import java.util.ArrayList;

public class SkillLocation {
    private String location;
    private List<String> methods;

    public SkillLocation(String location, List<String> methods) {
        this.location = location;
        this.methods = methods;
    }

    public String getLocation() {
        return location;
    }

    public List<String> getMethods() {
        return new ArrayList<>(methods);
    }
}