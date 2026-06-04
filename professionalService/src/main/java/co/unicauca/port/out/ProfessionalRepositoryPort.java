package co.unicauca.port.out;

import co.unicauca.domain.model.Professional;
import co.unicauca.domain.valueobject.ProfessionalId;

import java.util.List;
import java.util.Optional;

public interface ProfessionalRepositoryPort {
    List<Professional> findAll();
    Optional<Professional> findById(ProfessionalId id);
    Professional save(Professional professional);
    void deleteById(ProfessionalId id);
    boolean existsById(ProfessionalId id);
}
