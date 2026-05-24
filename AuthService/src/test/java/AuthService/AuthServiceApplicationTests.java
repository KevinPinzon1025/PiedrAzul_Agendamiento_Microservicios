package AuthService;

import AuthService.dto.RegisterRequest;
import AuthService.entity.Role;
import AuthService.entity.UserCredential;
import AuthService.repository.UserCredentialRepository;
import AuthService.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceApplicationTests {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Test
    void contextLoads() {
        assertNotNull(authService);
        assertNotNull(userCredentialRepository);
    }

    @Test
    void shouldRegisterPatientWithDocumentNumber() {
        String documentNumber = "123456789";

        userCredentialRepository.findByLogin(documentNumber)
                .ifPresent(userCredentialRepository::delete);

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setDocumentNumber(documentNumber);
        registerRequest.setFirstName("Maria");
        registerRequest.setSecondName("Elena");
        registerRequest.setFirstLastName("Gomez");
        registerRequest.setSecondLastName("Perez");
        registerRequest.setPhone("3001234567");
        registerRequest.setGender("Mujer");
        registerRequest.setBirthDate(LocalDate.of(1980, 5, 15));
        registerRequest.setEmail("maria@example.com");

        authService.register(registerRequest);

        UserCredential savedUser = userCredentialRepository.findByLogin(documentNumber)
                .orElseThrow();

        assertEquals(documentNumber, savedUser.getLogin());
        assertEquals(documentNumber, savedUser.getDocumentNumber());
        assertEquals("Maria", savedUser.getFirstName());
        assertEquals("Elena", savedUser.getSecondName());
        assertEquals("Gomez", savedUser.getFirstLastName());
        assertEquals("Perez", savedUser.getSecondLastName());
        assertEquals("3001234567", savedUser.getPhone());
        assertEquals("Mujer", savedUser.getGender());
        assertEquals(LocalDate.of(1980, 5, 15), savedUser.getBirthDate());
        assertEquals("maria@example.com", savedUser.getEmail());
        assertEquals(Role.PATIENT, savedUser.getRole());
        assertTrue(savedUser.isActive());
    }

    @Test
    void shouldRegisterPatientWithOptionalFieldsEmpty() {
        String documentNumber = "987654321";

        userCredentialRepository.findByLogin(documentNumber)
                .ifPresent(userCredentialRepository::delete);

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setDocumentNumber(documentNumber);
        registerRequest.setFirstName("Carlos");
        registerRequest.setSecondName(null);
        registerRequest.setFirstLastName("Ramirez");
        registerRequest.setSecondLastName(null);
        registerRequest.setPhone("3112223344");
        registerRequest.setGender("Hombre");
        registerRequest.setBirthDate(null);
        registerRequest.setEmail(null);

        authService.register(registerRequest);

        UserCredential savedUser = userCredentialRepository.findByLogin(documentNumber)
                .orElseThrow();

        assertEquals(documentNumber, savedUser.getLogin());
        assertEquals(documentNumber, savedUser.getDocumentNumber());
        assertEquals("Carlos", savedUser.getFirstName());
        assertNull(savedUser.getSecondName());
        assertEquals("Ramirez", savedUser.getFirstLastName());
        assertNull(savedUser.getSecondLastName());
        assertEquals("3112223344", savedUser.getPhone());
        assertEquals("Hombre", savedUser.getGender());
        assertNull(savedUser.getBirthDate());
        assertNull(savedUser.getEmail());
        assertEquals(Role.PATIENT, savedUser.getRole());
        assertTrue(savedUser.isActive());
    }
}