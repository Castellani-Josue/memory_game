package com.memorygame.domain.entity;

public class Game
{
    private final Board board;
    private int movescount;
    private long startTime;
    private long endTime;


    public Game(Board board)
    {
        this.board = board;
        this.movescount = 0;
        this.startTime = System.currentTimeMillis();
        this.endTime = 0;
    }


    /** Public Methods **/

    public  void start()
    {
        this.startTime = System.currentTimeMillis();
    }

    public void end()
    {
        this.endTime = System.currentTimeMillis();
    }

    public int getMovescount()
    {
        return movescount;
    }

    public long getElapsedTime()
    {
        if (endTime == 0)
        {
            return System.currentTimeMillis() - startTime;
        }
        return endTime - startTime;
    }

    public Board getBoard()
    {
        return board;
    }

    public boolean isGameOver()
    {
        return board.allCardsMatched();
    }

    public void callIncrementMoves()
    {
        incrementMoves();
    }


    /* Private Methods **/

    private void incrementMoves()
    {
        movescount++;
    }





}
