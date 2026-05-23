package co.unicauca.Service;

import co.unicauca.Entity.model.Appointment;
import co.unicauca.Entity.scheduling.ManualSchedule;
import co.unicauca.Entity.state.AppointmentState;
import co.unicauca.Entity.state.CreatedAppointment;
import co.unicauca.Repository.IAppointmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    @Autowired
    private IAppointmentRepository repository;

    public AppointmentService(IAppointmentRepository repository) {
        this.repository = repository;
    }

    public List<Appointment> findAll(){
        return repository.findAll();
    }

    public Optional<Appointment> findById(long id){
        return repository.findById(id);
    }

    public List<Appointment> findByProfessionalAndDate(
            String professional,
            LocalDate date
    ) {
        return findByProfessionalAndDate(null, professional, date);
    }

    public List<Appointment> findByProfessionalAndDate(
            Long professionalId,
            String professional,
            LocalDate date
    ) {

        LocalDateTime start = date.atStartOfDay();

        LocalDateTime end = date.atTime(23, 59, 59);

        if (professionalId != null) {
            return repository.findByProfessionalIdAndDate(
                    professionalId,
                    date
            );
        }

        if (professional == null || professional.isBlank()) {
            throw new IllegalArgumentException("professionalId o professional es requerido");
        }

        return repository.findByProfessionalAndDate(
                professional,
                start,
                end
        );
    }

    public Appointment findByPatientAndDate(long patient, LocalDate date) {
        return null;
    }

    @Transactional
    public Appointment createManualAppointment(Appointment appointment){
        appointment.initState();
        AppointmentState createAppointment = new CreatedAppointment();
        ManualSchedule scheduler =  new ManualSchedule();
        createAppointment.setContext(appointment);
        scheduler.schedule(appointment);
        return repository.save(appointment);
    }

    @Transactional
    public void markAppointmentCompleted(Appointment appointment, Long id){
        repository.findById(id);
        appointment.markCompleted();
    }

    @Transactional
    public boolean cancelById(long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        Appointment appointment = repository.findById(id).get();
        appointment.cancell();
        //repository.deleteById(id);
        return true;
    }

    @Transactional
    public Appointment save(Appointment appointment){
        return repository.save(appointment);
    }

}
