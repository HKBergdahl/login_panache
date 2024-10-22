package se.fk;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class LoginAttemptRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void save(LoginAttempt attempt) {
        entityManager.persist(attempt);
    }
}