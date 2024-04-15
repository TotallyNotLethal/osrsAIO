package com.Anomaly.AIO.Helpers.State;

import com.Anomaly.AIO.Helpers.Destination.Destination;
import com.Anomaly.AIO.Helpers.State.Methods.BankingState;
import com.Anomaly.AIO.Helpers.State.Methods.WalkToState;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.AbstractScript;

import java.util.Map;

public class StateUtil {
    private static StateManager stateManager;

    public static void initializeStateManager(AbstractScript script) {
        stateManager = new StateManager(script);
    }

    public static void goToBank(Map<String, Integer> requiredItems, Map<String, Integer> optionalItems, String... itemsToKeep) {
        if (stateManager == null) {
            throw new IllegalStateException("StateManager not initialized");
        }
        BankingState bankingState = new BankingState(stateManager.getScript(), requiredItems, optionalItems, false, true, itemsToKeep);
        stateManager.executeState(bankingState);
    }

    public static void walkTo(Destination destination) {
        if (stateManager == null) {
            throw new IllegalStateException("StateManager not initialized");
        }
        WalkToState bankingState = new WalkToState(stateManager.getScript(), destination);
        stateManager.executeState(bankingState);
    }
}
