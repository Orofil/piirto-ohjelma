package com.example.piirto;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
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
import javax.imageio.ImageIO;

/**
 * Ohjelman piirtonäkymä.
 */
public class NayttoPiirto extends Application implements Naytto {
    private int pikseleitaX = Main.pikseleitaX;
    private int pikseleitaY = Main.pikseleitaY;

    protected int VASENLEVEYS = Main.VASENLEVEYS;
    protected int ALAKORKEUS = Main.ALAKORKEUS;

    /**
     * Avattavan/avatun tiedoston nimi.
     */
    private String tiedosto = "";

    private String valittuTyokalu = Main.valittuTyokalu;
    private Color valittuVari = Main.valittuVari;
    private int valittuNakyvyys = Main.valittuNakyvyys;
    private int valittuPaksuus = Main.valittuPaksuus;
    private int valittuTaso = 0;

    /**
     * Fontti pienille, lihavoiduille otsikoille.
     */
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
                    private RadioButton rbVariMusta;
                    private RadioButton rbVariPunainen;
                    private RadioButton rbVariSininen;
                    private RadioButton rbVariMuu; // Näkymätön RadioButton muille väreille valittavaksi
                    private ToggleGroup tgVarit;
                    private Button bnVariValikko;
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

    /**
     * Vaihtaa näytöksi piirtonäytön ja asettaa {@link PiirtoAlue PiirtoAlueen} koon ja siihen avattavan tiedoston.
     * @param pikseleitaX PiirtoAlueen leveys Pikseleissä
     * @param pikseleitaY PiirtoAlueen korkeus Pikseleissä
     * @param tiedosto Avattavan tiedoston nimi
     * @param stage Stage
     */
    public void start(int pikseleitaX, int pikseleitaY, String tiedosto, Stage stage) {
        this.pikseleitaX = pikseleitaX;
        this.pikseleitaY = pikseleitaY;
        this.tiedosto = tiedosto;
        start(stage);
    }

    /**
     * Vaihtaa näytöksi piirtonäytön ja asettaa {@link PiirtoAlue PiirtoAlueelle} avattavan tiedoston.
     * @param tiedosto Avattavan tiedoston nimi
     * @param stage Stage
     */
    public void start(String tiedosto, Stage stage) {
        start(pikseleitaX, pikseleitaY, tiedosto, stage);
    }

    @Override
    public void start(Stage stage) {
        bpPiirto = new BorderPane();


        ///// Piirtoalue

        // Luodaan tyhjä PiirtoAlue
        if (tiedosto == null || tiedosto.equals("")) {
            piirtoAlue = new PiirtoAlue(
                    LEVEYS - VASENLEVEYS,
                    KORKEUS - ALAKORKEUS,
                    pikseleitaX,
                    pikseleitaY);
        }
        // Luodaan PiirtoAlue kuvasta
        else {
            try {
                ObjectInputStream fileInput = new ObjectInputStream(
                        new FileInputStream(tiedosto + ".dat"));

                Object[] tallennetut = (Object[]) fileInput.readObject();

                pikseleitaX = (Integer) tallennetut[2];
                pikseleitaY = (Integer) tallennetut[3];
                piirtoAlue = new PiirtoAlue((Integer) tallennetut[0],
                        (Integer) tallennetut[1],
                        pikseleitaX,
                        pikseleitaY,
                        (Object[]) tallennetut[4]);
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        piirtoAlue.setStyle("-fx-background-color: white");
        bpPiirto.setCenter(piirtoAlue);


        ///// Vasen valikko
        vbVasen = new VBox(5);
        vbVasen.setPadding(new Insets(10));
        vbVasen.setStyle("-fx-border-color: gray");
        bpPiirto.setLeft(vbVasen);

        lbVari = new Label("Värit");
        lbVari.setFont(fonttiOtsikko);
        rbVariMusta = new RadioButton("Musta");
        rbVariPunainen = new RadioButton("Punainen");
        rbVariSininen = new RadioButton("Sininen");
        rbVariMuu = new RadioButton();

        tgVarit = new ToggleGroup();
        rbVariMusta.setToggleGroup(tgVarit);
        rbVariPunainen.setToggleGroup(tgVarit);
        rbVariSininen.setToggleGroup(tgVarit);
        rbVariMuu.setToggleGroup(tgVarit);

        rbVariMusta.fire();
        bnVariValikko = new Button("Väri");

        vbVari = new VBox(5);
        vbVari.getChildren().addAll(lbVari, rbVariMusta, rbVariPunainen, rbVariSininen, bnVariValikko);

        rtVari = new Rectangle(40, 40, valittuVari);

        hbVari = new HBox(70);
        hbVari.getChildren().addAll(vbVari, rtVari);

        slPaksuus = new Slider(1, 100, 1);
        slPaksuus.setShowTickLabels(true);
        slPaksuus.setBlockIncrement(5);
        lbPaksuus = new Label("Paksuus: " + (int) slPaksuus.getValue());
        lbPaksuus.setFont(fonttiOtsikko);

        slNakyvyys = new Slider(0, 100, 100);
        slNakyvyys.setShowTickLabels(true);
        slNakyvyys.setBlockIncrement(5);
        lbNakyvyys = new Label("Näkyvyys: " + (int) slNakyvyys.getValue());
        lbNakyvyys.setFont(fonttiOtsikko);

        lbTasot = new Label("Tasot");
        lbTasot.setFont(fonttiOtsikko);
        bnUusiTaso = new Button("+");
        tfUusiTaso = new TextField();
        tfUusiTaso.setPromptText("Taso " + (PiirtoTaso.getTasoNro() + 1));
        hbUusiTaso = new HBox(bnUusiTaso, tfUusiTaso);
        hbUusiTaso.setSpacing(10);

        tasot = new ListView<>();
        tasot.setPrefWidth(140); // Kovakoodattu leveys :(((
        // Alussa luodaan lista tasoista ja valitaan ensimmäinen
        tasot.setItems(FXCollections.observableArrayList(piirtoAlue.getTasoNimet()));
        tasot.getSelectionModel().select(0);

        bnTasoNakyvyys = new Button();
        bnTasoYlos = new Button();
        bnTasoAlas = new Button();
        bnTasoPoista = new Button();
        // Asetetaan kuvat nappeihin
        setBnTasoNakyvyysKuva();
        bnTasoYlos.setGraphic(new ImageView(new Image(IMGPOLKU + "nuoli_ylös.png")));
        bnTasoAlas.setGraphic(new ImageView(new Image(IMGPOLKU + "nuoli_alas.png")));
        bnTasoPoista.setGraphic(new ImageView(new Image(IMGPOLKU + "x.png")));
        if (piirtoAlue.getTasoMaara() == 1) {
            bnTasoPoista.setDisable(true);
        }

        hbTaso = new HBox(bnTasoNakyvyys, bnTasoYlos, bnTasoAlas, bnTasoPoista);
        hbTaso.setSpacing(10);
        slTasoNakyvyys = new Slider(0, 100, 100);
        slTasoNakyvyys.setBlockIncrement(5);
        slTasoNakyvyys.setValue(piirtoAlue.getTaso(valittuTaso).getNakyvyys());
        lbTasoNakyvyys = new Label("Tason näkyvyys: " + (int) slTasoNakyvyys.getValue());

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
        // Asetetaan kuvat nappeihin
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


        ///// Toiminnallisuus

        // Värien valinta
        rbVariMusta.setOnAction(e -> setValittuVari(Color.BLACK));
        rbVariPunainen.setOnAction(e -> setValittuVari(Color.RED));
        rbVariSininen.setOnAction(e -> setValittuVari(Color.BLUE));
        // Pop-up-ikkuna värin valitsemiseksi
        bnVariValikko.setOnAction(e -> {
            // AtomicInteger tekee näistä laillisia muuttaa anonyymissä luokassa
            AtomicInteger r = new AtomicInteger();
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
        slPaksuus.valueProperty().addListener(ob -> {
            valittuPaksuus = (int) slPaksuus.getValue();
            lbPaksuus.setText("Paksuus: " + valittuPaksuus);
        });

        // Läpinäkyvyyden säätö
        slNakyvyys.valueProperty().addListener(ob -> {
            valittuNakyvyys = (int) slNakyvyys.getValue();
            lbNakyvyys.setText("Näkyvyys: " + valittuNakyvyys);
        });

        // Tason lisäys
        bnUusiTaso.setOnAction(e -> {
            piirtoAlue.lisaaTaso(tfUusiTaso.getText());
            tfUusiTaso.setText("");
            tfUusiTaso.setPromptText("Taso " + (PiirtoTaso.getTasoNro() + 1));
            // Päivitetään tasojen lista ja valitaan uusi taso
            tasot.setItems(FXCollections.observableArrayList(piirtoAlue.getTasoNimet()));
            setValittuTaso(piirtoAlue.getTasoMaara() - 1);
            bnTasoPoista.setDisable(false); // Tasoja voi taas poistaa
        });

        // Listasta klikatun tason valinta
        tasot.getSelectionModel().selectedItemProperty().addListener(ob ->
                setValittuTaso(tasot.getSelectionModel().getSelectedIndex()));

        // Tason muokkausnapit
        bnTasoNakyvyys.setOnAction(e -> {
            piirtoAlue.toggleTaso(valittuTaso);
            // Päivitetään tason näkyvyyden kuvake
            setBnTasoNakyvyysKuva();
        });

        // Jos tasot menisivät listassa järkevästi alhaalta ylös, voisivat alla olevat määrä-argumenttien arvot vaihtaa paikkoja
        bnTasoYlos.setOnAction(e -> {
            piirtoAlue.siirraTaso(valittuTaso, -1);
            tasot.setItems(FXCollections.observableArrayList(piirtoAlue.getTasoNimet()));
            setValittuTaso(valittuTaso);
        });
        bnTasoAlas.setOnAction(e -> {
            piirtoAlue.siirraTaso(valittuTaso, 1);
            tasot.setItems(FXCollections.observableArrayList(piirtoAlue.getTasoNimet()));
            setValittuTaso(valittuTaso);
        });

        bnTasoPoista.setOnAction(e -> {
            piirtoAlue.poistaTaso(valittuTaso);
            tasot.setItems(FXCollections.observableArrayList(piirtoAlue.getTasoNimet()));
            setValittuTaso(valittuTaso);
            if (piirtoAlue.getTasoMaara() == 1) {
                bnTasoPoista.setDisable(true);
            }
        });

        slTasoNakyvyys.valueProperty().addListener(ov -> {
            piirtoAlue.getTaso(valittuTaso).setNakyvyys((int) slTasoNakyvyys.getValue());
            lbTasoNakyvyys.setText("Tason näkyvyys: " + (int) slTasoNakyvyys.getValue());
        });

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
                            Täytetään pikseli jos sen väri on täysin sama kuin klikatulla pikselillä.
                            Voisi olla myös vähän liikkumavaraa värissä, mutta se käy vaikeaksi toteuttaa.
                             */
                            if (p.getVari().equals(alkuvari)) {
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
        bnTakaisin.setOnAction(e -> new NayttoAlku().start(this, stage));

        // Tallentaminen
        bnTallenna.setOnAction(e -> {
            Stage tallennusStage = new Stage();
            tallennusStage.initModality(Modality.WINDOW_MODAL);
            tallennusStage.initOwner(stage);

            VBox vbTallennus = new VBox(10);
            vbTallennus.setPadding(new Insets(15));
            vbTallennus.getChildren().add(new Label("Tiedostonimi:"));
            TextField tfTiedostonimi = new TextField(tiedosto);

            RadioButton rbTallennusPiirto = new RadioButton("Piirto-ohjelman kuva");
            RadioButton rbTallennusKuva = new RadioButton("PNG-kuva");
            ToggleGroup tgTallennus = new ToggleGroup();
            rbTallennusPiirto.setToggleGroup(tgTallennus);
            rbTallennusKuva.setToggleGroup(tgTallennus);
            rbTallennusPiirto.fire();
            // Luodaan tooltipit tallennustyypeille, jotka näkyvät, kun kursoria pitää napin yllä
            Tooltip ttTallennusPiirto = new Tooltip(
                    "Kuvan voi avata ja sitä voi muokata piirto-ohjelmassa, mutta ei muualla.");
            Tooltip ttTallennusKuva = new Tooltip(
                    "Kuvaa voi tarkastella muilla ohjelmilla, mutta sitä ei voi enää avata tai muokata piirto-ohjelmassa.");
            Tooltip.install(rbTallennusPiirto, ttTallennusPiirto);
            Tooltip.install(rbTallennusKuva, ttTallennusKuva);

            HBox hbTallennus = new HBox(rbTallennusPiirto, rbTallennusKuva);
            hbTallennus.setSpacing(10);

            Label lbTallennusHuom = new Label();
            lbTallennusHuom.setTextFill(Color.RED);
            Button bnTallennus = new Button("Tallenna");
            vbTallennus.getChildren().addAll(tfTiedostonimi, hbTallennus, lbTallennusHuom, bnTallennus);

            Scene tallennusScene = new Scene(vbTallennus, 300, 180);
            tallennusStage.setTitle("Tallennus");
            tallennusStage.setScene(tallennusScene);
            tallennusStage.show();

            /*
            Huomautetaan, jos tiedosto on jo olemassa ja kielletään tallentaminen jos nimi on tyhjä.
            Muuta tiedostonimen validaatiota ei nyt ole :(
             */
            if (!tiedosto.equals("")) {
                lbTallennusHuom.setText("HUOM: Tiedosto on jo olemassa!");
            }
            /*
            Tämä ei päivity silloin kun tiedostotyypin valinnan vaihtaa, koska se olisi vaatinut
            vähän kikkailua jos koodin toistoa haluaa välttää.
             */
            tfTiedostonimi.setOnKeyTyped(e1 -> {
                String tiedostopaate = rbTallennusPiirto.isSelected() ? ".dat" : ".png";
                if (new File(tfTiedostonimi.getText() + tiedostopaate).isFile()) {
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

                    // Piirto-ohjelman tiedoston tallennus
                    if (rbTallennusPiirto.isSelected()) {

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

                            /*
                            Poistetaan viimeinen nimi jos tiedostoja on yli 20
                            (Ei mitään tiettyä syytä siihen miksi juuri 20)
                             */
                            if (viimeisimmatTeksti.size() > 20) {
                                viimeisimmatTeksti.remove(viimeisimmatTeksti.size() - 1);
                            }

                            // Kirjoitetaan nimet tiedostoon
                            FileWriter writer = new FileWriter(viimeisimmat);
                            for (String nimi : viimeisimmatTeksti) {
                                writer.write(nimi + System.lineSeparator());
                            }
                            writer.close();
                        }
                    // Tallennetaan png
                    } else {
                        // Piilotetaan ruudukko siksi aikaa kun tallennetaan, halusit tai et >:)
                        boolean ruudukkoPiilotettu = piirtoAlue.getRuudukkoPiilotettu();
                        piirtoAlue.setRuudukkoPiilotettu(true);
                        WritableImage kuva = piirtoAlue.snapshot(null, null);
                        if (!ruudukkoPiilotettu) {
                            piirtoAlue.setRuudukkoPiilotettu(false);
                        }

                        File tiedosto = new File(tiedostonimi + ".png");

                        // Kuvan tallennus
                        // Lähde: http://www.java2s.com/example/java/javafx/save-javafx-writableimage.html
                        ImageIO.write(SwingFXUtils.fromFXImage(kuva, null), "png", tiedosto);
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

    /**
     * Asettaa valitun värin ja päivittää siihen liittyvät graafiset komponentit.
     * @param vari Uusi valittu väri
     */
    public void setValittuVari(Color vari) {
        valittuVari = vari;
        rtVari.setFill(vari);
        if (vari.equals(Color.BLACK)) {
            rbVariMusta.fire();
        } else if (vari.equals(Color.RED)) {
            rbVariPunainen.fire();
        } else if (vari.equals(Color.BLUE)) {
            rbVariSininen.fire();
        } else {
            rbVariMuu.fire();
        }
    }

    /**
     * Asettaa valitun tason ja päivittää siihen liittyvät graafiset komponentit.
     * @param taso Uuden valitun tason indeksi
     */
    public void setValittuTaso(int taso) {
        // Pidetään valittava taso tasojen määrän rajoissa
        if (taso < 0) {
            taso = 0;
        } else if (taso >= piirtoAlue.getTasoMaara()) {
            taso = piirtoAlue.getTasoMaara() - 1;
        }
        valittuTaso = taso;
        tasot.getSelectionModel().select(taso);
        tasot.scrollTo(taso);
        setBnTasoNakyvyysKuva();
        slTasoNakyvyys.setValue(piirtoAlue.getTaso(taso).getNakyvyys());
    }

    /**
     * Päivittää graafisen komponentin bnTasoNakyvyys kuvakkeen valitun tason näkyvyyttä vastaavaksi.
     */
    private void setBnTasoNakyvyysKuva() {
        bnTasoNakyvyys.setGraphic(new ImageView(new Image(IMGPOLKU +
                (piirtoAlue.getTaso(valittuTaso).getPiilotettu() ? "silmä_kiinni.png" : "silmä_auki.png"))));
    }

    /**
     * @return NäyttöPiirto-olion scene
     */
    public Scene getScene() {
        return scene;
    }
}
