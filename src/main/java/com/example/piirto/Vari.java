package com.example.piirto;

import javafx.scene.paint.Color;

import java.io.Serializable;

/**
 * Sarjallistettava versio JavaFX:n Color-luokasta sitä varten, että
 * voin tallentaa PiirtoAlueen ja siinä olevat asiat sarjallistamalla.
 */
public class Vari implements Serializable {
    /**
     * Punainen (0-255)
     */
    private int r;
    /**
     * Vihreä (0-255)
     */
    private int g;
    /**
     * Sininen (0-255)
     */
    private int b;
    /**
     * Läpinäkyvyys (0.0-1.0)
     */
    private double a = 1.0;

    /**
     * Luo uuden värin punaisen, vihreän ja sinisen määrän sekä läpinäkyvyyden pohjalta.
     * @param r Punainen (0-255)
     * @param g Vihreä (0-255)
     * @param b Sininen (0-255)
     * @param a Läpinäkyvyys (0.0-1.0)
     */
    public Vari(int r, int g, int b, double a) {
        Color.rgb(r, g, b, a); // Annetaan Color-luokan itse heittää Exceptionit jos parametrit eivät ole kelvollisia
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * Luo uuden värin punaisen, vihreän ja sinisen määrän pohjalta.
     * @param r Punainen (0-255)
     * @param g Vihreä (0-255)
     * @param b Sininen (0-255)
     */
    public Vari(int r, int g, int b) {
        this(r, g, b, 1.0);
    }

    /**
     * Luo uuden värin Color-luokan olion pohjalta.
     * @param c Color-luokan olio
     */
    public Vari(Color c) {
        r = (int) (c.getRed() * 255);
        g = (int) (c.getGreen() * 255);
        b = (int) (c.getBlue() * 255);
        a = c.getOpacity();
    }

    public Vari(Object[] o) {
        r = (Integer) o[0];
        g = (Integer) o[1];
        b = (Integer) o[2];
        a = (Double) o[3];
    }

    /**
     * Palauttaa tämän luokan olion väriä vastaavan Color-luokan olion.
     * @return Color-luokan olio
     */
    public Color toColor() {
        return Color.rgb(r, g, b, a);
    }

    public static Vari toVari(Color c) {
        return new Vari(c);
    }

    public String toString() { // TEMP
        return "Väri: R = " + r + ", G = " + g + ", B = " + b + ", A = " + a;
    }

    public Object[] tallennus() {
        return new Object[] {r, g, b, a};
    }

    /**
     * Palauttaa kahden värin (Vari) etäisyyden toisistaan, eli värien
     * punaisten, vihreiden ja sinisten arvojen erotusten summan painotettuna
     * värien näkyvyyksillä.
     * <br>
     * Summa voi saada arvoja välillä 0-765.
     * @param v1 Vari 1
     * @param v2 Vari 2
     * @return Värien 1 ja 2 etäisyys
     */
    public static double etaisyys(Vari v1, Vari v2) {
        // TODO HSL vertaus koska tämä on huoooono
        double a1 = Math.abs(1 - v1.a);
        double a2 = Math.abs(1 - v2.a);
        // Tätä olisi varmaan voinut sieventää paremmaksi
        return Math.abs(v1.r*a1 - v2.r*a2) + Math.abs(v1.g*a1 - v2.g*a2) + Math.abs(v1.b*a1 - v2.b*a2);
    }

    /**
     * Palauttaa kahden värin (Color) etäisyyden toisistaan, eli värien
     * punaisten, vihreiden ja sinisten arvojen erotusten summan painotettuna
     * värien näkyvyyksillä.
     * <br>
     * Summa voi saada arvoja välillä 0-765.
     * @param c1 Color 1
     * @param c2 Color 2
     * @return Värien 1 ja 2 etäisyys
     */
    public static double etaisyys(Color c1, Color c2) {
        return etaisyys(Vari.toVari(c1), Vari.toVari(c2));
    }
}