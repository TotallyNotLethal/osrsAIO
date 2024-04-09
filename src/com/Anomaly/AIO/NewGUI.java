package com.Anomaly.AIO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

class NewGUI extends JFrame {
    private final Map<String, List<String>> skillOptions = new HashMap<>();
    private final Map<String, ImageIcon> skillIcons = new HashMap<>();
    private final Map<String, List<String>> skillMethods = new HashMap<>();
    private JList<String> methodList;
    private JList<String> locationList;
    private final Map<String, List<String>> skillLocations = new HashMap<>();
    private final DefaultListModel<String> methodListModel = new DefaultListModel<>();
    private final DefaultListModel<String> locationListModel = new DefaultListModel<>();

    public NewGUI() {
        setTitle("Bot Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        populateSkillOptions();
        populateSkillLocations();
        populateSkillMethods();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        methodList = new JList<>(methodListModel);
        locationList = new JList<>(locationListModel);

        JPanel leftPanel = createLeftPanel();
        JSplitPane centerSplitPane = createCenterSplitPane(); // Create a split pane for center

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(centerSplitPane, BorderLayout.CENTER); // Add centerSplitPane to mainPanel
        mainPanel.add(createRightPanel(), BorderLayout.EAST);

        add(mainPanel);
        setVisible(true); // Make the frame visible
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        loadSkillIcons();
        for (Map.Entry<String, ImageIcon> entry : skillIcons.entrySet()) {
            JLabel skillLabel = new JLabel(entry.getValue());
            skillLabel.setToolTipText(entry.getKey());
            skillLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    updateTrainingPanels(entry.getKey());
                }
            });
            leftPanel.add(skillLabel);
        }
        return leftPanel;
    }

    private JSplitPane createCenterSplitPane() {
        // This split pane will contain methodList and locationList
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(new JScrollPane(methodList));
        splitPane.setBottomComponent(new JScrollPane(locationList));
        splitPane.setDividerLocation(300); // You can adjust this for the initial position of the divider
        return splitPane;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        JButton startButton = new JButton("Start");
        rightPanel.add(startButton);
        return rightPanel;
    }

    private void loadSkillIcons() {
        skillIcons.put("Attack", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Attack_icon.png"))));
        skillIcons.put("Defence", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Defence_icon.png"))));
        skillIcons.put("Strength", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Strength_icon.png"))));
        skillIcons.put("Hitpoints", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Hitpoints_icon.png"))));
        skillIcons.put("Ranged", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Ranged_icon.png"))));
        skillIcons.put("Prayer", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Prayer_icon.png"))));
        skillIcons.put("Magic", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Magic_icon.png"))));
        skillIcons.put("Cooking", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Cooking_icon.png"))));
        skillIcons.put("Woodcutting", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Woodcutting_icon.png"))));
        skillIcons.put("Fletching", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Fletching_icon.png"))));
        skillIcons.put("Fishing", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Fishing_icon.png"))));
        skillIcons.put("Firemaking", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Firemaking_icon.png"))));
        skillIcons.put("Crafting", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Crafting_icon.png"))));
        skillIcons.put("Smithing", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Smithing_icon.png"))));
        skillIcons.put("Mining", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Mining_icon.png"))));
        skillIcons.put("Herblore", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Herblore_icon.png"))));
        skillIcons.put("Agility", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Agility_icon.png"))));
        skillIcons.put("Thieving", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Thieving_icon.png"))));
        skillIcons.put("Slayer", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Slayer_icon.png"))));
        skillIcons.put("Farming", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Farming_icon.png"))));
        skillIcons.put("Runecraft", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Runecraft_icon.png"))));
        skillIcons.put("Hunter", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Hunter_icon.png"))));
        skillIcons.put("Construction", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Construction_icon.png"))));
    }

    private void updateTrainingPanels(String skill) {
        List<String> methods = skillOptions.getOrDefault(skill, Arrays.asList("No method"));
        List<String> locations = skillLocations.getOrDefault(skill, Arrays.asList("No location"));

        methodListModel.clear();
        methods.forEach(methodListModel::addElement);

        locationListModel.clear();
        locations.forEach(locationListModel::addElement);
    }

    private void populateSkillOptions() {
        skillOptions.put("Woodcutting", Arrays.asList("Trees", "Oaks", "Willows"));
        skillOptions.put("Fishing", Arrays.asList("Shrimps", "Trout", "Salmon"));
        skillOptions.put("Firemaking", Arrays.asList("Logs", "Oak", "Willow"));
        // ... add more skill options as needed ...
    }

    private void populateSkillLocations() {
        skillLocations.put("Woodcutting", Arrays.asList("Lumbridge", "Draynor Village"));
        skillLocations.put("Fishing", Arrays.asList("Lumbridge Swamp", "Barbarian Village"));
        skillLocations.put("Firemaking", Arrays.asList("Grand Exchange", "Falador Park"));
        // ... add more skill locations as needed ...
    }

    private void populateSkillMethods() {
        // Ensure to populate this similarly to skillLocations and skillOptions
        skillMethods.put("Woodcutting", Arrays.asList("Method 1", "Method 2"));
        // Add more methods for each skill
    }
}