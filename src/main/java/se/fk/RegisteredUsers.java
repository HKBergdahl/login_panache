package se.fk;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "registeredusers")
public class RegisteredUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Primärnyckel genereras automatiskt
    private Long id;

    @Column(unique = true, nullable = false)  // Unik e-post och inte null
    private String email;

    @OneToMany(mappedBy = "registeredUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LoginAttempt> loginAttempts = new ArrayList<>();

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

    public Long getId() {
        return id; // Lägg till denna metod
    }

    public List<LoginAttempt> getLoginAttempts() {
        return loginAttempts;
    }

    // Method to add a LoginAttempt to the user
    public void addLoginAttempt() {
        LoginAttempt attempt = new LoginAttempt(this); // Create a new LoginAttempt with reference to this user
        loginAttempts.add(attempt); // Add to the list of login attempts
    }
}