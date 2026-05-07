package co.unicauca.Service;

import co.unicauca.Entity.model.Appointment;
import co.unicauca.Entity.model.Professional;
import co.unicauca.Repository.IAppointmentRepository;
import co.unicauca.Repository.IProfessionalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessionalService {

    @Autowired
    private IProfessionalRepository repository;


    public ProfessionalService(IProfessionalRepository repository) {
        this.repository = repository;
    }

    public List<Professional> findAll(){
        return repository.findAll();
    }

    public Optional<Professional> findById(long id){
        return repository.findById(id);
    }
}
