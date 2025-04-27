package com.example.application.data;

import jakarta.persistence.Entity;

@Entity
public class Paikka extends AbstractEntity {

    private String nimi;
    private String osoite;
    private String kaupunki;

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getOsoite() {
        return osoite;
    }

    public void setOsoite(String osoite) {
        this.osoite = osoite;
    }

    public String getKaupunki() {
        return kaupunki;
    }

    public void setKaupunki(String kaupunki) {
        this.kaupunki = kaupunki;
    }
}
