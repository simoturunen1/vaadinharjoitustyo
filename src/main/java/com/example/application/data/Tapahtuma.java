package com.example.application.data;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class Tapahtuma extends AbstractEntity {

    private String nimi;
    private String kuvaus;
    private LocalDate paivamaara;

    @ManyToOne
    private Jarjestaja jarjestaja;

    @ManyToOne
    private Paikka paikka;

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getKuvaus() {
        return kuvaus;
    }

    public void setKuvaus(String kuvaus) {
        this.kuvaus = kuvaus;
    }

    public LocalDate getPaivamaara() {
        return paivamaara;
    }

    public void setPaivamaara(LocalDate paivamaara) {
        this.paivamaara = paivamaara;
    }

    public Jarjestaja getJarjestaja() {
        return jarjestaja;
    }

    public void setJarjestaja(Jarjestaja jarjestaja) {
        this.jarjestaja = jarjestaja;
    }

    public Paikka getPaikka() {
        return paikka;
    }

    public void setPaikka(Paikka paikka) {
        this.paikka = paikka;
    }
}
