package com.example.application.views.view;

import com.example.application.data.Tapahtuma;
import com.example.application.data.Jarjestaja;
import com.example.application.data.Paikka;
import com.example.application.services.TapahtumaService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Luo uusi tapahtuma")
@Route("uusi-tapahtuma")
@RolesAllowed("USER")
public class    UusiTapahtumaView extends Composite<VerticalLayout> {

    public UusiTapahtumaView(TapahtumaService tapahtumaService) {
        VerticalLayout layout = getContent();
        layout.setWidth("400px");
        layout.setSpacing(true);

        layout.add(new H1("Uusi tapahtuma"));

        TextField nimiField = new TextField("Tapahtuman nimi");
        TextField kuvausField = new TextField("Kuvaus");
        DatePicker paivaPicker = new DatePicker("Päivämäärä");

        TextField jarjestajaField = new TextField("Järjestäjän nimi");
        TextField sahkopostiField = new TextField("Sähköposti");

        TextField paikkaField = new TextField("Paikan nimi");
        TextField osoiteField = new TextField("Osoite");
        TextField kaupunkiField = new TextField("Kaupunki");

        Button tallenna = new Button("Tallenna tapahtuma", e -> {
            Tapahtuma t = new Tapahtuma();
            t.setNimi(nimiField.getValue());
            t.setKuvaus(kuvausField.getValue());
            t.setPaivamaara(paivaPicker.getValue());

            Jarjestaja j = new Jarjestaja();
            j.setNimi(jarjestajaField.getValue());
            j.setSahkoposti(sahkopostiField.getValue());
            t.setJarjestaja(j);

            Paikka p = new Paikka();
            p.setNimi(paikkaField.getValue());
            p.setOsoite(osoiteField.getValue());
            p.setKaupunki(kaupunkiField.getValue());
            t.setPaikka(p);

            tapahtumaService.save(t);

            getUI().ifPresent(ui -> ui.navigate("view"));
        });

        Button peruuta = new Button("Peruuta", e -> getUI().ifPresent(ui -> ui.navigate("view")));

        layout.add(nimiField, kuvausField, paivaPicker,
                jarjestajaField, sahkopostiField,
                paikkaField, osoiteField, kaupunkiField,
                tallenna, peruuta);
    }
}
