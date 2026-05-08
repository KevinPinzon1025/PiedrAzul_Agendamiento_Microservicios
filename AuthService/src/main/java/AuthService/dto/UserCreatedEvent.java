package AuthService.dto;

import AuthService.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String firstName;
    private String firstLastName;
    private Role role;
    private boolean active;
    private LocalDateTime occurredAt;
}
