package com.Anomaly.AIO.Helpers.Locations.Woodcutting;

import com.Anomaly.AIO.Helpers.Locations.Location;
import com.Anomaly.AIO.Helpers.Locations.Spot;
import com.Anomaly.AIO.Helpers.Requirements.Woodcutting.TreeType;
import org.dreambot.api.methods.map.Area;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class WoodcuttingLocations {
    private static final Map<Location, Map<TreeType, Spot>> locations = new EnumMap<>(Location.class);

    static {
        Map<TreeType, Spot> lumbridgeTrees = new HashMap<>();
        lumbridgeTrees.put(TreeType.TREE, new Spot(new Area(3184, 3227, 3197, 3213), false));
        lumbridgeTrees.put(TreeType.OAK, new Spot(new Area(3205, 3225, 3215, 3215), false));
        lumbridgeTrees.put(TreeType.WILLOW, new Spot(new Area(3190, 3270, 3195, 3275), false));
        lumbridgeTrees.put(TreeType.YEW, new Spot( new Area(3248, 3203, 3251, 3200), false));

        Map<TreeType, Spot> draynorVillageTrees = new HashMap<>();
        draynorVillageTrees.put(TreeType.WILLOW, new Spot(new Area(3081, 3240, 3091, 3225), false));

        Map<TreeType, Spot> portSarimTrees = new HashMap<>();
        portSarimTrees.put(TreeType.WILLOW, new Spot(new Area(3056, 3256, 3064, 3250), false));

        Map<TreeType, Spot> varrockTrees = new HashMap<>();
        varrockTrees.put(TreeType.MAPLE, new Spot(new Area(3220, 3500, 3225, 3505), true));
        varrockTrees.put(TreeType.YEW, new Spot(new Area(3200, 3500, 3205, 3505), true));

        Map<TreeType, Spot> faladorTrees = new HashMap<>();
        faladorTrees.put(TreeType.YEW, new Spot(new Area(3000, 3315, 3005, 3320), false));
        faladorTrees.put(TreeType.MAGIC, new Spot(new Area(2990, 3310, 2995, 3315), false));

        locations.put(Location.LUMBRIDGE, lumbridgeTrees);
        locations.put(Location.DRAYNOR_VILLAGE, draynorVillageTrees);
        locations.put(Location.PORT_SARIM, portSarimTrees);
        locations.put(Location.VARROCK, varrockTrees);
        locations.put(Location.FALADOR, faladorTrees);
    }

    public static Spot getWoodcuttingSpot(Location location, TreeType treeType) {
        Map<TreeType, Spot> spots = locations.get(location);
        if (spots != null) {
            return spots.get(treeType);
        }
        return null;
    }
}