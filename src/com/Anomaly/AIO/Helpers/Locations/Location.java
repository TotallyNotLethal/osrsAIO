package com.Anomaly.AIO.Helpers.Locations;

public enum Location {
    ALL("All",true),
    LUMBRIDGE("Lumbridge",true),
    BARBARIAN_VILLAGE("Barbarian village",true),
    CATHERBY("Catherby",false),
    KARAMJA("Karamja",true),
    VARROCK("Varrock",true),
    FALADOR("Falador",true),
    SEERS_VILLAGE("Seers village",false),
    ARDOUGNE("Ardougne",false),
    RELLEKKA("Rellekka",false),
    YANILLE("Yanille",false),
    DRAYNOR_VILLAGE("Draynor village",true),
    AL_KHARID("Al kharid",true),
    EDGEVILLE("Edgeville",true),
    TUTORIAL_ISLAND("Tutorial island",true),
    PORT_SARIM("Port sarim",true),
    RIMMINGTON("Rimmington",true),
    TAVERLEY("Taverley",false),
    BURTHORPE("Burthorpe",false),
    CANIFIS("Canifis",false),
    MORYTANIA("Morytania",false),
    PRIFDDINAS("Prifddinas",false),
    ZEAH("Zeah",false),
    GNOME_STRONGHOLD("Gnome stronghold",false),
    WILDERNESS("Wilderness",true),
    ASGARNIA_ICE_DUNGEON("Asgarnia ice dungeon",true),
    CRANDOR_ISLAND("Crandor island",true),
    MUDSKIPPER_POINT("Mudskipper point",true),
    WITCHAVEN("Witchaven",false),
    FISHING_GUILD("Fishing guild",false),
    MINING_GUILD("Mining guild",true),
    CRAFTING_GUILD("Crafting guild",true),
    COOKING_GUILD("Cooking guild",true),
    CHAMPIONS_GUILD("Champions guild",true),
    HEROES_GUILD("Heroes guild",false),
    LEGENDS_GUILD("Legends guild",false),
    PISCATORIS_FISHING_COLONY("Piscatoris fishing colony",false),
    SHILO_VILLAGE("Shilo village",false),
    APE_ATOLL("Ape atoll",false),
    HARMONY_ISLAND("Harmony island",false),
    MOS_LE_HARMLESS("Mos le harmless",false),
    LLETYA("Lletya",false),
    NEITIZNOT("Netiznot",false),
    MISCELLANIA("Miscellania", true),
    JATIZSO("Jatizso",false),
    DAGANNOTH_KINGS("Dagannoth kings",false),
    SMOKE_DUNGEON("Smoke dungeon",false),
    POLLNIVNEACH("Pollnivneach",false),
    SOPHANEM("Sophanem",false),
    MENAPHOS("Menaphos",false),
    ANACHRONIA("Anachronia",false),
    SARACHNIS_LADDER("Sarachnis ladder",false),
    SARACHNIS_LAIR("Sarachnis lair",false);

    private final boolean isFreeToPlay;
    private final String displayName;

    Location(String displayName, boolean isFreeToPlay) {
        this.displayName = displayName;
        this.isFreeToPlay = isFreeToPlay;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isFreeToPlay() {
        return isFreeToPlay;
    }

    public static Location byDisplayName(String displayName) {
        for (Location location : Location.values()) {
            if (location.getDisplayName().equalsIgnoreCase(displayName)) {
                return location;
            }
        }
        return null;
    }
}
