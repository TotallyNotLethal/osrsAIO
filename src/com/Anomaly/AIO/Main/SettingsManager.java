package com.Anomaly.AIO.Main;

public class SettingsManager {
    private boolean buyItems;
    private boolean sellItems;
    private boolean useStaminaPotions;

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
