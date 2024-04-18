package com.Anomaly.AIO.Tasks.GrandExchange;

import com.Anomaly.AIO.Helpers.State.Methods.GrandExchange.SellItemsState;
import com.Anomaly.AIO.Helpers.State.Methods.WalkToState;
import com.Anomaly.AIO.Helpers.State.StateManager;
import com.Anomaly.AIO.Main.SettingsManager;
import com.Anomaly.AIO.Main.Task;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;
import org.dreambot.api.wrappers.interactive.Player;
import org.dreambot.api.wrappers.items.Item;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MarketPlaceTask extends Task {
    private final AbstractScript script;
    private final StateManager stateManager;
    private final SettingsManager settings;
    private final Player player;
    private SellItemsState sellItemsState;
    Map<String, Integer> itemsToSell = new HashMap<>();
    private final Area grandExchangeArea = new Area(3148, 3506, 3181, 3473);
    private boolean taskComplete = false;

    public MarketPlaceTask(AbstractScript script, SettingsManager settings) {
        this.script = script;
        this.stateManager = new StateManager(script);
        this.settings = settings;
        this.player = Players.getLocal();
        getInventory();
        sellItemsState = new SellItemsState(script, itemsToSell);

        prepareStates();
    }

    private void prepareStates() {
        if (!grandExchangeArea.contains(Players.getLocal())) {
            stateManager.addState(new WalkToState(script, grandExchangeArea.getCenter()));
        }
        if (settings.isSellItemsEnabled()) {
            stateManager.addState(sellItemsState);
        } else Logger.log("Selling items has to be enabled in settings!");
    }

    @Override
    public int execute() {
        if (!taskComplete && !stateManager.hasStates()) {
            Logger.log("All items have been processed for sale.");
            taskComplete = true;
        } else {
            stateManager.executeCurrentState();
        }
        return 100;
    }

    public void getInventory() {
        for (Item i : Inventory.all()) {
            if(i != null && i.isValid() && i.isTradable())
                itemsToSell.put(i.getName(), i.getAmount());
        }
    }

    @Override
    public boolean isComplete() {
        return taskComplete;
    }

    @Override
    public void onPaint(Graphics g) {
        g.drawString(sellItemsState.getItemBeingSold(), 10, 290);
    }
}