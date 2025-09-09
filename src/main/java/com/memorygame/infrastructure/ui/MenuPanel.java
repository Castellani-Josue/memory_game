package com.memorygame.infrastructure.ui;

import com.memorygame.domain.enums.GameDifficulty;

import javax.swing.*;
import java.util.function.Consumer;

import java.awt.*;


public class MenuPanel extends JPanel
{
    private Consumer<GameDifficulty> onStartGame;
    private Runnable onShowSettings;
    private GameDifficulty selectedDifficulty = GameDifficulty.MEDIUM;

    public MenuPanel(Consumer<GameDifficulty> onStartGame, Runnable onShowSettings) {
        this.onStartGame = onStartGame;
        this.onShowSettings = onShowSettings;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(25, 25, 35));

        // Panel principal avec espacement
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));

        // Titre
        JLabel titleLabel = new JLabel("Memory Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Trouvez toutes les paires !");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 50)));

        // Sélection difficulté
        JPanel difficultyPanel = createDifficultyPanel();
        difficultyPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(difficultyPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Boutons
        JButton startButton = createStyledButton("Nouvelle Partie", new Color(34, 197, 94), Color.WHITE);
        startButton.addActionListener(e -> onStartGame.accept(selectedDifficulty));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton settingsButton = createStyledButton("Paramètres", new Color(75, 85, 99), Color.WHITE);
        settingsButton.addActionListener(e -> onShowSettings.run());
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(startButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(settingsButton);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createDifficultyPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JLabel difficultyLabel = new JLabel("Difficulté");
        difficultyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        difficultyLabel.setForeground(Color.WHITE);
        difficultyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(difficultyLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        ButtonGroup group = new ButtonGroup();

        for (GameDifficulty difficulty : GameDifficulty.values()) {
            JRadioButton radio = new JRadioButton(
                    difficulty.getDisplayName() + " (" +
                            difficulty.getRows() + "x" + difficulty.getColumns() + ", " +
                            difficulty.getPairs() + " paires)"
            );
            radio.setFont(new Font("Arial", Font.PLAIN, 14));
            radio.setForeground(Color.WHITE);
            radio.setOpaque(false);
            radio.setAlignmentX(Component.CENTER_ALIGNMENT);
            radio.setSelected(difficulty == selectedDifficulty);

            radio.addActionListener(e -> selectedDifficulty = difficulty);

            group.add(radio);
            panel.add(radio);
            panel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        return panel;
    }

    private JButton createStyledButton(String text, Color backgroundColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(textColor);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(250, 50));
        button.setMaximumSize(new Dimension(250, 50));

        // Effet hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }
}