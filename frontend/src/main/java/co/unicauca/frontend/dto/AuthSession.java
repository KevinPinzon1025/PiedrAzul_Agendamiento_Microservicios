package co.unicauca.frontend.dto;

public final class AuthSession {
    private static AuthResponse currentUser;

    private AuthSession() {
    }

    public static void setCurrentUser(AuthResponse authResponse) {
        currentUser = authResponse;
    }

    public static AuthResponse getCurrentUser() {
        return currentUser;
    }

    public static void clear() {
        currentUser = null;
    }

    public static String getAuthorizationHeader() {
        if (currentUser == null || currentUser.getToken() == null || currentUser.getToken().isBlank()) {
            return null;
        }
        String tokenType = currentUser.getTokenType() == null || currentUser.getTokenType().isBlank()
                ? "Bearer"
                : currentUser.getTokenType();
        return tokenType + " " + currentUser.getToken();
    }
}
