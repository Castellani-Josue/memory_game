package com.memorygame.infrastructure.ui;

import com.memorygame.domain.entity.Card;
import com.memorygame.domain.service.GameService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GamePanel extends JPanel
{
    private GameService gameService;
    private Runnable onBackToMenu;
    private Runnable onRestart;

    private JPanel boardPanel;
    private JLabel movesLabel;
    private JLabel timeLabel;
    private JLabel progressLabel;
    private JProgressBar progressBar;
    private CardButton[] cardButtons;
    private Timer uiTimer;

    public GamePanel(GameService gameService, Runnable onBackToMenu, Runnable onRestart) {
        this.gameService = gameService;
        this.onBackToMenu = onBackToMenu;
        this.onRestart = onRestart;
        initializeUI();
        startUITimer();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(15, 23, 42));

        // Header avec stats
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Panel du plateau
        boardPanel = new JPanel();
        boardPanel.setBackground(new Color(15, 23, 42));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(boardPanel, BorderLayout.CENTER);

        // Footer avec progression
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 41, 59));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Stats à gauche
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 0));
        statsPanel.setOpaque(false);

        movesLabel = new JLabel("0 coups");
        movesLabel.setFont(new Font("Arial", Font.BOLD, 14));
        movesLabel.setForeground(Color.WHITE);
        movesLabel.setIcon(new ColorIcon(Color.CYAN, 12, 12));

        timeLabel = new JLabel("00:00");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setIcon(new ColorIcon(Color.GREEN, 12, 12));

        statsPanel.add(movesLabel);
        statsPanel.add(timeLabel);

        // Boutons à droite
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setOpaque(false);

        JButton menuButton = createSmallButton("Menu", new Color(75, 85, 99));
        menuButton.addActionListener(e -> onBackToMenu.run());

        JButton restartButton = createSmallButton("Nouvelle", new Color(59, 130, 246));
        restartButton.addActionListener(e -> onRestart.run());

        buttonsPanel.add(menuButton);
        buttonsPanel.add(restartButton);

        panel.add(statsPanel, BorderLayout.WEST);
        panel.add(buttonsPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 41, 59));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        progressLabel = new JLabel("Progression: 0/0 paires");
        progressLabel.setFont(new Font("Arial", Font.BOLD, 12));
        progressLabel.setForeground(Color.WHITE);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setString("0%");
        progressBar.setPreferredSize(new Dimension(0, 20));
        progressBar.setForeground(new Color(34, 197, 94));
        progressBar.setBackground(new Color(55, 65, 81));

        panel.add(progressLabel, BorderLayout.NORTH);
        panel.add(Box.createRigidArea(new Dimension(0, 8)), BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);

        return panel;
    }

    private JButton createSmallButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(80, 30));

        return button;
    }

    public void initializeBoard() {
        if (gameService.getBoard() == null) return;

        var board = gameService.getBoard();
        var cards = board.getCards();
        var difficulty = gameService.getCurrentDifficulty();

        // Configuration du layout de la grille
        boardPanel.removeAll();
        boardPanel.setLayout(new GridLayout(difficulty.getRows(), difficulty.getColumns(), 10, 10));

        // Création des boutons cartes
        cardButtons = new CardButton[cards.size()];
        for (int i = 0; i < cards.size(); i++) {
            final int index = i;
            CardButton cardButton = new CardButton(cards.get(i));
            cardButton.addActionListener(e -> gameService.flipCard(index));
            cardButtons[i] = cardButton;
            boardPanel.add(cardButton);
        }

        updateGameState();
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    public void updateCards(List<Card> changedCards) {
        if (cardButtons == null) return;

        SwingUtilities.invokeLater(() -> {
            for (CardButton button : cardButtons) {
                button.updateDisplay();
            }
        });
    }

    public void updateGameState() {
        SwingUtilities.invokeLater(() -> {
            // Mise à jour des stats
            movesLabel.setText(gameService.getMovesCount() + " coups");

            long elapsedSeconds = gameService.getElapsedTime() / 1000;
            timeLabel.setText(String.format("%02d:%02d", elapsedSeconds / 60, elapsedSeconds % 60));

            // Mise à jour de la progression
            int matched = gameService.getMatchedPairs();
            int total = gameService.getTotalPairs();
            progressLabel.setText("Progression: " + matched + "/" + total + " paires");

            int percentage = total > 0 ? (matched * 100) / total : 0;
            progressBar.setValue(percentage);
            progressBar.setString(percentage + "%");
        });
    }

    private void startUITimer() {
        uiTimer = new Timer(100, e -> {
            if (gameService.getBoard() != null && !gameService.isGameOver()) {
                updateGameState();
            }
        });
        uiTimer.start();
    }

    // Classe pour créer une icône colorée simple
    private static class ColorIcon implements Icon {
        private Color color;
        private int width, height;

        public ColorIcon(Color color, int width, int height) {
            this.color = color;
            this.width = width;
            this.height = height;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.fillOval(x, y, width, height);
        }

        @Override
        public int getIconWidth() { return width; }

        @Override
        public int getIconHeight() { return height; }
    }
}