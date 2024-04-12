package com.Anomaly.AIO.Helpers.Locations.Fishing;

import org.dreambot.api.methods.map.Area;

public class FishingSpot {
    private final Area area;
    private final boolean isMembers;

    public FishingSpot(Area area, boolean isMembers) {
        this.area = area;
        this.isMembers = isMembers;
    }

    public Area getArea() {
        return area;
    }

    public boolean isMembers() {
        return isMembers;
    }
}