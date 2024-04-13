package com.Anomaly.AIO.Helpers.State;

import org.dreambot.api.script.AbstractScript;

public interface State {
    int execute();
    boolean isComplete();
}
