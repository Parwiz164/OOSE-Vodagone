package Vodagone.Domain;

public class User {
    private int id;
    private String username;
    private String naam;
    private String email;

    public User(int id, String username, String naam, String email) {
        this.id = id;
        this.username = username;
        this.naam = naam;
        this.email = email;
    }

    public User(int id, String naam) {
        this.id = id;
        this.naam = naam;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getNaam() {
        return naam;
    }
}
