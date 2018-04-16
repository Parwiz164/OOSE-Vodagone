package Vodagone.Presentation.REST.DTO;

public class LoginResponse {
    private String user;
    private String token;

    public LoginResponse(String token, String user) {
        this.token = token;
        this.user = user;
    }
}
