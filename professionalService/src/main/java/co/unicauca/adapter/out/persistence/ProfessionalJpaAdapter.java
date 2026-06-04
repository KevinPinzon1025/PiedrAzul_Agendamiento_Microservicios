package co.unicauca.adapter.out.persistence;

import co.unicauca.domain.model.Professional;
import co.unicauca.domain.valueobject.ProfessionalId;
import co.unicauca.port.out.ProfessionalRepositoryPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProfessionalJpaAdapter implements ProfessionalRepositoryPort {
    private final SpringDataProfessionalRepository professionalRepository;
    private final SpringDataProfessionalTypeRepository typeRepository;
    private final ProfessionalPersistenceMapper mapper;

    public ProfessionalJpaAdapter(SpringDataProfessionalRepository professionalRepository,
                                  SpringDataProfessionalTypeRepository typeRepository,
                                  ProfessionalPersistenceMapper mapper) {
        this.professionalRepository = professionalRepository;
        this.typeRepository = typeRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Professional> findAll() {
        return professionalRepository.findAll().stream().map(mapper::toDomainWithoutAvailability).toList();
    }

    @Override
    public Optional<Professional> findById(ProfessionalId id) {
        return professionalRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public Professional save(Professional professional) {
        ProfessionalTypeEntity typeEntity = typeRepository.findByName(professional.getType().name())
                .orElseThrow(() -> new IllegalStateException("Tipo de profesional no configurado: " + professional.getType().name()));
        ProfessionalEntity saved = professionalRepository.save(mapper.toEntity(professional, typeEntity));
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteById(ProfessionalId id) {
        professionalRepository.deleteById(id.value());
    }

    @Override
    public boolean existsById(ProfessionalId id) {
        return professionalRepository.existsById(id.value());
    }
}
