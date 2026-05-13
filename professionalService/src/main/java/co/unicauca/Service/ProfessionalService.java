package co.unicauca.Service;

import co.unicauca.Entity.model.Professional;
import co.unicauca.Repository.IProfessionalRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessionalService {

    private final IProfessionalRepository repository;

    public ProfessionalService(IProfessionalRepository repository) {
        this.repository = repository;
    }

    public List<Professional> findAll() {
        return repository.findAll();
    }

    public Optional<Professional> findById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Professional create(Professional professional) {
        return repository.save(professional);
    }

    @Transactional
    public boolean deleteById(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
