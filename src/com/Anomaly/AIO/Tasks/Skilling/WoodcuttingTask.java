package com.Anomaly.AIO.Tasks.Skilling;

public class WoodcuttingTask {

    /*public static int wclvl = Skills.getRealLevel(Skill.WOODCUTTING);
    public static String[] axe = {"Bronze axe", "Iron axe", "Steel axe", "Mithril axe", "Adamant axe", "Rune axe"};

    public static void updateWoodcuttingLevel() {
        wclvl = Skills.getRealLevel(Skill.WOODCUTTING);
    }

    public static GameObject findClosestTreeToChop() {
        String treeType;
        if (wclvl < 15) {
            treeType = "Tree"; // Regular tree
        } else if (wclvl < 30) {
            treeType = "Oak tree";
        } else {
            treeType = "Willow tree";
        }

        GameObject closestTree = null;
        double closestDistance = Double.MAX_VALUE;

        GameObject[] trees = GameObjects.all().toArray(new GameObject[0]);

        for (GameObject tree : trees) {
            if (tree.getName().equals(treeType)) {
                double distance = Players.getLocal().distance(tree);
                if (distance < closestDistance & WoodcuttingLocations.wcArea().contains(tree)) {
                    closestTree = tree;
                    closestDistance = distance;
                }
            }
        }

        return closestTree;
    }


    public static String toBank() {
        if (Inventory.contains("Logs")) {
            return "Logs";
        }
        if (Inventory.contains("Oak logs")) {
            return "Oak logs";
        } else {
            return "Willow logs";
        }


    }
    public static boolean nonTargetLog() {
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


    public static void chopTree() {
        WoodcuttingLocations.wcArea();

        if (!WoodcuttingLocations.wcArea().contains(Players.getLocal()) && !Inventory.isFull() && !nonTargetLog()) {
            Walking.walk(WoodcuttingLocations.wcArea().getRandomTile());
            Sleep.sleepUntil(() -> WoodcuttingLocations.wcArea().contains(Players.getLocal()), Calculations.random(3000, 5000));
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

    private static GameObject findNextBestTree(GameObject currentTree) {
        String treeType = findClosestTreeToChop().getName();
        return GameObjects.all(tree -> tree != null && tree.getName().contains(treeType)  && !tree.getName().contains("stump") && WoodcuttingLocations.wcArea().contains(tree) && !tree.equals(currentTree))
                .stream()
                .min(Comparator.comparingDouble(tree -> Players.getLocal().distance(tree)))
                .orElse(null);
    }

    public String axeToUse() {

        if (wclvl < 6) {
            return "Bronze Axe";
        } else if (wclvl < 21) {
            return "Steel Axe";
        } else if (wclvl < 31) {
            return "Mithril Axe";
        } else if (wclvl < 41) {
            return "Adamant Axe";
        } else {
            return "Rune Axe";
        }
    }

    public static void bankWood() {

        if (Inventory.isFull() || !Inventory.contains(axe) && !Equipment.contains(axe) || nonTargetLog()) {
            if (WoodcuttingLocations.wcArea() == WoodcuttingLocations.willowTreeArea) {
                if (!WoodcuttingLocations.wcDepositBox.contains(Players.getLocal())) {
                    Walking.walk(WoodcuttingLocations.wcDepositBox.getRandomTile());
                    Sleep.sleepUntil(() -> WoodcuttingLocations.wcDepositBox.contains(Players.getLocal()), Calculations.random(2550, 4550));
                }

                if (!DepositBox.isOpen() && WoodcuttingLocations.wcDepositBox.contains(Players.getLocal())) {
                    DepositBox.open();
                    Sleep.sleepUntil(DepositBox::isOpen, Calculations.random(1000, 2500));
                }

                if (DepositBox.isOpen()) {
                    DepositBox.depositAllExcept(axe);
                    Sleep.sleep(1500, 2500);
                    DepositBox.close();
                }
            }
            if(WoodcuttingLocations.wcArea() != WoodcuttingLocations.willowTreeArea){
                if (!Bank.contains(Players.getLocal())) {
                    Walking.walk(Bank.getClosestBankLocation());
                    Sleep.sleepUntil(() -> Bank.contains(Players.getLocal()), Calculations.random(2550, 4550));
                }

                if (!Bank.isOpen() && Bank.contains(Players.getLocal())) {
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

    public static void execute() {
        if (!Equipment.contains("axe")) {
            if (Bank.contains(Players.getLocal())) {
                Walking.walk(Bank.getClosestBankLocation());
                Sleep.sleep(1950, 3350);
            }


            if (Bank.contains(Players.getLocal())) {
                if (!Bank.isOpen()) {
                    Bank.open();
                    Sleep.sleepUntil(() -> Bank.isOpen(), 5000);
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
        if (Equipment.contains("Bronze axe")) {
            WoodcuttingTask.chopTree();
            WoodcuttingTask.bankWood();
        }

    }*/

}