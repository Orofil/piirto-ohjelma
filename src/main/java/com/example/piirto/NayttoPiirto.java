package com.example.piirto;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class NayttoPiirto extends Application implements Naytto {
    private int pikseleitaX = Main.pikseleitaX;
    private int pikseleitaY = Main.pikseleitaY;

    protected int VASENLEVEYS = Main.VASENLEVEYS;
    protected int ALAKORKEUS = Main.ALAKORKEUS;

    private String tiedosto;

    private String valittuTyokalu = Main.valittuTyokalu;
    private Color valittuVari = Main.valittuVari;
    private int valittuNakyvyys = Main.valittuNakyvyys;
    private int valittuPaksuus = Main.valittuPaksuus;
    private int valittuTaso;

    private final Font fonttiOtsikko = Font.font("", FontWeight.BOLD, 12);

    ///// Määritellään graafiset komponentit
    /*
    Graafiset komponentit on sisennetty sen mukaan, mikä niiden hierarkia on
    ikkunassa. Tämä on ehkä hieman outo menetelmä, mutta näin nyt kokeilin
    tehdä ja tämä toimii. Kaikkia ei tässä vaiheessa tarvitsisi määritellä,
    kuten IntelliJ huomauttaa, mutta ihan sama :)
     */
    private Scene scene;
    private BorderPane bpPiirto;
        private VBox vbVasen;
            private HBox hbVari;
                private VBox vbVari;
                    private Label lbVari;
                    private RadioButton variMusta;
                    private RadioButton variPunainen;
                    private RadioButton variSininen;
                    private RadioButton variMuu; // Näkymätön RadioButton muille väreille valittavaksi
                    private ToggleGroup tgVarit;
                    private Button variValikko;
                private Rectangle rtVari;
            private Label lbPaksuus;
            private Slider slPaksuus;
            private Label lbNakyvyys;
            private Slider slNakyvyys;
            private Label lbTasot;
            private HBox hbUusiTaso;
                private Button bnUusiTaso;
                private TextField tfUusiTaso;
            private ListView<String> tasot;
            private HBox hbTaso;
                private Button bnTasoNakyvyys;
                private Button bnTasoYlos;
                private Button bnTasoAlas;
                private Button bnTasoPoista;
            private Label lbTasoNakyvyys;
            private Slider slTasoNakyvyys;
        private BorderPane bpAla;
            private HBox hbAlaVasen;
                private ToggleButton bnPiirto;
                private ToggleButton bnPoisto;
                private ToggleButton bnTaytto;
                private ToggleButton bnVarinpoimija;
                private ToggleGroup tgTyokalu;
            private HBox hbAlaKeski;
                private Button bnRuudukko;
            private HBox hbAlaOikea;
                private Button bnTakaisin;
                private Button bnTallenna;
        private PiirtoAlue piirtoAlue;

    public void start(int pikseleitaX, int pikseleitaY, String tiedosto, Stage stage) {
        this.pikseleitaX = pikseleitaX;
        this.pikseleitaY = pikseleitaY;
        this.tiedosto = tiedosto;
        start(stage);
    }

    public void start(String tiedosto, Stage stage) {
        start(pikseleitaX, pikseleitaY, tiedosto, stage);
    }

    @Override
    public void start(Stage stage) {
        bpPiirto = new BorderPane();

        ///// Vasen valikko
        vbVasen = new VBox(5);
        vbVasen.setPadding(new Insets(10));
        vbVasen.setStyle("-fx-border-color: gray");
        bpPiirto.setLeft(vbVasen);

        lbVari = new Label("Värit");
        lbVari.setFont(fonttiOtsikko);
        variMusta = new RadioButton("Musta");
        variPunainen = new RadioButton("Punainen");
        variSininen = new RadioButton("Sininen");
        variMuu = new RadioButton();

        tgVarit = new ToggleGroup();
        variMusta.setToggleGroup(tgVarit);
        variPunainen.setToggleGroup(tgVarit);
        variSininen.setToggleGroup(tgVarit);
        variMuu.setToggleGroup(tgVarit);

        variMusta.fire();
        variValikko = new Button("Väri");

        vbVari = new VBox(5);
        vbVari.getChildren().addAll(lbVari, variMusta, variPunainen, variSininen, variValikko);

        rtVari = new Rectangle(40, 40, valittuVari);

        hbVari = new HBox(70);
        hbVari.getChildren().addAll(vbVari, rtVari);


        // TODO tooltipit näiden arvolle tai johonkin muuhun labeliin tai siis Textiin
        lbPaksuus = new Label("Paksuus");
        lbPaksuus.setFont(fonttiOtsikko);
        slPaksuus = new Slider(1, 100, 1);
        slPaksuus.setShowTickLabels(true);

        lbNakyvyys = new Label("Näkyvyys");
        lbNakyvyys.setFont(fonttiOtsikko);
        slNakyvyys = new Slider(0, 100, 100);
        slNakyvyys.setShowTickLabels(true);

        lbTasot = new Label("Tasot");
        lbTasot.setFont(fonttiOtsikko);
        bnUusiTaso = new Button("+");
        tfUusiTaso = new TextField();
        tfUusiTaso.setPromptText("Taso 2");
        hbUusiTaso = new HBox(bnUusiTaso, tfUusiTaso);
        hbUusiTaso.setSpacing(10);

        tasot = new ListView<>();
        tasot.setPrefWidth(140); // TODO tämän ei kannata olla vakio

        bnTasoNakyvyys = new Button();
        bnTasoYlos = new Button();
        bnTasoAlas = new Button();
        bnTasoPoista = new Button();
        // TODO kuvat on liian leveitä
        bnTasoNakyvyys.setGraphic(new ImageView(new Image(IMGPOLKU + "silmä_auki.png"))); // TODO vaihtuva kuva
        bnTasoYlos.setGraphic(new ImageView(new Image(IMGPOLKU + "nuoli_ylös.png")));
        bnTasoAlas.setGraphic(new ImageView(new Image(IMGPOLKU + "nuoli_alas.png")));
        bnTasoPoista.setGraphic(new ImageView(new Image(IMGPOLKU + "x.png")));

        hbTaso = new HBox(bnTasoNakyvyys, bnTasoYlos, bnTasoAlas, bnTasoPoista);
        hbTaso.setSpacing(10);
        lbTasoNakyvyys = new Label("Tason näkyvyys");
        slTasoNakyvyys = new Slider(0, 100, 100);

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
        bnPoisto = new ToggleButton();
        bnTaytto = new ToggleButton();
        bnVarinpoimija = new ToggleButton();
        bnPiirto.setGraphic(new ImageView(new Image(IMGPOLKU + "kynä.png")));
        bnPoisto.setGraphic(new ImageView(new Image(IMGPOLKU + "kumi.png")));
        bnTaytto.setGraphic(new ImageView(new Image(IMGPOLKU + "ämpäri.png")));
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
        if (tiedosto == null || tiedosto.equals("")) {
            piirtoAlue = new PiirtoAlue(
                    LEVEYS - VASENLEVEYS, // TODO tässä voisi olla dynaamiset luvut
                    KORKEUS - ALAKORKEUS,
                    pikseleitaX,
                    pikseleitaY);
        } else {
            try { // TODO avaamisessa ei tule pikselit mukana
                ObjectInputStream fileInput = new ObjectInputStream(
                        new FileInputStream(tiedosto + ".dat"));

                Object[] tallennetut = (Object[]) fileInput.readObject();

                pikseleitaX = (Integer) tallennetut[2];
                pikseleitaY = (Integer) tallennetut[3];
                System.out.println("Luodaan uusi PiirtoAlue objektista"); // TEMP
                piirtoAlue = new PiirtoAlue((Integer) tallennetut[0],
                        (Integer) tallennetut[1],
                        pikseleitaX,
                        pikseleitaY,
                        (Object[]) tallennetut[4]);
                System.out.println("PiirtoAlue luotu"); // TEMP
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
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
            variStage.initModality(Modality.WINDOW_MODAL);
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

            GridPane gpVarit = new GridPane();
            gpVarit.setHgap(8);
            gpVarit.setVgap(5);
            gpVarit.add(lbR, 0, 0);
            gpVarit.add(lbG, 0, 1);
            gpVarit.add(lbB, 0, 2);
            gpVarit.add(tfR, 1, 0);
            gpVarit.add(tfG, 1, 1);
            gpVarit.add(tfB, 1, 2);

            Rectangle rtVariUusi = new Rectangle(40, 40, valittuVari);
            Rectangle rtVariVanha = new Rectangle(40, 40, valittuVari);
            VBox vbVarit = new VBox(rtVariUusi, rtVariVanha);

            HBox hbVarit = new HBox(gpVarit, vbVarit);
            hbVarit.setSpacing(15);

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
                        rtVariUusi.setFill(Color.rgb(r.get(), g.get(), b.get()));
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

            Scene tallennusScene = new Scene(vbVariVal, 200, 180);
            variStage.setTitle("Väri");
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
        // TODO napit ei toimi vielä kunnolla
        bnTasoNakyvyys.setOnAction(e -> {
            piirtoAlue.toggleTaso(valittuTaso);
            // TODO kuvan vaihto
        });

        bnTasoYlos.setOnAction(e ->
                piirtoAlue.siirraTaso(valittuTaso, 1)); // TODO virhettä antaa jos on ylhäällä
        bnTasoAlas.setOnAction(e ->
                piirtoAlue.siirraTaso(valittuTaso, -1));

        bnTasoPoista.setOnAction(e -> {
            piirtoAlue.poistaTaso(valittuTaso);
            // TODO päivitä lista ja disable jos on vain yksi taso
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
                /*
                Täyttö ei ehtinyt valmistua, mutta jätin sen koodiin. Nyt se vain täyttää pikseleitä
                koko PiirtoAlueesta eikä vain suljettua aluetta.
                 */
                case "täyttö":
                    Color alkuvari = piirtoAlue.getPikseli(valittuTaso, x, y).getVari();
                    Pikseli[][] pikselit = piirtoAlue.getTaso(valittuTaso).getPikselit();
                    for (int xP = 0; xP < pikseleitaX; xP++) {
                        for (int yP = 0; yP < pikseleitaY; yP++) {
                            Pikseli p = pikselit[xP][yP];
                            /*
                            Täytetään pikseli uudella värillä jos sen värin "etäisyys" toisesta väristä on
                            tarpeeksi alhainen. Etäisyyden raja-arvo on nyt kovakoodattu, mutta sen voisi
                            korvata käyttäjän valinnalla.
                             */
                            if (Vari.etaisyys(p.getVari(), alkuvari) < 300) {
                                p.setPikseli(vari, valittuNakyvyys);
                            }
                        }
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

        // Takaisin päävalikkoon, viedään tämä NäyttöPiirto mukana
        // TODO ehkä tarkistus että onko muutoksia vielä tehty aiemmin jo tallennettuun tiedostoon, ja se näytettäisiin NäyttöAlku-valikossa ehkä nimellä plus * ja italic
        bnTakaisin.setOnAction(e -> new NayttoAlku().start(this, stage));

        // Tallentaminen
        bnTallenna.setOnAction(e -> { // TODO erilliseen luokkaan koodin siistimiseksi
            Stage tallennusStage = new Stage();
            tallennusStage.initModality(Modality.WINDOW_MODAL);
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

                try {
                    String tiedostonimi = tfTiedostonimi.getText();

                    ObjectOutputStream fileOutput = new ObjectOutputStream(
                            new FileOutputStream(tiedostonimi + ".dat"));

                    Object[] tallennettavat = new Object[] {
                            LEVEYS - VASENLEVEYS,
                            KORKEUS - ALAKORKEUS,
                            pikseleitaX,
                            pikseleitaY,
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

        //////////

        scene = new Scene(bpPiirto, LEVEYS, KORKEUS);
        stage.setTitle("Piirto-ohjelma");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
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

    public Scene getScene() {
        return scene;
    }
}
