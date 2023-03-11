package com.example.piirto;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {
    private final int LEVEYS = 800;
    private final int KORKEUS = 600;

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
        Pikseli.setLeveys((LEVEYS - vasenLeveys) / nelioitaX - 1);
        Pikseli.setKorkeus((KORKEUS - alaKorkeus) / nelioitaY - 1);

        BorderPane asettelu = new BorderPane();

        VBox vasen = new VBox(5);
        vasen.setStyle("-fx-fill-color: gray");
        asettelu.setLeft(vasen);

        RadioButton variMusta = new RadioButton("Musta");
        RadioButton variPunainen = new RadioButton("Punainen");
        RadioButton variSininen = new RadioButton("Sininen");
        ToggleGroup tgVarit = new ToggleGroup();
        variMusta.setToggleGroup(tgVarit);
        variPunainen.setToggleGroup(tgVarit);
        variSininen.setToggleGroup(tgVarit);
        variMusta.fire();
        vasen.getChildren().addAll(variMusta, variPunainen, variSininen);

        variMusta.setOnAction(e -> valittuVari = Color.BLACK);
        variPunainen.setOnAction(e -> valittuVari = Color.RED);
        variSininen.setOnAction(e -> valittuVari = Color.BLUE);

        Rectangle ala = new Rectangle(LEVEYS, alaKorkeus, Color.GREEN);
        asettelu.setBottom(ala);

        FlowPane fpNeliot = new FlowPane();
        asettelu.setCenter(fpNeliot);

        Rectangle[] neliot = new Rectangle[nelioitaX * nelioitaY];
        for (int i = 0; i < neliot.length; i++) {
            neliot[i] = new Rectangle((LEVEYS - vasenLeveys) / nelioitaX - 1, (KORKEUS - alaKorkeus) / nelioitaY - 1, Color.WHITE);
            neliot[i].setStroke(Color.GRAY);
            int finalI = i;
            // Yksittäinen väritys
            neliot[i].setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    neliot[finalI].setFill(valittuVari);
                } else if (e.getButton() == MouseButton.SECONDARY) {
                    neliot[finalI].setFill(Color.WHITE);
                }
            });
            // Jatkuva väritys
            neliot[i].setOnMouseMoved(e -> {
                if (e.isControlDown()) { // Väritys Control pohjassa
                    neliot[finalI].setFill(valittuVari);
                } else if (e.isShiftDown()) { // Poistaminen Shift pohjassa
                    neliot[finalI].setFill(Color.WHITE);
                }
            });
            fpNeliot.getChildren().add(neliot[i]);
        }

        Scene scene = new Scene(asettelu, LEVEYS, KORKEUS);
        stage.setTitle("Piirto-ohjelma");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}