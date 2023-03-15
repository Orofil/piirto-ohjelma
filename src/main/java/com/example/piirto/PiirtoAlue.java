package com.example.piirto;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Ruudukko neliöitä, joita voi värittää. Useita tasoja voi luoda.
 */
public class PiirtoAlue extends StackPane implements Serializable {
    private double leveys;
    private double korkeus;
    private int pikseleitaX;
    private int pikseleitaY;
    private ArrayList<PiirtoTaso> tasot = new ArrayList<>();
    private int valittuTaso;

    private EventHandler<MouseEvent> tkPiirto = e -> {
        setPikseliVari((int) e.getX(), (int) e.getY(), Color.BLACK);
        // TODO y ei ole ihan kohdillaan, virheitä tulee alueen ulkopuolella
    };

    public PiirtoAlue(double leveys, double korkeus, int pikseleitaX, int pikseleitaY) {
        super();
        this.leveys = leveys;
        this.korkeus = korkeus;
        this.pikseleitaX = pikseleitaX;
        this.pikseleitaY = pikseleitaY;

        // Luodaan ensimmäinen taso
        // TODO pikselit menevät mittojen yli, liittyy varmaan ääriviivoihin
        PiirtoTaso.setMitat(leveys, korkeus, pikseleitaX, pikseleitaY); // TODO näitä kaikkia ei kai tarvita
        Pikseli.setMitat(leveys, korkeus, pikseleitaX, pikseleitaY);

        PiirtoTaso t = new PiirtoTaso();
        tasot.add(t);
        this.getChildren().add(t);

        // TODO ehkä toiminnallisuus setOnMouseMove tänne tai Pikseliin
        this.addEventHandler(MouseEvent.MOUSE_DRAGGED, tkPiirto);
    }

    public void lisaaTaso(String nimi) {
        PiirtoTaso t = new PiirtoTaso(nimi);
        tasot.add(t);
        this.getChildren().add(t);
    }

    public void lisaaTaso() {
        PiirtoTaso t = new PiirtoTaso();
        tasot.add(t);
        this.getChildren().add(t);
    }

    public void toggleTaso(int taso) {
        tasot.get(taso).toggleNakyvyys();
        // TODO, voi olla myös kahdessa osassa; piilotaTaso() ja naytaTaso()
    }

    public int getValittuTaso() {
        return valittuTaso;
    }

    public void setValittuTaso(int valittuTaso) {
        this.valittuTaso = valittuTaso;
    }

    public void setPikseliVari(int x, int y, Color vari) {
        tasot.get(getValittuTaso()).
                getPikseli((int) (x / leveys * pikseleitaX), (int) (y / korkeus * pikseleitaY)).
                setVari(vari);
    }
}
