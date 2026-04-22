package AuthService.util;

public final class PasswordPolicy {

    private PasswordPolicy() {
    }

    public static boolean isValid(String password) {
        if (password == null) return false;
        if (password.length() < 6) return false;
        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasSpecial = password.matches(".*[^a-zA-Z0-9].*");
        return hasUpper && hasDigit && hasSpecial;
    }

    public static String errorMessage() {
        return "La contraseña debe tener mínimo 6 caracteres, al menos 1 mayúscula, 1 dígito y 1 carácter especial.";
    }
}
