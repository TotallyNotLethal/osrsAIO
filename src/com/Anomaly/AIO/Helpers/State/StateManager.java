package com.Anomaly.AIO.Helpers.State;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class StateManager {
    private Queue<State> states = new LinkedList<>();
    private AbstractScript script;
    private boolean isExecuting = false; // Flag to indicate if a state is currently being executed

    public StateManager(AbstractScript script) {
        this.script = script;
    }

    public void executeState(State state) {
        states.add(state);
        if (!isExecuting) {
            executeCurrentState();
        }
    }

    public void executeCurrentState() {
        if (states.isEmpty() || isExecuting) {
            return;
        }
        isExecuting = true; // Set the flag to true as execution starts
        State currentState = states.peek();
        int result = currentState.execute();
        if (currentState.isComplete()) {
            states.poll();
            isExecuting = false; // Reset the flag after state execution is complete
            if (!states.isEmpty()) {
                executeCurrentState(); // Continue with next state if available
            }
        } else {
            isExecuting = false; // Reset the flag if state is not complete and needs re-execution later
        }
    }

    public void addState(State state) {
        states.offer(state);
        if (!isExecuting && !states.isEmpty()) {
            executeCurrentState(); // Ensure that new states are executed if nothing is currently running
        }
    }

    public boolean hasStates() {
        return !states.isEmpty();
    }

    public void clearStates() {
        states.clear();
        isExecuting = false; // Reset execution flag when states are cleared
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
}
