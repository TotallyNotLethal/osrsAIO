package com.Anomaly.AIO.Helpers.Requirements.Thieving;

public interface ThievableEntity {
    String getDisplayName();
    int getLevelRequirement();
    boolean requiresLockpick();
    ThievingEntity getEntityType();
}