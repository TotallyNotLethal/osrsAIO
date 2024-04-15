package com.Anomaly.AIO.Helpers.Locations.Mining;

import com.Anomaly.AIO.Helpers.Locations.Location;
import com.Anomaly.AIO.Helpers.Locations.Spot;
import com.Anomaly.AIO.Helpers.Requirements.Mining.OreType;
import org.dreambot.api.methods.map.Area;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class MiningLocations {
    private static final Map<Location, Map<OreType, Spot>> locations = new EnumMap<>(Location.class);

    static {
        Map<OreType, Spot> lumbridgeOres = new HashMap<>();
        lumbridgeOres.put(OreType.TIN, new Spot(new Area(3220, 3150, 3226, 3144), false));
        lumbridgeOres.put(OreType.COPPER, new Spot(new Area(3231, 3143, 3225, 3149), false));



        locations.put(Location.LUMBRIDGE, lumbridgeOres);
    }

    public static Spot getMiningSpot(Location location, OreType oreType) {
        Map<OreType, Spot> spots = locations.get(location);
        if (spots != null) {
            return spots.get(oreType);
        }
        return null;
    }
}