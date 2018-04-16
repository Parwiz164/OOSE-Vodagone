package Vodagone.Presentation.REST.DTO;

public class AbonneeResponse {
    private int id;
    private String name;
    private String email;

    public AbonneeResponse(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
