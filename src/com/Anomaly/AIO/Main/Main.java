package com.Anomaly.AIO.Main;

import com.Anomaly.AIO.Helpers.Locations.Location;
import com.Anomaly.AIO.Helpers.Paintable;
import com.Anomaly.AIO.Tasks.Combat.Bossing.SarachnisTask;
import com.Anomaly.AIO.Tasks.Skilling.*;
import org.dreambot.api.Client;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.randoms.RandomSolver;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Sleep;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@ScriptManifest(author = "Team-Anomaly", name = "Anomaly AIO Bot", version = 1.0, description = "An all-in-one OSRS script", category = Category.MISC)
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
        Task task = currentTask.get();

        if (task == null) {
            return Calculations.random(200,700);
        }

        if (!task.isComplete()) {
            return task.execute();
        } else {
            currentTask.set(null);
            gui.updateLevels();
            return Calculations.random(200,700);
        }
    }

    @Override
    public void onSolverEnd(RandomSolver solver) {
        gui.updateLevels();
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

    public Task createTask(String skill, String method, String location, int duration, int stopLevel) {
        //return new SarachnisTask(this);
        switch (skill) {
            case "Fishing" -> {
                return new FishingTask(this, method, location, duration, stopLevel);
            }
            case "Firemaking" -> {
                return new FiremakingTask(this, method, location, duration, stopLevel);
            }
            case "Woodcutting" -> {
                return new WoodcuttingTask(this, method, location, duration, stopLevel);
            }
            case "Thieving" -> {
                return new ThievingTask(this, method, location, duration, stopLevel);
            }
            case "Agility" -> {
                return new AgilityTask(this, method, location, duration, stopLevel);
            }
            case "Mining" -> {
                return new MiningTask(this, method, location, duration, stopLevel);
            }
            case "Other" -> {
                return new SarachnisTask(this);
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