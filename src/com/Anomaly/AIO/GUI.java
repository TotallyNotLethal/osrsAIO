package com.Anomaly.AIO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class GUI extends JFrame {
    private final Map<String, List<String>> skillOptions = new HashMap<>();
    private final Map<String, List<String>> skillLocations = new HashMap<>();
    private final DefaultListModel<String> trainingListModel = new DefaultListModel<>();
    private final JPanel cardsPanel = new JPanel(new CardLayout());
    private final JComboBox<String> skillBox = new JComboBox<>();
    private final JPanel mainPanel = new JPanel(new BorderLayout());
    private final JPanel taskPanel = new JPanel(new BorderLayout());

    public GUI(Main script) {
        setTitle("Bot Settings");
        setSize(800, 600); // Adjust the size as needed
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        populateSkillOptions();
        populateSkillLocations();

        // Skill selection box
        skillBox.setModel(new DefaultComboBoxModel<>(skillOptions.keySet().toArray(new String[0])));
        skillBox.setPreferredSize(new Dimension(150, 30));
        skillBox.addActionListener(e -> updateMethodAndLocationPanel((String) skillBox.getSelectedItem()));

        // com.Anomaly.AIO.Main panel setup
        mainPanel.setBackground(Color.DARK_GRAY);
        mainPanel.setPreferredSize(new Dimension(400, getHeight())); // Half the width of the frame

        // Task panel setup for enabled tasks
        taskPanel.setBackground(Color.DARK_GRAY);
        taskPanel.setPreferredSize(new Dimension(400, getHeight())); // Half the width of the frame

        JList<String> trainingList = new JList<>(trainingListModel);
        trainingList.setCellRenderer(new TrainingListCellRenderer());
        JScrollPane trainingListScrollPane = new JScrollPane(trainingList);
        trainingListScrollPane.setPreferredSize(new Dimension(400, getHeight()));

        // Control panel setup
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBackground(Color.DARK_GRAY);
        controlPanel.add(skillBox);

        // Execute button setup
        JButton executeButton = new JButton("Execute Selected");
        executeButton.addActionListener(e -> {
            new Thread(() -> {
                // Loop through the training list model elements
                for (int i = 0; i < trainingListModel.size(); i++) {
                    String taskDescription = trainingListModel.get(i); // Get the task string
                    // You'll need to parse the taskDescription to get skill, method, and location.
                    // This is just an example; adapt it to match how your tasks are formatted.
                    String[] parts = taskDescription.split(" - | @ ");
                    if (parts.length == 3) {
                        String skill = parts[0];
                        String method = parts[1];
                        String location = parts[2];

                        // Create and execute the task on this separate thread, not on the EDT
                        Main.Task task = script.createTask(skill, method, location);
                        script.setCurrentTask(task); // Assume this is a non-blocking call
                    } else {
                        // Handle any parsing errors or unexpected task formats
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(GUI.this,
                                "Error parsing task: " + taskDescription,
                                "Task Execution Error", JOptionPane.ERROR_MESSAGE));
                    }
                }
                // After starting the tasks, you may want to clear the list or disable the button
                // until the tasks are complete, depending on your bot's behavior.
                // This should be done on the EDT
                SwingUtilities.invokeLater(() -> {
                    trainingListModel.clear();
                    executeButton.setEnabled(false);
                });
            }).start();
        });

        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(cardsPanel, BorderLayout.CENTER);
        mainPanel.add(executeButton, BorderLayout.SOUTH);

        // Adding main panel and training list to the frame
        add(mainPanel, BorderLayout.WEST);
        taskPanel.add(trainingListScrollPane, BorderLayout.CENTER);
        add(taskPanel, BorderLayout.EAST);

        updateMethodAndLocationPanel((String) skillBox.getSelectedItem());
    }

    private void updateMethodAndLocationPanel(String skill) {
        SwingUtilities.invokeLater(() -> {
            cardsPanel.removeAll(); // Remove all components before updating

            List<String> methods = skillOptions.get(skill);
            List<String> locations = skillLocations.get(skill);

            // Create a list model from the methods for the current skill
            DefaultListModel<String> methodListModel = new DefaultListModel<>();
            methods.forEach(methodListModel::addElement);
            JList<String> methodList = new JList<>(methodListModel);

            // Create a list model from the locations for the current skill
            DefaultListModel<String> locationListModel = new DefaultListModel<>();
            locations.forEach(locationListModel::addElement);
            JList<String> locationList = new JList<>(locationListModel);

            // Set up the lists' visual appearance
            methodList.setBackground(Color.DARK_GRAY);
            locationList.setBackground(Color.DARK_GRAY);

            // Add a mouse listener to the method list for double-click events
            methodList.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        String selectedMethod = methodList.getSelectedValue();
                        String selectedLocation = locationList.getSelectedValue();
                        if (selectedMethod != null && selectedLocation != null) {
                            String taskString = skill + " - " + selectedMethod + " @ " + selectedLocation;
                            trainingListModel.addElement(taskString);
                        }
                    }
                }
            });

            // Set up the cards panel with a new layout and add the method and location lists
            cardsPanel.setLayout(new GridLayout(0, 2)); // 2 columns for methods and locations
            cardsPanel.add(new JScrollPane(locationList));
            cardsPanel.add(new JScrollPane(methodList));

            // Force the update of the UI components
            cardsPanel.revalidate();
            cardsPanel.repaint();
        });
    }

    private void populateSkillOptions() {
            skillOptions.put("Woodcutting", List.of("Trees", "Oaks", "Willows"));
            skillOptions.put("Fishing", List.of("Shrimps", "Trout", "Salmon"));
            skillOptions.put("Firemaking", List.of("Logs", "Oak", "Willow"));
    }
    private void populateSkillLocations() {
            skillLocations.put("Woodcutting", List.of("Lumbridge", "Draynor Village"));
            skillLocations.put("Fishing", List.of("Lumbridge Swamp", "Barbarian Village"));
            skillLocations.put("Firemaking", List.of("Grand Exchange", "Falador Park"));
    }

    static class TrainingListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            c.setBackground(isSelected ? new Color(25, 80, 0, 180) : Color.LIGHT_GRAY);
            c.setForeground(Color.BLACK);
            return c;
        }
    }
}