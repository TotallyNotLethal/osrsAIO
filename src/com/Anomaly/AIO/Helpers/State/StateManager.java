package com.Anomaly.AIO.Helpers.State;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Logger;

import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class StateManager{
    private Queue<State> states = new LinkedList<>();
    private AbstractScript script;

    public StateManager(AbstractScript script) {
        this.script = script;
    }

    public void executeState(State state) {
        states.add(state);
        executeCurrentState();
    }

    public void executeCurrentState() {
        if (states.isEmpty()) {
            return;
        }
        State currentState = states.peek();
        int result = currentState.execute();
        if (currentState.isComplete()) {
            states.poll();
            if (!states.isEmpty()) {
                executeCurrentState();
            }
        }
    }

    public void addState(State state) { states.offer(state); }

    public boolean hasStates() { return !states.isEmpty(); }

    public void clearStates() { states.clear(); }

    public AbstractScript getScript(){ return this.script; }
    public State currentState() { return states.peek(); }

    public boolean isComplete() {
        return states.isEmpty();
    }
}