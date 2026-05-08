package AuthService.dto;

import AuthService.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String login;
    private String firstName;
    private String firstLastName;
    private Role role;
    private boolean active;
}
