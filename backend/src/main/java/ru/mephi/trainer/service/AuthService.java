package ru.mephi.trainer.service;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mephi.trainer.entity.User;
import ru.mephi.trainer.enums.UserRole;
import ru.mephi.trainer.exception.EmailAlreadyExistsException;
import ru.mephi.trainer.exception.FailedLoginException;
import ru.mephi.trainer.repository.UserRepository;
import ru.mephi.trainer.rest.dto.request.RegistrationRequest;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;

@Slf4j
@ApplicationScoped
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public User register(RegistrationRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed - email already exists: {}", request.getEmail());
            throw new EmailAlreadyExistsException("User with this email already exists");
        }

        String passwordHash = BcryptUtil.bcryptHash(request.getPassword());

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .appRole(UserRole.APP_USER)
                .passwordHash(passwordHash)
                .build();

        userRepository.persist(user);
        log.info("User registered successfully with id: {}", user.getId());

        return user;
    }

    public User authenticate(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty() || !BcryptUtil.matches(password, user.get().getPasswordHash())) {
            log.warn("Failed login attempt for email: {}", email);
            throw new FailedLoginException("Invalid email or password");
        }
        return user.get();
    }

    public String generateToken(User user) {
        return Jwt.issuer("trainer-app")
                .upn(user.getEmail())
                .groups(Set.of(user.getAppRole().getSecurityRole()))
                .claim("userId", user.getId())
                .expiresAt(Instant.now().plusSeconds(3600).getEpochSecond()) // 1 час
                .sign();
    }
}
