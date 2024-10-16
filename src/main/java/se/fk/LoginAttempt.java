package se.fk;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "login_attempts")
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private LocalDateTime timestamp;

    // Defaultkonstruktor (behövs av Hibernate)
    public LoginAttempt() {
    }

    public LoginAttempt(String email) {
        this.email = email;
        this.timestamp = LocalDateTime.now(); // Sätter tidsstämpeln vid skapande
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}