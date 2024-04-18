package com.Anomaly.AIO.Main;

import java.awt.*;

public abstract class Task {
    public abstract int execute();
    public abstract boolean isComplete();
    public void onPaint(Graphics g) {
        // Default implementation does nothing
        // This method can be overridden in specific tasks if needed
    }
}