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
            if (repository.count() == 0) {
                repository.save(UserCredential.builder()
                        .login("admin")
                        .documentNumber("admin")
                        .active(true)
                        .firstName("Admin")
                        .firstLastName("PiedraAzul")
                        .phone("0000000000")
                        .gender("Otro")
                        .role(Role.ADMIN)
                        .build());
            }
        };
    }
}
