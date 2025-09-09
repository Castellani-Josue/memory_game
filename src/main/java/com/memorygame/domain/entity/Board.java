package com.memorygame.domain.entity;

import java.util.List;

public class Board
{
    private final int rows;
    private final int columns;
    private final List<Card> cards;

    public Board(int rows, int columns, List<Card> cards)
    {
        this.rows = rows;
        this.columns = columns;
        this.cards = cards;
    }

    /** Getters and Setters **/

    public List<Card> getCards()
    {
        return cards;
    }

    public Card getCard(int index)
    {
        if (index < 0 || index >= cards.size())
        {
            throw new IndexOutOfBoundsException("Invalid card index: " + index);
        }
        return cards.get(index);
    }

    public List<Card> getRevealedCards()
    {
        return cards.stream().filter(card -> card.getState() == com.memorygame.domain.enums.CardState.FACE_UP).toList();
    }

    public boolean allCardsMatched()
    {
        return cards.stream().allMatch(Card::isMatch);
    }

    /** Other Methods **/

    private void shuffleCards()
    {
        java.util.Collections.shuffle(cards);
    }



}
