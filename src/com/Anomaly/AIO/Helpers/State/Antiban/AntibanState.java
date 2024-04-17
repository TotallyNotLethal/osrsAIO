package com.Anomaly.AIO.Helpers.State.Antiban;

import com.Anomaly.AIO.Helpers.State.State;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.input.Camera;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.interactive.Player;

public class AntibanState implements State {
    private final AbstractScript script;
    private final Player player;

    public AntibanState(AbstractScript script) {
        this.script = script;
        this.player = Players.getLocal();
    }

    @Override
    public int execute() {
        if(player.isAnimating() || player.isMoving()) {
            int action = Calculations.random(1, 100);
            performRandomAction(action);
            Sleep.sleep(1000, 2000);
        }
        return 0;
    }

    private void performRandomAction(int action) {
        switch (action) {
            case 1 -> rotateCamera();
            case 2 -> openRandomTab();
            case 3 -> examineRandomNPC();
            case 4 -> moveMouseOffScreen();
            case 5 -> takeShortBreak();
            case 6 -> adjustCameraZoom();
            case 7 -> checkGameSettings();
            case 8 -> idle();
            case 9 -> examineSelf();
            default -> Logger.log("AntiBan: Default action, doing nothing special.");
        }
    }

    private void rotateCamera() {
        Camera.rotateTo(Calculations.random(0, 360), Calculations.random(200, 600));
        Logger.log("AntiBan: Rotating camera");
    }

    private void openRandomTab() {
        Tab[] tabs = {Tab.INVENTORY, Tab.EQUIPMENT, Tab.PRAYER, Tab.MAGIC};
        Tab randomTab = tabs[Calculations.random(tabs.length)];
        Tabs.open(randomTab);
        Logger.log("AntiBan: Opening tab - " + randomTab.name());
    }

    private void examineRandomNPC() {
        NPC randomNPC = NPCs.closest(npc -> npc != null && !npc.getName().equals("Banker") && !npc.canAttack());
        if (randomNPC != null) {
            randomNPC.interact("Examine");
            Logger.log("AntiBan: Examining " + randomNPC.getName());
        }
    }

    private void moveMouseOffScreen() {
        Mouse.moveOutsideScreen();
        Logger.log("AntiBan: Moving mouse outside screen");
    }

    private void takeShortBreak() {
        Logger.log("AntiBan: Taking a short break");
    }

    private void adjustCameraZoom() {
        Camera.rotateTo(Calculations.random(0, 360), Calculations.random(0, 383));
        Logger.log("AntiBan: Adjusting camera zoom");
    }

    private void checkGameSettings() {
        Tabs.open(Tab.OPTIONS);
        Logger.log("AntiBan: Checking game settings");
        Tabs.open(Tab.INVENTORY);
        Logger.log("AntiBan: Returning to inventory");
    }

    private void idle() {
        Logger.log("AntiBan: Idle");
    }

    private void examineSelf() {
        Player local = Players.getLocal();
        if (local != null) {
            local.interact("Examine");
            Logger.log("AntiBan: Examining self");
        }
    }

    @Override
    public boolean isComplete() {
        return false;  // This state is not intended to complete on its own
    }
}
