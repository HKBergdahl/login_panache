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

    public LoginAttempt findByToken(String token) {
        return entityManager.createQuery("SELECT la FROM LoginAttempt la WHERE la.token = :token", LoginAttempt.class)
                .setParameter("token", token)
                .getSingleResult(); // eller .getResultList() och hantera tomma resultat
    }
}