package com.Anomaly.AIO.Helpers.Locations.Teleports;

import org.dreambot.api.methods.map.Area;

public class TeleportLocation {
    private final String name;
    private final Area area;

    public TeleportLocation(String name, Area area) {
        this.name = name;
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public Area getArea() {
        return area;
    }
}
