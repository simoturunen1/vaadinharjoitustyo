package com.example.application.views.view;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.example.application.data.Tapahtuma;
import com.example.application.services.TapahtumaService;
import com.vaadin.flow.component.button.Button;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;


@Route("tapahtuma/:id")
@PageTitle("Tapahtuman tiedot")
@RolesAllowed("USER")
public class TapahtumaDetailView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    @Autowired
    private TapahtumaService tapahtumaService;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Long tapahtumaId = Long.parseLong(event.getRouteParameters().get("id").orElse("0"));
        Tapahtuma t = tapahtumaService.findById(tapahtumaId);

        VerticalLayout layout = getContent();
        H2 otsikko = new H2("Tapahtuman tiedot");
        otsikko.setId("page-title");
        layout.add(otsikko);
        layout.add(new Span("Nimi: " + t.getNimi()));
        layout.add(new Span("Kuvaus: " + t.getKuvaus()));
        layout.add(new Span("Päivämäärä: " + t.getPaivamaara()));

        if (t.getJarjestaja() != null) {
            layout.add(new Span("Järjestäjä: " + t.getJarjestaja().getNimi()));
            layout.add(new Span("Sähköposti: " + t.getJarjestaja().getSahkoposti()));
        }

        if (t.getPaikka() != null) {
            layout.add(new Span("Paikka: " + t.getPaikka().getNimi()));
            layout.add(new Span("Osoite: " + t.getPaikka().getOsoite()));
            layout.add(new Span("Kaupunki: " + t.getPaikka().getKaupunki()));
        }

        Button takaisin = new Button("Takaisin", e -> {
            getUI().ifPresent(ui -> ui.navigate("view"));
        });

        Button muokkaa = new Button("Muokkaa", e -> {
            getUI().ifPresent(ui -> ui.navigate("muokkaa-tapahtuma/" + t.getId()));
        });
        layout.add(muokkaa);

        layout.add(takaisin);
    }
}
