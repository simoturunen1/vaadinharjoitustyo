package com.example.application.views.view;

import com.example.application.data.Jarjestaja;
import com.example.application.data.Paikka;
import com.example.application.data.Tapahtuma;
import com.example.application.services.TapahtumaService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route("muokkaa-tapahtuma/:id")
@PageTitle("Muokkaa tapahtumaa")
@RolesAllowed("USER")
public class TapahtumaFormView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    @Autowired
    private TapahtumaService tapahtumaService;

    private Tapahtuma tapahtuma;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Long tapahtumaId = Long.parseLong(event.getRouteParameters().get("id").orElse("0"));
        tapahtuma = tapahtumaService.findById(tapahtumaId);

        VerticalLayout layout = getContent();
        layout.add(new H2("Muokkaa tapahtumaa"));

        TextField nimiField = new TextField("Tapahtuman nimi");
        nimiField.setValue(tapahtuma.getNimi() != null ? tapahtuma.getNimi() : "");

        TextField kuvausField = new TextField("Kuvaus");
        kuvausField.setValue(tapahtuma.getKuvaus() != null ? tapahtuma.getKuvaus() : "");

        DatePicker paivaPicker = new DatePicker("Päivämäärä");
        paivaPicker.setValue(tapahtuma.getPaivamaara());

        TextField jarjestajaField = new TextField("Järjestäjän nimi");
        TextField sahkopostiField = new TextField("Sähköposti");

        if (tapahtuma.getJarjestaja() != null) {
            jarjestajaField.setValue(tapahtuma.getJarjestaja().getNimi());
            sahkopostiField.setValue(tapahtuma.getJarjestaja().getSahkoposti());
        }

        TextField paikkaField = new TextField("Paikan nimi");
        TextField osoiteField = new TextField("Osoite");
        TextField kaupunkiField = new TextField("Kaupunki");

        if (tapahtuma.getPaikka() != null) {
            paikkaField.setValue(tapahtuma.getPaikka().getNimi());
            osoiteField.setValue(tapahtuma.getPaikka().getOsoite());
            kaupunkiField.setValue(tapahtuma.getPaikka().getKaupunki());
        }

        layout.add(
                nimiField,
                kuvausField,
                paivaPicker,
                jarjestajaField,
                sahkopostiField,
                paikkaField,
                osoiteField,
                kaupunkiField
        );

        Button tallenna = new Button("Tallenna", e -> {
            tapahtuma.setNimi(nimiField.getValue());
            tapahtuma.setKuvaus(kuvausField.getValue());
            tapahtuma.setPaivamaara(paivaPicker.getValue());

            Jarjestaja j = tapahtuma.getJarjestaja() != null ? tapahtuma.getJarjestaja() : new Jarjestaja();
            j.setNimi(jarjestajaField.getValue());
            j.setSahkoposti(sahkopostiField.getValue());
            tapahtuma.setJarjestaja(j);

            Paikka p = tapahtuma.getPaikka() != null ? tapahtuma.getPaikka() : new Paikka();
            p.setNimi(paikkaField.getValue());
            p.setOsoite(osoiteField.getValue());
            p.setKaupunki(kaupunkiField.getValue());
            tapahtuma.setPaikka(p);

            tapahtumaService.save(tapahtuma);

            Notification.show("Tapahtuma tallennettu!");
            getUI().ifPresent(ui -> ui.navigate("tapahtuma/" + tapahtuma.getId()));
        });

        layout.add(tallenna);
    }
}
