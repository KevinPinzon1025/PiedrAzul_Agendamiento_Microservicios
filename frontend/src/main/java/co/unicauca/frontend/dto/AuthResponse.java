package co.unicauca.frontend.dto;

public class AuthResponse {
    private String token;
    private String tokenType;
    private Long expiresIn;
    private String login;
    private String role;
    private String fullName;

    public AuthResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullName() {
        return cleanName(fullName);
    }

    public void setFullName(String fullName) {
        this.fullName = cleanName(fullName);
    }

    public String getDisplayName() {
        String cleanedName = cleanName(fullName);
        if (cleanedName != null && !cleanedName.isBlank()) {
            return cleanedName;
        }

        String cleanedLogin = cleanName(login);
        if (cleanedLogin != null && !cleanedLogin.isBlank()) {
            return cleanedLogin;
        }

        return "Usuario";
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
