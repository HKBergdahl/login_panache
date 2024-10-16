package se.fk;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class LoginAttemptRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(LoginAttempt attempt) {
        entityManager.persist(attempt);
    }
}