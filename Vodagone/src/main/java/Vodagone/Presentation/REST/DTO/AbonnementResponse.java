package Vodagone.Presentation.REST.DTO;

import java.util.List;

public class AbonnementResponse {
    private List<AbonnementUser> abonnementen;
    private double totalPrice;

    public AbonnementResponse(List <AbonnementUser> abonnementen, double totalPrice) {
        this.abonnementen = abonnementen;
        this.totalPrice = totalPrice;
    }
}
