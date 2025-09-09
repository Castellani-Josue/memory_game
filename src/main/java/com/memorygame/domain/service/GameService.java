package com.memorygame.domain.service;

import com.memorygame.domain.entity.Board;
import com.memorygame.domain.entity.Card;
import com.memorygame.domain.entity.Game;
import com.memorygame.domain.enums.CardState;
import com.memorygame.domain.enums.GameDifficulty;
import com.memorygame.domain.enums.PowerUpType;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class GameService {
    private Game game;
    private Timer flipBackTimer;
    private List<Card> currentlyFlipped;
    private boolean processingMatch;
    private GameDifficulty currentDifficulty;

    // Callbacks pour notifier l'UI
    private Consumer<List<Card>> onCardsFlipped;
    private Consumer<List<Card>> onCardsMatched;
    private Consumer<List<Card>> onCardsHidden;
    private Runnable onGameWon;
    private Runnable onGameStateChanged;

    public GameService()
    {
        this.currentlyFlipped = new ArrayList<>();
        this.processingMatch = false;
        this.currentDifficulty = GameDifficulty.MEDIUM;
    }

    // Setters pour les callbacks
    public void setOnCardsFlipped(Consumer<List<Card>> callback)
    {
        this.onCardsFlipped = callback;
    }

    public void setOnCardsMatched(Consumer<List<Card>> callback)
    {
        this.onCardsMatched = callback;
    }

    public void setOnCardsHidden(Consumer<List<Card>> callback)
    {
        this.onCardsHidden = callback;
    }

    public void setOnGameWon(Runnable callback)
    {
        this.onGameWon = callback;
    }

    public void setOnGameStateChanged(Runnable callback)
    {
        this.onGameStateChanged = callback;
    }

    public void startNewGame(GameDifficulty difficulty)
    {
        this.currentDifficulty = difficulty;
        int rows = difficulty.getRows();
        int columns = difficulty.getColumns();
        List<String> values = generateCardValues(difficulty.getPairs());

        startNewGame(rows, columns, values);
    }

    public void startNewGame(int rows, int columns, List<String> values)
    {
        // Nettoyer l'état précédent
        if (flipBackTimer != null && flipBackTimer.isRunning()) {
            flipBackTimer.stop();
        }
        currentlyFlipped.clear();
        processingMatch = false;

        // Initialiser une nouvelle partie
        List<Card> cards = new ArrayList<>();
        int id = 0;
        for (String value : values) {
            cards.add(new Card(id++, value));
            cards.add(new Card(id++, value)); // chaque valeur a 2 cartes
        }
        Collections.shuffle(cards);

        Board board = new Board(rows, columns, cards);
        this.game = new Game(board);
        this.game.start();

        // Notifier l'UI
        if (onGameStateChanged != null)
        {
            onGameStateChanged.run();
        }
    }

    public boolean flipCard(int index) {
        if (processingMatch || isGameOver()) {
            return false;
        }

        Card card = game.getBoard().getCard(index);
        if (card.getState() != CardState.FACE_DOWN) {
            return false;
        }

        // Si on a déjà 2 cartes retournées, on ne peut pas en retourner d'autres
        if (currentlyFlipped.size() >= 2) {
            return false;
        }

        // Retourner la carte
        card.reveal();
        currentlyFlipped.add(card);

        // Notifier l'UI
        if (onCardsFlipped != null) {
            onCardsFlipped.accept(List.of(card));
        }

        // Vérifier s'il faut traiter un match
        if (currentlyFlipped.size() == 2) {
            processingMatch = true;
            processMatch();
        }

        return true;
    }

    private void processMatch() {
        game.callIncrementMoves();

        Card card1 = currentlyFlipped.get(0);
        Card card2 = currentlyFlipped.get(1);

        if (card1.getImagePath().equals(card2.getImagePath())) {
            // Match trouvé
            card1.match();
            card2.match();

            if (onCardsMatched != null) {
                onCardsMatched.accept(new ArrayList<>(currentlyFlipped));
            }

            currentlyFlipped.clear();
            processingMatch = false;

            // Vérifier si le jeu est terminé
            if (isGameOver()) {
                game.end();
                if (onGameWon != null) {
                    onGameWon.run();
                }
            }

            if (onGameStateChanged != null) {
                onGameStateChanged.run();
            }
        } else {
            // Pas de match - programmer le retournement après délai
            flipBackTimer = new Timer(1200, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    hideCurrentlyFlipped();
                    flipBackTimer.stop();
                }
            });
            flipBackTimer.setRepeats(false);
            flipBackTimer.start();
        }
    }

    private void hideCurrentlyFlipped() {
        List<Card> cardsToHide = new ArrayList<>(currentlyFlipped);

        for (Card card : cardsToHide) {
            card.hide();
        }

        if (onCardsHidden != null) {
            onCardsHidden.accept(cardsToHide);
        }

        currentlyFlipped.clear();
        processingMatch = false;

        if (onGameStateChanged != null) {
            onGameStateChanged.run();
        }
    }

    // Nouveau : Power-ups
    public void usePowerUp(PowerUpType powerUp) {
        switch (powerUp) {
            case REVEAL_ALL:
                revealAllCardsTemporarily();
                break;
            case FREEZE_TIME:

                break;
            case EXTRA_TIME:

                break;
        }
    }

    private void revealAllCardsTemporarily() {
        List<Card> hiddenCards = game.getBoard().getCards().stream()
                .filter(card -> card.getState() == CardState.FACE_DOWN)
                .toList();

        // Révéler temporairement
        hiddenCards.forEach(Card::reveal);

        if (onCardsFlipped != null) {
            onCardsFlipped.accept(hiddenCards);
        }

        // Cacher après 2 secondes
        Timer revealTimer = new Timer(2000, e -> {
            hiddenCards.forEach(Card::hide);
            if (onCardsHidden != null) {
                onCardsHidden.accept(hiddenCards);
            }
        });
        revealTimer.setRepeats(false);
        revealTimer.start();
    }

    public boolean isGameOver() {
        return game != null && game.isGameOver();
    }

    public int getMovesCount()
    {
        return game != null ? game.getMovescount() : 0;
    }

    public long getElapsedTime()
    {
        return game != null ? game.getElapsedTime() : 0;
    }

    public Board getBoard()
    {
        return game != null ? game.getBoard() : null;
    }

    public GameDifficulty getCurrentDifficulty()
    {
        return currentDifficulty;
    }

    public boolean isProcessingMatch()
    {
        return processingMatch;
    }

    public int getMatchedPairs()
    {
        if (game == null) return 0;
        return (int) game.getBoard().getCards().stream()
                .filter(Card::isMatch)
                .count() / 2;
    }

    public int getTotalPairs()
    {
        return currentDifficulty.getPairs();
    }

    private List<String> generateCardValues(int pairs) {
        List<String> imagePaths = List.of(
                "/images/card1.jpeg",
                "/images/card2.jpeg",
                "/images/card3.jpeg",
                "/images/card4.jpeg",
                "/images/card5.jpeg",
                "/images/card6.jpeg",
                "/images/card7.jpeg",
                "/images/card8.jpeg",
                "/images/card9.jpeg",
                "/images/card10.jpeg",
                "/images/card11.jpeg",
                "/images/card12.jpeg"
        );

        return imagePaths.subList(0, Math.min(pairs, imagePaths.size()));
    }


    public void restartGame() {
        startNewGame(currentDifficulty);
    }

}