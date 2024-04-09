import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AdvancedGUI extends JFrame {
    private final Map<String, List<String>> skillOptions = new HashMap<>();
    private final DefaultListModel<String> trainingListModel = new DefaultListModel<>();

    public AdvancedGUI(Main script) {
        setTitle("Complex AIO Bot Settings");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        populateSkillOptions();

        JList<String> trainingList = new JList<>(trainingListModel);
        JButton addButton = new JButton("Add");
        JComboBox<String> skillBox = new JComboBox<>(skillOptions.keySet().toArray(new String[0]));
        JComboBox<String> methodBox = new JComboBox<>();

        JButton executeButton = new JButton("Execute");
        executeButton.addActionListener(e -> {
            String skill = (String) skillBox.getSelectedItem();
            String method = (String) methodBox.getSelectedItem();
            Main.Task task = script.createTask(skill, method);
            if (task != null) {
                script.setCurrentTask(task);
            }
        });

        skillBox.addActionListener(e -> updateMethodBox(methodBox, (String) skillBox.getSelectedItem()));

        addButton.addActionListener(e -> {
            String skill = (String) skillBox.getSelectedItem();
            String method = (String) methodBox.getSelectedItem();
            if (skill != null && method != null) {
                trainingListModel.addElement(skill + " - " + method);
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.add(skillBox);
        controlPanel.add(methodBox);
        controlPanel.add(addButton);
        controlPanel.add(executeButton);

        add(new JScrollPane(trainingList), BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        updateMethodBox(methodBox, (String) skillBox.getSelectedItem());
    }

    private void populateSkillOptions() {
        skillOptions.put("Woodcutting", List.of("Trees", "Oaks", "Willows"));
        skillOptions.put("Fishing", List.of("Shrimps", "Trout", "Salmon"));
    }

    private void updateMethodBox(JComboBox<String> methodBox, String skill) {
        methodBox.removeAllItems();
        List<String> methods = skillOptions.get(skill);
        if (methods != null) {
            methods.forEach(methodBox::addItem);
        }
    }
}