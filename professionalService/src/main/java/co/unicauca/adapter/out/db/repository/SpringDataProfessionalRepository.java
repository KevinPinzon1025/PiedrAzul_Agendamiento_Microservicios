package co.unicauca.adapter.out.db.repository;

import co.unicauca.adapter.out.db.model.ProfessionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataProfessionalRepository
        extends JpaRepository<ProfessionalEntity, Long> {
}
