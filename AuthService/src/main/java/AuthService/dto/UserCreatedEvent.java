package AuthService.dto;

import AuthService.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedEvent {
    private String eventType;
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
    private LocalDateTime occurredAt;
}
