package com.example.piirto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Ruudukko {@link Pikseli Pikseleitä}, joita voi värittää. Useita tasoja voi luoda.
 */
public class PiirtoAlue extends StackPane {
    /**
     * Yksittäisen {@link Pikseli Pikselin} reunan pituus.
     */
    private double pikseliReunaPituus;
    /**
     * PiirtoAlueen leveys kuvapisteissä.
     */
    private double leveys;
    /**
     * PiirtoAlueen korkeus kuvapisteissä.
     */
    private double korkeus;
    /**
     * PiirtoAlueen leveys {@link Pikseli Pikseleissä}.
     */
    private int pikseleitaX;
    /**
     * PiirtoAlueen korkeus {@link Pikseli Pikseleissä}.
     */
    private int pikseleitaY;
    /**
     * Lista PiirtoAlueen {@link PiirtoTaso PiirtoTasoista}.
     */
    private ArrayList<PiirtoTaso> tasot = new ArrayList<>();
    /**
     * Ruudukko, joka osoittaa Pikselien rajat.
     */
    private Ruudukko ruudukko;
    /**
     * Onko ruudukko piilotettu vai ei.
     */
    private boolean ruudukkoPiilotettu = true;

    /**
     * Luo uuden PiirtoAlueen annetulla leveydellä, korkeudella ja pikselien määrillä.
     * Todellinen leveys ja korkeus riippuvat pikselien määristä, sillä pikselit
     * ovat aina neliön muotoisia.
     * @param leveys PiirtoAlueen leveys kuvapisteissä
     * @param korkeus PiirtoAlueen korkeus kuvapisteissä
     * @param pikseleitaX PiirtoAlueen leveys pikseleissä
     * @param pikseleitaY PiirtoAlueen korkeus pikseleissä
     */
    public PiirtoAlue(double leveys, double korkeus, int pikseleitaX, int pikseleitaY) {
        super();
        this.pikseliReunaPituus = Math.min(leveys / (double) pikseleitaX, korkeus / (double) pikseleitaY);
        this.leveys = pikseleitaX * pikseliReunaPituus;
        this.korkeus = pikseleitaY * pikseliReunaPituus;
        this.pikseleitaX = pikseleitaX;
        this.pikseleitaY = pikseleitaY;

        // Asetetaan mitat pikseleille
        PiirtoTaso.setMitat(this.leveys, this.korkeus, this.pikseleitaX, this.pikseleitaY);
        Pikseli.setReunaPituus(pikseliReunaPituus);

        // Luodaan ensimmäinen taso
        lisaaTaso();

        // Luodaan ruudukko
        ruudukko = new Ruudukko(leveys, korkeus, pikseleitaX, pikseleitaY);
        toggleRuudukko();
    }

    /**
     * Luo uuden PiirtoAlueen annetulla leveydellä, korkeudella, pikselien määrillä ja
     * {@link #tallennus() tallennus-metodilla} tallennetuilla tasojen tiedoilla.
     * Todellinen leveys ja korkeus riippuvat pikselien määristä, sillä pikselit
     * ovat aina neliön muotoisia.
     * @param leveys PiirtoAlueen leveys kuvapisteissä
     * @param korkeus PiirtoAlueen korkeus kuvapisteissä
     * @param pikseleitaX PiirtoAlueen leveys pikseleissä
     * @param pikseleitaY PiirtoAlueen korkeus pikseleissä
     * @param o Tiedot tallennetuista tasoista
     */
    public PiirtoAlue(double leveys, double korkeus, int pikseleitaX, int pikseleitaY, Object[] o) {
        super();
        this.pikseliReunaPituus = Math.min(leveys / (double) pikseleitaX, korkeus / (double) pikseleitaY);
        this.leveys = pikseleitaX * pikseliReunaPituus;
        this.korkeus = pikseleitaY * pikseliReunaPituus;
        this.pikseleitaX = pikseleitaX;
        this.pikseleitaY = pikseleitaY;

        // Asetetaan mitat pikseleille
        PiirtoTaso.setMitat(this.leveys, this.korkeus, this.pikseleitaX, this.pikseleitaY);
        Pikseli.setReunaPituus(pikseliReunaPituus);

        // Luodaan tasot
        Object[][] tasot = (Object[][]) o[0];
        for (Object[] oTaso : tasot) {
            lisaaTaso(oTaso);
        }

        // Luodaan ruudukko
        ruudukko = new Ruudukko(leveys, korkeus, pikseleitaX, pikseleitaY);
        setRuudukkoPiilotettu((Boolean) o[1]);
    }

    /**
     * @return Piirtoalueen tasojen lukumäärä
     */
    public int getTasoMaara() {
        return tasot.size();
    }

    /**
     * Palauttaa PiirtoAlueen kaikkien tasojen nimet siinä järjestyksessä, mihin ne on asetettu.
     * @return Taulukko tasojen nimistä
     */
    public String[] getTasoNimet() {
        String[] tasoNimet = new String[tasot.size()];
        for (int i = 0; i < tasot.size(); i++) {
            tasoNimet[i] = tasot.get(i).getNimi();
        }
        return tasoNimet;
    }

    /**
     * Palauttaa {@link PiirtoTaso PiirtoTason} annetusta PiirtoAlueen tasojen indeksistä.
     * @param taso Tason indeksi
     * @return Taso-olio
     */
    public PiirtoTaso getTaso(int taso) {
        return tasot.get(taso);
    }

    /**
     * Lisää annetulla nimellä nimetyn PiirtoTason PiirtoAlueeseen päällimmäiseksi.
     * @param nimi PiirtoTason nimi
     */
    public void lisaaTaso(String nimi) {
        PiirtoTaso t = new PiirtoTaso(nimi);
        tasot.add(t);
        this.getChildren().add(t);
        // Poistetaan ja lisätään ruudukko, jotta se on aina päällimmäisenä
        if (!ruudukkoPiilotettu) {
            this.getChildren().remove(ruudukko);
            this.getChildren().add(ruudukko);
        }
    }

    /**
     * Lisää PiirtoTason oletusnimellä PiirtoAlueeseen päällimmäiseksi.
     */
    public void lisaaTaso() {
        lisaaTaso("");
    }

    /**
     * Lisää {@link #tallennus() tallennus-metodilla} tallennetun tason PiirtoAlueeseen päällimmäiseksi.
     * @param o Tallennetun tason tiedot
     */
    private void lisaaTaso(Object[] o) {
        PiirtoTaso t = new PiirtoTaso(o);
        tasot.add(t);
        this.getChildren().add(t);
        // Poistetaan ja lisätään ruudukko, jotta se on aina päällimmäisenä
        if (!ruudukkoPiilotettu) {
            this.getChildren().remove(ruudukko);
            this.getChildren().add(ruudukko);
        }
    }

    /**
     * Poistaa PiirtoAlueesta tason annetusta indeksistä.
     * @param taso Poistettavan tason indeksi
     */
    public void poistaTaso(int taso) {
        tasot.remove(taso);
        this.getChildren().remove(taso);
    }

    /**
     * Piilottaa tai näyttää PiirtoAlueesta tason annetusta indeksistä.
     * @param taso Piilotettavan/Näytettävän tason indeksi
     */
    public void toggleTaso(int taso) {
        tasot.get(taso).toggleNakyvyys();
    }

    /**
     * Siirtää annettua tasoa annetun määrän tasoja ylös.
     * @param taso Siirrettävän tason indeksi
     * @param maara Kuinka monta tasoa ylös siirretään. Negatiivinen arvo siirtää alaspäin.
     * @return Kuinka monen tason verran tasoa siirrettiin ylös.
     */
    public int siirraTaso(int taso, int maara) {
        // Liian suuri tason siirtomäärä korjataan
        if (taso + maara < 0) {
            maara = -taso;
        } else if (taso + maara > tasot.size() - 1) {
            maara = tasot.size() - 1 - taso;
        }
        if (!(maara == 0)) {
            Collections.swap(tasot, taso, taso + maara);
            // Lähde: Reegan Miranda https://stackoverflow.com/a/22069230/18611804
            ObservableList<Node> childrenSwap = FXCollections.observableArrayList(this.getChildren());
            Collections.swap(childrenSwap, taso, taso + maara);
            this.getChildren().setAll(childrenSwap);
        }
        return maara;
    }

    /**
     * @return Onko PiirtoAlueen ruudukko piilotettu vai ei
     */
    public boolean getRuudukkoPiilotettu() {
        return ruudukkoPiilotettu;
    }

    /**
     * @param ruudukkoPiilotettu Onko PiirtoAlueen ruudukko piilotettu vai ei
     */
    public void setRuudukkoPiilotettu(boolean ruudukkoPiilotettu) {
        this.ruudukkoPiilotettu = ruudukkoPiilotettu;
        if (ruudukkoPiilotettu) {
            this.getChildren().remove(ruudukko);
        } else {
            this.getChildren().add(ruudukko);
        }
    }

    /**
     * Näyttää tai piilottaa PiirtoAlueen ruudukon.
     */
    public void toggleRuudukko() {
        setRuudukkoPiilotettu(!ruudukkoPiilotettu);
    }

    /**
     * Palauttaa {@link Pikseli Pikselin} annetulta tasolta kohdasta, jossa ikkunan
     * kuvapistekoordinaatit ovat x ja y.
     * @param taso Indeksi tasolle, josta haetaan pikseliä
     * @param x X-koordinaatti
     * @param y Y-koordinaatti
     * @return Pikseli
     */
    public Pikseli getPikseli(int taso, int x, int y) throws IllegalArgumentException {
        try {
            return tasot.get(taso).
                    getPikseli((int) (x / leveys * pikseleitaX), (int) (y / korkeus * pikseleitaY));
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("Annettu piste " + x + ", " + y + " on PiirtoAlueen ulkopuolella");
        }
    }

    /**
     * Etsii PiirtoAlueen pikselin annettujen koordinaattien perusteella ja muuttaa tämän väriä ja näkyvyyttä.
     * @param taso Indeksi tasolle, josta haetaan pikseliä
     * @param x X-koordinaatti
     * @param y Y-koordinaatti
     * @param vari Pikselin uusi väri
     * @param nakyvyys Pikselin uusi näkyvyys
     */
    public void setPikseli(int taso, int x, int y, Color vari, int nakyvyys) {
        try {
            getPikseli(taso, x, y).setPikseli(vari, nakyvyys);
        } catch (IllegalArgumentException ignored) {}
    }

    /**
     * Etsii PiirtoAlueen pikselin annettujen koordinaattien perusteella ja muuttaa tämän ja ympäröivien pikseleiden
     * väriä ja näkyvyyttä. Paksuus määrittää, kuinka laajalta alueelta pikseleitä muutetaan.
     * @param taso Indeksi tasolle, josta haetaan pikseliä
     * @param x X-koordinaatti
     * @param y Y-koordinaatti
     * @param vari Pikselin uusi väri
     * @param nakyvyys Pikselin uusi näkyvyys
     * @param paksuus Kuinka laajalta alueelta pikseleitä muutetaan
     */
    public void setPikseli(int taso, int x, int y, Color vari, int nakyvyys, int paksuus) {
        int pikseliX = (int) (x / leveys * pikseleitaX);
        int pikseliY = (int) (y / korkeus * pikseleitaY);
        PiirtoTaso Ptaso = tasot.get(taso);
        for (int pX = (int) (pikseliX - ((paksuus / 2d) - 1)); pX < pikseliX + ((paksuus / 2d) + 1); pX++) {
            for (int pY = (int) (pikseliY - ((paksuus / 2d) - 1)); pY < pikseliY + ((paksuus / 2d) + 1); pY++) {
                // Väritetään ympyrän muotoinen alue
                try {
                    if (Math.sqrt(Math.pow(pX - pikseliX, 2) + Math.pow(pY - pikseliY, 2)) <= paksuus / 2d) {
                        Ptaso.getPikseli(pX, pY).setPikseli(vari, nakyvyys);
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {}
            }
        }
    }

    /**
     * Object-taulukko PiirtoAlueen tallentamista varten.
     * @return Tasot, ruudukkoPiilotettu, valittuTaso
     */
    public Object[] tallennus() {
        Object[][] tasotTallennus = new Object[tasot.size()][4]; // Kovakoodattu palautettavien alkioiden määrä, voi muuttua :(
        for (int i = 0; i < tasot.size(); i++) {
            tasotTallennus[i] = tasot.get(i).tallennus();
        }
        return new Object[] {
                tasotTallennus,
                ruudukkoPiilotettu
        };
    }
}
