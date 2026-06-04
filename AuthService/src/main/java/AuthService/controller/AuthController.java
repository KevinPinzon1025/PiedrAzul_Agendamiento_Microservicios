package AuthService.controller;

import AuthService.dto.KeycloakTokenResponse;
import AuthService.dto.LoginRequest;
import AuthService.dto.RegisterRequest;
import AuthService.dto.TokenValidationResponse;
import AuthService.dto.UserResponse;
import AuthService.service.AuthService;
import AuthService.service.KeycloakAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final KeycloakAuthService keycloakAuthService;
    private final JwtDecoder jwtDecoder;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/register/patient")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerPatient(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public KeycloakTokenResponse login(@Valid @RequestBody LoginRequest request) {
        return keycloakAuthService.login(request);
    }

    @GetMapping("/validate")
    public TokenValidationResponse validate(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.replace("Bearer ", "").trim();
            Jwt jwt = jwtDecoder.decode(token);

            return TokenValidationResponse.builder()
                    .valid(true)
                    .login(resolveLogin(jwt))
                    .role(resolveRoles(jwt))
                    .build();
        } catch (Exception ex) {
            return TokenValidationResponse.builder()
                    .valid(false)
                    .build();
        }
    }

    private String resolveLogin(Jwt jwt) {
        String preferredUsername = jwt.getClaimAsString("preferred_username");
        return preferredUsername != null ? preferredUsername : jwt.getSubject();
    }

    private String resolveRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null) {
            return null;
        }

        Object rolesObject = realmAccess.get("roles");
        if (rolesObject instanceof Collection<?> roles) {
            return roles.toString();
        }

        return null;
    }
}
