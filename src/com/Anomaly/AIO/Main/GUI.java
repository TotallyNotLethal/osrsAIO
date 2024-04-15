package com.Anomaly.AIO.Main;

import com.Anomaly.AIO.Main.Skills.SkillManager;
import org.dreambot.api.Client;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.utilities.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

class GUI extends JFrame {
    private final Map<String, List<String>> skillOptions = new HashMap<>();
    private final Map<String, ImageIcon> skillIcons = new HashMap<>();
    private final Map<String, List<String>> skillMethods = new HashMap<>();
    private JList<String> methodList;
    private JList<String> locationList;
    private Map<String, Integer> skillLevels;
    private final Map<String, List<String>> skillLocations = new HashMap<>();
    private final DefaultListModel<String> methodListModel = new DefaultListModel<>();
    private final DefaultListModel<String> locationListModel = new DefaultListModel<>();
    private final DefaultListModel<String> trainingListModel = new DefaultListModel<>();
    private JList<String> taskList;
    private final Map<String, JLabel> skillLabels = new HashMap<>();
    private JLabel levelLabel;
    private final Main mainScript;
    private String selectedSkill = "";
    private JLabel selectedSkillLabel;
    private JTabbedPane tabbedPane;
    private JTextField durationField;
    private JTextField stopLevelField;
    private JPanel settingsPanel;
    private JPanel skillSettingsPanel;
    private JButton startButton;
    private final SkillManager skillManager = new SkillManager();

    private final Color backgroundColor = new Color(60, 63, 65);
    private final Color panelColor = new Color(43, 43, 43);
    private final Color accentColor = new Color(28, 134, 238);
    private final Color textColor = Color.WHITE;
    private final Color levelColor = new Color(191, 97, 106);
    private final Font textFont = new Font("SansSerif", Font.BOLD, 14);

    public GUI(Main script) {
        this.mainScript = script;
        setTitle("Bot Settings");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tabbedPane = new JTabbedPane();
        settingsPanel = new JPanel();

        JPanel botSettingsPanel = new JPanel(new BorderLayout());
        botSettingsPanel.add(createLeftPanel(), BorderLayout.WEST);
        botSettingsPanel.add(createCenterSplitPane(), BorderLayout.CENTER);
        botSettingsPanel.add(createRightPanel(), BorderLayout.EAST);

        tabbedPane.addTab("General", mainPanel);
        tabbedPane.addTab("Settings", settingsPanel);

        methodList = new JList<>(methodListModel);

        locationList = new JList<>(locationListModel);


        mainPanel.add(createLeftPanel(), BorderLayout.WEST);
        mainPanel.add(createCenterSplitPane(), BorderLayout.CENTER);
        mainPanel.add(createRightPanel(), BorderLayout.EAST);
        //add(mainPanel);
        add(tabbedPane, BorderLayout.CENTER);


        mainPanel.setBackground(backgroundColor);
        getContentPane().setBackground(backgroundColor);
        updateLevels();

        pack();
        setupListListeners();
        setupTaskListListener();
        setVisible(true);
    }

    private JPanel createSkillSettingsPanel(String skill) {
        skillSettingsPanel = new JPanel();
        skillSettingsPanel.setLayout(new BoxLayout(skillSettingsPanel, BoxLayout.Y_AXIS));

        if (skillIcons.containsKey(skill)) {
            JLabel skillIconLabel = new JLabel(skillIcons.get(skill));
            skillIconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            skillSettingsPanel.add(skillIconLabel);
        }

        skillSettingsPanel.add(new JLabel("Duration (min):"));
        durationField = new JTextField("5");
        skillSettingsPanel.add(durationField);

        skillSettingsPanel.add(new JLabel("Stop Level:"));
        stopLevelField = new JTextField("1");
        skillSettingsPanel.add(stopLevelField);

        skillSettingsPanel.setBackground(panelColor);
        skillSettingsPanel.setForeground(textColor);
        styleComponent(skillSettingsPanel);

        return skillSettingsPanel;
    }

    private JPanel createLeftPanel() {
        JPanel skillsPanel = new JPanel(new GridLayout(0, 3, 5, 5));
        loadSkillIcons();
        skillLevels = new HashMap<>();

        for (String skill : skillIcons.keySet()) {
            JPanel skillContainer = new JPanel(new BorderLayout());
            skillContainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            skillContainer.setBackground(backgroundColor);

            JLabel skillLabel = new JLabel(skillIcons.get(skill));
            skillLabel.setHorizontalAlignment(JLabel.CENTER);
            skillLabels.put(skill, skillLabel);

            levelLabel = new JLabel(String.valueOf(skillLevels.getOrDefault(skill, 1)), JLabel.CENTER);
            levelLabel.setForeground(levelColor);
            levelLabel.setFont(textFont);

            skillContainer.add(skillLabel, BorderLayout.CENTER);
            //skillContainer.add(levelLabel, BorderLayout.SOUTH);

            skillContainer.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectedSkill = skill;
                    updateLocationsPanel(skill);
                    updateLevels();

                    SwingUtilities.invokeLater(() -> updateSkillSettingsPanel(skill));

                    if (selectedSkillLabel != null) {
                        selectedSkillLabel.setBorder(null);
                    }
                    skillLabel.setBorder(BorderFactory.createLineBorder(accentColor));
                    selectedSkillLabel = skillLabel;
                }
            });

            skillsPanel.add(skillContainer);
        }

        return skillsPanel;
    }

    private void updateLocationsPanel(String skill) {
        List<String> locations = skillManager.getLocations(skill.toUpperCase());
        Logger.log("Updating locations for skill: " + skill + " with locations: " + locations);
        locationListModel.clear();
        for (String location : locations) {
            locationListModel.addElement(location);
        }
        methodListModel.clear();
    }

    private void updateMethodsPanel(String skill, String location) {
        List<String> methods = skillManager.getMethods(skill.toUpperCase(), location);
        methodListModel.clear();
        for (String method : methods) {
            methodListModel.addElement(method);
        }
    }

    private void setupListListeners() {
        locationList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                String selectedLocation = locationList.getSelectedValue();
                if (selectedLocation != null && !selectedSkill.isEmpty()) {
                    updateMethodsPanel(selectedSkill, selectedLocation);
                }
            }
        });

        methodList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                taskAdded(selectedSkill);
                String selectedMethod = methodList.getSelectedValue();
                String selectedLocation = locationList.getSelectedValue();
                if (selectedMethod != null && selectedLocation != null) {
                    String taskDescription = selectedSkill + " - " + selectedMethod + " @ " + selectedLocation;
                    trainingListModel.addElement(taskDescription);
                    taskList.ensureIndexIsVisible(trainingListModel.size() - 1);

                    Logger.log("Added task: " + taskDescription);
                } else {
                    Logger.log("Either method or location is null");
                }
            }
        });
    }

    public void taskAdded(String skill) {
        stopLevelField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    int stopLevel = Integer.parseInt(stopLevelField.getText());
                    //JLabel levelLabel = skillLabels.get(skill);
                    if (levelLabel != null) {
                        levelLabel.setText(String.valueOf(stopLevel));
                    }
                } catch (NumberFormatException ignored) {
                    int stopLevel = 99;
                }
            }
        });
    }

    private JSplitPane createCenterSplitPane() {
        locationList = new JList<>(locationListModel);
        locationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane locationScrollPane = new JScrollPane(locationList);
        styleComponent(locationScrollPane);

        methodList = new JList<>(methodListModel);
        methodList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane methodScrollPane = new JScrollPane(methodList);
        styleComponent(methodScrollPane);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, locationScrollPane, methodScrollPane);
        splitPane.setDividerLocation(150);
        splitPane.setResizeWeight(0.5);
        styleComponent(splitPane);

        return splitPane;
    }

    private JPanel createRightPanel() {
        taskList = new JList<>(trainingListModel);
        JScrollPane taskListScroll = new JScrollPane(taskList);
        styleComponent(taskListScroll);

        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        rightSplitPane.setTopComponent(taskListScroll);
        skillSettingsPanel = createSkillSettingsPanel(selectedSkill);
        rightSplitPane.setBottomComponent(skillSettingsPanel);
        rightSplitPane.setDividerLocation(200);
        rightSplitPane.setResizeWeight(0.7);
        styleComponent(rightSplitPane);

        startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension(100, 40));
        startButton.addActionListener(e -> startTasks());
        styleButton(startButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(startButton);
        styleComponent(buttonPanel);

        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.add(rightSplitPane, BorderLayout.CENTER);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        styleComponent(rightPanel);

        return rightPanel;
    }

    public void updateSkillSettingsPanel(String skill) {
        skillSettingsPanel.removeAll();

        skillSettingsPanel.setLayout(new BoxLayout(skillSettingsPanel, BoxLayout.Y_AXIS));

        JPanel iconPanel = new JPanel();
        iconPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        if (skillIcons.containsKey(skill)) {
            JLabel skillIconLabel = new JLabel(scaleIcon(skillIcons.get(skill), 32, 32));
            iconPanel.add(skillIconLabel);
        }

        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        fieldsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel durationLabel = new JLabel("Duration (min):");
        durationField = new JTextField("5");
        JLabel stopLevelLabel = new JLabel("Stop Level:");
        stopLevelField = new JTextField("1");

        fieldsPanel.add(durationLabel);
        fieldsPanel.add(durationField);
        fieldsPanel.add(stopLevelLabel);
        fieldsPanel.add(stopLevelField);

        styleComponent(durationLabel);
        styleComponent(durationField);
        styleComponent(stopLevelLabel);
        styleComponent(stopLevelField);

        skillSettingsPanel.add(iconPanel);
        skillSettingsPanel.add(Box.createVerticalStrut(10));
        skillSettingsPanel.add(fieldsPanel);
        pack();

        skillSettingsPanel.revalidate();
        skillSettingsPanel.repaint();
    }

    private ImageIcon scaleIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }

    private void styleComponent(JComponent component) {
        component.setFont(new Font("SansSerif", Font.BOLD, 12));
        if (component instanceof JTextField) {
            component.setPreferredSize(new Dimension(100, 30));
        }
    }

    private void startTasks() {
        new Thread(() -> {
            int duration = parseInteger(durationField.getText(), 0);
            int stopLevel = parseInteger(stopLevelField.getText(), 99);

            for (int i = 0; i < trainingListModel.size(); i++) {
                String taskDescription = trainingListModel.get(i);
                String[] parts = taskDescription.split(" - | @ ");
                if (parts.length == 3) {
                    String skill = parts[0];
                    String method = parts[1];
                    String location = parts[2];

                    Task task = mainScript.createTask(skill, method, location, duration, stopLevel);

                    mainScript.setCurrentTask(task);

                    Logger.log("Started task: " + taskDescription + " for " + duration + " minutes or until level " + stopLevel);
                } else {
                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(GUI.this,
                            "Error parsing task: " + taskDescription,
                            "Task Execution Error", JOptionPane.ERROR_MESSAGE));
                }
            }

            SwingUtilities.invokeLater(() -> {
                trainingListModel.clear();
                startButton.setEnabled(true);
                durationField.setText("");
                stopLevelField.setText("");
            });
        }).start();
    }

    private int parseInteger(String value, int defaultValue) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private void styleList(JList<?> list) {
        list.setBackground(panelColor);
        list.setForeground(textColor);
        list.setFont(textFont);
        list.setSelectionBackground(accentColor);
        list.setSelectionForeground(textColor);
        list.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        list.setFixedCellHeight(30);
    }

    private void styleButton(JButton button) {
        button.setBackground(accentColor);
        button.setForeground(textColor);
        button.setFont(textFont);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(accentColor.darker());
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(accentColor);
            }
        });
    }

    private void styleComponent(Component comp) {
        if (comp instanceof JScrollPane scrollPane) {
            scrollPane.getViewport().setBackground(panelColor);
            scrollPane.setBorder(BorderFactory.createLineBorder(backgroundColor));
            Component view = scrollPane.getViewport().getView();
            if (view instanceof JList) {
                styleList((JList<?>) view);
            }
        } else if (comp instanceof JPanel panel) {
            panel.setBackground(panelColor);
            panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        }
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
        skillIcons.put("Runecrafting", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Runecraft_icon.png"))));
        skillIcons.put("Hunter", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Hunter_icon.png"))));
        skillIcons.put("Construction", new ImageIcon(Objects.requireNonNull(getClass().getResource("/Icons/Construction_icon.png"))));
    }

    private void setupTaskListListener() {
        taskList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
                    int index = taskList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        int confirm = JOptionPane.showConfirmDialog(GUI.this,
                                "Are you sure you want to remove this task?",
                                "Remove Task", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            trainingListModel.remove(index);
                        }
                    }
                }
            }
        });
    }

    public void updateLevels()
    {
        SwingUtilities.invokeLater(() -> {
            for (Skill s : Skill.values()) {
                skillLevels.put(s.getName(), s.getLevel());
                JLabel levelLabel = skillLabels.get(s.getName());
                if (levelLabel != null) {
                    levelLabel.setText(String.valueOf(s.getLevel()));
                }
            }
        });
    }
}