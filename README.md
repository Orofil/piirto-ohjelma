# Asentaminen

Ohjelma ladataan GitHubista, josta sen saa Code-näppäimen takaa joko ZIP-kansiona tai HTTPS-linkkinä.

Ohjelmaa ei voi avata suoraan, sillä siitä ei ole tehty suoritettavaa pakkausta, vaan se täytyy suorittaa jossakin IDEssä. Ohjeet siihen löytyvät OHII-kurssin Moodlessa olevasta pdf-tiedostosta dioilta 35–39.

Ohjelman suorittamiseen tarvitaan jokin JDK (minulla Amazon Corretto 17, Windows x64). Siihen tarvitaan myös JavaFX (minulla versio 20, Windows x86), jonka ainakin IntelliJ IDEA luultavasti lataa automaattisesti. Ohjelma käyttää myös JavaFX:n swing-kirjastoa, jota IntelliJ ei minulla ladannut automaattisesti, vaan minun piti siinä vaiheessa ladata koko JavaFX erikseen, josta sitten lisäsin projektin käyttämiin kirjastoihin javafx.swing.jar-tiedoston (IntelliJ IDEAlla File > Project Structure > Libraries > New Project Library > Java > javafx.swing.jar (pitäisi tulla luetteloon nimellä javafx.swing)).

Jos swing-kirjaston lisäys ei toimi, voi ohjelmaa käyttää ilman sitä muuttamalla seuraavat rivit kommenteiksi: TODO
- src/main/java/com/example/piirto/NayttoPiirto.java
  - Rivi 5: import javafx.embed.swing.SwingFXUtils;
  - Rivi 643: ImageIO.write(SwingFXUtils.fromFXImage(...
- src/main/java/module-info.java
  - Rivi 4: requires javafx.swing;
Tällöin ainoa ominaisuus, joka ei toimi, on png-tiedostoksi tallentaminen.
Ohjelma suoritetaan ajamalla tiedoston src/main/java/com/example/piirto/Main.java metodi main().

# Käyttö

Ohjelma käynnistyy kuvan 1 näkymään, jossa käyttäjä voi syöttää luotavalle piirtopohjalle leveyden ja korkeuden ja painaa sitten suurta plusnappia (+) luodakseen uuden kuvan. Tällöin aukeaa näkymä 2, jossa käyttäjä voi värittää ruudullista piirtoaluetta oletuksena mustaksi painamalla hiiren vasenta näppäintä pohjassa. Painamalla hiiren oikeaa näppäintä, väritystä voi poistaa.

Vasemman reunan valikoista voi vaihtaa piirtämiseen käytettävää väriä, paksuutta ja läpinäkyvyyttä ja lisätä, järjestää ja poistaa kuvan tasoja sekä muuttaa niiden läpinäkyvyyttä. Huomaa, että kuvassa alimmat tasot ovat listassa ylimpänä ja kuvassa ylimmät tasot ovat listassa alimpana.

Alareunan valikoista voi valita käytettävän työkalun, joka on joko yksinkertainen kynä piirtämiseen, kumi piirretyn poistamiseen, täyttötyökalu kaiken suurin piirtein samanvärisen muuttamiseen ja värinpoimija, jolla voi muuttaa valittua piirtoväriä kuvan pikselien mukaisesti. Lisäksi alavalikon keskellä on nappi piirtoalueen ruudukon piilottamiseen ja näyttämiseen uudelleen, nappi, jolla pääsee takaisin päävalikkoon ja nappi tiedoston tallentamiseen.

Jos päävalikkoon saavutaan tiedoston muokkauksesta tai tiedoston tallentamisen jälkeen, näkyy Avaa-valikossa vaihtoehdot avata enintään kuusi viimeisimpänä muokattua tiedostoa. 
