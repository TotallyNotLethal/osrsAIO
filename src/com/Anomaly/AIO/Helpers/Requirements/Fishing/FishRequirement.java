package com.Anomaly.AIO.Helpers.Requirements.Fishing;

public class FishRequirement {
    private final int requiredLevel;
    private final boolean isMembersOnly;

    public FishRequirement(int requiredLevel, boolean isMembersOnly) {
        this.requiredLevel = requiredLevel;
        this.isMembersOnly = isMembersOnly;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public boolean isMembersOnly() {
        return isMembersOnly;
    }
}