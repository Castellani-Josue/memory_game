package com.memorygame.domain.service;

import com.memorygame.domain.entity.Board;
import com.memorygame.domain.entity.Card;
import com.memorygame.domain.entity.Game;
import com.memorygame.domain.enums.CardState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameService
{
    private Game game;

    public void startNewGame(int rows, int columns, List<String> values)
    {
        // Initialize a new game with a board and cards
        List<Card> cards = new ArrayList<>();
        int id = 0;
        for (String value : values)
        {
            cards.add(new Card(id++, value));
            cards.add(new Card(id++, value)); // chaque valeur a 2 cartes
        }
        Collections.shuffle(cards);

        Board board = new Board(rows, columns, cards);
        this.game = new Game(board);
        this.game.start();
    }

    public void flipCard(int index)
    {

        var card = game.getBoard().getCard(index);
        if (card.getState() == CardState.FACE_DOWN)
        {
            card.setState(CardState.FACE_UP);
            checkMatch();
        }

    }

    public boolean isGameOver()
    {
        return game.isGameOver();
    }

    public int getMovesCount()
    {
        return game.getMovescount();
    }

    public long getElapsedTime()
    {
        return game.getElapsedTime();
    }

    public Board getBoard()
    {
        return game.getBoard();
    }


    /* Other Methods **/

    private void checkMatch()
    {
        var revealedCards = game.getBoard().getRevealedCards();
        if (revealedCards.size() == 2) {
            game.callIncrementMoves();
            if (revealedCards.get(0).getValue().equals(revealedCards.get(1).getValue()))
            {
                //revealedCards.forEach(c -> c.setState(CardState.MATCHED));
                revealedCards.forEach(Card::match);
            }
            else
            {
                //revealedCards.forEach(c -> c.setState(CardState.FACE_DOWN));
                revealedCards.forEach(Card::hide);
            }
        }
    }


}
