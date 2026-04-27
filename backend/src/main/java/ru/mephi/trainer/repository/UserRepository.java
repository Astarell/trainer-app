package ru.mephi.trainer.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import ru.mephi.trainer.entity.User;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<User, UUID> {

    public Optional<User> findByEmail(String email) {
        return find("email", email).singleResultOptional();
    }

    public boolean existsByEmail(String email) {
        return find("SELECT EXISTS(SELECT 1 FROM User WHERE email = ?1)", email)
                .project(Boolean.class)
                .firstResultOptional()
                .orElse(false);
    }
}
