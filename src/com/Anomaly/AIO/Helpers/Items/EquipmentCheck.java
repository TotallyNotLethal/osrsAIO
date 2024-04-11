package com.Anomaly.AIO.Helpers.Items;

import com.Anomaly.AIO.Helpers.Items.EquipmentSet;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.wrappers.items.Item;

public class EquipmentCheck {
    public static String checkForSet() {
        try {
            Field[] fields = EquipmentSets.class.getDeclaredFields();
            for (Field field : fields) {
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) &&
                        field.getType().equals(EquipmentSet.class)) {
                    EquipmentSet set = (EquipmentSet) field.get(null);
                    if (hasEquipmentSet(set.getItems())) {
                        return field.getName();
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "None";
    }

    private static boolean hasEquipmentSet(Map<String, Integer> requiredItems) {
        Map<String, Integer> inventoryItems = new HashMap<>();
        List<Item> items = Bank.all();
        for (Item item : items) {
            inventoryItems.put(item.getName(), inventoryItems.getOrDefault(item.getName(), 0) + item.getAmount());
        }

        for (Map.Entry<String, Integer> entry : requiredItems.entrySet()) {
            if (!inventoryItems.containsKey(entry.getKey()) ||
                    inventoryItems.get(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }

        return true;
    }
}
