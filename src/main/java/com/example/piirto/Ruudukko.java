package com.example.piirto;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.io.Serializable;

public class Ruudukko extends Pane implements Serializable {
    public Ruudukko(double leveys, double korkeus, int pikseleitaX, int pikseleitaY) {
        double pikseliMitta = Math.min(leveys / (double) pikseleitaX, korkeus / (double) pikseleitaY);
        for (int i = 0; i < pikseleitaX; i++) {
            Line l = new Line(pikseliMitta * i, 0, pikseliMitta * i, korkeus);
            l.setFill(Color.GRAY);
            l.setStrokeWidth(0.5);
            this.getChildren().add(l);
        }
        for (int i = 0; i < pikseleitaY; i++) {
            Line l = new Line(0, pikseliMitta * i, leveys, pikseliMitta * i);
            l.setFill(Color.GRAY);
            l.setStrokeWidth(0.5);
            this.getChildren().add(l);
        }
    }
}
