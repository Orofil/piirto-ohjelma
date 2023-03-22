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

    // TODO läpinäkyvyys ei toimi, alempien tasojen värit eivät näy

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
     * Asettaa Pikselille sopivan {@link #reunaPituus reunaPituuden} ympäröivän
     * {@link PiirtoAlue PiirtoAlueen} mittojen mukaan.
     * @param alueLeveys PiirtoAlueen leveys kuvapisteissä
     * @param alueKorkeus PiirtoAlueen korkeus kuvapisteissä
     * @param pikseleitaX PiirtoAlueen leveys Pikseleissä
     * @param pikseleitaY PiirtoAlueen korkeus Pikseleissä
     */
    public static void setMitat(double alueLeveys, double alueKorkeus, int pikseleitaX, int pikseleitaY) {
        Pikseli.reunaPituus = Math.min(alueLeveys / (double) pikseleitaX, alueKorkeus / (double) pikseleitaY);
    }

    /**
     * Luo Pikselin annettuun sijaintiin (sarake ja rivi) annetulla
     * värillä ja läpinäkyvyydellä.
     * @param x Pikselin sarake (x-koordinaatti)
     * @param y Pikselin rivi (y-koordinaatti)
     * @param vari Pikselin väri
     * @param nakyvyys Pikselin (läpi)näkyvyys asteikolla 0-100
     */
    public Pikseli(double x, double y, Color vari, int nakyvyys) {
        // Lisätään leveyteen ja korkeuteen vähän, jotta pikselien väleissä ei näy valkoista
        // TODO tee niin scuffed että jos näkyvyys on 100 niin +0.7 mittoihin mutta muuten ei, koska overlap
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
    public Pikseli(double x, double y, Color vari) {
        this(x, y, vari, 100);
    }

    /**
     * Luo Pikselin annettuun sijaintiin (sarake ja rivi). Väri on oletuksena
     * läpinäkyvä ja läpinäkyvyys oletuksena 100.
     * @param x Pikselin sarake (x-koordinaatti)
     * @param y Pikselin rivi (y-koordinaatti)
     */
    public Pikseli(double x, double y) {
        this(x, y, Color.TRANSPARENT, 100); // TODO tämä voi olla valkoinen jos läpinäkyvyys toteutetaan
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
}
