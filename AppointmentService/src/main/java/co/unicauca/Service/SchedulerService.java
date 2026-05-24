package co.unicauca.Service;

import co.unicauca.Entity.model.Professional;
import co.unicauca.Entity.model.Scheduler;
import co.unicauca.Repository.IProfessionalRepository;
import co.unicauca.Repository.ISchedulerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchedulerService {
    @Autowired
    private ISchedulerRepository repository;


    public SchedulerService(ISchedulerRepository repository) {
        this.repository = repository;
    }

    public List<Scheduler> findAll(){ return repository.findAll(); }

    public Optional<Scheduler> findById(long id){
        return repository.findById(id);
    }
}
