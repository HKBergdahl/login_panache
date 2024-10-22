package se.fk;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
@Table(name = "registeredusers")
public class RegisteredUsers extends PanacheEntity {


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