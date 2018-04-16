package Vodagone.Presentation.REST.DTO;

public class AbonnementUser {
    private int id;
    private String aanbieder;
    private String dienst;

    public AbonnementUser(int id, String aanbieder, String dienst) {
        this.id = id;
        this.aanbieder = aanbieder;
        this.dienst = dienst;
    }
}
