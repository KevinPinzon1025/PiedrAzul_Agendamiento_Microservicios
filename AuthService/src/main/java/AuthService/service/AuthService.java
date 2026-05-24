package AuthService.service;

import AuthService.dto.AuthResponse;
import AuthService.dto.LoginRequest;
import AuthService.dto.RegisterRequest;
import AuthService.dto.TokenValidationResponse;
import AuthService.dto.UserCreatedEvent;
import AuthService.dto.UserResponse;
import AuthService.entity.Role;
import AuthService.entity.UserCredential;
import AuthService.repository.UserCredentialRepository;
import AuthService.messaging.AuthEventPublisher;
import AuthService.util.JwtService;
import AuthService.util.PasswordHasher;
import AuthService.util.PasswordPolicy;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserCredentialRepository repository;
    private final JwtService jwtService;
    private final AuthEventPublisher authEventPublisher;

    public UserResponse register(RegisterRequest request) {

        String documentNumber = request.getDocumentNumber().trim();
        if (repository.existsByLogin(documentNumber)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con ese número de documento");
        }

        if (!PasswordPolicy.isValid(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PasswordPolicy.errorMessage());
        }

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

    public AuthResponse login(LoginRequest request) {
        UserCredential user = repository.findByLogin(request.getLogin().trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario está inactivo");
        }

        if (user.getPasswordSalt() == null || user.getPasswordHash() == null
                || !PasswordHasher.verify(request.getPassword(), user.getPasswordSalt(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        return AuthResponse.builder()
                .token(jwtService.generateToken(user))
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationMs() / 1000)
                .login(user.getLogin())
                .role(user.getRole().name())
                .fullName(buildFullName(user))
                .build();
    }

    public TokenValidationResponse validateToken(String token) {
        if (!jwtService.isTokenValid(token)) {
            return TokenValidationResponse.builder().valid(false).build();
        }

        Claims claims = jwtService.extractAllClaims(token);
        return TokenValidationResponse.builder()
                .valid(true)
                .login(claims.getSubject())
                .role(String.valueOf(claims.get("role")))
                .build();
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

    private String buildFullName(UserCredential user) {
        return String.join(" ",
                cleanOptional(user.getFirstName()),
                cleanOptional(user.getSecondName()),
                cleanOptional(user.getFirstLastName()),
                cleanOptional(user.getSecondLastName()))
                .replaceAll("\\s+", " ")
                .trim();
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
