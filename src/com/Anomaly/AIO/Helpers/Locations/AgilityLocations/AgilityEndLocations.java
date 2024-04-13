package com.Anomaly.AIO.Helpers.Locations.AgilityLocations;

import com.Anomaly.AIO.Helpers.Locations.Location;
import com.Anomaly.AIO.Helpers.Locations.Spot;
import org.dreambot.api.methods.map.Area;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class AgilityEndLocations {

    private static final Map<Location, Map<Location, Spot>> locations = new EnumMap<>(Location.class);

    static {
        Map<Location, Spot> allEndLocationsSpots = new HashMap<>();
        allEndLocationsSpots.put(Location.AL_KHARID, new Spot(new Area(3300, 3196, 3297, 3193), false));
        allEndLocationsSpots.put(Location.VARROCK, new Spot(new Area(3233, 3419, 3238, 3413), false));
        allEndLocationsSpots.put(Location.DRAYNOR_VILLAGE, new Spot(new Area(3098, 3263, 3103, 3257), false));
        allEndLocationsSpots.put(Location.SEERS_VILLAGE, new Spot(new Area(2704, 3467, 2707, 3458), true));
        allEndLocationsSpots.put(Location.ARDOUGNE, new Spot(new Area(2664, 3298, 2670, 3294), true));
        allEndLocationsSpots.put(Location.FALADOR, new Spot(new Area(3022, 3337, 3030, 3330), false));
        allEndLocationsSpots.put(Location.POLLNIVNEACH, new Spot(new Area(3358, 3006, 3362, 2999), true));
        allEndLocationsSpots.put(Location.CANIFIS, new Spot(new Area(3508, 3486, 3517, 3483), true));
        allEndLocationsSpots.put(Location.GNOME_STRONGHOLD, new Spot(new Area(2482, 3437, 2488, 3436), true));
        allEndLocationsSpots.put(Location.WILDERNESS, new Spot(new Area(2990, 3935, 2996, 3931), true));
        allEndLocationsSpots.put(Location.RELLEKKA, new Spot(new Area(2651, 3682, 2658, 3674), true));

        locations.put(Location.ALL, allEndLocationsSpots);
    }

    public static Spot getAgilitySpot(Location location, Location method) {
        Map<Location, Spot> spots = locations.get(location);
        if (spots != null) {
            return spots.get(method);
        }
        return null;
    }
}
