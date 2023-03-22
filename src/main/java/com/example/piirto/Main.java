package com.example.piirto;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Main extends Application {
    Scene sceneAlku, scenePiirto;

    private final int LEVEYS = 1000;
    private final int KORKEUS = 600;

    // TODO näitä ei ehkä kiinteinä vaan ne katsotaan kun on tehty
    private int vasenLeveys = 80;
    private int alaKorkeus = 80;

    private int pikseleitaX = 70;
    private int pikseleitaY = 50;

    private String valittuTyokalu = "piirto";
    private Color valittuVari = Color.BLACK;
    private int valittuNakyvyys = 100;
    private int valittuPaksuus = 1;

    @Override
    public void start(Stage stage) {
        ////////// Alkuvalikko //////////

        Label lbLuo = new Label("Luo uusi");
        // TODO kuvat
        Button bnUusiPikseli = new Button("Pikselikuva");
        Button bnUusiKuvio = new Button("Grafiikkakuva");
        HBox uudet = new HBox(bnUusiPikseli, bnUusiKuvio);
        uudet.setSpacing(20);

        Label lbAvaa = new Label("Avaa");
        // TODO viimeisimmät tiedostot

        VBox alkuvalikko = new VBox(lbLuo, uudet, lbAvaa);
        alkuvalikko.setSpacing(40);
        alkuvalikko.setPadding(new Insets(40));

        ///// Toiminnallisuus
        bnUusiPikseli.setOnAction(e -> stage.setScene(scenePiirto));
        // TODO scene kuviopiirrolle

        ///// Scene
        sceneAlku = new Scene(alkuvalikko, LEVEYS, KORKEUS);



        ////////// Piirtonäkymä //////////

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

        // TODO tooltipit näiden arvolle tai johonkin muuhun labeliin tai siis Textiin
        Label lbPaksuus = new Label("Paksuus");
        Slider slPaksuus = new Slider(1, 100, 1);
        slPaksuus.setShowTickLabels(true);

        Label lbNakyvyys = new Label("Näkyvyys");
        Slider slNakyvyys = new Slider(0, 100, 100);
        slNakyvyys.setShowTickLabels(true);

        Label lbTasot = new Label("Tasot");
        Button bnUusiTaso = new Button("+");
        TextField uusiTasoNimi = new TextField();
        uusiTasoNimi.setPromptText("Taso 2");
        HBox hbUusiTaso = new HBox(bnUusiTaso, uusiTasoNimi);
        hbUusiTaso.setSpacing(10);

        // TODO valitse ensimmäinen taso automaattisesti
        ListView<String> tasot = new ListView<>();
        tasot.setPrefWidth(140); // TODO tämän ei kannata olla vakio

        // TODO kuvat
        Button bnTasoNakyvyys = new Button("Näk");
        Button bnTasoYlos = new Button("Ylös");
        Button bnTasoAlas = new Button("Alas");
        HBox hbTaso = new HBox(bnTasoNakyvyys, bnTasoYlos, bnTasoAlas);
        hbTaso.setSpacing(10);
        Label lbTasoNakyvyys = new Label("Näkyvyys");
        Slider slTasoNakyvyys = new Slider(0, 100, 100);

        // TODO lisää tasoille listan pohjaan napit siirtämiselle, poistamiselle ja näkyvyyden togglelle

        vasen.getChildren().addAll(lbVari, variMusta, variPunainen, variSininen, variValikko,
                lbPaksuus, slPaksuus,
                lbNakyvyys, slNakyvyys,
                lbTasot, hbUusiTaso, tasot,
                hbTaso, lbTasoNakyvyys, slTasoNakyvyys);

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

        ///// Piirtoalue
        PiirtoAlue piirtoAlue = new PiirtoAlue(
                LEVEYS - vasenLeveys, // TODO tässä voisi olla dynaamiset luvut
                KORKEUS - alaKorkeus,
                pikseleitaX,
                pikseleitaY);
        piirtoAlue.setStyle("-fx-background-color: white");
        asettelu.setCenter(piirtoAlue);

        ///// Toiminnallisuus
        // Värien valinta
//        variMusta.setOnAction(e -> piirtoAlue.setValittuVari(Color.BLACK));
//        variPunainen.setOnAction(e -> piirtoAlue.setValittuVari(Color.RED));
//        variSininen.setOnAction(e -> piirtoAlue.setValittuVari(Color.BLUE));
        variMusta.setOnAction(e -> valittuVari = Color.BLACK);
        variPunainen.setOnAction(e -> valittuVari = Color.RED);
        variSininen.setOnAction(e -> valittuVari = Color.BLUE);
        // Pop-up-ikkuna värin valitsemiseksi
        variValikko.setOnAction(e -> { // TODO erilliseen luokkaan koodin siistimiseksi
            Stage variStage = new Stage();
            variStage.initModality(Modality.NONE);
            variStage.initOwner(stage);



            VBox vbTallennus = new VBox(10);
            vbTallennus.setPadding(new Insets(15));
            vbTallennus.getChildren().add(new Label("Tiedostonimi:"));
            TextField tfTiedostonimi = new TextField();
            Label lbTallennusHuom = new Label();
            lbTallennusHuom.setTextFill(Color.RED);
            Button bnTallennus = new Button("Tallenna");
            vbTallennus.getChildren().addAll(tfTiedostonimi, lbTallennusHuom, bnTallennus);

            Scene tallennusScene = new Scene(vbTallennus, 300, 150);
            variStage.setScene(tallennusScene);
            variStage.show();
        });

        // Paksuuden säätö
//        slPaksuus.valueProperty().addListener(ov ->
//            piirtoAlue.setValittuPaksuus((int) slPaksuus.getValue()));
        slPaksuus.valueProperty().addListener(ov ->
            valittuPaksuus = (int) slPaksuus.getValue());

        // Läpinäkyvyyden säätö
//        slNakyvyys.valueProperty().addListener(ov ->
//            piirtoAlue.setValittuNakyvyys((int) slNakyvyys.getValue()));
        slNakyvyys.valueProperty().addListener(ov ->
            valittuNakyvyys = (int) slNakyvyys.getValue());

        // Tason lisäys
        // TODO lisää nimeäminen ehkä vierellä olevana TextFieldinä
        bnUusiTaso.setOnAction(e -> {
            piirtoAlue.lisaaTaso(uusiTasoNimi.getText());
            uusiTasoNimi.setText("");
            uusiTasoNimi.setPromptText("Taso " + (PiirtoTaso.getTasoNro() + 1));
            // Päivitetään tasojen lista
            tasot.setItems(FXCollections.observableArrayList(piirtoAlue.getTasoNimet()));
        });

        // Luodaan lista tasoista
        tasot.setItems(FXCollections.observableArrayList(piirtoAlue.getTasoNimet()));
        // Aktiivisen tason valinta
        tasot.getSelectionModel().selectedItemProperty().addListener(ov ->
            piirtoAlue.setValittuTaso(tasot.getSelectionModel().getSelectedIndex())
        );

        // Tason muokkausnapit
        // TODO
        bnTasoNakyvyys.setOnAction(e ->
            piirtoAlue.toggleTaso(piirtoAlue.getValittuTasoNro())); // TODO tämän voisi tehdä ehkä kompaktimmin tuolla piirtotasossa

        slTasoNakyvyys.valueProperty().addListener(ov ->
            piirtoAlue.getValittuTaso().setNakyvyys((int) slTasoNakyvyys.getValue()));

        // Työkalut
        // TODO

        // Ruudukko
        bnRuudukko.setOnAction(e -> piirtoAlue.toggleRuudukko());

        // Piirtoalue
        // TODO tulisiko tämä tänne
        EventHandler<MouseEvent> tkPiirto = e -> {
            int x = (int) e.getX();
            int y = (int) e.getY();
            Color vari = valittuVari;
            if (e.isSecondaryButtonDown()) {
                vari = Color.TRANSPARENT;
            }

            switch (valittuTyokalu) {
                case "piirto":
                    if (valittuPaksuus == 1) {
                        piirtoAlue.setPikseli(x, y, vari, valittuNakyvyys);
                    } else {
                        piirtoAlue.setPikseli(x, y, vari, valittuNakyvyys, valittuPaksuus);
                    }
                    break;
                case "poisto":
                    if (valittuPaksuus == 1) {
                        piirtoAlue.setPikseli(x, y, Color.TRANSPARENT, 100);
                    } else {
                        piirtoAlue.setPikseli(x, y, Color.TRANSPARENT, 100, valittuPaksuus);
                    }
                    break;
                case "täyttö":  // TODO täyttö
                    if (valittuPaksuus == 1) {
                        piirtoAlue.setPikseli(x, y, valittuVari, valittuNakyvyys);
                    } else {
                        piirtoAlue.setPikseli(x, y, valittuVari, valittuNakyvyys, valittuPaksuus);
                    }
                    break;
                case "värinpoimija":  // TODO värinpoimija
                    Pikseli pikseli = piirtoAlue.getPikseli(x, y);
                    valittuVari = pikseli.getVari();
                    valittuNakyvyys = pikseli.getNakyvyys();
                    break;
            }
        };
        piirtoAlue.addEventHandler(MouseEvent.MOUSE_PRESSED, tkPiirto);
        piirtoAlue.addEventHandler(MouseEvent.MOUSE_DRAGGED, tkPiirto);

        // Tallentaminen
        bnTallenna.setOnAction(e -> { // TODO erilliseen luokkaan koodin siistimiseksi
            Stage tallennusStage = new Stage();
            tallennusStage.initModality(Modality.NONE);
            tallennusStage.initOwner(stage);

            VBox vbTallennus = new VBox(10);
            vbTallennus.setPadding(new Insets(15));
            vbTallennus.getChildren().add(new Label("Tiedostonimi:"));
            TextField tfTiedostonimi = new TextField();
            Label lbTallennusHuom = new Label();
            lbTallennusHuom.setTextFill(Color.RED);
            Button bnTallennus = new Button("Tallenna");
            vbTallennus.getChildren().addAll(tfTiedostonimi, lbTallennusHuom, bnTallennus);

            Scene tallennusScene = new Scene(vbTallennus, 300, 150);
            tallennusStage.setScene(tallennusScene);
            tallennusStage.show();

            // Huomautetaan, jos tiedosto on jo olemassa
            tfTiedostonimi.setOnKeyTyped(e1 -> {
                if (new File(tfTiedostonimi.getText() + ".dat").isFile()) { // TODO ehkä tarkista onko laillinen tiedostonimi
                    lbTallennusHuom.setText("HUOM: Tiedosto on jo olemassa!");
                } else {
                    lbTallennusHuom.setText("");
                }
            });
            bnTallennus.setOnAction(e1 -> {
                System.out.println("Tallennetaan");

                try {
                    ObjectOutputStream fileOutput = new ObjectOutputStream(
                            new FileOutputStream(tfTiedostonimi.getText() + ".dat"));

                    fileOutput.writeObject(piirtoAlue);

                    fileOutput.close();
                    tallennusStage.close();
                    System.out.println("Tallennettu tiedosto nimellä " + tfTiedostonimi.getText() + ".dat"); // TEMP
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        });

        ///// Scene
        scenePiirto = new Scene(asettelu, LEVEYS, KORKEUS);

        //////////

        stage.setTitle("Piirto-ohjelma");
//        stage.setResizable(false); // TEMP kommenttina
        stage.setScene(sceneAlku);
        stage.show();

        // TODO viimeisimpien tiedostojen tietojen tallentaminen ehkä sulkiessa tai aiemmin
        stage.setOnCloseRequest(e -> {

        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}