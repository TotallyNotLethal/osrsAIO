package com.Anomaly.AIO.Helpers.Locations.Fishing;

import com.Anomaly.AIO.Helpers.Locations.Location;
import com.Anomaly.AIO.Helpers.Locations.Spot;
import com.Anomaly.AIO.Helpers.Requirements.Fishing.FishType;
import org.dreambot.api.methods.map.Area;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class FishingLocations {
    private static final Map<Location, Map<FishType, Spot>> locations = new EnumMap<>(Location.class);

    static {
        Map<FishType, Spot> lumbridgeSpots = new HashMap<>();
        lumbridgeSpots.put(FishType.SHRIMP, new Spot(new Area(3236, 3157, 3250, 3140), false));
        lumbridgeSpots.put(FishType.TROUT, new Spot(new Area(3200, 3230, 3210, 3220), false));

        Map<FishType, Spot> barbarianVillageSpots = new HashMap<>();
        barbarianVillageSpots.put(FishType.TROUT, new Spot(new Area(3110, 3436, 3101, 3422), false));
        barbarianVillageSpots.put(FishType.SALMON, new Spot(new Area(3110, 3436, 3101, 3422), false));
        barbarianVillageSpots.put(FishType.RAINBOW_FISH, new Spot(new Area(3110, 3436, 3101, 3422), false));
        barbarianVillageSpots.put(FishType.PIKE, new Spot(new Area(3110, 3436, 3101, 3422), false));


        Map<FishType, Spot> catherbySpots = new HashMap<>();
        catherbySpots.put(FishType.LOBSTER, new Spot(new Area(2835, 3430, 2860, 3415), true));
        catherbySpots.put(FishType.SHARK, new Spot(new Area(2835, 3430, 2860, 3415), true));

        Map<FishType, Spot> karamjaSpots = new HashMap<>();
        karamjaSpots.put(FishType.LOBSTER, new Spot(new Area(2922, 3182, 2926, 3175), false));
        karamjaSpots.put(FishType.SWORDFISH, new Spot(new Area(2922, 3182, 2926, 3175), false));
        karamjaSpots.put(FishType.TUNA, new Spot(new Area(2922, 3182, 2926, 3175), false));

        locations.put(Location.LUMBRIDGE, lumbridgeSpots);
        locations.put(Location.CATHERBY, catherbySpots);
        locations.put(Location.KARAMJA, karamjaSpots);
        locations.put(Location.BARBARIAN_VILLAGE, barbarianVillageSpots);
    }

    public static Spot getFishingSpot(Location location, FishType fishType) {
        Map<FishType, Spot> spots = locations.get(location);
        if (spots != null) {
            return spots.get(fishType);
        }
        return null;
    }
}