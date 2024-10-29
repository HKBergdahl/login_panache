package se.fk;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "login_attempts")
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // Indicates that many LoginAttempts can belong to one RegisteredUser
    @JoinColumn(name = "registered_user_id", nullable = false)
    private RegisteredUsers registeredUser;

    @Column(nullable = true)
    private String email;

    @Column(nullable = false, unique = true) // Token ska vara unikt
    private String token; // Nytt fält för att lagra token

    private LocalDateTime timestamp;

    // Defaultkonstruktor (behövs av Hibernate)
    public LoginAttempt() {
    }

    public LoginAttempt(RegisteredUsers registeredUser, String token) {
        this.email = registeredUser.getEmail();
        this.registeredUser = registeredUser; // Set the registered user
        this.token = token;
        this.timestamp = LocalDateTime.now(); // Set the timestamp when created
    }

    public LoginAttempt(RegisteredUsers registeredUser) {
        this.email = registeredUser.getEmail();
        this.registeredUser = registeredUser; // Set the registered user
        this.timestamp = LocalDateTime.now(); // Set the timestamp when created
    }

    public String getEmail() {
        return email;
    }

public RegisteredUsers getRegisteredUser() {
    return registeredUser;
}

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getToken() {
        return token; // Getter för token
    }
}