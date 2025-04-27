package com.example.application.data;

import jakarta.persistence.Entity;

@Entity
public class Jarjestaja extends AbstractEntity {

    private String nimi;
    private String sahkoposti;

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getSahkoposti() {
        return sahkoposti;
    }

    public void setSahkoposti(String sahkoposti) {
        this.sahkoposti = sahkoposti;
    }
}
