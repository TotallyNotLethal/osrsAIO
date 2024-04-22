package com.Anomaly.AIO.Helpers.Locations.Thieving;

import com.Anomaly.AIO.Helpers.Locations.Location;
import com.Anomaly.AIO.Helpers.Locations.Spot;
import com.Anomaly.AIO.Helpers.Requirements.Thieving.ThievableEntity;
import com.Anomaly.AIO.Helpers.Requirements.Thieving.ThievingNPC;
import com.Anomaly.AIO.Helpers.Requirements.Thieving.ThievingStall;
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

        Map<ThievableEntity, Spot> miscellaniaSpots = new HashMap<>();
        miscellaniaSpots.put(ThievingStall.VEGETABLE_STALL, new Spot(new Area(2515, 3863, 2518, 3860), true));

        Map<ThievableEntity, Spot> ardougneSpots = new HashMap<>();
        ardougneSpots.put(ThievingStall.BAKERY_STALL, new Spot(new Area(2670, 3310, 2671, 3310), true));
        ardougneSpots.put(ThievingStall.SILK_STALL, new Spot(new Area(2653, 3303, 2654, 3303), true));
        ardougneSpots.put(ThievingStall.FUR_STALL, new Spot(new Area(2664, 3294, 2664, 3294), true));
        ardougneSpots.put(ThievingStall.SILVER_STALL, new Spot(new Area(2657, 3316, 2657, 3316), true));
        ardougneSpots.put(ThievingNPC.FARMER, new Spot( new Area(2643, 3369, 2646, 3367), true));
        ardougneSpots.put(ThievingNPC.KNIGHT_OF_ARDOUGNE, new Spot( new Area(2652, 3316, 2654, 3315), true));
        ardougneSpots.put(ThievingNPC.PALADIN, new Spot( new Area(2654, 3316, 2660, 3306), true));
        ardougneSpots.put(ThievingNPC.HERO, new Spot( new Area(2645, 3308, 2647, 3307), true));

        Map<ThievableEntity, Spot> draynorVillageSpots = new HashMap<>();
        draynorVillageSpots.put(ThievingStall.SEED_STALL, new Spot(new Area(3080, 3256, 3080, 3255), false));
        draynorVillageSpots.put(ThievingStall.WINE_STALL, new Spot(new Area(3085, 3253, 3086, 3253), false));
        draynorVillageSpots.put(ThievingNPC.MASTER_FARMER, new Spot(new Area(3079, 3251, 3081, 3249), false));
        draynorVillageSpots.put(ThievingNPC.MAN, new Spot(new Area(3099, 3278, 3099, 3278), false));



        locations.put(Location.DRAYNOR_VILLAGE, draynorVillageSpots);
        locations.put(Location.ARDOUGNE, ardougneSpots);
        locations.put(Location.MISCELLANIA, miscellaniaSpots);
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
