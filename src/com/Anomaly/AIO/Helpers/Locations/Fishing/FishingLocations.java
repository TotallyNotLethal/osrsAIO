package com.Anomaly.AIO.Helpers.Locations.Fishing;

import com.Anomaly.AIO.Helpers.Locations.Location;
import com.Anomaly.AIO.Helpers.Requirements.Fishing.FishType;
import org.dreambot.api.methods.map.Area;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class FishingLocations {
    private static final Map<Location, Map<FishType, FishingSpot>> locations = new EnumMap<>(Location.class);

    static {
        Map<FishType, FishingSpot> lumbridgeSpots = new HashMap<>();
        lumbridgeSpots.put(FishType.SHRIMP, new FishingSpot(new Area(3236, 3157, 3250, 3140), false));
        lumbridgeSpots.put(FishType.TROUT, new FishingSpot(new Area(3200, 3230, 3210, 3220), false));

        Map<FishType, FishingSpot> catherbySpots = new HashMap<>();
        catherbySpots.put(FishType.LOBSTER, new FishingSpot(new Area(2835, 3430, 2860, 3415), true));
        catherbySpots.put(FishType.SHARK, new FishingSpot(new Area(2835, 3430, 2860, 3415), true));

        Map<FishType, FishingSpot> karamjaSpots = new HashMap<>();
        karamjaSpots.put(FishType.LOBSTER, new FishingSpot(new Area(2922, 3182, 2926, 3175), false));
        karamjaSpots.put(FishType.SWORDFISH, new FishingSpot(new Area(2922, 3182, 2926, 3175), false));
        karamjaSpots.put(FishType.TUNA, new FishingSpot(new Area(2922, 3182, 2926, 3175), false));

        locations.put(Location.LUMBRIDGE, lumbridgeSpots);
        locations.put(Location.CATHERBY, catherbySpots);
        locations.put(Location.KARAMJA, karamjaSpots);
    }

    public static FishingSpot getFishingSpot(Location location, FishType fishType) {
        Map<FishType, FishingSpot> spots = locations.get(location);
        if (spots != null) {
            return spots.get(fishType);
        }
        return null;
    }
}