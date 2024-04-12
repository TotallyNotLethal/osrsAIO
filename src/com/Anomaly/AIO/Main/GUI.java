package com.Anomaly.AIO.Main;

import com.Anomaly.AIO.Main.Skills.SkillManager;
import org.dreambot.api.Client;
import org.dreambot.api.data.GameState;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.utilities.Logger;

import javax.swing.*;
import java.awt.*;
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

        methodList = new JList<>(methodListModel);
        methodList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    String selectedMethod = methodList.getSelectedValue();
                    String selectedLocation = locationList.getSelectedValue();
                    if (selectedMethod != null && selectedLocation != null) {
                        String taskDescription = selectedSkill + " - " + selectedMethod + " @ " + selectedLocation;
                        trainingListModel.addElement(taskDescription);
                        taskAdded(selectedSkill);
                    }
                }
            }
        });
        locationList = new JList<>(locationListModel);

        JPanel leftPanel = createLeftPanel();
        JSplitPane centerSplitPane = createCenterSplitPane();

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(centerSplitPane, BorderLayout.CENTER);
        mainPanel.add(createRightPanel(), BorderLayout.EAST);

        add(mainPanel);

        mainPanel.setBackground(backgroundColor);
        getContentPane().setBackground(backgroundColor);
        updateLevels();

        pack();

        setupLocationListListener();
        setupTaskListListener();
        setVisible(true);
    }

    private JPanel createLeftPanel() {
        JPanel skillsPanel = new JPanel();
        skillsPanel.setLayout(new GridLayout(0, 3, 5, 5));

        loadSkillIcons();
        skillLevels = new HashMap<>();

        for (String skill : skillIcons.keySet()) {
            JPanel skillContainer = new JPanel(new BorderLayout());
            skillContainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            skillContainer.setBackground(new Color(60, 63, 65));

            JLabel skillLabel = new JLabel(skillIcons.get(skill));
            skillLabel.setHorizontalAlignment(JLabel.CENTER);

            levelLabel = new JLabel(String.valueOf(skillLevels.getOrDefault(skill, 1)));
            levelLabel.setHorizontalAlignment(JLabel.CENTER);
            levelLabel.setForeground(Color.ORANGE);
            levelLabel.setFont(levelLabel.getFont().deriveFont(Font.BOLD, 14f));

            skillLabel.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70)));

            skillContainer.add(skillLabel, BorderLayout.CENTER);
            skillContainer.add(levelLabel, BorderLayout.SOUTH);
            skillContainer.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectedSkill = skill;
                    updateLocationsPanel(skill);

                    if (selectedSkillLabel != null) {
                        selectedSkillLabel.setBorder(null);
                    }
                    skillLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
                    selectedSkillLabel = skillLabel;

                    updateLevels();
                }
            });
            skillLabels.put(skill, levelLabel);
            skillsPanel.add(skillContainer);
        }

        for (Component comp : skillsPanel.getComponents()) {
            if (comp instanceof JPanel skillContainer) {
                styleComponent(skillContainer);
            }
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

    private void setupLocationListListener() {
        locationList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                String selectedLocation = locationList.getSelectedValue();
                if (selectedLocation != null && !selectedSkill.isEmpty()) {
                    updateMethodsPanel(selectedSkill, selectedLocation);
                }
            }
        });
    }

    public void taskAdded(String skill) {
        JLabel label = skillLabels.get(skill);
        if (label != null) {
            label.setForeground(Color.GREEN);
        }
    }

    private JSplitPane createCenterSplitPane() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(new JScrollPane(locationList));
        splitPane.setBottomComponent(new JScrollPane(methodList));
        splitPane.setDividerLocation(this.getSize().height/2 - 50);
        return splitPane;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        taskList = new JList<>(trainingListModel);
        JScrollPane taskListScroll = new JScrollPane(taskList);
        rightPanel.add(taskListScroll, BorderLayout.CENTER);

        JButton startButton = new JButton("Start");
        rightPanel.add(startButton, BorderLayout.PAGE_END);

        startButton.addActionListener(e -> {
            new Thread(() -> {
                for (int i = 0; i < trainingListModel.size(); i++) {
                    String taskDescription = trainingListModel.get(i);
                    String[] parts = taskDescription.split(" - | @ ");
                    if (parts.length == 3) {
                        String skill = parts[0];
                        String method = parts[1];
                        String location = parts[2];

                        Task task = mainScript.createTask(skill, method, location);
                        mainScript.setCurrentTask(task);
                    } else {
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                                "Error parsing task: " + taskDescription,
                                "Task Execution Error", JOptionPane.ERROR_MESSAGE));
                    }
                }
                SwingUtilities.invokeLater(() -> {
                    trainingListModel.clear();
                    startButton.setEnabled(false);
                });
            }).start();
        });

        rightPanel.setBackground(backgroundColor);
        styleComponent(taskList);
        styleButton(startButton);

        return rightPanel;
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