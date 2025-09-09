package com.memorygame.domain.entity;

import com.memorygame.domain.enums.CardState;

public class Card
{

    private final int identifier;
    private final String value;
    private CardState state;

    public Card(int identifier, String value)
    {
        this.identifier = identifier;
        this.value = value;
        this.state = CardState.FACE_DOWN;
    }
    public int getIdentifier()
    {
        return identifier;
    }

    public void setIdentifier(int identifier)
    {
        // this.identifier = identifier; // identifier is final, cannot be changed
        throw new UnsupportedOperationException("Identifier is final and cannot be changed.");
    }

    public String getValue()
    {
        return value;
    }

    public CardState getState()
    {
        return state;
    }

    public void setState(CardState state)
    {
        this.state = state;
    }


    /* Methods to change the state of the card */

    /** Public **/

    public boolean isMatch()
    {
        return state == CardState.MATCHED;
    }

    /** Private **/

    public void reveal()
    {
        if (state == CardState.FACE_DOWN)
        {
            state = CardState.FACE_UP;
        }
    }

    public void hide()
    {
        if (state == CardState.FACE_UP)
        {
            state = CardState.FACE_DOWN;
        }
    }

    public void match()
    {
        if(state == CardState.FACE_UP)
        {
            state = CardState.MATCHED;
        }
        else
        {
            throw new IllegalStateException("Card must be face up to be matched.");
        }

    }




}
