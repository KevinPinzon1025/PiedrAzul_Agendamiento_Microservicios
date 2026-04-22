package AuthService.repository;

import AuthService.entity.UserCredential;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {
    Optional<UserCredential> findByLogin(String login);
    boolean existsByLogin(String login);
}
