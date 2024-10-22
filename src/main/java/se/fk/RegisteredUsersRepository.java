package se.fk;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class RegisteredUsersRepository implements PanacheRepository<RegisteredUsers> {

    @Transactional
    public void registerUser(RegisteredUsers user) {
        persist(user); // Använd Panache för att spara användaren
    }

    // Renodlad panachefråga men som inte fungerar då den skapar en felaktig HQL-fråga
    //
//    public Optional<RegisteredUsers> findByEmail(String email) {
//        return find("email", email).firstResultOptional(); // Hitta användaren baserat på email
//    }

    // Hitta användaren baserat på email med HQL
    public Optional<RegisteredUsers> findByEmail(String email) {
        // Använd HQL för att hämta användaren
        return getEntityManager()
                .createQuery("FROM RegisteredUsers WHERE email = :email", RegisteredUsers.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

//    public Optional<RegisteredUsers> findByEmail(String email) {
//        System.out.println("Finding user with email: " + email);
//        Optional<RegisteredUsers> user = find("email", email).firstResultOptional();
//        if (user.isPresent()) {
//            System.out.println("User found: " + user.get().getEmail());
//        } else {
//            System.out.println("User not found");
//        }
//        return user;
//    }
}