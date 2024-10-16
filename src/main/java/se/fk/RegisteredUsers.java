package se.fk;

import jakarta.persistence.*;

@Entity
@Table(name = "registered_users")
public class RegisteredUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)  // Unik e-post och inte null
    private String email;

    // Defaultkonstruktor (krävs av Hibernate)
    public RegisteredUsers() {
    }

    // Konstruktor för att skapa användare med en e-postadress
    public RegisteredUsers(String email) {
        this.email = email;
    }

    // Getter och setter för e-post
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}