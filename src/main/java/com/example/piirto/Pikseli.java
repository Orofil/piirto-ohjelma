package com.example.piirto;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

public class Pikseli extends Rectangle implements Serializable {
    private static double leveys;
    private static double korkeus;

    private int rivi;
    private int sarake;

    private Color vari;

    public static double getLeveys() {
        return leveys;
    }

    public static void setLeveys(double leveys) {
        Pikseli.leveys = leveys;
    }

    public static double getKorkeus() {
        return korkeus;
    }

    public static void setKorkeus(double korkeus) {
        Pikseli.korkeus = korkeus;
    }

    public Pikseli(int rivi, int sarake, Color vari) {
        super(leveys, korkeus, vari);
        this.rivi = rivi;
        this.sarake = sarake;
        this.vari = vari;
    }

    public Pikseli(int rivi, int sarake) {
        this(rivi, sarake, Color.TRANSPARENT);
    }
}
