package AuthService.service;

import AuthService.dto.RegisterRequest;
import AuthService.dto.UserCreatedEvent;
import AuthService.dto.UserResponse;
import AuthService.entity.Role;
import AuthService.entity.UserCredential;
import AuthService.repository.UserCredentialRepository;
import AuthService.messaging.AuthEventPublisher;
import AuthService.util.PasswordHasher;
import AuthService.util.PasswordPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserCredentialRepository repository;
    private final AuthEventPublisher authEventPublisher;
    private final KeycloakAdminService keycloakAdminService;

    @Transactional
    public UserResponse register(RegisterRequest request) {

        String documentNumber = request.getDocumentNumber().trim();
        if (repository.existsByLogin(documentNumber)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con ese número de documento");
        }

        if (!PasswordPolicy.isValid(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PasswordPolicy.errorMessage());
        }

        // Primero se crea el usuario en Keycloak. Así el paciente puede iniciar sesión con su número de documento.
        keycloakAdminService.createPatientUser(request);

        String salt = PasswordHasher.newSaltBase64();
        String passwordHash = PasswordHasher.hashBase64(request.getPassword().toCharArray(), salt);

        UserCredential saved = repository.save(UserCredential.builder()
                .login(documentNumber)
                .documentNumber(documentNumber)
                .active(true)
                .passwordSalt(salt)
                .passwordHash(passwordHash)
                .firstName(cleanRequired(request.getFirstName()))
                .secondName(cleanOptional(request.getSecondName()))
                .firstLastName(cleanRequired(request.getFirstLastName()))
                .secondLastName(cleanOptional(request.getSecondLastName()))
                .phone(cleanRequired(request.getPhone()))
                .gender(cleanRequired(request.getGender()))
                .birthDate(request.getBirthDate())
                .email(cleanOptional(request.getEmail()))
                .role(Role.PATIENT)
                .build());

        authEventPublisher.publishUserCreated(mapToUserCreatedEvent(saved));

        return mapToResponse(saved);
    }


    private UserCreatedEvent mapToUserCreatedEvent(UserCredential user) {
        return UserCreatedEvent.builder()
                .eventType("USER_CREATED")
                .id(user.getId())
                .login(user.getLogin())
                .documentNumber(user.getDocumentNumber())
                .firstName(user.getFirstName())
                .secondName(user.getSecondName())
                .firstLastName(user.getFirstLastName())
                .secondLastName(user.getSecondLastName())
                .phone(user.getPhone())
                .gender(user.getGender())
                .birthDate(user.getBirthDate())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.isActive())
                .occurredAt(LocalDateTime.now())
                .build();
    }

    private UserResponse mapToResponse(UserCredential user) {
        return UserResponse.builder()
                .id(user.getId())
                .login(user.getLogin())
                .documentNumber(user.getDocumentNumber())
                .firstName(user.getFirstName())
                .secondName(user.getSecondName())
                .firstLastName(user.getFirstLastName())
                .secondLastName(user.getSecondLastName())
                .phone(user.getPhone())
                .gender(user.getGender())
                .birthDate(user.getBirthDate())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.isActive())
                .build();
    }


    private String cleanRequired(String value) {
        return value == null ? null : value.trim();
    }

    private String cleanOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
