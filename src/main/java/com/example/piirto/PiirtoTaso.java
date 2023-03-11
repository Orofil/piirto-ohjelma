package com.example.piirto;

import javafx.scene.layout.GridPane;

public class PiirtoTaso extends GridPane {
    private static double leveys;
    private static double korkeus;
    private static int pikseleitaX;
    private static int pikseleitaY;
    private static int tasoNro;

    public static void setMitat(double leveys, double korkeus, int pikseleitaX, int pikseleitaY) {
        PiirtoTaso.leveys = leveys;
        PiirtoTaso.korkeus = korkeus;
        PiirtoTaso.pikseleitaX = pikseleitaX;
        PiirtoTaso.pikseleitaY = pikseleitaY;
    }

    private String nimi;
    private int lapinakyvyys = 100;
    private boolean isPohjataso;

    public PiirtoTaso() { // TODO
//        super();
//
//        tasoNro++;
//        nimi = "Taso " + tasoNro;
//
//        tayta();
        this("Taso " + (tasoNro + 1));
    }

    public PiirtoTaso(String nimi) {
        super();

        tasoNro++;
        this.nimi = nimi;

        tayta();
    }

    private void tayta() {
        Pikseli.setLeveys(leveys / (double) pikseleitaX);
        Pikseli.setKorkeus(korkeus / (double) pikseleitaY);

        for (int x = 0; x < pikseleitaX; x++) {
            for (int y = 0; y < pikseleitaY; y++) {
                this.add(new Pikseli(y, x), y, x);
            }
        }
    }
}
