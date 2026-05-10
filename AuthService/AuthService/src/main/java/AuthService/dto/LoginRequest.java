package AuthService.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "El usuario o número de identificación es obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9]{3,30}$", message = "El usuario debe tener entre 3 y 30 caracteres y solo puede contener letras o números")
    @JsonAlias({ "documentNumber" })
    private String login;

}
