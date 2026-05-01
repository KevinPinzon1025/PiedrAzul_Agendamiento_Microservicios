package co.unicauca.Service;

import co.unicauca.Entity.model.TypeProfessional;
import co.unicauca.Repository.ITypeProfessionalRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TypeProfessionalService {

    private final ITypeProfessionalRepository repository;

    public TypeProfessionalService(ITypeProfessionalRepository repository) {
        this.repository = repository;
    }

    public List<TypeProfessional> findAll() {
        return repository.findAll();
    }

    public Optional<TypeProfessional> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public TypeProfessional create(TypeProfessional typeProfessional) {
        return repository.save(typeProfessional);
    }
}
