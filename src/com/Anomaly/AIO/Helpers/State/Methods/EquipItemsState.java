package com.Anomaly.AIO.Helpers.State.Methods;

import com.Anomaly.AIO.Helpers.Items.EquipmentSet;
import com.Anomaly.AIO.Helpers.State.State;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.api.script.AbstractScript;

import java.util.Objects;

public class EquipItemsState implements State {
    private final AbstractScript script;
    private final EquipmentSet setToEquip;
    private boolean isComplete = false;

    public EquipItemsState(AbstractScript script, EquipmentSet setToEquip) {
        this.script = script;
        this.setToEquip = setToEquip;
    }

    @Override
    public int execute() {
        if (setToEquip != null) {
            equipSpecificItems();
        } else {
            equipAnyEquippableItems();
        }

        Sleep.sleep(500);
        return isComplete ? 0 : Calculations.random(200,700);
    }

    private void equipSpecificItems() {
        setToEquip.getItems().forEach((itemName, quantity) -> equipItemByName(itemName));
        isComplete = setToEquip.getItems().keySet().stream()
                .allMatch(Equipment::contains);
    }

    private void equipAnyEquippableItems() {
        Inventory.all().stream()
                .filter(Objects::nonNull)
                .filter(item -> item.hasAction("Wear") || item.hasAction("Wield"))
                .forEach(item -> {
                    Logger.log("Equipping item: " + item);
                    boolean actionResult = item.interact(item.hasAction("Wear") ? "Wear" : "Wield");

                    if (actionResult) {
                        Sleep.sleep(100, 500);
                    }
                });

        isComplete = true;
    }

    private void equipItemByName(String itemName) {
        Item item = Inventory.get(itemName);
        Logger.log("Equipping item: " + itemName);
        if (item != null && (item.hasAction("Wear") || item.hasAction("Wield"))) {
            item.interact(item.hasAction("Wear") ? "Wear" : "Wield");
            Sleep.sleepUntil(() -> Equipment.contains(itemName), Calculations.random(100,250));
        }
    }

    @Override
    public boolean isComplete() {
        return isComplete;
    }
}
