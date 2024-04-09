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
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        Font font = new Font("SansSerif", Font.PLAIN, 12);

        Color backgroundColor = new Color(50, 50, 50);
        Color primaryColor = new Color(30, 150, 160);
        Color textColor = Color.WHITE;


        populateSkillOptions();
        populateSkillLocations();

        // Skill selection box
        skillBox.setModel(new DefaultComboBoxModel<>(skillOptions.keySet().toArray(new String[0])));
        skillBox.setPreferredSize(new Dimension(150, 30));
        skillBox.addActionListener(e -> updateMethodAndLocationPanel((String) skillBox.getSelectedItem()));
        skillBox.setFont(font);
        skillBox.setForeground(textColor);
        skillBox.setBackground(backgroundColor);
        skillBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setBackground(backgroundColor);
                c.setForeground(isSelected ? primaryColor : textColor);
                return c;
            }
        });

        mainPanel.setBackground(backgroundColor);
        mainPanel.setPreferredSize(new Dimension(400, getHeight()));

        taskPanel.setBackground(backgroundColor);
        taskPanel.setPreferredSize(new Dimension(400, getHeight()));

        JList<String> trainingList = new JList<>(trainingListModel);
        trainingList.setCellRenderer(new TrainingListCellRenderer());
        JScrollPane trainingListScrollPane = new JScrollPane(trainingList);
        trainingListScrollPane.setPreferredSize(new Dimension(400, getHeight()));
        trainingList.setFont(font);
        trainingList.setForeground(textColor);
        trainingList.setBackground(backgroundColor);
        trainingList.setFixedCellHeight(30);
        trainingList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.setBackground(Color.DARK_GRAY);
        controlPanel.add(skillBox);

        JButton executeButton = new JButton("Execute Selected");
        executeButton.setFont(font);
        executeButton.setForeground(textColor);
        executeButton.setBackground(primaryColor);
        executeButton.setFocusPainted(false);
        executeButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        executeButton.setBorderPainted(false);
        executeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        executeButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                executeButton.setBackground(primaryColor.darker());
            }
            public void mouseExited(MouseEvent evt) {
                executeButton.setBackground(primaryColor);
            }
        });
        executeButton.addActionListener(e -> {
            new Thread(() -> {
                for (int i = 0; i < trainingListModel.size(); i++) {
                    String taskDescription = trainingListModel.get(i);
                    String[] parts = taskDescription.split(" - | @ ");
                    if (parts.length == 3) {
                        String skill = parts[0];
                        String method = parts[1];
                        String location = parts[2];

                        Main.Task task = script.createTask(skill, method, location);
                        script.setCurrentTask(task);
                    } else {
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(GUI.this,
                                "Error parsing task: " + taskDescription,
                                "Task Execution Error", JOptionPane.ERROR_MESSAGE));
                    }
                }
                SwingUtilities.invokeLater(() -> {
                    trainingListModel.clear();
                    executeButton.setEnabled(false);
                });
            }).start();
        });

        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(cardsPanel, BorderLayout.CENTER);
        mainPanel.add(executeButton, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.WEST);
        taskPanel.add(trainingListScrollPane, BorderLayout.CENTER);
        add(taskPanel, BorderLayout.EAST);

        updateMethodAndLocationPanel((String) skillBox.getSelectedItem());
    }

    private void updateMethodAndLocationPanel(String skill) {
        SwingUtilities.invokeLater(() -> {
            cardsPanel.removeAll();

            List<String> methods = skillOptions.get(skill);
            List<String> locations = skillLocations.get(skill);

            DefaultListModel<String> methodListModel = new DefaultListModel<>();
            methods.forEach(methodListModel::addElement);
            JList<String> methodList = new JList<>(methodListModel);

            DefaultListModel<String> locationListModel = new DefaultListModel<>();
            locations.forEach(locationListModel::addElement);
            JList<String> locationList = new JList<>(locationListModel);

            methodList.setBackground(Color.DARK_GRAY);
            locationList.setBackground(Color.DARK_GRAY);

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

            cardsPanel.setLayout(new GridLayout(0, 2));
            cardsPanel.add(new JScrollPane(locationList));
            cardsPanel.add(new JScrollPane(methodList));

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