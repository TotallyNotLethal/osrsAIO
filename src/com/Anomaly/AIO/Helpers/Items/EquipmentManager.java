package com.Anomaly.AIO.Helpers.Items;

import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.script.AbstractScript;

import java.util.Map;
import java.util.TreeMap;

public class EquipmentManager {
    private static final TreeMap<Integer, EquipmentSet> levelToEquipmentMap = new TreeMap<>();

    static {
        levelToEquipmentMap.put(1, EquipmentSets.BRONZE);
        levelToEquipmentMap.put(1, EquipmentSets.IRON);
        levelToEquipmentMap.put(5, EquipmentSets.STEEL);
        levelToEquipmentMap.put(10, EquipmentSets.BLACK);
        levelToEquipmentMap.put(20, EquipmentSets.MITHRIL);
        levelToEquipmentMap.put(30, EquipmentSets.ADAMANT);
        levelToEquipmentMap.put(40, EquipmentSets.RUNE);
        levelToEquipmentMap.put(60, EquipmentSets.DRAGON);
    }

    public static EquipmentSet getAppropriateEquipmentSet(AbstractScript script) {
        int defenceLevel = Skills.getRealLevel(Skill.DEFENCE);

        return levelToEquipmentMap.floorEntry(defenceLevel).getValue();
    }

    public static void equipAppropriateSet(AbstractScript script) {
        EquipmentSet setToEquip = getAppropriateEquipmentSet(script);

        for (Map.Entry<String, Integer> entry : setToEquip.getItems().entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
        }
    }
}
