package com.Anomaly.AIO.Helpers.Locations.Teleports.Tablets;

import com.Anomaly.AIO.Helpers.Locations.Teleports.TeleportLocation;
import org.dreambot.api.methods.map.Area;

public enum TeleportTablet {
    ARDOUGNE(new TeleportLocation("Ardougne teleport", new Area(2685, 3379, 2559, 3259))),
    CAMELOT(new TeleportLocation("Camelot teleport", new Area(2785, 3518, 2687, 3472))),
    FALADOR(new TeleportLocation("Falador teleport", new Area(2934, 3393, 3062, 3310))),
    LUMBRIDGE(new TeleportLocation("Lumbridge teleport", new Area(3203, 3261, 3248, 3195))),
    VARROCK(new TeleportLocation("Varrock teleport", new Area(3137, 3513, 3314, 3355))),
    KOUNREND(new TeleportLocation("Kourend castle teleport", new Area(1601, 3699, 1693, 3646))),
    WATCHTOWER(new TeleportLocation("Watchtower teleport", new Area(2622, 3120, 2533, 3071))),
    RIMMINGTON(new TeleportLocation("Rimmington teleport", new Area(2924, 3225, 2984, 3198))),
    TAVERLEY(new TeleportLocation("Taverley teleport", new Area(2879, 3478, 2935, 3409))),
    POLLNIVNEACH(new TeleportLocation("Pollnivneach teleport", new Area(3333, 3007, 3377, 2939))),
    //HOSIDIUS(new TeleportLocation("Hosidius teleport", new Area(1718, 3625, 1804, 3554))),
    //RELLEKKA(new TeleportLocation("Rellekka teleport", new Area(2691, 3648, 2615, 3713))),
    BRIMHAVEN(new TeleportLocation("Brimhaven teleport", new Area(2814, 3149, 2731, 3215))),
    YANILLE(new TeleportLocation("Yanille teleport", new Area(2618, 3073, 2538, 3109))),
    //PRIFDDINAS(new TeleportLocation("Prifddinas teleport", new Area(3323, 6023, 3206, 6136))),
    //TROLLHEIM(new TeleportLocation("Trollheim teleport", new Area(2913, 3655, 2870, 3704))),
    ARCEUUS_LIBRARY(new TeleportLocation("Arceuus library teleport", new Area(1665, 3774, 1600, 3836))),
    DRAYNOR_MANOR(new TeleportLocation("Draynor manor teleport", new Area(3124, 3330, 3086, 3384))),
    BATTLEFRONT(new TeleportLocation("Battlefront teleport", new Area(1404, 3689, 1340, 3727))),
    MIND_ALTAR(new TeleportLocation("Mind altar teleport", new Area(2972, 3519, 2990, 3506))),
    SALVE_GRAVEYARD(new TeleportLocation("Salve graveyard teleport", new Area(3440, 3456, 3422, 3469))),
    FENKENSTRAINS_CASTLE(new TeleportLocation("Fenkenstrain's castle teleport", new Area(3565, 3537, 3533, 3567))),
    WEST_ARDOUGNE(new TeleportLocation("West ardougne teleport", new Area(2555, 3281, 2463, 3334))),
    HARMONY_ISLAND(new TeleportLocation("Harmony island teleport", new Area(3839, 2817, 3778, 2878))),
    CEMETERY(new TeleportLocation("Cemetery teleport", new Area(2995, 3734, 2957, 3767))),
    BARROWS(new TeleportLocation("Barrows teleport", new Area(3583, 3269, 3546, 3308))),
    APE_ATOLL(new TeleportLocation("Ape atoll teleport", new Area(2810, 2691, 2695, 2812)));
    private final TeleportLocation location;

    TeleportTablet(TeleportLocation location) {
        this.location = location;
    }

    public TeleportLocation getLocation() {
        return location;
    }
}
