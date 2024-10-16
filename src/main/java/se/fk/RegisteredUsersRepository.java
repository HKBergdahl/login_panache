package se.fk;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import java.util.Optional;

public class RegisteredUsersRepository {

    @Inject
    EntityManager entityManager;

    @Transactional
    public void registerUser(RegisteredUsers user) {
        entityManager.persist(user);
    }

    public Optional<RegisteredUsers> findByEmail(String email) {
        try {
            return Optional.of(entityManager.createQuery("SELECT u FROM RegisteredUsers u WHERE u.email = :email", RegisteredUsers.class)
                    .setParameter("email", email)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}