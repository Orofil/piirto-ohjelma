package com.example.piirto;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.io.Serializable;

/**
 * {@link PiirtoAlue PiirtoAlueelle} lisättävä taso, joka sisältää väritettäviä
 * {@link Pikseli Pikseleitä}.
 */
public class PiirtoTaso extends Pane {
    /**
     * Piirtotason leveys kuvapisteissä.
     */
    private static double leveys;
    /**
     * Piirtotason korkeus kuvapisteissä.
     */
    private static double korkeus;
    /**
     * Piirtotason leveys {@link Pikseli Pikseleissä}..
     */
    private static int pikseleitaX;
    /**
     * Piirtotason korkeus {@link Pikseli Pikseleissä}.
     */
    private static int pikseleitaY;
    /**
     * Piirtotason järjestysluku.
     */
    private static int tasoNro;

    /**
     * Kaksiulotteinen taulukko piirtotason sisältämistä {@link Pikseli Pikseleistä}.
     * <br>
     * Taulukon ylempi (ulompi) taso on kuvaa sarakkeita ja alempi
     * (sisempi) rivejä.
     * <br>
     * Esimerkiksi: <code>Pikseli[2][3]</code> = Pikseli sarakkeessa 2 ja rivissä 3
     * (alkaen nollasta).
     */
    private Pikseli[][] pikselit = new Pikseli[pikseleitaX][pikseleitaY];
    /**
     * Tason nimi. Oletuksena <code>Taso n</code>, jossa <code>n</code> on {@link #tasoNro tasoNro}.
     */
    private String nimi;
    /**
     * Tason näkyvyys asteikolla 0-100.
     */
    private int nakyvyys = 100;
    /**
     * Onko taso piilotettu vai ei. {@link #nakyvyys nakyvyys} 100 ei tarkoita, että
     * <code>piilotettu</code> olisi tosi, eikä toisin päin.
     */
    private boolean piilotettu;

    public static void setMitat(double leveys, double korkeus, int pikseleitaX, int pikseleitaY) {
        PiirtoTaso.leveys = leveys;
        PiirtoTaso.korkeus = korkeus;
        PiirtoTaso.pikseleitaX = pikseleitaX;
        PiirtoTaso.pikseleitaY = pikseleitaY;
    }

    /**
     * Luo PiirtoTason annetulla nimellä. Jos <code>nimi</code> on tyhjä, annetaan
     * nimeksi oletuksena <code>Taso n</code>, jossa <code>n</code> on {@link #tasoNro tasoNro}.
     * @param nimi Tason nimi
     */
    public PiirtoTaso(String nimi) {
        super();

        tasoNro++;
        // Asetetaan tason nimeksi Taso n (jossa n on tason järjestysluku) jos nimi on null tai tyhjä
        if (nimi.equals("")) {
            this.nimi = "Taso " + tasoNro;
        } else {
            this.nimi = nimi;
        }

        // Täytetään taso pikseleillä
        tayta();
    }

    /**
     * Luo PiirtoTason oletusnimellä <code>Taso n</code>, jossa <code>n</code> on {@link #tasoNro tasoNro}.
     */
    public PiirtoTaso() {
        this("");
    }

    /**
     * Luo PiirtoTason {@link #tallennus() tallennus-metodilla} tallennettujen tietojen pohjalta.
     * @param o Nimi, näkyvyys, piilotettu, Pikselit
     */
    public PiirtoTaso(Object[] o) {
        super();

        tasoNro++;
        nimi = (String) o[0];

        setNakyvyys((Integer) o[1]);
        setPiilotettu((Boolean) o[2]);

        tayta((Object[][][]) o[3]);
    }

    /**
     * @return Tason järjestysluku, jota käytetään tason oletusnimen asettamisessa.
     */
    public static int getTasoNro() {
        return tasoNro;
    }

    /**
     * @param tasoNro Tason järjestysluku, jota käytetään tason oletusnimen asettamisessa.
     */
    public static void setTasoNro(int tasoNro) {
        PiirtoTaso.tasoNro = tasoNro;
    }

    /**
     * Palauttaa kaksiulotteisen taulukon piirtotason {@link Pikseli Pikseleistä}.
     * <br>
     * Taulukon ylempi (ulompi) taso on kuvaa sarakkeita ja alempi
     * (sisempi) rivejä.
     * <br>
     * Esimerkiksi: <code>Pikseli[2][3]</code> = Pikseli sarakkeessa 2 ja rivissä 3
     * (alkaen nollasta).
     * @return Pikseli-olioiden taulukko
     */
    public Pikseli[][] getPikselit() {
        return pikselit;
    }

    /**
     * Palauttaa {@link Pikseli Pikselin} annetusta kohdasta (sarake ja rivi) PiirtoTasoa.
     * @param x Pikselin sarake (x-koordinaatti)
     * @param y Pikselin rivi (y-koordinaatti)
     * @return Pikseli-olio
     */
    public Pikseli getPikseli(int x, int y) {
        return pikselit[x][y];
    }

    /**
     * Palauttaa taulukon annettua {@link Pikseli Pikseliä} ympäröivistä kahdeksasta Pikselistä,
     * alkaen vasemmasta yläkulmasta ja kiertäen myötäpäivään.
     * @param x Pikselin sarake (x-koordinaatti)
     * @param y Pikselin rivi (y-koordinaatti)
     * @return Kahdeksan Pikseli-olion taulukko
     */
    public Pikseli[] getYmparoivatPikselit8(int x, int y) {
        return new Pikseli[] {
            pikselit[x - 1][y - 1],
            pikselit[x - 1][y],
            pikselit[x - 1][y + 1],
            pikselit[x][y - 1],
            pikselit[x][y + 1],
            pikselit[x + 1][y - 1],
            pikselit[x + 1][y],
            pikselit[x + 1][y + 1]
        };
    }

    /**
     * Palauttaa taulukon annettua {@link Pikseli Pikseliä} ympäröivistä kahdeksasta Pikselistä,
     * alkaen yläreunasta ja kiertäen myötäpäivään.
     * @param x Pikselin sarake (x-koordinaatti)
     * @param y Pikselin rivi (y-koordinaatti)
     * @return Neljän Pikseli-olion taulukko
     */
    public Pikseli[] getYmparoivatPikselit4(int x, int y) {
        return new Pikseli[] {
                pikselit[x - 1][y],
                pikselit[x][y - 1],
                pikselit[x][y + 1],
                pikselit[x + 1][y]
        };
    }

    /**
     * Asettaa sarakkeessa x ja rivillä y olevan pikselin värin ja näkyvyyden.
     * @param x Pikselin sarake (x-koordinaatti)
     * @param y Pikselin rivi (y-koordinaatti)
     * @param vari Pikselin uusi väri
     * @param nakyvyys Pikselin uusi näkyvyys
     */
    public void setPikseli(int x, int y, Color vari, int nakyvyys) {
        pikselit[x][y].setPikseli(vari, nakyvyys);
    }

    /**
     * @return PiirtoTason nimi
     */
    public String getNimi() {
        return nimi;
    }

    /**
     * @param nimi PiirtoTason nimi
     */
    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    /**
     * @return PiirtoTason näkyvyys asteikolla 0-100.
     */
    public int getNakyvyys() {
        return nakyvyys;
    }

    /**
     * @param nakyvyys PiirtoTason näkyvyys asteikolla 0-100.
     */
    public void setNakyvyys(int nakyvyys) {
        this.nakyvyys = nakyvyys;
        this.setOpacity(nakyvyys / 100d);
    }

    /**
     * @return Onko taso piilotettu vai ei
     */
    public boolean getPiilotettu() {
        return piilotettu;
    }

    /**
     * @param piilotettu Onko taso piilotettu vai ei
     */
    public void setPiilotettu(boolean piilotettu) {
        this.piilotettu = piilotettu;
        this.setVisible(!piilotettu);
    }

    /**
     * Piilottaa tai näyttää tason. Tasolle asetettu näkyvyys (0-100) säilyy erikseen muistissa.
     */
    public void toggleNakyvyys() {
        setPiilotettu(!piilotettu);
    }

    /**
     * Täyttää tason tyhjillä {@link Pikseli Pikseleillä}.
     */
    private void tayta() {
        for (int x = 0; x < pikseleitaX; x++) {
            for (int y = 0; y < pikseleitaY; y++) {
                Pikseli p = new Pikseli(x, y);
                pikselit[x][y] = p;
                this.getChildren().add(p);
            }
        }
    }

    /**
     * Täyttää tason {@link #tallennus() tallennus-metodilla} tallennetuilla
     * {@link Pikseli Pikseleillä}.
     * @param o Tiedot tallennetuista pikseleistä
     */
    private void tayta(Object[][][] o) {
        for (int x = 0; x < pikseleitaX; x++) {
            for (int y = 0; y < pikseleitaY; y++) {
                Pikseli p = new Pikseli(x, y, new Vari((Object[]) o[x][y][0]).toColor(), (Integer) o[x][y][1]);
                pikselit[x][y] = p;
                this.getChildren().add(p);
            }
        }
    }

    /**
     * Object-taulukko PiirtoTason tallentamista varten.
     * @return Nimi, näkyvyys, piilotettu, Pikselit
     */
    public Object[] tallennus() {
        Object[][][] pikselitTallennus = new Object[pikseleitaX][pikseleitaY][2]; // Kovakoodattu palautettavien alkioiden määrä, voi muuttua :(
        for (int x = 0; x < pikseleitaX; x++) {
            for (int y = 0; y < pikseleitaY; y++) {
                pikselitTallennus[x][y] = pikselit[x][y].tallennus();
            }
        }
        return new Object[] {
                nimi,
                nakyvyys,
                piilotettu,
                pikselitTallennus
        };
    }
}
