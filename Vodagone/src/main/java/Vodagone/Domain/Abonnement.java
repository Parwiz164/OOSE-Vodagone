package Vodagone.Domain;

public class Abonnement {
    private int id;
    private String aanbieder;
    private String dienst;
    private double prijs;
    private String startDatum;
    private String verdubbeling;
    private Boolean isDeelbaar;
    private String status;

    public Abonnement(int id, String aanbieder, String dienst, double prijs, String startDatum, String verdubbeling, Boolean deelbaar, String status) {
        this.id = id;
        this.aanbieder = aanbieder;
        this.dienst = dienst;
        this.prijs = prijs;
        this.startDatum = startDatum;
        this.verdubbeling = verdubbeling;
        this.isDeelbaar = deelbaar;
        this.status = status;
    }

    public Abonnement(int id, String aanbieder, String dienst) {
        this.id = id;
        this.aanbieder = aanbieder;
        this.dienst = dienst;
    }

    public int getId() {
        return id;
    }

    public String getAanbieder() {
        return aanbieder;
    }

    public String getDienst() {
        return dienst;
    }

    public double getPrijs() {
        return prijs;
    }

    public String getStartDatum() {
        return startDatum;
    }

    public String getVerdubbeling() {
        return verdubbeling;
    }

    public Boolean isDeelbaar() {
        return isDeelbaar;
    }

    public String getStatus() {
        return status;
    }
}
