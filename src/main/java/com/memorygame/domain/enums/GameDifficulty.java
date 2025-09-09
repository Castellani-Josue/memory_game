package com.memorygame.domain.enums;

public enum GameDifficulty
{
    EASY(3, 4, 6, "Facile"),
    MEDIUM(4, 4, 8, "Moyen"),
    HARD(4, 6, 12, "Difficile");

    private final int rows;
    private final int columns;
    private final int pairs;
    private final String displayName;

    GameDifficulty(int rows, int columns, int pairs, String displayName) {
        this.rows = rows;
        this.columns = columns;
        this.pairs = pairs;
        this.displayName = displayName;
    }

    public int getRows() { return rows; }
    public int getColumns() { return columns; }
    public int getPairs() { return pairs; }
    public String getDisplayName() { return displayName; }
    public int getTotalCards() { return pairs * 2; }
}
