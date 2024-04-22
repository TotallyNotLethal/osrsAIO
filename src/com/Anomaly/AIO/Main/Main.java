package com.Anomaly.AIO.Main;

import com.Anomaly.AIO.Tasks.Combat.Bossing.GiantMoleTask;
import com.Anomaly.AIO.Tasks.Combat.Bossing.SarachnisTask;
import com.Anomaly.AIO.Tasks.GrandExchange.MarketPlaceTask;
import com.Anomaly.AIO.Tasks.MoneyMakers.CrushBirdsNestTask;
import com.Anomaly.AIO.Tasks.MoneyMakers.HighAlchTask;
import com.Anomaly.AIO.Tasks.Skilling.*;
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
    private SettingsManager settingsManager;
    private GUI gui;

    @Override
    public void onStart() {
        SwingUtilities.invokeLater(() -> {
            settingsManager = new SettingsManager();
            gui = new GUI(this, settingsManager);
            Sleep.sleep(500);
            gui.setVisible(true);
        });
        log("Anomaly AIO Script started!");
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
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

    @Override
    public void onPaint(Graphics g) {
        Task task = currentTask.get();
        if (task != null) {
            task.onPaint(g);
        }
    }

    public Task createTask(String skill, String method, String location, int duration, int stopLevel) {
        switch (skill) {
            case "Fishing" -> {
                return new FishingTask(this, settingsManager, method, location, duration, stopLevel);
            }
            case "Firemaking" -> {
                return new FiremakingTask(this, settingsManager, method, location, duration, stopLevel);
            }
            case "Woodcutting" -> {
                return new WoodcuttingTask(this, settingsManager, method, location, duration, stopLevel);
            }
            case "Thieving" -> {
                return new ThievingTask(this, settingsManager, method, location, duration, stopLevel);
            }
            case "Agility" -> {
                return new AgilityTask(this, settingsManager, method, location, duration, stopLevel);
            }
            case "Mining" -> {
                return new MiningTask(this, settingsManager, method, location, duration, stopLevel);
            }
            case "Other" -> {
                switch(method) {
                    case "Sarachnis" -> {
                        return new SarachnisTask(this, settingsManager);
                    }
                    case "Giant mole" -> {
                        return new GiantMoleTask(this, settingsManager);
                    }
                    case "Sell inventory" -> {
                        return new MarketPlaceTask(this, settingsManager);
                    }
                    case "Crush bird nests" -> {
                        return new CrushBirdsNestTask(this, settingsManager);
                    }
                    case "High alch items" -> {
                        return new HighAlchTask(this, settingsManager);
                    }
                    default -> {
                        log("Unsupported skill/method: " + skill + "/" + method);
                        return null;
                    }
                }
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