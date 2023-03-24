package com.example.piirto;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

/**
 * Suorakulmio, joka toimii {@link PiirtoTaso PiirtoTasolla} muokattavana
 * pikselinä.
 */
public class Pikseli extends Rectangle implements Serializable {
    /**
     * Yksittäisen pikselin reunan pituus.
     */
    private static double reunaPituus;

    /**
     * @return Yksittäisen pikselin reunan pituus.
     */
    public static double getReunaPituus() {
        return reunaPituus;
    }

    /**
     * @param reunaPituus Yksittäisen pikselin reunan pituus.
     */
    public static void setReunaPituus(double reunaPituus) {
        Pikseli.reunaPituus = reunaPituus;
    }

    /**
     * Luo Pikselin annettuun sijaintiin (sarake ja rivi) annetulla
     * värillä ja läpinäkyvyydellä.
     * @param x Pikselin sarake (x-koordinaatti)
     * @param y Pikselin rivi (y-koordinaatti)
     * @param vari Pikselin väri
     * @param nakyvyys Pikselin (läpi)näkyvyys asteikolla 0-100
     */
    public Pikseli(int x, int y, Color vari, int nakyvyys) {
        /*
        Lisätään leveyteen ja korkeuteen vähän, jotta pikselien väleissä ei näy valkoista.
        Toisaalta se aiheuttaa alemmilla näkyvyyksillä tummia viivoja pikselien väleihin.
         */
        super(x * reunaPituus, y * reunaPituus, reunaPituus + 0.7, reunaPituus + 0.7);
        this.setFill(vari);

        this.setNakyvyys(nakyvyys);
    }

    /**
     * Luo Pikselin annettuun sijaintiin (sarake ja rivi) annetulla
     * värillä. Läpinäkyvyys on oletuksena 100.
     * @param x Pikselin sarake (x-koordinaatti)
     * @param y Pikselin rivi (y-koordinaatti)
     * @param vari Pikselin väri
     */
    public Pikseli(int x, int y, Color vari) {
        this(x, y, vari, 100);
    }

    /**
     * Luo Pikselin annettuun sijaintiin (sarake ja rivi). Väri on oletuksena
     * läpinäkyvä ja läpinäkyvyys oletuksena 100.
     * @param x Pikselin sarake (x-koordinaatti)
     * @param y Pikselin rivi (y-koordinaatti)
     */
    public Pikseli(int x, int y) {
        this(x, y, Color.TRANSPARENT, 100); // TODO tämä voi olla valkoinen jos läpinäkyvyys toteutetaan
    }

    /**
     * Luo Pikselin {@link #tallennus() tallennus-metodilla} tallennettujen tietojen pohjalta.
     * @param o X, Y, väri, näkyvyys
     */
    public Pikseli(Object[] o) { // TODO ei tarvita
        this(
                (Integer) o[0],
                (Integer) o[1],
                ((Vari) o[2]).toColor(),
                (Integer) o[3]);
    }

    /**
     * @return Pikselin väri
     */
    public Color getVari() { // TODO ei kai näitä tarvitse
        return (Color) this.getFill();
    }

    /**
     * @param vari Pikselin väri
     */
    public void setVari(Color vari) {
        this.setFill(vari);
    }

    /**
     * @return Pikselin läpinäkyvyys asteikolla 0-100
     */
    public int getNakyvyys() {
        return (int) this.getOpacity() * 100;
    }

    /**
     * @param nakyvyys Pikselin läpinäkyvyys asteikolla 0-100
     */
    public void setNakyvyys(int nakyvyys) {
        if (nakyvyys < 0) { // TODO tarvitseeko tätä
            this.setOpacity(0);
        } else if (nakyvyys > 100) {
            this.setOpacity(1);
        } else {
            this.setOpacity(nakyvyys / 100d);
        }
    }

    /**
     * Asettaa sekä Pikselin värin että läpinäkyvyyden
     * @param vari Pikselin väri
     * @param nakyvyys Pikselin läpinäkyvyys asteikolla 0-100
     */
    public void setPikseli(Color vari, int nakyvyys) {
        setVari(vari);
        setNakyvyys(nakyvyys);
    }

    /**
     * Object-taulukko Pikselin tiedostoon tallentamista varten.
     * @return X, Y, väri, näkyvyys
     */
    public Object[] tallennus() {
        return new Object[] {
                Vari.toVari((Color) this.getFill()).tallennus(),
                (int) this.getOpacity()
        };
    }
}
