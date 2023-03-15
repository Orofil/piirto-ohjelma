package com.example.piirto;

import javafx.scene.layout.GridPane;

import java.io.Serializable;

public class PiirtoTaso extends GridPane implements Serializable {
    private static double leveys;
    private static double korkeus;
    private static int pikseleitaX;
    private static int pikseleitaY;
    private static int tasoNro;

    private Pikseli[][] pikselit = new Pikseli[pikseleitaX][pikseleitaY];
    private String nimi;
    private int nakyvyys = 100;
    private boolean piilotettu;
    private boolean isPohjataso;

    // TODO ehkä poistetaan tämä ja tähän liittyvät kentät täältä
    public static void setMitat(double leveys, double korkeus, int pikseleitaX, int pikseleitaY) {
        PiirtoTaso.leveys = leveys;
        PiirtoTaso.korkeus = korkeus;
        PiirtoTaso.pikseleitaX = pikseleitaX;
        PiirtoTaso.pikseleitaY = pikseleitaY;
    }

    public PiirtoTaso(String nimi) {
        super();

        tasoNro++;
        this.nimi = nimi;

        tayta();
    }

    public PiirtoTaso() { // TODO
//        super();
//
//        tasoNro++;
//        nimi = "Taso " + tasoNro;
//
//        tayta();
        this("Taso " + (tasoNro + 1));
    }


    public Pikseli getPikseli(int x, int y) {
        return pikselit[x][y];
    }

    public int getNakyvyys() {
        return nakyvyys;
    }

    public void setNakyvyys(int nakyvyys) {
        this.nakyvyys = nakyvyys;
    }

    /**
     * Piilottaa tai näyttää tason. Tasolle asetettu näkyvyys (0-100) säilyy erikseen muistissa.
     */
    public void toggleNakyvyys() {
        piilotettu = !piilotettu;
    }

    /**
     * Täyttää tason pikseleillä.
     */
    private void tayta() {
        for (int x = 0; x < pikseleitaX; x++) {
            for (int y = 0; y < pikseleitaY; y++) {
                Pikseli p = new Pikseli();
                pikselit[x][y] = p;
                this.add(p, x, y);
            }
        }
    }
}
