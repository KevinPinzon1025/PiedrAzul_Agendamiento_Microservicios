package co.unicauca.frontend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {

    private static final ObjectMapper CLAIMS_MAPPER = new ObjectMapper();

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("refresh_expires_in")
    private Long refreshExpiresIn;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private Long expiresIn;

    private String token;
    private String scope;
    private String login;
    private String role;
    private String fullName;

    private transient Map<String, Object> cachedClaims;

    public AuthResponse() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getRefreshExpiresIn() {
        return refreshExpiresIn;
    }

    public void setRefreshExpiresIn(Long refreshExpiresIn) {
        this.refreshExpiresIn = refreshExpiresIn;
    }

    /**
     * Compatibilidad con el frontend anterior.
     * Antes el backend devolvia "token"; Keycloak devuelve "access_token".
     */
    public String getToken() {
        if (token != null && !token.isBlank()) {
            return token;
        }
        return accessToken;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        if (tokenType == null || tokenType.isBlank()) {
            return "Bearer";
        }
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getLogin() {
        String cleanedLogin = cleanName(login);
        if (cleanedLogin != null) {
            return cleanedLogin;
        }

        Object preferredUsername = getClaim("preferred_username");
        if (preferredUsername != null && !preferredUsername.toString().isBlank()) {
            return preferredUsername.toString().trim();
        }

        Object subject = getClaim("sub");
        return subject == null ? null : subject.toString().trim();
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRole() {
        String cleanedRole = normalizeRole(role);
        if (cleanedRole != null) {
            return cleanedRole;
        }

        Object realmAccessObject = getClaim("realm_access");
        if (realmAccessObject instanceof Map<?, ?> realmAccess) {
            Object rolesObject = realmAccess.get("roles");
            if (rolesObject instanceof List<?> roles) {
                for (Object roleObject : roles) {
                    String candidate = normalizeRole(roleObject == null ? null : roleObject.toString());
                    if (candidate == null) {
                        continue;
                    }

                    if ("PATIENT".equals(candidate)
                            || "PROFESSIONAL".equals(candidate)
                            || "SCHEDULER".equals(candidate)
                            || "ADMIN".equals(candidate)) {
                        return candidate;
                    }
                }
            }
        }

        return null;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullName() {
        String cleanedFullName = cleanName(fullName);
        if (cleanedFullName != null) {
            return cleanedFullName;
        }

        Object name = getClaim("name");
        String cleanedName = cleanName(name == null ? null : name.toString());
        if (cleanedName != null) {
            return cleanedName;
        }

        Object givenName = getClaim("given_name");
        Object familyName = getClaim("family_name");
        String combined = ((givenName == null ? "" : givenName.toString())
                + " "
                + (familyName == null ? "" : familyName.toString())).trim();

        return cleanName(combined);
    }

    public void setFullName(String fullName) {
        this.fullName = cleanName(fullName);
    }

    public String getDisplayName() {
        String cleanedName = getFullName();
        if (cleanedName != null && !cleanedName.isBlank()) {
            return cleanedName;
        }

        String cleanedLogin = getLogin();
        if (cleanedLogin != null && !cleanedLogin.isBlank()) {
            return cleanedLogin;
        }

        return "Usuario";
    }

    public String getDocumentNumber() {
        Object documentNumber = getClaim("documentNumber");
        if (documentNumber != null && !documentNumber.toString().isBlank()) {
            return documentNumber.toString().trim();
        }

        Object documentNumbers = getClaim("documentNumbers");
        if (documentNumbers instanceof List<?> values && !values.isEmpty()) {
            Object firstValue = values.get(0);
            if (firstValue != null && !firstValue.toString().isBlank()) {
                return firstValue.toString().trim();
            }
        }

        String cleanedLogin = getLogin();
        if (cleanedLogin != null && cleanedLogin.matches("\\d+")) {
            return cleanedLogin;
        }

        return null;
    }

    private Object getClaim(String claimName) {
        Map<String, Object> claims = getClaims();
        if (claims == null) {
            return null;
        }
        return claims.get(claimName);
    }

    private Map<String, Object> getClaims() {
        if (cachedClaims != null) {
            return cachedClaims;
        }

        String jwt = getToken();
        if (jwt == null || jwt.isBlank()) {
            return null;
        }

        String[] parts = jwt.split("\\.");
        if (parts.length < 2) {
            return null;
        }

        try {
            byte[] decodedPayload = Base64.getUrlDecoder().decode(parts[1]);
            String payloadJson = new String(decodedPayload, StandardCharsets.UTF_8);
            cachedClaims = CLAIMS_MAPPER.readValue(payloadJson, new TypeReference<Map<String, Object>>() {});
            return cachedClaims;
        } catch (Exception ex) {
            return null;
        }
    }

    private String normalizeRole(String value) {
        if (value == null) {
            return null;
        }

        String cleaned = value.trim().toUpperCase();
        if (cleaned.startsWith("ROLE_")) {
            cleaned = cleaned.substring(5);
        }

        return cleaned.isBlank() ? null : cleaned;
    }

    private String cleanName(String value) {
        if (value == null) {
            return null;
        }

        String cleaned = value
                .replaceAll("(?i)\\bnull\\b", "")
                .replaceAll("\\s+", " ")
                .trim();

        return cleaned.isBlank() ? null : cleaned;
    }
}
