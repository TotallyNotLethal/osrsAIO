package com.Anomaly.AIO.Main;

import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.utilities.Logger;

public class SettingsManager {
    private boolean buyItems;
    private boolean sellItems;
    private boolean useStaminaPotions;
    private boolean usePOH;

    public boolean isUsePOHEnabled() {
        return usePOH;
    }

    public void setUsePOH(boolean usePOH) {
        this.usePOH = usePOH;
    }

    public boolean isInPlayerOwnedHouse() {
        return GameObjects.closest(gameObject -> gameObject != null && gameObject.getName().contains("Portal") && gameObject.hasAction("Lock")) != null;
    }

    public boolean isBuyItemsEnabled() {
        return buyItems;
    }

    public void setBuyItems(boolean buyItems) {
        this.buyItems = buyItems;
    }

    public boolean isSellItemsEnabled() {
        return sellItems;
    }

    public void setSellItems(boolean sellItems) {
        this.sellItems = sellItems;
    }

    public boolean isUseStaminaPotionsEnabled() {
        return useStaminaPotions;
    }

    public void setUseStaminaPotions(boolean useStaminaPotions) {
        this.useStaminaPotions = useStaminaPotions;
    }
}
