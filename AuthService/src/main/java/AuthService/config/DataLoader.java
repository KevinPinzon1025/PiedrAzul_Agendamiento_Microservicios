package AuthService.config;

import AuthService.entity.Administrador;
import AuthService.entity.Agendador;
import AuthService.entity.Role;
import AuthService.repository.AdministradorRepository;
import AuthService.repository.AgendadorRepository;
import AuthService.util.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private AgendadorRepository agendadorRepository;

    @Autowired
    private AdministradorRepository administradorRepository;

    @Override
    public void run(String... args) {

        if (agendadorRepository.count() == 0) {
            String salt1 = PasswordHasher.newSaltBase64();
            agendadorRepository.save(Agendador.builder()
                    .login("agendador.maria")
                    .passwordHash(PasswordHasher.hashBase64("TestPass1*".toCharArray(), salt1))
                    .passwordSalt(salt1)
                    .active(true)
                    .firstName("María")
                    .firstLastName("García")
                    .role(Role.SCHEDULER)
                    .build());

            String salt2 = PasswordHasher.newSaltBase64();
            agendadorRepository.save(Agendador.builder()
                    .login("agendador.juan")
                    .passwordHash(PasswordHasher.hashBase64("TestPass1*".toCharArray(), salt2))
                    .passwordSalt(salt2)
                    .active(true)
                    .firstName("Juan")
                    .firstLastName("Martínez")
                    .role(Role.SCHEDULER)
                    .build());
        }

        if (administradorRepository.count() == 0) {
            String salt3 = PasswordHasher.newSaltBase64();
            administradorRepository.save(Administrador.builder()
                    .login("admin.carlos")
                    .passwordHash(PasswordHasher.hashBase64("AdminPass1*".toCharArray(), salt3))
                    .passwordSalt(salt3)
                    .active(true)
                    .firstName("Carlos")
                    .firstLastName("López")
                    .role(Role.ADMIN)
                    .build());

            String salt4 = PasswordHasher.newSaltBase64();
            administradorRepository.save(Administrador.builder()
                    .login("admin.ana")
                    .passwordHash(PasswordHasher.hashBase64("AdminPass1*".toCharArray(), salt4))
                    .passwordSalt(salt4)
                    .active(true)
                    .firstName("Ana")
                    .firstLastName("Rodríguez")
                    .role(Role.ADMIN)
                    .build());
        }
    }
}