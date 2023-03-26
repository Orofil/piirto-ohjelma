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

    public String toString() {
        return "Väri: R = " + r + ", G = " + g + ", B = " + b + ", A = " + a;
    }

    public Object[] tallennus() {
        return new Object[] {r, g, b, a};
    }
}