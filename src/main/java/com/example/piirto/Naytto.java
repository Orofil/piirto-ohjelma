package com.example.piirto;

/**
 * Ehkä vähän oudolla tavalla hyödynnetty rajapinta, joka toimii pohjana
 * eri näytöille (joita on kylläkin vain kaksi), antaen niille käyttöön
 * ikkunan oletusleveyden ja korkeuden sekä tiedostojen polun.
 */
public interface Naytto {
    /**
     * Muille tiedostoille kuin kuville käytettävä tiedostopolku.
     */
    String POLKU = Main.POLKU;

    /**
     * Kuville käytettävä tiedostopolku.
     */
    String IMGPOLKU = "file:" + Main.POLKU;

    /**
     * Ikkunan leveys kuvapisteissä.
     */
    int LEVEYS = Main.LEVEYS;
    /**
     * Ikkunan korkeus kuvapisteissä.
     */
    int KORKEUS = Main.KORKEUS;
}
