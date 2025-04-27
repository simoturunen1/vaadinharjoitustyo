@Route("tapahtuma/:id")
@PageTitle("Tapahtuman tiedot")
public class TapahtumaDetailView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    @Autowired
    private TapahtumaService tapahtumaService;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Long tapahtumaId = Long.parseLong(event.getRouteParameters().get("id").orElse("0"));
        Tapahtuma t = tapahtumaService.findById(tapahtumaId);

        VerticalLayout layout = getContent();
        layout.add(new H2("Tapahtuman tiedot"));
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

        layout.add(takaisin);
    }
}
