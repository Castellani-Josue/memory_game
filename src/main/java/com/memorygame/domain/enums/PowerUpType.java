package com.memorygame.domain.enums;

public enum PowerUpType
{
    REVEAL_ALL("Révéler tout", "Montre toutes les cartes pendant 2 secondes"),
    FREEZE_TIME("Geler le temps", "Arrête le chronomètre pendant 30 secondes"),
    EXTRA_TIME("Temps bonus", "Ajoute 1 minute au chronomètre");

    private final String name;
    private final String description;

    PowerUpType(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    public String getName()
    {
        return name;
    }
    public String getDescription()
    {
        return description;
    }
}
