package com.example.application.views.view;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import jakarta.annotation.security.RolesAllowed;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import com.vaadin.flow.component.grid.Grid;
import com.example.application.data.Tapahtuma;
import com.example.application.services.TapahtumaService;
import com.vaadin.flow.component.button.Button;
import java.time.LocalDate;
import com.vaadin.flow.component.textfield.TextField;
import java.util.List;

@PageTitle("Tapahtumat")
@Route("view")
@Menu(order = 2, icon = LineAwesomeIconUrl.PENCIL_RULER_SOLID)
@RolesAllowed("USER")
public class ViewView extends Composite<VerticalLayout> {

    private final TapahtumaService tapahtumaService;

    public ViewView(TapahtumaService tapahtumaService) {
        this.tapahtumaService = tapahtumaService;

        HorizontalLayout layoutRow = new HorizontalLayout();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.setHeight("min-content");
        getContent().add(layoutRow);
        getContent().add(layoutColumn2);
        getContent().add(layoutRow2);

        /*
        // --- <TESTI: Luo tapahtuma ---
        Button luoTapahtumaNappi = new Button("Luo testitapahtuma", e -> {
            Tapahtuma tapahtuma = new Tapahtuma();
            tapahtuma.setNimi("Testitapahtuma");
            tapahtuma.setKuvaus("Kuvaus tähän");
            tapahtuma.setPaivamaara(LocalDate.now());

            tapahtumaService.save(tapahtuma);
            naytaTapahtumat(layoutColumn2); // Päivitä näkymä
        });
        // ---- TESTI> */

        Button uusiTapahtumaNappi = new Button("Luo uusi tapahtuma", e -> {
            getUI().ifPresent(ui -> ui.navigate("uusi-tapahtuma"));
        });
        layoutRow.add(uusiTapahtumaNappi);

        //layoutRow.add(luoTapahtumaNappi);
        getContent().add(layoutRow, layoutColumn2, layoutRow2);

        naytaTapahtumat(layoutColumn2);
    }

    private void naytaTapahtumat(VerticalLayout container) {
        container.removeAll();

        TextField nimiFilter = new TextField("Nimi");
        TextField kuvausFilter = new TextField("Kuvaus");
        TextField paivaFilter = new TextField("Päivämäärä (vvvv-kk-pp)");
        TextField jarjestajaFilter = new TextField("Järjestäjä");
        TextField paikkaFilter = new TextField("Paikka");

        Button haeButton = new Button("Hae");
        Button tyhjennaButton = new Button("Tyhjennä");

        HorizontalLayout filterLayout = new HorizontalLayout(
                nimiFilter, kuvausFilter, paivaFilter, jarjestajaFilter, paikkaFilter, haeButton, tyhjennaButton
        );
        filterLayout.setWidthFull();
        filterLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        filterLayout.setAlignItems(FlexComponent.Alignment.END);
        filterLayout.addClassName("horizontal-filters");

        Grid<Tapahtuma> grid = new Grid<>(Tapahtuma.class, false);
        List<Tapahtuma> kaikkiTapahtumat = tapahtumaService.findAll();
        GridListDataView<Tapahtuma> dataView = grid.setItems(kaikkiTapahtumat);

        grid.addColumn(Tapahtuma::getNimi)
                .setHeader("Nimi")
                .setSortable(true);

        grid.addColumn(Tapahtuma::getKuvaus)
                .setHeader("Kuvaus")
                .setSortable(true);

        grid.addColumn(t -> t.getPaivamaara() != null ? t.getPaivamaara().toString() : "")
                .setHeader("Päivämäärä")
                .setSortable(true);

        grid.addColumn(t -> t.getJarjestaja() != null ? t.getJarjestaja().getNimi() : "")
                .setHeader("Järjestäjä")
                .setSortable(true);

        grid.addColumn(t -> t.getPaikka() != null ? t.getPaikka().getNimi() : "")
                .setHeader("Paikka")
                .setSortable(true);

        grid.addComponentColumn(t -> new Button("Lisätiedot", e ->
                getUI().ifPresent(ui -> ui.navigate("tapahtuma/" + t.getId())))).setHeader("Lisätiedot");

        grid.addComponentColumn(t -> {

            Button poista = new Button("Poista", e -> {
                tapahtumaService.delete(t);
                naytaTapahtumat(container);
            });
            poista.addClassName("danger-button");
            //poista.getStyle().set("color", "red");
            return poista;
        }).setHeader("Toiminnot");

        Runnable applyFilter = () -> dataView.setFilter(tapahtuma -> {
            boolean matchesNimi = StringUtils.containsIgnoreCase(tapahtuma.getNimi(), nimiFilter.getValue());
            boolean matchesKuvaus = StringUtils.containsIgnoreCase(tapahtuma.getKuvaus(), kuvausFilter.getValue());
            boolean matchesPaiva = tapahtuma.getPaivamaara() != null &&
                    tapahtuma.getPaivamaara().toString().contains(paivaFilter.getValue());
            boolean matchesJarjestaja = tapahtuma.getJarjestaja() != null &&
                    StringUtils.containsIgnoreCase(tapahtuma.getJarjestaja().getNimi(), jarjestajaFilter.getValue());
            boolean matchesPaikka = tapahtuma.getPaikka() != null &&
                    StringUtils.containsIgnoreCase(tapahtuma.getPaikka().getNimi(), paikkaFilter.getValue());

            return matchesNimi && matchesKuvaus && matchesPaiva && matchesJarjestaja && matchesPaikka;
        });

        nimiFilter.addValueChangeListener(e -> applyFilter.run());
        kuvausFilter.addValueChangeListener(e -> applyFilter.run());
        paivaFilter.addValueChangeListener(e -> applyFilter.run());
        jarjestajaFilter.addValueChangeListener(e -> applyFilter.run());
        paikkaFilter.addValueChangeListener(e -> applyFilter.run());

        haeButton.addClickListener(e -> applyFilter.run());
        tyhjennaButton.addClickListener(e -> {
            nimiFilter.clear();
            kuvausFilter.clear();
            paivaFilter.clear();
            jarjestajaFilter.clear();
            paikkaFilter.clear();
            dataView.setFilter(null);
        });

        container.add(filterLayout, grid);
    }



}