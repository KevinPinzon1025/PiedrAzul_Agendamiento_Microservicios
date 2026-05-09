package AuthService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterRequest {
    private static final String ONLY_LETTERS_MESSAGE = "Solo se permiten letras y espacios";

    @NotBlank(message = "El número de documento es obligatorio")
    @Pattern(regexp = "^[0-9]{6,15}$", message = "El número de documento debe tener entre 6 y 15 dígitos")
    private String documentNumber;

    @NotBlank(message = "El primer nombre es obligatorio")
    @Size(min = 2, max = 80, message = "El primer nombre debe tener entre 2 y 80 caracteres")
    @Pattern(regexp = "^[\\p{L} ]+$", message = ONLY_LETTERS_MESSAGE)
    private String firstName;

    @Size(max = 80, message = "El segundo nombre no debe superar 80 caracteres")
    @Pattern(regexp = "^$|^[\\p{L} ]+$", message = ONLY_LETTERS_MESSAGE)
    private String secondName;

    @NotBlank(message = "El primer apellido es obligatorio")
    @Size(min = 2, max = 80, message = "El primer apellido debe tener entre 2 y 80 caracteres")
    @Pattern(regexp = "^[\\p{L} ]+$", message = ONLY_LETTERS_MESSAGE)
    private String firstLastName;

    @Size(max = 80, message = "El segundo apellido no debe superar 80 caracteres")
    @Pattern(regexp = "^$|^[\\p{L} ]+$", message = ONLY_LETTERS_MESSAGE)
    private String secondLastName;

    @NotBlank(message = "El celular es obligatorio")
    @Pattern(regexp = "^[0-9]{7,15}$", message = "El celular debe tener entre 7 y 15 dígitos")
    private String phone;

    @NotBlank(message = "El género es obligatorio")
    @Pattern(regexp = "^(Hombre|Mujer|Otro)$", message = "El género debe ser Hombre, Mujer u Otro")
    private String gender;

    @Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
    private LocalDate birthDate;

    @Email(message = "El correo electrónico no tiene un formato válido")
    @Size(max = 120, message = "El correo electrónico no debe superar 120 caracteres")
    private String email;
}
