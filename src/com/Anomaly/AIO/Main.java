package com.Anomaly.AIO;

import com.Anomaly.AIO.Tasks.Skilling.FiremakingTask;
import com.Anomaly.AIO.Tasks.Skilling.FishingTask;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Sleep;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@ScriptManifest(author = "Team-Anomaly", name = "Advanced AIO Bot", version = 1.0, description = "An all-in-one OSRS script", category = Category.MISC)
public class Main extends AbstractScript {

    private final List<Task> tasks = new ArrayList<>();
    private AtomicReference<Task> currentTask = new AtomicReference<>();
    public boolean taskStarted = false;
    private GUI gui;

    @Override
    public void onStart() {
        SwingUtilities.invokeLater(() -> {
            gui = new GUI(this);
            Sleep.sleep(500);
            gui.setVisible(true);
        });

        log("Anomaly AIO Script started!");
    }

    @Override
    public int onLoop() {
        if(!taskStarted) {
            taskStarted = true;
            Task task = currentTask.get();
            if (task != null) {
                return task.execute();
            }
        }
        return Calculations.random(1000, 2000);
    }

    public void updateTask(Task task) {
        currentTask.set(task);
    }

    @Override
    public void onExit() {
        SwingUtilities.invokeLater(() -> {
            if (gui != null) {
                gui.dispose();
            }
        });
        log("Anomaly AIO Script stopped.");
    }

    public interface Task {
        int execute();
    }

    public Task createTask(String skill, String method, String location) {
        switch (skill) {
            case "Fishing" -> {
                return new FishingTask(this, method, location);
            }
            case "Firemaking" -> {
                return new FiremakingTask(this, method, location);
            }
            default -> {
                log("Unsupported skill/method: " + skill + "/" + method);
                return null;
            }
        }
    }

    public void setCurrentTask(Task task) {
        this.currentTask.set(task);
    }
}