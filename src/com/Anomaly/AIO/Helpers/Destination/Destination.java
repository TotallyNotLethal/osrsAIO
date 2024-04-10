package com.Anomaly.AIO.Helpers.Destination;

import org.dreambot.api.script.AbstractScript;

public interface Destination {
    boolean hasReached(AbstractScript script);
    void walkTo(AbstractScript script);

    boolean isOnScreen();
}