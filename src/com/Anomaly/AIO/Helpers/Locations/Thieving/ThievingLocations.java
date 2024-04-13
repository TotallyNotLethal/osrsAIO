package com.Anomaly.AIO.Helpers.Locations.Thieving;

import com.Anomaly.AIO.Helpers.Locations.Location;
import com.Anomaly.AIO.Helpers.Locations.Spot;
import com.Anomaly.AIO.Helpers.Requirements.Thieving.ThievableEntity;
import com.Anomaly.AIO.Helpers.Requirements.Thieving.ThievingNPC;
import org.dreambot.api.methods.map.Area;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class ThievingLocations {
    private static final Map<Location, Map<ThievableEntity, Spot>> locations = new EnumMap<>(Location.class);

    static {
        Map<ThievableEntity, Spot> lumbridgeSpots = new HashMap<>();
        lumbridgeSpots.put(ThievingNPC.MAN, new Spot(new Area(3217, 3226, 3226, 3211), false));
        lumbridgeSpots.put(ThievingNPC.WOMAN, new Spot(new Area(3217, 3226, 3226, 3211), false));
        // ... Add other NPCs, stalls, chests, and doors as needed

        locations.put(Location.LUMBRIDGE, lumbridgeSpots);
    }

    public static Spot getThievingSpot(Location location, ThievableEntity entity) {
        Map<ThievableEntity, Spot> spots = locations.get(location);
        if (spots != null) {
            return spots.get(entity);
        }
        return null;
    }
}
