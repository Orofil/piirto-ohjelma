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
    private int valittuTaso;
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

        // Valitaan oikea taso
        setValittuTaso((Integer) o[2]);
    }

    public int getTasoMaara() {
        return tasot.size();
    }

    // TODO onko nämä järkevät
    public PiirtoTaso getValittuTaso() {
        return tasot.get(valittuTaso);
    }

    public int getValittuTasoNro() {
        return valittuTaso;
    }

    public void setValittuTaso(int valittuTaso) {
        this.valittuTaso = valittuTaso;
        System.out.println("Valittu taso: " + valittuTaso); // TEMP
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
        // Valitaan uusi taso
        setValittuTaso(tasot.size() - 1);
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

    public void toggleTaso(int taso) {
        tasot.get(taso).toggleNakyvyys();
    }

    /**
     * Siirtää valittua tasoa annetun määrän tasoja ylös.
     * @param maara Kuinka monta tasoa ylös siirretään. Negatiivinen arvo siirtää alaspäin.
     */
    public void siirraTaso(int maara) {
        // TODO https://stackoverflow.com/questions/17761415/how-to-change-order-of-children-in-javafx
        // TODO laita lähde koodille
        ObservableList<Node> workingCollection = FXCollections.observableArrayList(this.getChildren());
        Collections.swap(workingCollection, valittuTaso, valittuTaso - maara); // TODO tarkistus ettei mene yli rajojen
        this.getChildren().setAll(workingCollection); // TODO ruudukko
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

    public Pikseli getPikseli(int x, int y) {
        return tasot.get(valittuTaso).
                getPikseli((int) (x / leveys * pikseleitaX), (int) (y / korkeus * pikseleitaY));
    }

    /**
     * Etsii PiirtoAlueen pikselin annettujen koordinaattien perusteella ja muuttaa tämän väriä ja näkyvyyttä.
     * @param x Pikselin x-koordinaatti
     * @param y Pikselin y-koordinaatti
     * @param vari Pikselin uusi väri
     * @param nakyvyys Pikselin uusi näkyvyys
     */
    public void setPikseli(int x, int y, Color vari, int nakyvyys) {
        getPikseli(x, y).setPikseli(vari, nakyvyys);
    }

    // TODO paksuus piirto
    public void setPikseli(int x, int y, Color vari, int nakyvyys, int paksuus) {
        int pikseliX = (int) (x / leveys * pikseleitaX);
        int pikseliY = (int) (y / korkeus * pikseleitaY);
        PiirtoTaso taso = tasot.get(valittuTaso);
        // TODO int castin sijaan läpinäkyvyyttä reunapikseleille
        for (int pX = (int) (pikseliX - ((paksuus / 2d) - 1)); pX < pikseliX + ((paksuus / 2d) + 1); pX++) {
            for (int pY = (int) (pikseliY - ((paksuus / 2d) - 1)); pY < pikseliY + ((paksuus / 2d) + 1); pY++) {
                // TODO alue ei ole ympyrä vaan neliö, voi toki olla asetus
                if (Math.sqrt(Math.pow(pX - pikseliX, 2) + Math.pow(pY - pikseliY, 2)) <= paksuus) {
                    taso.getPikseli(pX, pY).setPikseli(vari, nakyvyys);
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
                ruudukkoPiilotettu,
                valittuTaso
        };
    }
}
