package com.example.piirto;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Ruudukko {@link PiirtoTaso PiirtoTason} pikseleille.
 */
public class Ruudukko extends Pane {
    /**
     * Luo uuden ruudukon {@link PiirtoTaso PiirtoTason} pikseleille sen mittojen mukaan.
     * @param leveys PiirtoTason leveys kuvapisteissä
     * @param korkeus PiirtoTason korkeus kuvapisteissä
     * @param pikseleitaX PiirtoTason leveys pikseleissä
     * @param pikseleitaY PiirtoTason korkeus pikseleissä
     */
    public Ruudukko(double leveys, double korkeus, int pikseleitaX, int pikseleitaY) {
        double pikseliMitta = Math.min(leveys / (double) pikseleitaX, korkeus / (double) pikseleitaY);
        for (int i = 0; i <= pikseleitaX; i++) {
            Line l = new Line(pikseliMitta * i, 0, pikseliMitta * i, pikseliMitta * pikseleitaY);
            l.setFill(Color.GRAY);
            l.setStrokeWidth(0.5);
            this.getChildren().add(l);
        }
        for (int i = 0; i <= pikseleitaY; i++) {
            Line l = new Line(0, pikseliMitta * i, pikseliMitta * pikseleitaX, pikseliMitta * i);
            l.setFill(Color.GRAY);
            l.setStrokeWidth(0.5);
            this.getChildren().add(l);
        }
        this.setOpacity(0.5);
    }
}
