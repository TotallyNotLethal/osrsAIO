package com.Anomaly.AIO.Helpers.Locations;

import org.dreambot.api.methods.map.Area;

public class Spot {
    private final Area area;
    private final boolean isMembers;

    public Spot(Area area, boolean isMembers) {
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