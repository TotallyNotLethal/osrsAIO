package com.Anomaly.AIO.Main.Skills;

import java.util.Arrays;

public enum GUISkill {
    WOODCUTTING,
    FISHING,
    AGILITY,
    MINING,
    OTHER,
    THIEVING;

    private SkillData skillData;

    GUISkill() {
        this.skillData = new SkillData();
    }

    // Static block to initialize data for all enum instances
    static {
        for (GUISkill skill : values()) {
            initializeData(skill);
        }
    }

    private static void initializeData(GUISkill skill) {
        switch (skill) {
            case WOODCUTTING:
                skill.skillData.addSkillLocation("Lumbridge", Arrays.asList("Tree", "Oak", "Willow", "Yew"));
                skill.skillData.addSkillLocation("Draynor village", Arrays.asList("Willow"));
                skill.skillData.addSkillLocation("Port sarim", Arrays.asList("Willow"));
                skill.skillData.addSkillLocation("Varrock", Arrays.asList("Maple", "Yew"));
                skill.skillData.addSkillLocation("Falador", Arrays.asList("Yew", "Magic"));
                break;
            case FISHING:
                skill.skillData.addSkillLocation("Lumbridge", Arrays.asList("Shrimp", "Anchovies"));
                skill.skillData.addSkillLocation("Barbarian village", Arrays.asList("Trout", "Salmon", "Rainbow fish", "Pike"));
                skill.skillData.addSkillLocation("Karamja", Arrays.asList("Lobster", "Swordfish", "Tuna", "Shrimp", "Anchovies", "Sardine", "Herring"));
                break;
            case AGILITY:
                skill.skillData.addSkillLocation("All", Arrays.asList(
                        "Draynor village", "Al kharid", "Gnome stronghold",
                        "Varrock", "Falador", "Pollnivneach", "Canifis",
                        "Rellekka", "Ardougne", "Seers village", "Wilderness"));
                break;
            case MINING:
                skill.skillData.addSkillLocation("Lumbridge", Arrays.asList("Tin", "Copper"));
                break;
            case OTHER:
                skill.skillData.addSkillLocation("Bossing", Arrays.asList("Sarachnis", "Giant mole"));
                skill.skillData.addSkillLocation("Mini-games", Arrays.asList(""));
                skill.skillData.addSkillLocation("Grand Exchange", Arrays.asList("Sell inventory"));
                skill.skillData.addSkillLocation("Money Making", Arrays.asList("Crush bird nests", "High alch items"));
                break;
            case THIEVING:
                skill.skillData.addSkillLocation("Lumbridge", Arrays.asList("Man", "Woman"));
                skill.skillData.addSkillLocation("Draynor village", Arrays.asList("Master farmer", "Seed Stall", "Wine Stall", "Man"));
                break;
        }
    }

    public SkillData getSkillData() {
        return skillData;
    }
}
