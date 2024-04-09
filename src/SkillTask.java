import org.dreambot.api.methods.Calculations;

public class SkillTask implements Main.Task {
    private final String skillName;
    private final String method;

    public SkillTask(String skillName, String method) {
        this.skillName = skillName;
        this.method = method;
    }

    @Override
    public int execute() {
        return Calculations.random(100, 200);
    }

    @Override
    public String toString() {
        return skillName + " - " + method;
    }
}