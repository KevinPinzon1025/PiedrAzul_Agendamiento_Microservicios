package AuthService.util;

public final class PasswordPolicy {

    private PasswordPolicy() {
    }

    public static boolean isValid(String password) {
        return password != null && password.length() >= 6;
    }

    public static String errorMessage() {
        return "La contraseña debe tener mínimo 6 caracteres.";
    }
}
