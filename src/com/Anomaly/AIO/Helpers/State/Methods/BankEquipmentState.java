package com.Anomaly.AIO.Helpers.State.Methods;

import com.Anomaly.AIO.Helpers.State.State;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.wrappers.items.Item;

import java.util.ArrayList;
import java.util.List;

public class BankEquipmentState implements State {
    private final Tile bankTile = new Tile(3162, 3487); // my favorite ge tile <3
    private boolean complete = false;


    List<Item> hatItems = new ArrayList<>();
    List<Item> chestItems = new ArrayList<>();
    List<Item> legsItems = new ArrayList<>();
    List<Item> feetItems = new ArrayList<>();
    List<Item> weaponItems = new ArrayList<>();
    List<Item> shieldItems = new ArrayList<>();
    List<Item> amuletItems = new ArrayList<>();
    List<Item> arrowsItems = new ArrayList<>();
    List<Item> capeItems = new ArrayList<>();
    List<Item> ringItems = new ArrayList<>();
    List<Item> handsItems = new ArrayList<>();

    @Override
    public int execute() {
        if (!Bank.isOpen()) {
            if (bankTile.distance() > 5) {
                Walking.walk(bankTile);
            } else {
                Bank.open();
            }
        } else {

            for (Item item : Bank.all()) {
                if(item != null && item.isValid()) {
                    EquipmentSlot slot = Equipment.getSlotForItem(i -> i.getID() == item.getID());
                    if (slot != null) {
                        switch (slot) {
                            case HAT:
                                hatItems.add(item);
                                break;
                            case CHEST:
                                chestItems.add(item);
                                break;
                            case LEGS:
                                legsItems.add(item);
                                break;
                            case FEET:
                                feetItems.add(item);
                                break;
                            case WEAPON:
                                weaponItems.add(item);
                                break;
                            case SHIELD:
                                shieldItems.add(item);
                                break;
                            case AMULET:
                                amuletItems.add(item);
                                break;
                            case ARROWS:
                                arrowsItems.add(item);
                            case CAPE:
                                capeItems.add(item);
                                break;
                            case RING:
                                ringItems.add(item);
                                break;
                            case HANDS:
                                handsItems.add(item);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            complete = true;
        }
        logItems("Hat", hatItems);
        logItems("Chest", chestItems);
        logItems("Legs", legsItems);
        logItems("Feet", feetItems);
        logItems("Weapon", weaponItems);
        logItems("Shield", shieldItems);
        logItems("Amulet", amuletItems);
        logItems("Arrows", arrowsItems);
        logItems("Cape", capeItems);
        logItems("Ring", ringItems);
        logItems("Hands", handsItems);
        return 600;
    }

    private void logItems(String category, List<Item> items) {
        if (items.isEmpty()) {
            Logger.log(category + ": None");
        } else {
            StringBuilder sb = new StringBuilder(category + ": ");
            for (Item item : items) {
                sb.append(item.getName()).append(" (").append(item.getAmount()).append("), ");
            }
            Logger.log(sb.toString());
        }
    }

    @Override
    public boolean isComplete() {
        return complete;
    }
}
