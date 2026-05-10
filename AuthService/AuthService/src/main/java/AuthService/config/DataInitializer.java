package AuthService.config;

import AuthService.entity.Role;
import AuthService.entity.UserCredential;
import AuthService.repository.UserCredentialRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

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
        if (repository.existsByLogin(login)) {
            return;
        }

        repository.save(UserCredential.builder()
                .login(login)
                .documentNumber(login)
                .active(true)
                .firstName(firstName)
                .firstLastName(firstLastName)
                .phone("0000000000")
                .gender("Otro")
                .role(role)
                .build());
    }
}
