package com.Anomaly.AIO.Helpers.Locations.AgilityLocations;

import com.Anomaly.AIO.Helpers.Locations.Location;
import com.Anomaly.AIO.Helpers.Locations.Spot;
import org.dreambot.api.methods.map.Area;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class AgilityLocations {

    private static final Map<Location, Map<Location, Spot>> locations = new EnumMap<>(Location.class);

    static {
        Map<Location, Spot> allLocationsSpots = new HashMap<>();
        allLocationsSpots.put(Location.AL_KHARID, new Spot(new Area(3270, 3197, 3276, 3195), false));
        allLocationsSpots.put(Location.VARROCK, new Spot(new Area(3223, 3416, 3221, 3412), false));
        allLocationsSpots.put(Location.DRAYNOR_VILLAGE, new Spot(new Area(3103, 3281, 3105, 3277), false));

        locations.put(Location.ALL, allLocationsSpots);
    }

    public static Spot getAgilitySpot(Location location, Location method) {
        Map<Location, Spot> spots = locations.get(location);
        if (spots != null) {
            return spots.get(method);
        }
        return null;
    }
}
