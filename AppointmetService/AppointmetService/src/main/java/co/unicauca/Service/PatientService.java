package co.unicauca.Service;

import co.unicauca.Entity.model.Patient;
import co.unicauca.Repository.IPatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    @Autowired
    private IPatientRepository repository;

    public List<Patient> findAll(){
        return repository.findAll();
    }

    public Optional<Patient> findById(long id){
        return repository.findById(id);
    }
}
