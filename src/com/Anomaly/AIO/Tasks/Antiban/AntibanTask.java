package com.Anomaly.AIO.Tasks.Antiban;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.input.Camera;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;

public class AntibanTask {

    public static void execute() {
        int action = Calculations.random(1, 100);
        if(Players.getLocal().isAnimating() || Players.getLocal().isMoving()) {
            if (action <= 5) {
                Camera.rotateTo(Calculations.random(0, 360), Calculations.random(200, 600));
                Logger.log("AntiBan: Rotating camera");
                Sleep.sleep(2000, 4000);
            } else if (action <= 6) {
                openRandomTab();
                Sleep.sleep(3000, 5000);
            } else if (action <= 10) { // ~4% chance
                examineRandomNPC();
                Sleep.sleep(3000, 5000);
            } else if (action <= 12) {
                Mouse.moveOutsideScreen();
                Logger.log("AntiBan: Moving mouse outside screen");
                Sleep.sleep(3000, 6000);
            } else if (action <= 14) {
                Logger.log("AntiBan: Taking a short break");
                Sleep.sleep(8000, 12000);
            } else if (action <= 18) {
                Camera.rotateTo(Calculations.random(0, 360), Calculations.random(0, 383));
                Logger.log("AntiBan: Adjusting camera zoom");
                Sleep.sleep(1500, 3000);
            } else if (action <= 20) {
                Tabs.open(Tab.OPTIONS);
                Logger.log("AntiBan: Checking game settings");
                Sleep.sleep(1500, 3000);
                Tabs.open(Tab.INVENTORY);
                Logger.log("AntiBan: Returning to inventory");
                Sleep.sleep(500, 1500);
            } else if (action <= 25) {
                Sleep.sleep(2000, 4000);
            } else if (action <= 26) {
                Player local = Players.getLocal();
                if (local != null) {
                    local.interact("Examine");
                    Logger.log("AntiBan: Examining self");
                    Sleep.sleep(1000, 2000);
                }
            } else if (action <= 30) {
                Logger.log("AntiBan: Idle");
                Sleep.sleep(2000, 5000);
            } else {
                Logger.log("AntiBan: Default action, doing nothing special.");
                Sleep.sleep(1000, 2000);
            }
        }
    }

    private static void openRandomTab() {
        Tab[] tabs = {Tab.INVENTORY, Tab.EQUIPMENT, Tab.PRAYER, Tab.MAGIC};
        Tab randomTab = tabs[Calculations.random(tabs.length)];
        Tabs.open(randomTab);
        Logger.log("AntiBan: Opening tab - " + randomTab.name());
        Sleep.sleep(1950, 15500);
        Tabs.open(Tab.INVENTORY);
        Logger.log("AntiBan: Back to Inventory");
    }

    private static void examineRandomNPC() {
        NPC randomNPC = NPCs.closest(npc -> npc != null && !npc.getName().equals("Banker") && !npc.canAttack());
        if (randomNPC != null) {
            randomNPC.interact("Examine");
            Logger.log("AntiBan: Examining " + randomNPC.getName());
        }
    }

    private static void checkSocialTab() {
        if (Calculations.random(0, 1) == 0) {
            Tabs.open(Tab.FRIENDS);
            Logger.log("AntiBan: Checking friends list");
        } else {
            Tabs.open(Tab.QUEST);
            Logger.log("AntiBan: Checking quests");
        }
    }
}
