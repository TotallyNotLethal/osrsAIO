package com.Anomaly.AIO.Tasks.Skilling;

import com.Anomaly.AIO.Helpers.Locations.WCLocations;
import com.Anomaly.AIO.Main;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.depositbox.DepositBox;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.GameObject;

import java.util.*;

public class WoodcuttingTask implements Main.Task {

    private final Map<String, Integer> requiredItems;

    public static int wclvl = Skills.getRealLevel(Skill.WOODCUTTING);
    public static String[] axe = {"Bronze axe", "Iron axe", "Steel axe", "Mithril axe", "Adamant axe", "Rune axe"};

    public WoodcuttingTask(Main main, String method, String location) {
        execute();
        requiredItems = new HashMap<>();

    }

    public void updateWoodcuttingLevel() {
        wclvl = Skills.getRealLevel(Skill.WOODCUTTING);
    }

    public GameObject findClosestTreeToChop() {
        String treeType;
        if (wclvl < 15) {
            treeType = "Tree";
            requiredItems.put("Bronze axe", 1);
        } else if (wclvl < 30) {
            treeType = "Oak tree";
            requiredItems.put("Steel axe", 1);
        } else if (wclvl < 60) {
            treeType = "Willow tree";
            if(wclvl >= 31 && wclvl < 41) {
                requiredItems.put("Adamant axe", 1);
            } else requiredItems.put("Rune axe", 1);
        } else {
             treeType = "Yew tree";
             requiredItems.put("Rune axe", 1);
        }

        GameObject closestTree = null;
        double closestDistance = Double.MAX_VALUE;

        GameObject[] trees = GameObjects.all().toArray(new GameObject[0]);

        for (GameObject tree : trees) {
            if (tree.getName().equals(treeType)) {
                double distance = Players.getLocal().distance(tree);
                if (distance < closestDistance && WCLocations.wcArea().contains(tree)) {
                    closestTree = tree;
                    closestDistance = distance;
                }
            }
        }

        return closestTree;
    }

    public String toBank() {
        if (Inventory.contains("Logs")) {
            return "Logs";
        }
        if (Inventory.contains("Oak logs")) {
            return "Oak logs";
        } else {
            return "Willow logs";
        }


    }
    public boolean nonTargetLog() {
        updateWoodcuttingLevel();
        boolean containsNonTargetLog = false;

        if (wclvl < 15) {
            containsNonTargetLog = Inventory.contains("Oak logs") || Inventory.contains("Willow logs");
        } else if (wclvl < 30) {
            containsNonTargetLog = Inventory.contains("Logs") || Inventory.contains("Willow logs");
        } else {
            containsNonTargetLog = Inventory.contains("Logs") || Inventory.contains("Oak logs");
        }

        return containsNonTargetLog;
    }


    public void chopTree() {
        WCLocations.wcArea();

        if (!WCLocations.wcArea().contains(Players.getLocal()) && !Inventory.isFull() && !nonTargetLog()) {
            Walking.walk(WCLocations.wcArea().getRandomTile());
            Sleep.sleepUntil(() -> WCLocations.wcArea().contains(Players.getLocal()), Calculations.random(3000, 5000));
        } else if (!Inventory.isFull() && !Players.getLocal().isAnimating()) {
            GameObject closestTree = findClosestTreeToChop(); // Find any closest tree initially
            if (closestTree != null && closestTree.interact("Chop down")) {
                Sleep.sleepUntil(() -> Players.getLocal().isAnimating(), 1200, 1800);

                GameObject nextTree = findNextBestTree(closestTree);
                if (nextTree != null) {
                    Mouse.move(nextTree);
                }

                Sleep.sleepUntil(() -> !Players.getLocal().isAnimating() || Inventory.isFull(), Calculations.random(4000, 6000));
            }
        }

    }

    private GameObject findNextBestTree(GameObject currentTree) {
        String treeType = findClosestTreeToChop().getName();
        return GameObjects.all(tree -> tree != null && tree.getName().contains(treeType)  && !tree.getName().contains("stump") && WCLocations.wcArea().contains(tree) && !tree.equals(currentTree))
                .stream()
                .min(Comparator.comparingDouble(tree -> Players.getLocal().distance(tree)))
                .orElse(null);
    }

    public void bankWood() {

        if (Inventory.isFull() || !Inventory.contains(axe) && !Equipment.contains(axe) || nonTargetLog()) {
            if (WCLocations.wcArea() == WCLocations.willowTreeArea) {
                if (!WCLocations.wcDepositBox.contains(Players.getLocal())) {
                    Walking.walk(WCLocations.wcDepositBox.getRandomTile());
                    Sleep.sleepUntil(() -> WCLocations.wcDepositBox.contains(Players.getLocal()), Calculations.random(2550, 4550));
                }

                if (!DepositBox.isOpen() && WCLocations.wcDepositBox.contains(Players.getLocal())) {
                    DepositBox.open();
                    Sleep.sleepUntil(DepositBox::isOpen, Calculations.random(1000, 2500));
                }

                if (DepositBox.isOpen()) {
                    DepositBox.depositAllExcept(axe);
                    Sleep.sleep(1500, 2500);
                    DepositBox.close();
                }
            }
            if(WCLocations.wcArea() != WCLocations.willowTreeArea){
                if (!WCLocations.wcBank.contains(Players.getLocal())) {
                    Walking.walk(WCLocations.wcBank.getRandomTile());
                    Sleep.sleepUntil(() -> WCLocations.wcBank.contains(Players.getLocal()), Calculations.random(2550, 4550));
                }

                if (!Bank.isOpen() && WCLocations.wcBank.contains(Players.getLocal())) {
                    Bank.open();
                    Sleep.sleepUntil(Bank::isOpen, Calculations.random(1000, 2500));
                }

                if (Bank.isOpen()) {
                    Bank.depositAllExcept(axe);
                    if(!Inventory.contains("Bronze axe") && !Equipment.contains("Bronze axe")
                            || (!Inventory.contains("Steel axe") && !Equipment.contains("Steel axe"))) {
                        Bank.withdraw("Bronze axe");
                        Sleep.sleepUntil(() -> Inventory.contains("Bronze axe"), 2000);
                        Bank.close();

                    }
                }
            }
        }
    }

    public int execute() {
        if (!Equipment.contains("Bronze axe")) {
            if (!WCLocations.lumbridgeBank.contains(Players.getLocal())) {
                Walking.walk(WCLocations.lumbridgeBank);
                Sleep.sleep(1950, 3350);
            }


            if (WCLocations.lumbridgeBank.contains(Players.getLocal())) {
                if (!Bank.isOpen()) {
                    Bank.open();
                    Sleep.sleepUntil(Bank::isOpen, 5000);
                } else if (Bank.isOpen()) {
                    if (!Inventory.onlyContains("Bronze axe")) {
                        Bank.depositAllItems();

                        Sleep.sleep(950, 1450);
                    }

                    if (Bank.contains("Bronze axe")) {
                        Bank.withdraw("Bronze axe");
                    }

                    if (Inventory.onlyContains("Bronze axe")) {
                        Bank.close();
                    }
                }

            }


            if (Inventory.contains("Bronze axe") && !Equipment.contains("Bronze axe") && !Bank.isOpen()) {
                Inventory.interact("Bronze axe", "Wield");
                Sleep.sleepUntil(() -> Equipment.contains("Bronze axe"), 1000);
            }
        }
        Logger.log("Debug: Woodcutting task");
        if (Equipment.contains(requiredItems) || Inventory.contains(requiredItems)) {
            chopTree();
            bankWood();
        }
        return Calculations.random(200, 700);
    }



}