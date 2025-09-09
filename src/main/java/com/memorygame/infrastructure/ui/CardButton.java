package com.memorygame.infrastructure.ui;

import com.memorygame.domain.entity.Card;
import com.memorygame.domain.enums.CardState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CardButton extends JButton
{
    private Card card;
    private boolean isHovered = false;
    private static final Color FACE_DOWN_COLOR = new Color(59, 130, 246);
    private static final Color FACE_UP_COLOR = Color.WHITE;
    private static final Color MATCHED_COLOR = new Color(34, 197, 94);
    private static final Color HOVER_COLOR = new Color(147, 197, 253);

    public CardButton(Card card)
    {
        this.card = card;
        initializeButton();
        updateDisplay();
    }

    private void initializeButton()
    {
        setFont(new Font("Arial", Font.BOLD, 24));
        setFocusPainted(false);
        setBorderPainted(false);
        setPreferredSize(new Dimension(80, 80));

        // Coins arrondis
        setBorder(BorderFactory.createEmptyBorder());
        setOpaque(false);

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (card.getState() == CardState.FACE_DOWN) {
                    isHovered = true;
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    public void updateDisplay()
    {
        CardState state = card.getState();

        switch (state) {
            case FACE_DOWN:
                setIcon(null);
                setText("?");
                setForeground(Color.WHITE);
                break;
            case FACE_UP:
                setIcon(loadImage(card.getImagePath()));
                setText(null);
                setForeground(Color.BLACK);
                break;
            case MATCHED:
                setIcon(loadImage(card.getImagePath()));
                setText(null);
                setForeground(Color.WHITE);
                break;
        }

        //setEnabled(state == CardState.FACE_DOWN);
        setEnabled(true);
        setDisabledIcon(null);
        repaint();
    }

    private ImageIcon loadImage(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            Image img = new ImageIcon(imgURL).getImage();
            // Ici on scale selon la taille du bouton
            Image scaledImg = img.getScaledInstance(getWidth() - 10, getHeight() - 10, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } else {
            System.err.println("Image not found: " + path);
            return null;
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Couleur de fond selon l'état
        Color bgColor;
        CardState state = card.getState();

        if (state == CardState.MATCHED) {
            bgColor = MATCHED_COLOR;
        } else if (state == CardState.FACE_UP) {
            bgColor = FACE_UP_COLOR;
        } else {
            bgColor = isHovered ? HOVER_COLOR : FACE_DOWN_COLOR;
        }

        // Dessiner le fond avec coins arrondis
        g2d.setColor(bgColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

        // Ombre pour l'effet de profondeur
        if (state == CardState.FACE_UP || state == CardState.MATCHED) {
            g2d.setColor(new Color(0, 0, 0, 20));
            g2d.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 15, 15);
        }

        g2d.dispose();
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Bordure subtile
        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

        g2d.dispose();
    }

    public Card getCard()
    {
        return card;
    }
}
