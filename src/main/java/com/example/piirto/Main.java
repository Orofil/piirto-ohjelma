package com.example.piirto;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    private final int LEVEYS = 1000;
    private final int KORKEUS = 600;

    // TODO näitä ei ehkä kiinteinä vaan ne katsotaan kun on tehty
    private int vasenLeveys = 80;
    private int alaKorkeus = 80;

    private int nelioitaX = 40;
    private int nelioitaY = 28;

    private Color valittuVari = Color.BLACK;

    /*
    TODO eri piirtomoodi jossa voi tehdä muotoja;
    suorakulmioita, ympyröitä ja tekstiä ainakin.
     */

    @Override
    public void start(Stage stage) {
        BorderPane asettelu = new BorderPane();

        ///// Vasen valikko

        VBox vasen = new VBox(5);
        vasen.setPadding(new Insets(10,10,10,10));
        vasen.setStyle("-fx-border-color: gray");
        asettelu.setLeft(vasen);

        Label lbVari = new Label("Värit");
        RadioButton variMusta = new RadioButton("Musta");
        RadioButton variPunainen = new RadioButton("Punainen");
        RadioButton variSininen = new RadioButton("Sininen");
        ToggleGroup tgVarit = new ToggleGroup();
        variMusta.setToggleGroup(tgVarit);
        variPunainen.setToggleGroup(tgVarit);
        variSininen.setToggleGroup(tgVarit);
        variMusta.fire();

        Button variValikko = new Button("Väri");

        Label lbPaksuus = new Label("Paksuus");
        Slider slPaksuus = new Slider(1, 100, 1);

        Label lbNakyvyys = new Label("Näkyvyys");
        Slider slNakyvyys = new Slider(0, 100, 100);

        Label lbTasot = new Label("Tasot");
        Button uusiTaso = new Button("+");
//        ListView<> tasot = new ListView();

        vasen.getChildren().addAll(lbVari, variMusta, variPunainen, variSininen, variValikko,
                lbPaksuus, slPaksuus,
                lbNakyvyys, slNakyvyys,
                lbTasot, uusiTaso);

        ///// Alavalikko

        HBox ala = new HBox(10);
        ala.setPadding(new Insets(10,10,10,10));
        ala.setStyle("-fx-border-color: gray");
        asettelu.setBottom(ala);

        // TODO kuvat
        Button bnPiirto = new Button("Piirrä");
        Button bnPoisto = new Button("Poista");
        Button bnTaytto = new Button("Täytä");
        Button bnVarinpoimija = new Button("Värinpoimija");

        Button bnRuudukko = new Button("Ruudukko");
        bnRuudukko.setAlignment(Pos.CENTER);
        HBox.setMargin(bnRuudukko, new Insets(0,20,0,20));

        Button bnUndo = new Button("Undo");
        Button bnRedo = new Button("Redo");
        Button bnTallenna = new Button("Tallenna");
        bnUndo.setAlignment(Pos.CENTER_RIGHT);
        bnRedo.setAlignment(Pos.CENTER_RIGHT);
        bnTallenna.setAlignment(Pos.CENTER_RIGHT);

        ala.getChildren().addAll(bnPiirto, bnPoisto, bnTaytto, bnVarinpoimija,
                bnRuudukko, bnUndo, bnRedo, bnTallenna);

//        FlowPane fpNeliot = new FlowPane();
//        asettelu.setCenter(fpNeliot);
//
//        Rectangle[] neliot = new Rectangle[nelioitaX * nelioitaY];
//        for (int i = 0; i < neliot.length; i++) {
//            neliot[i] = new Rectangle((LEVEYS - vasenLeveys) / nelioitaX - 1, (KORKEUS - alaKorkeus) / nelioitaY - 1, Color.WHITE);
//            neliot[i].setStroke(Color.GRAY);
//            int finalI = i;
//            // Yksittäinen väritys
//            neliot[i].setOnMouseClicked(e -> {
//                if (e.getButton() == MouseButton.PRIMARY) {
//                    neliot[finalI].setFill(valittuVari);
//                } else if (e.getButton() == MouseButton.SECONDARY) {
//                    neliot[finalI].setFill(Color.WHITE);
//                }
//            });
//            // Jatkuva väritys
//            neliot[i].setOnMouseMoved(e -> {
//                if (e.isControlDown()) { // Väritys Control pohjassa
//                    neliot[finalI].setFill(valittuVari);
//                } else if (e.isShiftDown()) { // Poistaminen Shift pohjassa
//                    neliot[finalI].setFill(Color.WHITE);
//                }
//            });
//            fpNeliot.getChildren().add(neliot[i]);
//        }

        ///// Piirtoalue

        PiirtoAlue piirtoAlue = new PiirtoAlue(
                LEVEYS - vasen.getWidth(),
                KORKEUS - ala.getHeight(),
                nelioitaX,
                nelioitaY);
        asettelu.setCenter(piirtoAlue);

        ///// Toiminnallisuus

        variMusta.setOnAction(e -> valittuVari = Color.BLACK);
        variPunainen.setOnAction(e -> valittuVari = Color.RED);
        variSininen.setOnAction(e -> valittuVari = Color.BLUE);

        /////

        Scene scene = new Scene(asettelu, LEVEYS, KORKEUS);
        scene.setFill(Color.WHITE); // TODO ei toimi
        stage.setTitle("Piirto-ohjelma");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}