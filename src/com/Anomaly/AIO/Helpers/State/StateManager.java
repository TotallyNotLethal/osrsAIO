package com.Anomaly.AIO.Helpers.State;

import com.Anomaly.AIO.Helpers.TickManagement.TickManagerState;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.listener.GameTickListener;
import org.dreambot.api.utilities.Logger;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class StateManager extends AbstractScript {
    private Queue<State> states = new LinkedList<>();
    private TickManagerState tickManagerState;
    private State backgroundState;
    private AbstractScript script;
    private boolean isExecuting = false;

    public StateManager(AbstractScript script) {
        this.script = script;
    }

    public void setBackgroundState(State state) {
        this.backgroundState = state;
    }

    public void executeState(State state) {
        states.add(state);
        if (!isExecuting) {
            executeCurrentState();
        }
    }

    public void executeCurrentState() {
        if (backgroundState != null && !backgroundState.isComplete()) {
            backgroundState.execute();
        }

        if (states.isEmpty() || isExecuting) {
            return;
        }
        isExecuting = true;
        State currentState = states.peek();
        int result = currentState.execute();
        if (currentState.isComplete()) {
            states.poll();
            isExecuting = false;
            if (!states.isEmpty()) {
                executeCurrentState();
            }
        } else {
            isExecuting = false;
        }
    }

    public void addState(State state) {
        states.offer(state);
        if (!isExecuting && !states.isEmpty()) {
            executeCurrentState();
        }
    }

    public boolean hasStates() {
        return !states.isEmpty();
    }

    public void clearStates() {
        states.clear();
        isExecuting = false;
    }

    public AbstractScript getScript() {
        return this.script;
    }

    public State currentState() {
        return states.peek();
    }

    public boolean contains(State state) {
        return states.contains(state);
    }

    public boolean isComplete() {
        return states.isEmpty();
    }

    @Override
    public int onLoop() {
        return 600;  // Delay before next loop iteration
    }

    @Override
    public void onPaint(Graphics g) {
        super.onPaint(g); // Call to super if required
        Logger.log("Painting Sarachnis...");
        if (currentState() instanceof TickManagerState tickManagerState) {
            List<String> actions = tickManagerState.getFutureActions();
            int y = 50;
            for (int i = 0; i < actions.size(); i++) {
                String actionText = "Tick +" + (i + 1) + ": " + actions.get(i);
                g.drawString(actionText, 20, y);
                y += 15;
            }
        }
    }
}