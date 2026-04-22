package AuthService.service;

import AuthService.dto.AuthResponse;
import AuthService.dto.LoginRequest;
import AuthService.dto.RegisterRequest;
import AuthService.dto.TokenValidationResponse;
import AuthService.dto.UserResponse;
import AuthService.entity.UserCredential;
import AuthService.repository.UserCredentialRepository;
import AuthService.util.JwtService;
import AuthService.util.PasswordHasher;
import AuthService.util.PasswordPolicy;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserCredentialRepository repository;
    private final JwtService jwtService;

    public UserResponse register(RegisterRequest request) {
        if (!PasswordPolicy.isValid(request.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, PasswordPolicy.errorMessage());
        }

        if (repository.existsByLogin(request.getLogin())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un usuario con ese login");
        }

        String salt = PasswordHasher.newSaltBase64();
        String hash = PasswordHasher.hashBase64(request.getPassword().toCharArray(), salt);

        UserCredential saved = repository.save(UserCredential.builder()
                .login(request.getLogin())
                .passwordHash(hash)
                .passwordSalt(salt)
                .active(request.isActive())
                .firstName(request.getFirstName())
                .firstLastName(request.getFirstLastName())
                .role(request.getRole())
                .build());

        return mapToResponse(saved);
    }

    public AuthResponse login(LoginRequest request) {
        UserCredential user = repository.findByLogin(request.getLogin())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario está inactivo");
        }

        if (!PasswordHasher.verify(request.getPassword(), user.getPasswordSalt(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        return AuthResponse.builder()
                .token(jwtService.generateToken(user))
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationMs() / 1000)
                .login(user.getLogin())
                .role(user.getRole().name())
                .fullName(user.getFirstName() + " " + user.getFirstLastName())
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

    private UserResponse mapToResponse(UserCredential user) {
        return UserResponse.builder()
                .id(user.getId())
                .login(user.getLogin())
                .firstName(user.getFirstName())
                .firstLastName(user.getFirstLastName())
                .role(user.getRole())
                .active(user.isActive())
                .build();
    }
}
