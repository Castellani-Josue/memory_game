package com.memorygame.infrastructure.ui;

import com.memorygame.domain.entity.Card;
import com.memorygame.domain.enums.GameDifficulty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class VictoryPanel extends JPanel {
    private Runnable onBackToMenu;
    private Runnable onPlayAgain;
    private JLabel movesValueLabel;
    private JLabel timeValueLabel;
    private JLabel difficultyValueLabel;

    public VictoryPanel(Runnable onBackToMenu, Runnable onPlayAgain) {
        this.onBackToMenu = onBackToMenu;
        this.onPlayAgain = onPlayAgain;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(251, 191, 36)); // Couleur dorée pour la victoire

        // Panel principal
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(80, 50, 80, 50));

        // Icône et titre de victoire
        JLabel trophyLabel = new JLabel();
        Card card;
        card = new Card(100, "/images/trophy.jpeg");
        trophyLabel.setIcon(loadImage(card.getImagePath()));
        trophyLabel.setFont(new Font("Arial", Font.PLAIN, 80));
        trophyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Félicitations !");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(new Color(146, 64, 14));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Vous avez gagné !");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(120, 53, 15));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(trophyLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(subtitleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Panel des statistiques
        JPanel statsPanel = createStatsPanel();
        statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Boutons
        JButton playAgainButton = createStyledButton("Rejouer", new Color(34, 197, 94), Color.WHITE);
        playAgainButton.addActionListener(e -> onPlayAgain.run());
        playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton menuButton = createStyledButton("Menu Principal", new Color(107, 114, 128), Color.WHITE);
        menuButton.addActionListener(e -> onBackToMenu.run());
        menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentPanel.add(playAgainButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        contentPanel.add(menuButton);

        add(contentPanel, BorderLayout.CENTER);
    }

    private ImageIcon loadImage(String path)
    {
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            Image img = new ImageIcon(imgURL).getImage();
            // Ici on scale selon la taille du bouton
            Image scaledImg = img.getScaledInstance(140, 140, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } else {
            System.err.println("Image not found: " + path);
            return null;
        }
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 255, 255, 200));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(217, 119, 6), 2),
                BorderFactory.createEmptyBorder(20, 30, 20, 30)
        ));

        JLabel statsTitle = new JLabel("Vos statistiques");
        statsTitle.setFont(new Font("Arial", Font.BOLD, 18));
        statsTitle.setForeground(new Color(146, 64, 14));
        statsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(statsTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Statistiques individuelles
        panel.add(createStatRow("Coups joués :", "0"));
        movesValueLabel = getLastValueLabel();

        panel.add(createStatRow("Temps :", "00:00"));
        timeValueLabel = getLastValueLabel();

        panel.add(createStatRow("Difficulté :", "Moyen"));
        difficultyValueLabel = getLastValueLabel();

        return panel;
    }

    private JPanel createStatRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.PLAIN, 14));
        labelComponent.setForeground(new Color(75, 85, 99));

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.BOLD, 14));
        valueComponent.setForeground(new Color(17, 24, 39));

        row.add(labelComponent, BorderLayout.WEST);
        row.add(valueComponent, BorderLayout.EAST);

        lastValueLabel = valueComponent; // Pour pouvoir la récupérer
        return row;
    }

    private JLabel lastValueLabel; // Variable temporaire pour récupérer le dernier label créé

    private JLabel getLastValueLabel() {
        return lastValueLabel;
    }

    private JButton createStyledButton(String text, Color backgroundColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(textColor);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(200, 45));
        button.setMaximumSize(new Dimension(200, 45));

        // Effet hover
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(backgroundColor.brighter());
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });

        return button;
    }

    public void updateStats(int moves, long timeInMillis, GameDifficulty difficulty) {
        movesValueLabel.setText(String.valueOf(moves));

        long seconds = timeInMillis / 1000;
        timeValueLabel.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));

        difficultyValueLabel.setText(difficulty.getDisplayName());
    }
}