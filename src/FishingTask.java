import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.utilities.Sleep;
import org.dreambot.api.wrappers.interactive.NPC;

public class FishingTask implements Main.Task {
    private final AbstractScript script;
    private final Area fishingArea;
    private final int fishingSpotId;
    private final String[] requiredItems;

    public FishingTask(AbstractScript script, String fishingMethod) {
        this.script = script;

        switch (fishingMethod) {
            case "Shrimps" -> {
                fishingArea = new Area(3243, 3150, 3253, 3140);
                fishingSpotId = 1526;
                requiredItems = new String[]{"Small fishing net"};
            }
            case "Trout", "Salmon" -> {
                fishingArea = new Area(3100, 3425, 3107, 3435);
                fishingSpotId = 1527;
                requiredItems = new String[]{"Fly fishing rod", "Feather"};
            }
            default -> throw new IllegalArgumentException("Invalid fishing method");
        }
    }

    @Override
    public int execute() {
        for (String item : requiredItems) {
            if (!Inventory.contains(item)) {
                script.log("Missing required item: " + item);
                return -1;
            }
        }

        if (!fishingArea.contains(Players.getLocal())) {
            Walking.walk(fishingArea.getRandomTile());
            return Calculations.random(4000, 7000);
        }
        NPC fishingSpot = NPCs.closest(fishingSpotId);
        if (fishingSpot != null && fishingSpot.interact("Fish")) {
            Sleep.sleepUntil(() -> !Players.getLocal().isAnimating(), 15000);
        }

        return Calculations.random(1000, 2000);
    }
}
