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
 * Ruudukko neliöitä, joita voi värittää. Useita tasoja voi luoda.
 */
public class PiirtoAlue extends StackPane implements Serializable {
    private double pikseliReunaPituus;
    private double leveys;
    private double korkeus;
    private int pikseleitaX;
    private int pikseleitaY;
    private ArrayList<PiirtoTaso> tasot = new ArrayList<>();
    private Ruudukko ruudukko;
    private boolean ruudukkoPiilotettu = true;
    // TODO undo ja redo toiminnallisuus jotenkin ihmeellisesti

    public PiirtoAlue(double leveys, double korkeus, int pikseleitaX, int pikseleitaY) {
        super();
        this.pikseliReunaPituus = Math.min(leveys / (double) pikseleitaX, korkeus / (double) pikseleitaY);
        this.leveys = pikseleitaX * pikseliReunaPituus;
        this.korkeus = pikseleitaY * pikseliReunaPituus;
        this.pikseleitaX = pikseleitaX;
        this.pikseleitaY = pikseleitaY;

        // Asetetaan mitat pikseleille
        PiirtoTaso.setMitat(this.leveys, this.korkeus, this.pikseleitaX, this.pikseleitaY); // TODO näitä kaikkia ei kai tarvita
//        Pikseli.setMitat(this.leveys, this.korkeus, this.pikseleitaX, this.pikseleitaY);
        Pikseli.setReunaPituus(pikseliReunaPituus);

        // Luodaan ensimmäinen taso
        lisaaTaso();

        // Luodaan ruudukko
        ruudukko = new Ruudukko(leveys, korkeus, pikseleitaX, pikseleitaY);
        toggleRuudukko();
    }

    public PiirtoAlue(double leveys, double korkeus, int pikseleitaX, int pikseleitaY, Object[] o) {
        super();
        this.pikseliReunaPituus = Math.min(leveys / (double) pikseleitaX, korkeus / (double) pikseleitaY);
        this.leveys = pikseleitaX * pikseliReunaPituus;
        this.korkeus = pikseleitaY * pikseliReunaPituus;
        this.pikseleitaX = pikseleitaX;
        this.pikseleitaY = pikseleitaY;

        // Asetetaan mitat pikseleille
        PiirtoTaso.setMitat(this.leveys, this.korkeus, this.pikseleitaX, this.pikseleitaY); // TODO näitä kaikkia ei kai tarvita
//        Pikseli.setMitat(this.leveys, this.korkeus, this.pikseleitaX, this.pikseleitaY);
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

    public int getTasoMaara() {
        return tasot.size();
    }

    /**
     * Palauttaa PiirtoAlueen kaikkien tasojen nimet siinä järjestyksessä, mihin ne on asetettu.
     * @return Taulukko tasojen nimistä
     */
    public String[] getTasoNimet() {
        String[] tasonimet = new String[tasot.size()];
        for (int i = 0; i < tasot.size(); i++) {
            tasonimet[i] = tasot.get(i).getNimi();
        }
        return tasonimet;
    }

    public PiirtoTaso getTaso(int taso) {
        return tasot.get(taso);
    }

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

    public void lisaaTaso() {
        lisaaTaso("");
    }

    private void lisaaTaso(Object[] o) { // TODO koodin toistoa
        PiirtoTaso t = new PiirtoTaso(o);
        tasot.add(t);
        this.getChildren().add(t);
        // Poistetaan ja lisätään ruudukko, jotta se on aina päällimmäisenä
        if (!ruudukkoPiilotettu) {
            this.getChildren().remove(ruudukko);
            this.getChildren().add(ruudukko);
        }
    }

    public void poistaTaso(int taso) {
        tasot.remove(taso);
        this.getChildren().remove(taso);
    }

    public void toggleTaso(int taso) {
        tasot.get(taso).toggleNakyvyys();
    }

    /**
     * Siirtää annettua tasoa annetun määrän tasoja ylös.
     * @param taso Mitä tasoa siirretään
     * @param maara Kuinka monta tasoa ylös siirretään. Negatiivinen arvo siirtää alaspäin.
     */
    public void siirraTaso(int taso, int maara) {
        // Liian suuri tason siirtomäärä korjataan
        if (taso + maara < 0) {
            maara = -taso;
        } else if (taso + maara > tasot.size() - 2) { // -2 koska ei haluta mennä ruudukon päälle
            maara = tasot.size() - 2 - taso;
        }
        if (!(maara == 0)) {
            // Lähde: Reegan Miranda https://stackoverflow.com/a/22069230/18611804
            ObservableList<Node> workingCollection = FXCollections.observableArrayList(this.getChildren());
            Collections.swap(workingCollection, taso, taso + maara);
            this.getChildren().setAll(workingCollection);
        }
    }

    private void setRuudukkoPiilotettu(boolean ruudukkoPiilotettu) {
        this.ruudukkoPiilotettu = ruudukkoPiilotettu;
        if (ruudukkoPiilotettu) {
            this.getChildren().remove(ruudukko);
        } else {
            this.getChildren().add(ruudukko);
        }
    }

    public void toggleRuudukko() {
        setRuudukkoPiilotettu(!ruudukkoPiilotettu);
    }

    public Pikseli getPikseli(int taso, int x, int y) {
        return tasot.get(taso).
                getPikseli((int) (x / leveys * pikseleitaX), (int) (y / korkeus * pikseleitaY));
    }

    /**
     * Etsii PiirtoAlueen pikselin annettujen koordinaattien perusteella ja muuttaa tämän väriä ja näkyvyyttä.
     * @param x Pikselin x-koordinaatti
     * @param y Pikselin y-koordinaatti
     * @param vari Pikselin uusi väri
     * @param nakyvyys Pikselin uusi näkyvyys
     */
    public void setPikseli(int taso, int x, int y, Color vari, int nakyvyys) {
        getPikseli(taso, x, y).setPikseli(vari, nakyvyys);
    }

    // TODO siirrä Mainiin
    public void setPikseli(int taso, int x, int y, Color vari, int nakyvyys, int paksuus) {
        int pikseliX = (int) (x / leveys * pikseleitaX);
        int pikseliY = (int) (y / korkeus * pikseleitaY);
        PiirtoTaso Ptaso = tasot.get(taso);
        // TODO int castin sijaan läpinäkyvyyttä reunapikseleille
        for (int pX = (int) (pikseliX - ((paksuus / 2d) - 1)); pX < pikseliX + ((paksuus / 2d) + 1); pX++) {
            for (int pY = (int) (pikseliY - ((paksuus / 2d) - 1)); pY < pikseliY + ((paksuus / 2d) + 1); pY++) {
                double etaisyys = Math.sqrt(Math.pow(pX - pikseliX, 2) + Math.pow(pY - pikseliY, 2));
                double etaisyysReunasta = etaisyys - (paksuus / 2d) + 0.5;

                // Reunojen pehmennys
                if (etaisyysReunasta > 0 && etaisyysReunasta < 1) { // TODO ei toimi
                    Ptaso.getPikseli(pX, pY).setPikseli(vari, (int) (nakyvyys * etaisyysReunasta));
                } else if (etaisyys <= paksuus / 2d) {
                    Ptaso.getPikseli(pX, pY).setPikseli(vari, nakyvyys);
                }
            }
        }
    }

    // TODO tallennukseen!
    /**
     * Object-taulukko PiirtoAlueen tallentamista varten.
     * @return Tasot, ruudukkoPiilotettu, valittuTaso
     */
    public Object[] tallennus() {
        Object[][] tasotTallennus = new Object[tasot.size()][4]; // TODO 4 voi muuttua
        for (int i = 0; i < tasot.size(); i++) {
            tasotTallennus[i] = tasot.get(i).tallennus();
        }
        return new Object[] {
                tasotTallennus,
                ruudukkoPiilotettu
        };
    }
}
