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

    @Bean
    CommandLineRunner seedUsers(UserCredentialRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                String salt = PasswordHasher.newSaltBase64();
                repository.save(UserCredential.builder()
                        .login("admin")
                        .passwordSalt(salt)
                        .passwordHash(PasswordHasher.hashBase64("Admin1*".toCharArray(), salt))
                        .active(true)
                        .firstName("Admin")
                        .firstLastName("PiedraAzul")
                        .role(Role.ADMIN)
                        .build());
            }
        };
    }
}
