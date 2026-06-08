package co.unicauca.adapter.out.db;

import co.unicauca.adapter.out.db.mapper.ProfessionalPersistenceMapper;
import co.unicauca.adapter.out.db.model.ProfessionalEntity;
import co.unicauca.adapter.out.db.model.ProfessionalTypeEntity;
import co.unicauca.adapter.out.db.repository.SpringDataProfessionalRepository;
import co.unicauca.adapter.out.db.repository.SpringDataProfessionalTypeRepository;
import co.unicauca.domain.model.Professional;
import co.unicauca.domain.valueobject.ProfessionalId;
import co.unicauca.port.out.db.ProfessionalDbPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProfessionalDbAdapter implements ProfessionalDbPort {

    private final SpringDataProfessionalRepository professionalRepository;
    private final SpringDataProfessionalTypeRepository typeRepository;
    private final ProfessionalPersistenceMapper mapper;

    public ProfessionalDbAdapter(
            SpringDataProfessionalRepository professionalRepository,
            SpringDataProfessionalTypeRepository typeRepository,
            ProfessionalPersistenceMapper mapper
    ) {
        this.professionalRepository = professionalRepository;
        this.typeRepository = typeRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Professional> findAll() {
        return professionalRepository.findAll()
                .stream()
                .map(mapper::toDomainWithoutAvailability)
                .toList();
    }

    @Override
    public Optional<Professional> findById(ProfessionalId id) {
        return professionalRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public Professional save(Professional professional) {
        ProfessionalTypeEntity typeEntity =
                typeRepository.findByName(professional.getType().name())
                        .orElseThrow(() -> new IllegalStateException(
                                "Tipo de profesional no configurado: "
                                        + professional.getType().name()
                        ));

        ProfessionalEntity saved =
                professionalRepository.save(
                        mapper.toEntity(professional, typeEntity)
                );

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
