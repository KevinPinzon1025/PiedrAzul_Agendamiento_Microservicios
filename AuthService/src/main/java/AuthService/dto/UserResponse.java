package AuthService.dto;

import AuthService.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String login;
    private String documentNumber;
    private String firstName;
    private String secondName;
    private String firstLastName;
    private String secondLastName;
    private String phone;
    private String gender;
    private LocalDate birthDate;
    private String email;
    private Role role;
    private boolean active;
}
