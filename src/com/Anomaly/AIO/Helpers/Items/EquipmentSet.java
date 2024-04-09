package com.Anomaly.AIO.Helpers.Items;

import java.util.HashMap;
import java.util.Map;

public class EquipmentSet {
    private final Map<String, Integer> items = new HashMap<>();

    public EquipmentSet addItem(String itemName, int quantity) {
        items.put(itemName, quantity);
        return this;
    }

    public Map<String, Integer> getItems() {
        return new HashMap<>(items);
    }
}
