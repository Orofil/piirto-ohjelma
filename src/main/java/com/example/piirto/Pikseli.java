package com.example.piirto;

import javafx.beans.value.ObservableValue;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

public class Pikseli extends Rectangle implements Serializable {
    private static double leveys;
    private static double korkeus;

    private Color vari;
    private int nakyvyys;

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

    public static void setMitat(double alueLeveys, double alueKorkeus, int pikseleitaX, int pikseleitaY) {
        double reunaPituus = Math.min(alueLeveys / (double) pikseleitaX, alueKorkeus / (double) pikseleitaY);
        Pikseli.leveys = reunaPituus;
        Pikseli.korkeus = reunaPituus;
    }

    public Pikseli(Color vari, int nakyvyys) {
        /*
        Leveydestä ja korkeudesta vähennetään, koska ääriviivat tekevät neliöistä
        suurempia. TODO ehkä ääriviivat voisi toteuttaa erillisillä Lineillä jotka tulee tasojen ylle
         */
        super(leveys - 1, korkeus - 1, vari);
        this.setStroke(Color.GRAY); // TEMP
        this.setStrokeWidth(0.5); // TEMP

        this.vari = vari; // TODO tätä ei tarvita erillisenä kenttänä kai koska se on jo yliluokassa
        if (nakyvyys < 0) {
            this.nakyvyys = 0;
        } else if (nakyvyys > 100) {
            this.nakyvyys = 100;
        } else {
            this.nakyvyys = nakyvyys;
        }

        // TODO ehkä toiminnallisuus setOnMouseMove tänne tai PiirtoAlueeseen
//        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
//            this.setFill(Color.BLACK);
//        });
    }

    public Pikseli(Color vari) {
        this(vari, 100);
    }

    public Pikseli() {
        this(Color.TRANSPARENT, 100); // TODO tämä voi olla valkoinen jos läpinäkyvyys toteutetaan
    }

    public Color getVari() {
        return vari;
    }

    public void setVari(Color vari) {
        this.vari = vari;
        this.setFill(vari);
    }

    public int getNakyvyys() {
        return nakyvyys;
    }

    public void setNakyvyys(int nakyvyys) {
        this.nakyvyys = nakyvyys;
    }
}
