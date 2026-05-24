package AuthService.config;

import AuthService.entity.Role;
import AuthService.entity.UserCredential;
import AuthService.repository.UserCredentialRepository;
import AuthService.util.PasswordHasher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    public static final String DEFAULT_PASSWORD = "123456";

    @Bean
    CommandLineRunner seedUsers(UserCredentialRepository repository) {
        return args -> {
            createIfMissing(repository, "123", "Admin", "PiedraAzul", Role.ADMIN);
            createIfMissing(repository, "124", "Agendador", "PiedraAzul", Role.SCHEDULER);
            createIfMissing(repository, "125", "Medico", "PiedraAzul", Role.PROFESSIONAL);
        };
    }

    private void createIfMissing(UserCredentialRepository repository, String login, String firstName,
            String firstLastName, Role role) {
        String salt = PasswordHasher.newSaltBase64();
        String passwordHash = PasswordHasher.hashBase64(DEFAULT_PASSWORD.toCharArray(), salt);

        repository.findByLogin(login).ifPresentOrElse(existing -> {
            // Los usuarios semilla siempre quedan con la contraseña conocida 123456.
            // Esto evita problemas cuando ya existe un volumen antiguo de Docker.
            existing.setPasswordSalt(salt);
            existing.setPasswordHash(passwordHash);
            existing.setActive(true);
            existing.setRole(role);
            if (existing.getDocumentNumber() == null || existing.getDocumentNumber().isBlank()) {
                existing.setDocumentNumber(login);
            }
            if (existing.getFirstName() == null || existing.getFirstName().isBlank()) {
                existing.setFirstName(firstName);
            }
            if (existing.getFirstLastName() == null || existing.getFirstLastName().isBlank()) {
                existing.setFirstLastName(firstLastName);
            }
            repository.save(existing);
        }, () -> repository.save(UserCredential.builder()
                .login(login)
                .documentNumber(login)
                .active(true)
                .passwordSalt(salt)
                .passwordHash(passwordHash)
                .firstName(firstName)
                .firstLastName(firstLastName)
                .phone("0000000000")
                .gender("Otro")
                .role(role)
                .build()));
    }
}
