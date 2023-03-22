package com.example.piirto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
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
    private String valittuTyokalu = "piirto"; // TODO tuleeko nämä Mainiin
    private Color valittuVari = Color.BLACK;
    private int valittuNakyvyys = 100;
    private int valittuPaksuus = 1;
    // TODO undo ja redo toiminnallisuus jotenkin ihmeellisesti

    /**
     * Tapahtumankäsittelijä piirtämiseen.
     */
//    private EventHandler<MouseEvent> tkPiirto = e -> { // TODO siirrä mainiin koska tämä ei ole serializable joten ei voi tallentaa :(((
//        int x = (int) e.getX();
//        int y = (int) e.getY();
//        Color vari = valittuVari;
//        if (e.isSecondaryButtonDown()) {
//            vari = Color.TRANSPARENT;
//        }
//
//        switch (valittuTyokalu) {
//            case "piirto":
//                if (valittuPaksuus == 1) {
//                    setPikseli(x, y, vari, valittuNakyvyys);
//                } else {
//                    setPikseli(x, y, vari, valittuNakyvyys, valittuPaksuus);
//                }
//                break;
//            case "poisto":
//                if (valittuPaksuus == 1) {
//                    setPikseli(x, y, Color.TRANSPARENT, 100);
//                } else {
//                    setPikseli(x, y, Color.TRANSPARENT, 100, valittuPaksuus);
//                }
//                break;
//            case "täyttö":  // TODO täyttö
//                if (valittuPaksuus == 1) {
//                    setPikseli(x, y, valittuVari, valittuNakyvyys);
//                } else {
//                    setPikseli(x, y, valittuVari, valittuNakyvyys, valittuPaksuus);
//                }
//                break;
//            case "värinpoimija":  // TODO värinpoimija
//                Pikseli pikseli = getPikseli(x, y);
//                setValittuVari(pikseli.getVari());
//                setValittuNakyvyys(pikseli.getNakyvyys());
//                break;
//        }
//    };

    public PiirtoAlue(double leveys, double korkeus, int pikseleitaX, int pikseleitaY) {
        super();
        this.pikseliReunaPituus = Math.min(leveys / (double) pikseleitaX, korkeus / (double) pikseleitaY);
        this.leveys = pikseleitaX * pikseliReunaPituus;
        this.korkeus = pikseleitaY * pikseliReunaPituus;
        this.pikseleitaX = pikseleitaX;
        this.pikseleitaY = pikseleitaY;

        // Luodaan ensimmäinen taso
        PiirtoTaso.setMitat(this.leveys, this.korkeus, this.pikseleitaX, this.pikseleitaY); // TODO näitä kaikkia ei kai tarvita
//        Pikseli.setMitat(this.leveys, this.korkeus, this.pikseleitaX, this.pikseleitaY);
        Pikseli.setReunaPituus(pikseliReunaPituus);

        PiirtoTaso t = new PiirtoTaso();
        tasot.add(t);
        this.getChildren().add(t);

        // Ruudukon piirtäminen
        ruudukko = new Ruudukko(leveys, korkeus, pikseleitaX, pikseleitaY);
        // TODO pitäisi olla ylimpänä aina, ehkä tason lisäys metodissa
        toggleRuudukko();

//        this.addEventHandler(MouseEvent.MOUSE_PRESSED, tkPiirto);
//        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, tkPiirto);
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
        System.out.println("Valittu taso: " + valittuTaso);
    }

    public Color getValittuVari() {
        return valittuVari;
    }

    public void setValittuVari(Color valittuVari) {
        this.valittuVari = valittuVari;
    }

    public int getValittuNakyvyys() {
        return valittuNakyvyys;
    }

    public void setValittuNakyvyys(int valittuNakyvyys) {
        this.valittuNakyvyys = valittuNakyvyys;
    }

    public int getValittuPaksuus() {
        return valittuPaksuus;
    }

    public void setValittuPaksuus(int valittuPaksuus) {
        this.valittuPaksuus = valittuPaksuus;
    }

    public void lisaaTaso(String nimi) {
        PiirtoTaso t = new PiirtoTaso(nimi);
        tasot.add(t);
        this.getChildren().add(t);
        // Poistetaan ja lisätään ruudukko, jotta se on aina päällimmäisenä
        this.getChildren().remove(ruudukko);
        this.getChildren().add(ruudukko);
    }

    public void lisaaTaso() {
        lisaaTaso(null);
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

    public void toggleRuudukko() {
        ruudukkoPiilotettu = !ruudukkoPiilotettu;
        if (ruudukkoPiilotettu) {
            this.getChildren().remove(ruudukko);
        } else {
            this.getChildren().add(ruudukko);
        }
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
}
