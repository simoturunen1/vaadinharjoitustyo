package com.example.application.views.Asetukset;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import elemental.json.JsonArray;

@Route("asetukset")
@PageTitle("Asetukset")
@Menu(order = 4, icon = LineAwesomeIconUrl.PENCIL_RULER_SOLID)
@RolesAllowed("USER")
public class Asetukset extends VerticalLayout {

    public Asetukset() {
        Select<String> variTeema = new Select<>();
        variTeema.setLabel("Väriteema");
        variTeema.setItems("Vaalea", "Tumma", "Vihreä", "Pinkki");
        //variTeema.setValue("Vaalea");

        Select<String> fonttiValinta = new Select<>();
        fonttiValinta.setLabel("Fontti");
        fonttiValinta.setItems("Arial", "Roboto", "Courier New", "Georgia", "Coral Pixels", "Dancing Script");
        //fonttiValinta.setValue("Arial");

        UI.getCurrent().getPage().executeJs("""
            const teema = localStorage.getItem('teema-variteema') || 'Vaalea';
            const fontti = localStorage.getItem('teema-fontti') || 'Arial';
            return [teema, fontti];
                """).then(json -> {
            JsonArray array = (JsonArray) json;
            variTeema.setValue(array.getString(0));
            fonttiValinta.setValue(array.getString(1));
        });

        Button tallenna = new Button("Tallenna asetukset", e -> {
            String taustavari = "#ffffff";
            String tekstivari = "#000000";
            String buttonTausta = "#007bff";
            String buttonTeksti = "#ffffff";

            switch (variTeema.getValue()) {
                case "Tumma" -> {
                    taustavari = "#121212";
                    tekstivari = "#f0f0f0";
                    buttonTausta = "#5c5c5c";
                    buttonTeksti = "#ffffff";
                }
                case "Vihreä" -> {
                    taustavari = "#e9f5e9";
                    tekstivari = "#1b5e20";
                    buttonTausta = "#4caf50";
                    buttonTeksti = "#ffffff";
                }
                case "Pinkki" -> {
                    taustavari = "#ffe4f0";
                    tekstivari = "#880e4f";
                    buttonTausta = "#f06292";
                    buttonTeksti = "#ffffff";
                }
            }

            String js = String.format("""
                const root = document.documentElement;
                root.style.setProperty('--taustavari', '%s');
                root.style.setProperty('--tekstivari', '%s');
                root.style.setProperty('--painiketausta', '%s');
                root.style.setProperty('--painiketeksti', '%s');
                root.style.setProperty('--fontti', '%s');
                localStorage.setItem('teema-taustavari', '%s');
                localStorage.setItem('teema-tekstivari', '%s');
                localStorage.setItem('teema-painike-tausta', '%s');
                localStorage.setItem('teema-painike-teksti', '%s');
                localStorage.setItem('teema-fontti', '%s');
                localStorage.setItem('teema-variteema', '%s');
            """,
                    taustavari, tekstivari, buttonTausta, buttonTeksti, fonttiValinta.getValue(),
                    taustavari, tekstivari, buttonTausta, buttonTeksti, fonttiValinta.getValue(),
                    variTeema.getValue());

            UI.getCurrent().getPage().executeJs(js);
        });

        Button reset = new Button("Palauta oletukset", e -> {
            UI.getCurrent().getPage().executeJs("""
                const root = document.documentElement;
                localStorage.removeItem('teema-taustavari');
                localStorage.removeItem('teema-tekstivari');
                localStorage.removeItem('teema-painike-tausta');
                localStorage.removeItem('teema-painike-teksti');
                localStorage.removeItem('teema-fontti');
                localStorage.removeItem('teema-variteema');
                root.style.removeProperty('--taustavari');
                root.style.removeProperty('--tekstivari');
                root.style.removeProperty('--painiketausta');
                root.style.removeProperty('--painiketeksti');
                root.style.removeProperty('--fontti');
            """);
        });

        add(variTeema, fonttiValinta, tallenna, reset);
        setSpacing(true);
        setPadding(true);
    }
}
