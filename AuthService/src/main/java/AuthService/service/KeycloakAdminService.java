package AuthService.service;

import AuthService.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeycloakAdminService {

    private static final String PATIENT_ROLE = "PATIENT";

    private final WebClient.Builder webClientBuilder;

    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.admin-username:admin}")
    private String adminUsername;

    @Value("${keycloak.admin-password:admin}")
    private String adminPassword;

    public void createPatientUser(RegisterRequest request) {
        String accessToken = getAdminAccessToken();
        String username = cleanRequired(request.getDocumentNumber());

        try {
            String userId = createUser(accessToken, request, username);
            assignRealmRole(accessToken, userId, PATIENT_ROLE);
        } catch (WebClientResponseException.Conflict ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un usuario en Keycloak con ese número de documento");
        } catch (WebClientResponseException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Error creando usuario en Keycloak: " + ex.getResponseBodyAsString(),
                    ex);
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "No fue posible crear el usuario en Keycloak",
                    ex);
        }
    }

    private String getAdminAccessToken() {
        String tokenUrl = keycloakUrl + "/realms/master/protocol/openid-connect/token";

        LinkedMultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", "admin-cli");
        formData.add("username", adminUsername);
        formData.add("password", adminPassword);

        try {
            Map<?, ?> response = webClientBuilder.build()
                    .post()
                    .uri(tokenUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response == null || response.get("access_token") == null) {
                throw new ResponseStatusException(
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "Keycloak no devolvió token de administrador");
            }

            return response.get("access_token").toString();

        } catch (WebClientResponseException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "No fue posible obtener token admin de Keycloak: " + ex.getResponseBodyAsString(),
                    ex);
        }
    }

    private String createUser(String accessToken, RegisterRequest request, String username) {
        String usersUrl = keycloakUrl + "/admin/realms/" + realm + "/users";
        Map<String, Object> userPayload = buildUserPayload(request, username);

        HttpHeaders headers = webClientBuilder.build()
                .post()
                .uri(usersUrl)
                .headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userPayload)
                .retrieve()
                .toBodilessEntity()
                .block()
                .getHeaders();

        URI location = headers.getLocation();

        if (location != null) {
            String path = location.getPath();
            return path.substring(path.lastIndexOf('/') + 1);
        }

        return findUserIdByUsername(accessToken, username);
    }

    private Map<String, Object> buildUserPayload(RegisterRequest request, String username) {
        String email = cleanOptional(request.getEmail());

        String firstLastName = cleanRequired(request.getFirstLastName());
        String secondLastName = cleanOptional(request.getSecondLastName());

        String fullLastName = firstLastName;
        if (secondLastName != null) {
            fullLastName = fullLastName + " " + secondLastName;
        }

        Map<String, Object> payload = new LinkedHashMap<>();

        payload.put("username", username);
        payload.put("enabled", true);
        payload.put("firstName", cleanRequired(request.getFirstName()));
        payload.put("lastName", fullLastName);

        /*
         * IMPORTANTE:
         * Si el correo viene vacío, NO se envía a Keycloak.
         * Tampoco se envía emailVerified.
         * Esto evita que Keycloak deje la cuenta como "Account is not fully set up".
         */
        if (email != null) {
            payload.put("email", email);
            payload.put("emailVerified", true);
        }

        /*
         * IMPORTANTE:
         * Evita que Keycloak asigne acciones obligatorias como UPDATE_PROFILE,
         * VERIFY_EMAIL o UPDATE_PASSWORD.
         */
        payload.put("requiredActions", new ArrayList<>());

        payload.put("credentials", List.of(Map.of(
                "type", "password",
                "value", cleanRequired(request.getPassword()),
                "temporary", false)));

        Map<String, Object> attributes = new LinkedHashMap<>();
        attributes.put("documentNumber", List.of(username));
        attributes.put("phone", List.of(cleanRequired(request.getPhone())));
        attributes.put("gender", List.of(cleanRequired(request.getGender())));

        if (request.getBirthDate() != null) {
            attributes.put(
                    "birthDate",
                    List.of(request.getBirthDate().format(DateTimeFormatter.ISO_LOCAL_DATE)));
        }

        payload.put("attributes", attributes);

        return payload;
    }

    private String findUserIdByUsername(String accessToken, String username) {
        String searchUrl = keycloakUrl + "/admin/realms/" + realm + "/users?username=" + username + "&exact=true";

        List<?> users = webClientBuilder.build()
                .get()
                .uri(searchUrl)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(List.class)
                .block();

        if (users == null || users.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Keycloak creó el usuario pero no fue posible obtener su ID");
        }

        Object first = users.get(0);

        if (first instanceof Map<?, ?> user && user.get("id") != null) {
            return user.get("id").toString();
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_GATEWAY,
                "La respuesta de Keycloak no contiene el ID del usuario");
    }

    private void assignRealmRole(String accessToken, String userId, String roleName) {
        Map<?, ?> role = getRealmRole(accessToken, roleName);
        String assignUrl = keycloakUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm";

        webClientBuilder.build()
                .post()
                .uri(assignUrl)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(List.of(role))
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    private Map<?, ?> getRealmRole(String accessToken, String roleName) {
        String roleUrl = keycloakUrl + "/admin/realms/" + realm + "/roles/" + roleName;

        Map<?, ?> role = webClientBuilder.build()
                .get()
                .uri(roleUrl)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (role == null || role.get("id") == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "No fue posible encontrar el rol " + roleName + " en Keycloak");
        }

        return role;
    }

    private String cleanRequired(String value) {
        if (value == null) {
            return "";
        }

        return value.trim();
    }

    private String cleanOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}