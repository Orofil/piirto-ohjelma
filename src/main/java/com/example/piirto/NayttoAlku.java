package com.example.piirto;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Ohjelman päävalikko.
 */
public class NayttoAlku extends Application implements Naytto {
    private int pikseleitaX = Main.pikseleitaX;
    private int pikseleitaY = Main.pikseleitaY;
    /**
     * Uuden kuvan pikselien määrien välinen suhde.
     */
    double piksSuhde = (double) pikseleitaY / (double) pikseleitaX;

    /**
     * Avattavan tiedoston nimi.
     */
    private String tiedosto = "";

    /**
     * Aiemmin avattuna ollut tallentaton tiedosto.
     */
    private NayttoPiirto avattuPiirto;

    ///// Määritellään graafiset komponentit
    /*
    Graafiset komponentit on sisennetty sen mukaan, mikä niiden hierarkia on
    ikkunassa. Tämä on ehkä hieman outo menetelmä, mutta näin nyt kokeilin
    tehdä ja tämä toimii. Kaikkia ei tässä vaiheessa tarvitsisi määritellä,
    kuten IntelliJ huomauttaa, mutta ihan sama :)
     */
    private Scene scene;
        private BorderPane bpAlkuvalikko;
            private VBox vbAlkuvalikkoVasen;
                private Label lbLuo;
                private HBox hbUusi;
                    private Button bnUusi;
                    private GridPane gpUusiPiks;
                        private Label lbPiksX;
                        private Label lbPiksY;
                        private Label lbPiksSuhde;
                        private TextField tfPiksX;
                        private TextField tfPiksY;
                        private CheckBox cbPiksSuhde;
                private Label lbAvaa;
                private VBox vbAvaa;
            private VBox vbAlkuvalikkoOikea;
                private ImageView imgAlkuvalikko;
                private Text txAlkuvalikko;
                private Text txAlkuvalikkoLinkki;


    /**
     * Vaihtaa näytöksi alkunäytön ja näyttää annetun tallentamattoman piirtonäkymän
     * avattavien tiedostojen listassa.
     * @param avattuPiirto NayttoPiirto, josta ollaan tultu NayttoAlkuun
     * @param stage Stage
     */
    public void start(NayttoPiirto avattuPiirto, Stage stage) {
        this.avattuPiirto = avattuPiirto;
        start(stage);
    }

    @Override
    public void start(Stage stage) {
        // Vasen valikko
        lbLuo = new Label("Luo uusi");
        lbAvaa = new Label("Avaa");
        lbLuo.setFont(Font.font(24));
        lbAvaa.setFont(Font.font(24));

        bnUusi = new Button();
        bnUusi.setGraphic(new ImageView(new Image(IMGPOLKU + "uusi.png")));

        lbPiksX = new Label("Leveys:");
        lbPiksY = new Label("Korkeus:");
        tfPiksX = new TextField(String.valueOf(pikseleitaX));
        tfPiksY = new TextField(String.valueOf(pikseleitaY));
        tfPiksX.setPrefWidth(40);
        tfPiksY.setPrefWidth(40);
        cbPiksSuhde = new CheckBox();
        cbPiksSuhde.fire();
        lbPiksSuhde = new Label("Säilytä suhde");

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

        // Näytetään äsken muokattu tiedosto jos sellainen on
        if (avattuPiirto != null) {
            Button bn = new Button("Tallentamaton tiedosto");
            bn.setPrefWidth(250);
            bn.setFont(Font.font("", FontPosture.ITALIC, 18));

            vbAvaa.getChildren().add(bn);

            // Toiminnallisuus, vaihtaa scenen takaisin äskeiseen NäyttöPiirtoon
            bn.setOnAction(e -> stage.setScene(avattuPiirto.getScene()));
        }
        // Luodaan luettelo viimeisimmistä tiedostoista
        if (new File(POLKU + "viimeisimmät.txt").isFile()) {
            try {
                ArrayList<String> viimeisimmatTiedostot = new ArrayList<>();

                Scanner scanner = new Scanner(new File(POLKU + "viimeisimmät.txt"));
                while (scanner.hasNextLine()) {
                    viimeisimmatTiedostot.add(scanner.nextLine());
                }

                for (int i = 0; i < viimeisimmatTiedostot.size(); i++) {
                    if (vbAvaa.getChildren().size() < 6) { // Viimeisimpiä tiedostoja voi olla näkyvissä enintään 6
                        Button bn = new Button(viimeisimmatTiedostot.get(i));
                        bn.setPrefWidth(250);
                        bn.setFont(Font.font(18));

                        vbAvaa.getChildren().add(bn);

                        // Toiminnallisuus, avaa uuden piirtonäytön (a.k.a. NayttoPiirto) valitulla tiedostonimellä
                        int finalI = i;
                        bn.setOnAction(e -> new NayttoPiirto().start(viimeisimmatTiedostot.get(finalI), stage));
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        // Jos viimeisimpiä tiedostoja ei ole
        if (vbAvaa.getChildren().isEmpty()) {
            vbAvaa.getChildren().add(new Text("Ei viimeisimpiä tiedostoja"));
        }

        // Oikea valikko
        imgAlkuvalikko = new ImageView(new Image(IMGPOLKU + "piirto-ohjelma.png"));
        txAlkuvalikko = new Text("""
                Piirto-ohjelma, jonka nimi on luovasti "piirto-ohjelma"
                Soveltuu pikselien värittämiseen
                Sen lisäksi ei tee paljon muuta
                Saatan päivittää ohjelmaa tulevaisuudessa
                                    
                Jaakko Saano (Orofil), 2023""");
        txAlkuvalikko.setTextAlignment(TextAlignment.RIGHT);
        txAlkuvalikkoLinkki = new Text("github.com/Orofil/piirto-ohjelma");
        txAlkuvalikkoLinkki.setUnderline(true);
        txAlkuvalikkoLinkki.setFill(Color.web("#0000EE"));

        vbAlkuvalikkoOikea = new VBox(imgAlkuvalikko, txAlkuvalikko, txAlkuvalikkoLinkki);
        vbAlkuvalikkoOikea.setAlignment(Pos.TOP_RIGHT);

        bpAlkuvalikko = new BorderPane();
        bpAlkuvalikko.setPadding(new Insets(40));
        bpAlkuvalikko.setLeft(vbAlkuvalikkoVasen);
        bpAlkuvalikko.setRight(vbAlkuvalikkoOikea);


        ///// Toiminnallisuus

        // Luodaan uusi NayttoPiirto ja näytetään se
        bnUusi.setOnAction(e ->
            new NayttoPiirto().start(
                    Integer.parseInt(tfPiksX.getText()),
                    Integer.parseInt(tfPiksY.getText()),
                    tiedosto, stage));

        // Kelvollisten kuvan mittojen validaatio
        tfPiksX.setOnKeyTyped(e -> {
            int x;
            if (cbPiksSuhde.isSelected()) {
                try {
                    x = Integer.parseInt(tfPiksX.getText());
                    if (x > 0) {
                        bnUusi.setDisable(false);
                        tfPiksY.setText(String.valueOf((int) Math.round(x * piksSuhde)));
                    }
                } catch (NumberFormatException ignored) {
                    bnUusi.setDisable(true);
                }
            }
        });
        tfPiksY.setOnKeyTyped(e -> {
            int y;
            if (cbPiksSuhde.isSelected()) {
                try {
                    y = Integer.parseInt(tfPiksY.getText());
                    if (y > 0) {
                        bnUusi.setDisable(false);
                        tfPiksX.setText(String.valueOf((int) Math.round(y / piksSuhde)));
                    }
                } catch (NumberFormatException ignored) {
                    bnUusi.setDisable(true);
                }
            }
        });

        cbPiksSuhde.setOnAction(e -> {
            try {
                piksSuhde = Double.parseDouble(tfPiksY.getText()) / Double.parseDouble(tfPiksX.getText());
            } catch (NumberFormatException ignored) {
            }
        });

        txAlkuvalikkoLinkki.setOnMouseEntered(e -> scene.setCursor(Cursor.HAND));
        txAlkuvalikkoLinkki.setOnMouseExited(e -> scene.setCursor(Cursor.DEFAULT));
        txAlkuvalikkoLinkki.setOnMouseClicked(e ->
                getHostServices().showDocument("https://github.com/Orofil/piirto-ohjelma"));

        ///// Scene
        scene = new Scene(bpAlkuvalikko, LEVEYS, KORKEUS);
        stage.setTitle("Piirto-ohjelma");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
