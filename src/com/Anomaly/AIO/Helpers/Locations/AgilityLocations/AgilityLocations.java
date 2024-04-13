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
        allLocationsSpots.put(Location.SEERS_VILLAGE, new Spot(new Area(2728, 3489, 2730, 3487), true));
        allLocationsSpots.put(Location.ARDOUGNE, new Spot(new Area(2672, 3298, 2675, 3296), true));
        allLocationsSpots.put(Location.FALADOR, new Spot(new Area(3034, 3341, 3038, 3340), true));
        allLocationsSpots.put(Location.POLLNIVNEACH, new Spot(new Area(3350, 2962, 3352, 2961), true));
        allLocationsSpots.put(Location.CANIFIS, new Spot(new Area(3505, 3490, 3508, 3488), true));
        allLocationsSpots.put(Location.RELLEKKA, new Spot(new Area(2623, 3678, 2626, 3677), true));


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
