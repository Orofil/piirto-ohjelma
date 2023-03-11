package com.example.piirto;

import javafx.scene.layout.StackPane;

import java.util.ArrayList;

public class PiirtoAlue extends StackPane {
    private static double leveys;
    private static double korkeus;
    private static int pikseleitaX;
    private static int pikseleitaY;
//    private ArrayList<PiirtoTaso> tasot = new ArrayList<>();

    public PiirtoAlue() {
        this.getChildren().add(new PiirtoTaso());
    }

    public void lisaaTaso(String nimi) {
        this.getChildren().add(new PiirtoTaso(nimi));
    }

    public void lisaaTaso() {
        this.getChildren().add(new PiirtoTaso());
    }
}
