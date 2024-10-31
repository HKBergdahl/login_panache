package se.fk;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.Optional;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class RegisteredUsersRepository  {

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public void registerUser(RegisteredUsers user) {
        entityManager.persist(user);
    }


    // Hitta användaren baserat på email med HQL
    public Optional<RegisteredUsers> findByEmail(String email) {
        return entityManager.createQuery("FROM RegisteredUsers WHERE email = :email", RegisteredUsers.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    // Metoden behövs eftersom vi inte använder Panache måste vi skapa en egen metod som hämtar användare baserat på Id.
    public Optional<RegisteredUsers> findById(Long id) {
        return Optional.ofNullable(entityManager.find(RegisteredUsers.class, id));
    }

}