package com.Anomaly.AIO.Helpers.Locations;

public enum Location {
    LUMBRIDGE(true),
    CATHERBY(false),
    KARAMJA(true),
    VARROCK(true),
    FALADOR(true),
    SEERS_VILLAGE(false),
    ARDOUGNE(false),
    RELLEKKA(false),
    YANILLE(false),
    DRAYNOR_VILLAGE(true),
    AL_KHARID(true),
    EDGEVILLE(true),
    TUTORIAL_ISLAND(true),
    PORT_SARIM(true),
    RIMMINGTON(true),
    TAVERLEY(false),
    BURTHORPE(false),
    CANIFIS(false),
    MORYTANIA(false),
    PRIFDDINAS(false),
    ZEAH(false),
    GNOME_STRONGHOLD(false),
    WILDERNESS(true),
    ASGARNIA_ICE_DUNGEON(true),
    CRANDOR_ISLAND(true),
    MUDSKIPPER_POINT(true),
    WITCHAVEN(false),
    FISHING_GUILD(false),
    MINING_GUILD(true),
    CRAFTING_GUILD(true),
    COOKING_GUILD(true),
    CHAMPIONS_GUILD(true),
    HEROES_GUILD(false),
    LEGENDS_GUILD(false),
    PISCATORIS_FISHING_COLONY(false),
    SHILO_VILLAGE(false),
    APE_ATOLL(false),
    HARMONY_ISLAND(false),
    MOS_LE_HARMLESS(false),
    LLETYA(false),
    NEITIZNOT(false),
    JATIZSO(false),
    DAGANNOTH_KINGS(false),
    SMOKE_DUNGEON(false),
    POLLNIVNEACH(false),
    SOPHANEM(false),
    MENAPHOS(false),
    ANACHRONIA(false);

    private final boolean isFreeToPlay;

    Location(boolean isFreeToPlay) {
        this.isFreeToPlay = isFreeToPlay;
    }

    public boolean isFreeToPlay() {
        return isFreeToPlay;
    }
}
