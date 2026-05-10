package AuthService.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "El número de identificación es obligatorio")
    @Pattern(regexp = "^[0-9]{6,15}$", message = "El número de identificación debe tener entre 6 y 15 dígitos")
    @JsonAlias({ "documentNumber" })
    private String login;

}
