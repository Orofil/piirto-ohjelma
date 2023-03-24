package com.example.piirto;

import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    /**
     * Resurssien (kuvien ja viimeisimpien tiedostojen) tiedostopolku.
     */
    public final static String POLKU = "src/main/resources/com/example/piirto/";

    /**
     * Ikkunan leveys kuvapisteissä.
     */
    public final static int LEVEYS = 1000;
    /**
     * Ikkunan korkeus kuvapisteissä.
     */
    public final static int KORKEUS = 600;

    /*
    Vasemman valikon ja alavalikon paksuudet on valitettavasti kovakoodattu,
    koska en saanut niitä toimimaan paremmin.
     */
    /**
     * Vasemman valikon leveys kuvapisteissä.
     */
    public final static int VASENLEVEYS = 206;
    /**
     * Alavalikon korkeus kuvapisteissä.
     */
    public final static int ALAKORKEUS = 80;

    /**
     * {@link PiirtoTaso PiirtoTason} oletusleveys
     */
    public static int pikseleitaX = 76;
    /**
     * {@link PiirtoTaso PiirtoTason} oletuskorkeus
     */
    public static int pikseleitaY = 50;

    public static String valittuTyokalu = "piirto";
    public static Color valittuVari = Color.BLACK;
    public static int valittuNakyvyys = 100;
    public static int valittuPaksuus = 1;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        new NayttoAlku().start(stage);
    }
}