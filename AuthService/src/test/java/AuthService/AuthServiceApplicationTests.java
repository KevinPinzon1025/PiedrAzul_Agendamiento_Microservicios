package AuthService;

import static org.assertj.core.api.Assertions.assertThat;

import AuthService.dto.LoginRequest;
import AuthService.dto.RegisterRequest;
import AuthService.entity.Role;
import AuthService.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthServiceApplicationTests {

    @Autowired
    private AuthService authService;

    @Test
    void shouldRegisterAndLoginUser() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setLogin("paciente1");
        registerRequest.setPassword("Paciente1*");
        registerRequest.setFirstName("Kevin");
        registerRequest.setFirstLastName("Santiago");
        registerRequest.setRole(Role.PATIENT);
        registerRequest.setActive(true);

        authService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("paciente1");
        loginRequest.setPassword("Paciente1*");

        assertThat(authService.login(loginRequest).getToken()).isNotBlank();
    }

    @Test
    void shouldPreserveUnicodeNames() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setLogin("muñoz1");
        registerRequest.setPassword("Munoz1*");
        registerRequest.setFirstName("Peña");
        registerRequest.setFirstLastName("Muñoz");
        registerRequest.setRole(Role.PATIENT);
        registerRequest.setActive(true);

        var response = authService.register(registerRequest);

        assertThat(response.getFirstName()).isEqualTo("Peña");
        assertThat(response.getFirstLastName()).isEqualTo("Muñoz");

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin("muñoz1");
        loginRequest.setPassword("Munoz1*");

        assertThat(authService.login(loginRequest).getFullName()).isEqualTo("Peña Muñoz");
    }
}
