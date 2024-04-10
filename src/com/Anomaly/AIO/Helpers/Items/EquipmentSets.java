package com.Anomaly.AIO.Helpers.Items;

import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;

public class EquipmentSets {

    public int defenceLvl = Skills.getRealLevel(Skill.DEFENCE);
    public static String ArmorType;
    public void getArmorType() {
        if (defenceLvl < 5) {
            ArmorType = "Iron";
        } else if (defenceLvl < 20) {
            ArmorType = "Steel";
        } else if (defenceLvl < 30) {
            ArmorType = "Mithril";
        } else if (defenceLvl < 40) {
            ArmorType = "Adamant";
        } else if (defenceLvl < 70) {
            ArmorType = "Rune";
        }
    }

    public static EquipmentSet GRACEFUL = new EquipmentSet()
            .addItem("Graceful hood", 1)
            .addItem("Graceful top", 1)
            .addItem("Graceful legs", 1)
            .addItem("Graceful gloves", 1)
            .addItem("Graceful boots", 1)
            .addItem("Graceful cape", 1);

    public static EquipmentSet ARMOR = new EquipmentSet()
            .addItem(ArmorType + " Full helm", 1)
            .addItem(ArmorType + " Platebody", 1)
            .addItem(ArmorType + " Platelegs", 1)
            .addItem(ArmorType + " Kiteshield", 1);






    //public static EquipmentSet NEXTSET = new EquipmentSet()
            //.addItem("ItemName", 1);
}