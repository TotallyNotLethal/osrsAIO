import org.dreambot.api.methods.Calculations;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.impl.Condition;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@ScriptManifest(author = "Team-Anomaly", name = "Advanced AIO Bot", version = 1.0, description = "An all-in-one OSRS script", category = Category.MISC)
public class Main extends AbstractScript {

    private final List<Task> tasks = new ArrayList<>();
    private AtomicReference<Task> currentTask = new AtomicReference<>();
    private AdvancedGUI gui;

    @Override
    public void onStart() {
        gui = new AdvancedGUI(this);
        SwingUtilities.invokeLater(() -> gui.setVisible(true));
        log("Complex AIO Script started!");
    }

    @Override
    public int onLoop() {
        Task task = currentTask.get();
        if (task != null) {
            return task.execute();
        }
        return Calculations.random(1000, 2000);
    }

    public void updateTask(Task task) {
        currentTask.set(task);
    }

    @Override
    public void onExit() {
        if (gui != null) {
            gui.dispose();
        }
        log("Advanced AIO Script stopped.");
    }

    public interface Task {
        int execute();
    }

    public Task createTask(String skill, String method) {
        switch (skill) {
            case "Fishing" -> {
                return new FishingTask(this, method);
            }
            case "Firemaking" -> {
                return new FiremakingTask(this, method);
            }
            //case "Woodcutting" -> {
                //return new WoodcuttingTask(this, method);
            //}
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