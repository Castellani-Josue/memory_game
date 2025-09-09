package com.memorygame.main;

import com.memorygame.infrastructure.ui.GameFrame;

import javax.swing.*;
import java.awt.*;

public class MemoryGameApp
{
    public static void main(String[] args)
    {
        // Configuration pour une meilleure apparence sur tous les systèmes
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            try {
                // Utiliser le Look & Feel du système pour une apparence native
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                // Personnalisation globale des couleurs
                UIManager.put("Panel.background", new Color(15, 23, 42));
                UIManager.put("Button.background", new Color(59, 130, 246));
                UIManager.put("Button.foreground", Color.WHITE);

            } catch (Exception e) {
                e.printStackTrace();
            }

            // Créer et afficher la fenêtre principale
            GameFrame gameFrame = new GameFrame();
            gameFrame.setVisible(true);
        });
    }
}