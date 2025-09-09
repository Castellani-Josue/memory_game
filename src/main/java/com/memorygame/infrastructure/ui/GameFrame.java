package com.memorygame.infrastructure.ui;


import com.memorygame.domain.service.GameService;
import com.memorygame.domain.enums.GameDifficulty;

import javax.swing.*;
import java.awt.*;


public class GameFrame extends JFrame
{
    private GameService gameService;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private MenuPanel menuPanel;
    private GamePanel gamePanel;
    private VictoryPanel victoryPanel;

    public GameFrame() {
        this.gameService = new GameService();
        initializeFrame();
        setupPanels();
        setupGameServiceCallbacks();
        showMenu();
    }

    private void initializeFrame() {
        setTitle("Memory Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 900);
        setLocationRelativeTo(null);
        setResizable(false);

        // Style moderne
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupPanels() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        menuPanel = new MenuPanel(this::startGame, this::showSettings);
        gamePanel = new GamePanel(gameService, this::showMenu, this::restartGame);
        victoryPanel = new VictoryPanel(this::showMenu, this::restartGame);

        mainPanel.add(menuPanel, "MENU");
        mainPanel.add(gamePanel, "GAME");
        mainPanel.add(victoryPanel, "VICTORY");

        add(mainPanel);
    }

    private void setupGameServiceCallbacks() {
        gameService.setOnCardsFlipped(gamePanel::updateCards);
        gameService.setOnCardsMatched(gamePanel::updateCards);
        gameService.setOnCardsHidden(gamePanel::updateCards);
        gameService.setOnGameStateChanged(gamePanel::updateGameState);
        gameService.setOnGameWon(this::showVictory);
    }

    public void startGame(GameDifficulty difficulty) {
        gameService.startNewGame(difficulty);
        gamePanel.initializeBoard();
        cardLayout.show(mainPanel, "GAME");
    }

    public void showMenu() {
        cardLayout.show(mainPanel, "MENU");
    }

    public void showSettings() {
        // À implémenter - fenêtre de paramètres
        JOptionPane.showMessageDialog(this, "Paramètres à venir!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public void restartGame() {
        gameService.restartGame();
        gamePanel.initializeBoard();
        cardLayout.show(mainPanel, "GAME");
    }

    public void showVictory() {
        victoryPanel.updateStats(
                gameService.getMovesCount(),
                gameService.getElapsedTime(),
                gameService.getCurrentDifficulty()
        );
        cardLayout.show(mainPanel, "VICTORY");
    }
}