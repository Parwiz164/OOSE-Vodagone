package Vodagone.Presentation.REST.DTO;

public class SpecificAbonnementResponse {
    private int id;
    private String aanbieder;
    private String dienst;
    private String prijs;
    private String startDatum;
    private String verdubbeling;
    private Boolean deelbaar;
    private String status;

    public SpecificAbonnementResponse(int id, String aanbieder, String dienst, double prijs,
                                      String startDatum, String verdubbeling, Boolean deelbaar, String status) {
        this.id = id;
        this.aanbieder = aanbieder;
        this.dienst = dienst;
        this.prijs = "€ " + prijs + " ,- per maand";
        this.startDatum = startDatum;
        this.verdubbeling = verdubbeling;
        this.deelbaar = deelbaar;
        this.status = status;
    }
}
