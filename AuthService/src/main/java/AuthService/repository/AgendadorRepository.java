package AuthService.repository;

import AuthService.entity.Agendador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendadorRepository extends JpaRepository<Agendador, Long> {
}
