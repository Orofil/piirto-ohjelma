package com.example.piirto;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Main extends Application {
    private final String POLKU = "src/main/resources/com/example/piirto/";
    private final String IMGPOLKU = "file:" + POLKU;

    private final int LEVEYS = 1000;
    private final int KORKEUS = 600;

    // TODO näitä ei ehkä kiinteinä vaan ne katsotaan kun on tehty
    private final int VASENLEVEYS = 80;
    private final int ALAKORKEUS = 80;

    private int pikseleitaX = 76;
    private int pikseleitaY = 50;

    private String tiedosto = "";

    private String valittuTyokalu = "piirto";
    private Color valittuVari = Color.BLACK;
    private int valittuNakyvyys = 100;
    private int valittuPaksuus = 1;
    private int valittuTaso;

    // TODO tänne kaikki komponentit niin niitä voi käyttää startin ulkopuolella
    Scene sceneAlku, scenePiirto;
    // Alkuvalikko
    BorderPane bpAlkuvalikko;
        VBox vbAlkuvalikkoVasen;
            Label lbLuo;
            HBox hbUusi;
                Button bnUusi;
                GridPane gpUusiPiks;
                    Label lbPiksX; // TODO näitä ei varmaan kaikkia tarvitsisi määritellä erikseen, ainakaan Labeleita joilla ei ole toiminnallisuutta
                    Label lbPiksY;
                    Label lbPiksSuhde;
                    TextField tfPiksX;
                    TextField tfPiksY;
                    CheckBox cbPiksSuhde;
            Label lbAvaa;
            VBox vbAvaa;
        VBox vbAlkuvalikkoOikea;
            ImageView imgAlkuvalikko;
            Text txAlkuvalikko;
            Text txAlkuvalikkoLinkki;

    // Piirtonäkymä
    BorderPane bpPiirto;
        VBox vbVasen;
            HBox hbVari;
                VBox vbVari;
                    Label lbVari;
                    RadioButton variMusta;
                    RadioButton variPunainen;
                    RadioButton variSininen;
                    RadioButton variMuu; // Näkymätön RadioButton muille väreille valittavaksi
                    ToggleGroup tgVarit;
                    Button variValikko;
                Rectangle rtVari;
            Label lbPaksuus;
            Slider slPaksuus;
            Label lbNakyvyys;
            Slider slNakyvyys;
            Label lbTasot;
            HBox hbUusiTaso;
                Button bnUusiTaso;
                TextField tfUusiTaso;
            ListView<String> tasot;
            HBox hbTaso;
                Button bnTasoNakyvyys;
                Button bnTasoYlos;
                Button bnTasoAlas;
                Button bnTasoPoista;
            Label lbTasoNakyvyys;
            Slider slTasoNakyvyys;
        BorderPane bpAla;
            HBox hbAlaVasen;
                ToggleButton bnPiirto;
                ToggleButton bnPoisto;
                ToggleButton bnTaytto;
                ToggleButton bnVarinpoimija;
                ToggleGroup tgTyokalu;
            HBox hbAlaKeski;
                Button bnRuudukko;
            HBox hbAlaOikea;
                Button bnTakaisin;
                Button bnTallenna;
        PiirtoAlue piirtoAlue;

    double piksSuhde = (double) pikseleitaY / (double) pikseleitaX; // TODO ehkä eri luokkaan niiden yksien kanssa emt

    @Override
    public void start(Stage stage) {
        ////////// Alkuvalikko //////////

        lbLuo = new Label("Luo uusi");
        lbAvaa = new Label("Avaa");
        lbLuo.setFont(Font.font(24));
        lbAvaa.setFont(Font.font(24));

        bnUusi = new Button(); // TODO resoluution valinta
        bnUusi.setGraphic(new ImageView(new Image(IMGPOLKU + "uusi.png")));

        lbPiksX = new Label("Leveys:");
        lbPiksY = new Label("Korkeus:");
        tfPiksX = new TextField(String.valueOf(pikseleitaX));
        tfPiksY = new TextField(String.valueOf(pikseleitaY)); // TODO nämä ei vielä vaikuta kuvaan
        tfPiksX.setPrefWidth(40);
        tfPiksY.setPrefWidth(40);
        cbPiksSuhde = new CheckBox();
        cbPiksSuhde.fire();
        lbPiksSuhde = new Label("Säilytä suhde"); // TODO tämä ei vielä tee mitään

        gpUusiPiks = new GridPane();
        gpUusiPiks.add(lbPiksX, 0, 0);
        gpUusiPiks.add(lbPiksY, 0, 1);
        gpUusiPiks.add(cbPiksSuhde, 0, 2);
        gpUusiPiks.add(tfPiksX, 1, 0);
        gpUusiPiks.add(tfPiksY, 1, 1);
        gpUusiPiks.add(lbPiksSuhde, 1, 2);
        gpUusiPiks.setHgap(5);
        gpUusiPiks.setVgap(5);
        GridPane.setHalignment(cbPiksSuhde, HPos.RIGHT);

        hbUusi = new HBox(bnUusi, gpUusiPiks);
        hbUusi.setSpacing(10);

        vbAvaa = new VBox(5);

        vbAlkuvalikkoVasen = new VBox(lbLuo, hbUusi, lbAvaa, vbAvaa);
        vbAlkuvalikkoVasen.setSpacing(40);

        if (new File(POLKU + "viimeisimmät.txt").isFile()) {
            try {
                ArrayList<String> viimeisimmatTiedostot = new ArrayList<>();

                Scanner scanner = new Scanner(new File(POLKU + "viimeisimmät.txt"));
                while (scanner.hasNextLine()) {
                    viimeisimmatTiedostot.add(scanner.nextLine());
                }

                Button[] bnViimeisimmat = new Button[viimeisimmatTiedostot.size()];

                for (int i = 0; i < viimeisimmatTiedostot.size(); i++) {
                    Button bn = new Button(viimeisimmatTiedostot.get(i));
                    bn.setPrefWidth(250);
                    bn.setFont(Font.font(18));
                    bnViimeisimmat[i] = bn;
                    // TODO kuva piirroksesta

                    vbAvaa.getChildren().add(bnViimeisimmat[i]);

                    int finalI = i;
                    bnViimeisimmat[i].setOnAction(e -> {
                        tiedosto = viimeisimmatTiedostot.get(finalI) + ".dat";
                        stage.setScene(scenePiirto);
                    });
                }

                // TODO tee napit jokaiselle
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            vbAvaa.getChildren().add(new Text("Ei viimeisimpiä tiedostoja"));
        }

        imgAlkuvalikko = new ImageView(new Image(IMGPOLKU + "piirto-ohjelma.png"));
        txAlkuvalikko = new Text("""
                        Piirto-ohjelma, jonka nimi on luovasti "piirto-ohjelma"
                        Soveltuu pikselien värittämiseen
                        Sen lisäksi ei tee paljon muuta
                        Saatan päivittää ohjelmaa tulevaisuudessa
                        
                        Jaakko Saano (Orofil), 2023""");
        txAlkuvalikko.setTextAlignment(TextAlignment.RIGHT);
        txAlkuvalikkoLinkki = new Text("github.com/Orofil/piirto-ohjelma"); // TODO linkin avautuminen
        txAlkuvalikkoLinkki.setUnderline(true);
        txAlkuvalikkoLinkki.setFill(Color.web("#0000EE"));

        vbAlkuvalikkoOikea = new VBox(imgAlkuvalikko, txAlkuvalikko, txAlkuvalikkoLinkki);
        vbAlkuvalikkoOikea.setAlignment(Pos.TOP_RIGHT);

        bpAlkuvalikko = new BorderPane();
        bpAlkuvalikko.setPadding(new Insets(40));
        bpAlkuvalikko.setLeft(vbAlkuvalikkoVasen);
        bpAlkuvalikko.setRight(vbAlkuvalikkoOikea);

        ///// Toiminnallisuus
        bnUusi.setOnAction(e -> stage.setScene(scenePiirto));

        tfPiksX.setOnKeyTyped(e -> {
            AtomicInteger x = new AtomicInteger(); // TODO ei tarvitse olla atomic integer jos on erillisessä luokassa
            if (cbPiksSuhde.isSelected()) {
                try {
                    x.set(Integer.parseInt(tfPiksX.getText()));
                    tfPiksY.setText(String.valueOf((int) Math.round(x.intValue() * piksSuhde)));
                } catch (NumberFormatException ignored) {}
            }
        });
        tfPiksY.setOnKeyTyped(e -> {
            AtomicInteger y = new AtomicInteger(); // TODO ei tarvitse olla atomic integer jos on erillisessä luokassa
            if (cbPiksSuhde.isSelected()) {
                try {
                    y.set(Integer.parseInt(tfPiksY.getText()));
                    tfPiksX.setText(String.valueOf((int) Math.round(y.intValue() / piksSuhde)));
                } catch (NumberFormatException ignored) {}
            }
        });

        cbPiksSuhde.setOnAction(e -> {
            try {
                piksSuhde = Double.parseDouble(tfPiksY.getText()) / Double.parseDouble(tfPiksX.getText());
            } catch (NumberFormatException ignored) {}
        });

        txAlkuvalikkoLinkki.setOnMouseEntered(e -> sceneAlku.setCursor(Cursor.HAND));
        txAlkuvalikkoLinkki.setOnMouseExited(e -> sceneAlku.setCursor(Cursor.DEFAULT));
        txAlkuvalikkoLinkki.setOnMouseClicked(e ->
            getHostServices().showDocument("https://github.com/Orofil/piirto-ohjelma"));

        ///// Scene
        sceneAlku = new Scene(bpAlkuvalikko, LEVEYS, KORKEUS);



        ////////// Piirtonäkymä //////////
        // TODO tiedoston lukeminen jos tiedostonimi ei ole null

        bpPiirto = new BorderPane();

        ///// Vasen valikko
        vbVasen = new VBox(5);
        vbVasen.setPadding(new Insets(10,10,10,10));
        vbVasen.setStyle("-fx-border-color: gray");
        bpPiirto.setLeft(vbVasen);

        lbVari = new Label("Värit");
        variMusta = new RadioButton("Musta");
        variPunainen = new RadioButton("Punainen");
        variSininen = new RadioButton("Sininen");
        variMuu = new RadioButton();

        tgVarit = new ToggleGroup();
        variMusta.setToggleGroup(tgVarit);
        variPunainen.setToggleGroup(tgVarit);
        variSininen.setToggleGroup(tgVarit);
        variMuu.setToggleGroup(tgVarit);

        variMusta.fire(); // TODO ei näin jos on eri valittuVäri tiedostoa avattaessa emt
        variValikko = new Button("Väri");

        vbVari = new VBox(5);
        vbVari.getChildren().addAll(lbVari, variMusta, variPunainen, variSininen, variValikko);

        rtVari = new Rectangle(40, 40, valittuVari); // TODO värin muutos setteriin

        hbVari = new HBox(70);
        hbVari.getChildren().addAll(vbVari, rtVari);


        // TODO tooltipit näiden arvolle tai johonkin muuhun labeliin tai siis Textiin
        lbPaksuus = new Label("Paksuus");
        slPaksuus = new Slider(1, 100, 1);
        slPaksuus.setShowTickLabels(true);

        lbNakyvyys = new Label("Näkyvyys");
        slNakyvyys = new Slider(0, 100, 100);
        slNakyvyys.setShowTickLabels(true);

        lbTasot = new Label("Tasot");
        bnUusiTaso = new Button("+");
        tfUusiTaso = new TextField();
        tfUusiTaso.setPromptText("Taso 2");
        hbUusiTaso = new HBox(bnUusiTaso, tfUusiTaso);
        hbUusiTaso.setSpacing(10);

        tasot = new ListView<>();
        tasot.setPrefWidth(140); // TODO tämän ei kannata olla vakio

        // TODO kuvat
        bnTasoNakyvyys = new Button("Näk");
        bnTasoYlos = new Button("Ylös");
        bnTasoAlas = new Button("Alas");
        bnTasoPoista = new Button("Pois");
        hbTaso = new HBox(bnTasoNakyvyys, bnTasoYlos, bnTasoAlas, bnTasoPoista); // TODO on varmaan liian leveä
        hbTaso.setSpacing(10);
        lbTasoNakyvyys = new Label("Näkyvyys");
        slTasoNakyvyys = new Slider(0, 100, 100);

        // TODO lisää tasoille listan pohjaan napit siirtämiselle, poistamiselle ja näkyvyyden togglelle

        vbVasen.getChildren().addAll(hbVari,
                lbPaksuus, slPaksuus,
                lbNakyvyys, slNakyvyys,
                lbTasot, hbUusiTaso, tasot,
                hbTaso, lbTasoNakyvyys, slTasoNakyvyys);

        ///// Alavalikko
        bpAla = new BorderPane();
        bpAla.setPadding(new Insets(10,10,10,10));
        bpAla.setStyle("-fx-border-color: gray");
        bpPiirto.setBottom(bpAla);

        bnPiirto = new ToggleButton();
        bnPiirto.setGraphic(new ImageView(new Image(IMGPOLKU + "kynä.png")));
        bnPoisto = new ToggleButton();
        bnPoisto.setGraphic(new ImageView(new Image(IMGPOLKU + "kumi.png")));
        bnTaytto = new ToggleButton();
        bnTaytto.setGraphic(new ImageView(new Image(IMGPOLKU + "ämpäri.png")));
        bnVarinpoimija = new ToggleButton();
        bnVarinpoimija.setGraphic(new ImageView(new Image(IMGPOLKU + "värinpoimija.png")));

        tgTyokalu = new ToggleGroup();
        bnPiirto.setToggleGroup(tgTyokalu);
        bnPoisto.setToggleGroup(tgTyokalu);
        bnTaytto.setToggleGroup(tgTyokalu);
        bnVarinpoimija.setToggleGroup(tgTyokalu);
        bnPiirto.fire();

        bnRuudukko = new Button();
        bnRuudukko.setGraphic(new ImageView(new Image(IMGPOLKU + "ruudukko.png")));
        bnRuudukko.setAlignment(Pos.CENTER);

        bnTakaisin = new Button("Takaisin");
        bnTallenna = new Button("Tallenna");
        bnTakaisin.setFont(Font.font(18));
        bnTallenna.setFont(Font.font("", FontWeight.BOLD, 24));

        hbAlaVasen = new HBox(10);
        hbAlaVasen.getChildren().addAll(bnPiirto, bnPoisto, bnTaytto, bnVarinpoimija);
        hbAlaKeski = new HBox(10);
        hbAlaKeski.setAlignment(Pos.CENTER);
        hbAlaKeski.getChildren().add(bnRuudukko);
        hbAlaOikea = new HBox(10);
        hbAlaOikea.setAlignment(Pos.CENTER_RIGHT);
        hbAlaOikea.getChildren().addAll(bnTakaisin, bnTallenna);

        bpAla.setLeft(hbAlaVasen);
        bpAla.setCenter(hbAlaKeski);
        bpAla.setRight(hbAlaOikea);


        ///// Piirtoalue
        piirtoAlue = new PiirtoAlue(
                LEVEYS - VASENLEVEYS, // TODO tässä voisi olla dynaamiset luvut
                KORKEUS - ALAKORKEUS,
                pikseleitaX,
                pikseleitaY);
        piirtoAlue.setStyle("-fx-background-color: white");
        bpPiirto.setCenter(piirtoAlue);


        ///// Toiminnallisuus
        // Värien valinta
        variMusta.setOnAction(e -> setValittuVari(Color.BLACK));
        variPunainen.setOnAction(e -> setValittuVari(Color.RED));
        variSininen.setOnAction(e -> setValittuVari(Color.BLUE));
        // Pop-up-ikkuna värin valitsemiseksi
        variValikko.setOnAction(e -> { // TODO erilliseen luokkaan koodin siistimiseksi
            AtomicInteger r = new AtomicInteger(); // TODO ei tarvitse olla atomic integer jos on erillisessä luokassa
            AtomicInteger g = new AtomicInteger();
            AtomicInteger b = new AtomicInteger();

            Stage variStage = new Stage();
            variStage.initModality(Modality.NONE);
            variStage.initOwner(stage);

            Label lbR = new Label("Punainen (R):");
            Label lbG = new Label("Vihreä (G):");
            Label lbB = new Label("Sininen (B):");
            TextField tfR = new TextField(String.valueOf((int) (valittuVari.getRed() * 255)));
            TextField tfG = new TextField(String.valueOf((int) (valittuVari.getGreen() * 255)));
            TextField tfB = new TextField(String.valueOf((int) (valittuVari.getBlue() * 255)));
            tfR.setPrefWidth(40);
            tfG.setPrefWidth(40);
            tfB.setPrefWidth(40);

            GridPane hbVarit = new GridPane();
            hbVarit.setHgap(5);
            hbVarit.add(lbR, 0, 0);
            hbVarit.add(lbG, 0, 1);
            hbVarit.add(lbB, 0, 2);
            hbVarit.add(tfR, 1, 0);
            hbVarit.add(tfG, 1, 1);
            hbVarit.add(tfB, 1, 2);

            Label lbHuom = new Label();
            lbHuom.setTextFill(Color.RED);
            Button bnOK = new Button("OK");

            VBox vbVariVal = new VBox(10);
            vbVariVal.setPadding(new Insets(10));
            vbVariVal.getChildren().addAll(hbVarit, lbHuom, bnOK);

            /// Toiminnallisuus
            EventHandler<KeyEvent> tkHuom = e1 -> {
                try {
                    r.set((int) Double.parseDouble(tfR.getText()));
                    g.set((int) Double.parseDouble(tfG.getText()));
                    b.set((int) Double.parseDouble(tfB.getText()));
                    if (r.get() < 0 || r.get() > 255 ||
                        g.get() < 0 || g.get() > 255 ||
                        b.get() < 0 || b.get() > 255) {
                        lbHuom.setText("HUOM: Väri ei ole kelvollinen!");
                        bnOK.setDisable(true);
                    } else {
                        lbHuom.setText("");
                        bnOK.setDisable(false);
                    }
                } catch (NumberFormatException ex) {
                    lbHuom.setText("HUOM: Väri ei ole kelvollinen!");
                    bnOK.setDisable(true);
                }
            };
            tfR.setOnKeyTyped(tkHuom);
            tfG.setOnKeyTyped(tkHuom);
            tfB.setOnKeyTyped(tkHuom);

            bnOK.setOnAction(e1 -> {
                setValittuVari(Color.rgb(r.get(), g.get(), b.get()));
                variStage.close();
            });

            Scene tallennusScene = new Scene(vbVariVal, 150, 200);
            variStage.setScene(tallennusScene);
            variStage.show();
        });

        // Paksuuden säätö
        slPaksuus.valueProperty().addListener(ov ->
            valittuPaksuus = (int) slPaksuus.getValue());

        // Läpinäkyvyyden säätö
        slNakyvyys.valueProperty().addListener(ov ->
            valittuNakyvyys = (int) slNakyvyys.getValue());

        // Tason lisäys
        bnUusiTaso.setOnAction(e -> {
            piirtoAlue.lisaaTaso(tfUusiTaso.getText());
            tfUusiTaso.setText("");
            tfUusiTaso.setPromptText("Taso " + (PiirtoTaso.getTasoNro() + 1));
            // Päivitetään tasojen lista
            tasot.setItems(FXCollections.observableArrayList(piirtoAlue.getTasoNimet()));
            tasot.getSelectionModel().select(piirtoAlue.getTasoMaara() - 1);
            tasot.scrollTo(piirtoAlue.getTasoMaara() - 1);
        });

        // Alussa luodaan lista tasoista ja valitaan ensimmäinen
        tasot.setItems(FXCollections.observableArrayList(piirtoAlue.getTasoNimet()));
        tasot.getSelectionModel().select(0);

        // Aktiivisen tason valinta
        tasot.getSelectionModel().selectedItemProperty().addListener(ob ->
                valittuTaso = tasot.getSelectionModel().getSelectedIndex());

        // Tason muokkausnapit
        bnTasoNakyvyys.setOnAction(e ->
                piirtoAlue.toggleTaso(valittuTaso));

        bnTasoYlos.setOnAction(e ->
                piirtoAlue.siirraTaso(valittuTaso, 1));
        bnTasoAlas.setOnAction(e ->
                piirtoAlue.siirraTaso(valittuTaso, -1));

        bnTasoPoista.setOnAction(e -> {
            piirtoAlue.poistaTaso(valittuTaso);

        });

        slTasoNakyvyys.valueProperty().addListener(ov ->
                piirtoAlue.getTaso(valittuTaso).setNakyvyys((int) slTasoNakyvyys.getValue()));

        // Työkalut
        bnPiirto.setOnAction(e -> valittuTyokalu = "piirto");
        bnPoisto.setOnAction(e -> valittuTyokalu = "poisto");
        bnTaytto.setOnAction(e -> valittuTyokalu = "täyttö");
        bnVarinpoimija.setOnAction(e -> valittuTyokalu = "värinpoimija");

        // Ruudukko
        bnRuudukko.setOnAction(e -> piirtoAlue.toggleRuudukko());

        // Piirtoalue
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
                        piirtoAlue.setPikseli(valittuTaso, x, y, vari, valittuNakyvyys);
                    } else {
                        piirtoAlue.setPikseli(valittuTaso, x, y, vari, valittuNakyvyys, valittuPaksuus);
                    }
                    break;
                case "poisto":
                    if (valittuPaksuus == 1) {
                        piirtoAlue.setPikseli(valittuTaso, x, y, Color.TRANSPARENT, 100);
                    } else {
                        piirtoAlue.setPikseli(valittuTaso, x, y, Color.TRANSPARENT, 100, valittuPaksuus);
                    }
                    break;
                case "täyttö":  // TODO täyttö
                    if (valittuPaksuus == 1) {
                        piirtoAlue.setPikseli(valittuTaso, x, y, valittuVari, valittuNakyvyys);
                    } else {
                        piirtoAlue.setPikseli(valittuTaso, x, y, valittuVari, valittuNakyvyys, valittuPaksuus);
                    }
                    break;
                case "värinpoimija":
                    Pikseli pikseli = piirtoAlue.getPikseli(valittuTaso, x, y);
                    setValittuVari(pikseli.getVari());
                    valittuNakyvyys = pikseli.getNakyvyys();
                    break;
            }
        };
        piirtoAlue.addEventHandler(MouseEvent.MOUSE_PRESSED, tkPiirto);
        piirtoAlue.addEventHandler(MouseEvent.MOUSE_DRAGGED, tkPiirto);

        // Takaisin päävalikkoon
        bnTakaisin.setOnAction(e -> stage.setScene(sceneAlku)); // TODO asiat eivät päivity näin

        // Tallentaminen
        bnTallenna.setOnAction(e -> { // TODO erilliseen luokkaan koodin siistimiseksi
            Stage tallennusStage = new Stage();
            tallennusStage.initModality(Modality.NONE); // TODO jotenkin niin että alemmalla ikkunalla ei voi tehdä mitään
            tallennusStage.initOwner(stage);

            VBox vbTallennus = new VBox(10);
            vbTallennus.setPadding(new Insets(15));
            vbTallennus.getChildren().add(new Label("Tiedostonimi:"));
            TextField tfTiedostonimi = new TextField(tiedosto);
            Label lbTallennusHuom = new Label();
            lbTallennusHuom.setTextFill(Color.RED);
            Button bnTallennus = new Button("Tallenna");
            vbTallennus.getChildren().addAll(tfTiedostonimi, lbTallennusHuom, bnTallennus);

            Scene tallennusScene = new Scene(vbTallennus, 300, 150);
            tallennusStage.setScene(tallennusScene);
            tallennusStage.show();


            /*
            Huomautetaan, jos tiedosto on jo olemassa ja kielletään tallentaminen jos nimi on tyhjä.
            Muuta tiedostonimen validaatiota ei nyt ole :(
             */
            if (!tiedosto.equals("")) {
                lbTallennusHuom.setText("HUOM: Tiedosto on jo olemassa!");
            }
            tfTiedostonimi.setOnKeyTyped(e1 -> {
                if (new File(tfTiedostonimi.getText() + ".dat").isFile()) { // TODO ehkä tarkista onko laillinen tiedostonimi
                    lbTallennusHuom.setText("HUOM: Tiedosto on jo olemassa!");
                } else {
                    lbTallennusHuom.setText("");
                }
                bnTallennus.setDisable(tfTiedostonimi.getText().equals(""));
            });

            // Tallentaminen
            bnTallennus.setOnAction(e1 -> {

                try { // TODO tallennusjuttu, tärkeä!
                    String tiedostonimi = tfTiedostonimi.getText();

                    ObjectOutputStream fileOutput = new ObjectOutputStream(
                            new FileOutputStream(tiedostonimi + ".dat"));

                    Object[] tallennettavat = new Object[] {
                            LEVEYS - VASENLEVEYS,
                            KORKEUS - ALAKORKEUS,
                            pikseleitaX,
                            pikseleitaY,
                            // TODO jotenkin muut tiedot valituista työkaluista ja muista
                            piirtoAlue.tallennus()
                    };
                    fileOutput.writeObject(tallennettavat);

                    fileOutput.close();

                    // Tallennetaan tieto viimeisimmistä tiedostoista
                    File viimeisimmat = new File(POLKU + "viimeisimmät.txt");
                    if (viimeisimmat.createNewFile()) {
                        FileWriter writer = new FileWriter(viimeisimmat);
                        writer.write(tiedostonimi);
                        writer.close();
                    } else {
                        // Lisätään uusin tiedostonimi muiden eteen
                        ArrayList<String> viimeisimmatTeksti = new ArrayList<>();
                        viimeisimmatTeksti.add(tiedostonimi);

                        Scanner scanner = new Scanner(viimeisimmat);
                        while (scanner.hasNextLine()) {
                            String nextLine = scanner.nextLine();
                            // Jos sama tiedostonimi oli jo listassa, se jätetään pois
                            if (!nextLine.equals(tiedostonimi)) {
                                viimeisimmatTeksti.add(nextLine);
                            }
                        }

                        // Poistetaan viimeinen nimi jos tiedostoja on yli 6
                        if (viimeisimmatTeksti.size() > 6) {
                            viimeisimmatTeksti.remove(viimeisimmatTeksti.size() - 1);
                        }

                        // Kirjoitetaan nimet tiedostoon
                        FileWriter writer = new FileWriter(viimeisimmat);
                        for (String nimi : viimeisimmatTeksti) {
                            writer.write(nimi + System.lineSeparator());
                        }
                        writer.close();
                    }

                    tallennusStage.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        });

        ///// Scene
        scenePiirto = new Scene(bpPiirto, LEVEYS, KORKEUS);

        //////////

        stage.setTitle("Piirto-ohjelma");
        stage.setResizable(false);
        stage.setScene(sceneAlku);
        stage.show();

        // TODO viimeisimpien tiedostojen tietojen tallentaminen ehkä sulkiessa tai aiemmin
        stage.setOnCloseRequest(e -> {

        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void sceneAlku(Stage stage) {
        stage.setScene(sceneAlku);
    }

    public void scenePiirto(Stage stage) {
        stage.setScene(scenePiirto);
    }

    public void setValittuVari(Color vari) {
        valittuVari = vari;
        rtVari.setFill(vari);
        if (vari.equals(Color.BLACK)) {
            variMusta.fire();
        } else if (vari.equals(Color.RED)) {
            variPunainen.fire();
        } else if (vari.equals(Color.BLUE)) {
            variSininen.fire();
        } else {
            variMuu.fire();
        }
    }
}