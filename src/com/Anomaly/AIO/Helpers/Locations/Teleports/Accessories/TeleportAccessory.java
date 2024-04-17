package com.Anomaly.AIO.Helpers.Locations.Teleports.Accessories;

import com.Anomaly.AIO.Helpers.Locations.Teleports.TeleportLocation;
import org.dreambot.api.methods.map.Area;

public enum TeleportAccessory {
    // Games Necklace
    BARBARIAN_ASSAULT("Games necklace", new TeleportLocation("Barbarian Assault", new Area(2516, 3584, 2543, 3564))),
    BURTHORPE_GAMES_ROOM("Games necklace", new TeleportLocation("Burthorpe Games Room", new Area(2938, 3525, 2879, 3580))),
    TEARS_OF_GUTHIX("Games necklace", new TeleportLocation("Tears of Guthix", new Area(2487, 9400, 2493, 9394))),
    CORPORAL_BEAST("Games necklace", new TeleportLocation("Corporal Beast", new Area(2977, 4374, 2981, 4370))),
    WINTERTODT_CAMP("Games necklace", new TeleportLocation("Wintertodt Camp", new Area(1667, 3924, 1595, 4035))),

    // Ring of Dueling
    EMIRS_ARENA("Ring of dueling", new TeleportLocation("Emir's Arena", new Area(3393, 3201, 3311, 3266))),
    FEROX_ENCLAVE("Ring of dueling", new TeleportLocation("Ferox Enclave", new Area(3155, 3609, 3122, 3648))),
    CASTLE_WARS("Ring of dueling", new TeleportLocation("Castle Wars", new Area(2448, 3078, 2435, 3099))),
    FORTIS_COLOSSEUM("Ring of dueling", new TeleportLocation("Fortis Colosseum", new Area(3355, 3285, 3382, 3220))), // Placeholder area

    // Combat Bracelet
    WARRIORS_GUILD("Combat braclet", new TeleportLocation("Warriors' Guild", new Area(2876, 3531, 2836, 3556))),
    CHAMPIONS_GUILD("Combat braclet", new TeleportLocation("Champions' Guild", new Area(3199, 3348, 3184, 3364))),
    EDGEVILLE_MONASTERY("Combat braclet", new TeleportLocation("Edgeville Monastery", new Area(3062, 3480, 3041, 3509))),
    RANGING_GUILD("Combat braclet", new TeleportLocation("Ranging Guild", new Area(2686, 3410, 2650, 3446))),

    // Skills Necklace
    FISHING_GUILD("Skills necklace", new TeleportLocation("Fishing Guild", new Area(2623, 3394, 2579, 3425))),
    MINING_GUILD("Skills necklace", new TeleportLocation("Mining Guild", new Area(3024, 3336, 3014, 3342))),
    CRAFTING_GUILD("Skills necklace", new TeleportLocation("Crafting Guild", new Area(2943, 3265, 2912, 3292))),
    COOKS_GUILD("Skills necklace", new TeleportLocation("Cooking Guild", new Area(3134, 3456, 3151, 3439))),
    WOODCUTTING_GUILD("Skills necklace", new TeleportLocation("Woodcutting Guild", new Area(1561, 3468, 1658, 3516))),
    FARMING_GUILD("Skills necklace", new TeleportLocation("Farming Guild", new Area(1274, 3719, 1219, 3765))),

    // Amulet of Glory
    EDGEVILLE_GLORY("Amulet of glory", new TeleportLocation("Edgeville", new Area(3074, 3514, 3102, 3464))),
    KARAMJA_GLORY("Amulet of glory", new TeleportLocation("Karamja", new Area(2941, 3142, 2875, 3179))),
    DRAYNOR_VILLAGE_GLORY("Amulet of glory", new TeleportLocation("Draynor Village", new Area(3111, 3235, 3073, 3282))),
    AL_KHARID_GLORY("Amulet of glory", new TeleportLocation("Al Kharid", new Area(3319, 3139, 3268, 3214))),

    // Ring of Wealth
    MISCELLANIA("Ring of wealth", new TeleportLocation("Miscellania", new Area(2494, 3840, 2584, 3901))),
    GRAND_EXCHANGE("Ring of wealth", new TeleportLocation("Grand Exchange", new Area(3184, 3468, 3143, 3507))),
    FALADOR_PARK("Ring of wealth", new TeleportLocation("Falador Park", new Area(3020, 3367, 2982, 3389))),

    // Slayer Ring
    SLAYER_TOWER("Slayer ring", new TeleportLocation("Slayer Tower", new Area(3453, 3531, 3404, 3579))),
    FREMENNIK_SLAYER_DUNGEON("Slayer ring", new TeleportLocation("Fremennik Slayer Dungeon", new Area(2812, 9989, 2690, 10039))),
    STRONGHOLD_SLAYER_CAVE("Slayer ring", new TeleportLocation("Stronghold Slayer Cave", new Area(2494, 9764, 2380, 9836))),

    // Digsite Pendant
    DIGSITE("Digsite pendant", new TeleportLocation("Digsite", new Area(3379, 3391, 3331, 3442))),
    FOSSIL_ISLAND("Digsite pendant", new TeleportLocation("Fossil Island", new Area(3837, 3728, 3642, 3899))),
    LITHKREN("Digsite pendant", new TeleportLocation("Lithkren", new Area(3596, 3971, 3540, 4025))),

    // Necklace of Passage
    WIZARDS_TOWER("Necklace of passage", new TeleportLocation("Wizards' Tower", new Area(3122, 3148, 3095, 3175))),

    // Burning Amulet
    CHAOS_TEMPLE("Burning amulet", new TeleportLocation("Chaos Temple", new Area(2940, 3511, 2928, 3519))),
    BANDIT_CAMP("Burning amulet", new TeleportLocation("Bandit Camp", new Area(3056, 3686, 3022, 3709))),
    LAVA_MAZE("Burning amulet", new TeleportLocation("Lava Maze", new Area(3118, 3824, 3022, 3883))),

    // Xeric Talisman

    XERICS_GLADE("Xeric's talisman", new TeleportLocation("Xeric's Glade", new Area(1732, 3569, 1755, 3565)));

    private final TeleportLocation location;
    private final String accessoryName;

    TeleportAccessory(String accessoryName, TeleportLocation teleportLocation) {
        this.accessoryName = accessoryName;
        this.location = teleportLocation;
    }

    public TeleportLocation getLocation() {
        return location;
    }
    public String getAccessoryName() {
        return accessoryName;
    }
}