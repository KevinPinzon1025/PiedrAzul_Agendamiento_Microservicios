package AuthService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenValidationResponse {
    private boolean valid;
    private String login;
    private String role;
}
