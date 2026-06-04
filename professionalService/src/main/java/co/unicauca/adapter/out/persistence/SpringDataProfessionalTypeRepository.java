package co.unicauca.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataProfessionalTypeRepository extends JpaRepository<ProfessionalTypeEntity, Long> {
    Optional<ProfessionalTypeEntity> findByName(String name);
}
