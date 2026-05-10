package co.unicauca.Entity.facade;

import co.unicauca.Entity.factory.AppointmentSchedulerFactory;
import co.unicauca.Entity.model.Appointment;
import co.unicauca.Entity.model.Patient;
import co.unicauca.Entity.model.Professional;
import co.unicauca.Entity.model.Scheduler;
import co.unicauca.Entity.scheduling.AppointmentScheduler;
import co.unicauca.Entity.state.AppointmentState;
import co.unicauca.Entity.state.CreatedAppointment;
import co.unicauca.Service.AppointmentService;
import co.unicauca.Service.PatientService;
import co.unicauca.Service.ProfessionalService;
import co.unicauca.Service.SchedulerService;
import co.unicauca.infra.dto.CreateAppointmentRequestDTO;
import co.unicauca.infra.event.AppointmentCreatedEvent;
import co.unicauca.infra.messaging.AppointmentEventPublisher;  
import org.slf4j.Logger;                                       
import org.slf4j.LoggerFactory;                                
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class AppointmentFacade {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentFacade.class);

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private ProfessionalService professionalService;

    @Autowired
    private SchedulerService schedulerService;

    @Autowired
    private AppointmentSchedulerFactory schedulingFactory;

    @Autowired
    private AppointmentEventPublisher eventPublisher; 

    public AppointmentFacade(AppointmentService appointmentService,
                             AppointmentEventPublisher eventPublisher) {
        this.appointmentService = appointmentService;
        this.eventPublisher     = eventPublisher;
    }

    //FACADE
    public Appointment createAppointment(CreateAppointmentRequestDTO request) {

        //mapear profesional y paciente del dto a los objetos concretos de profesional y paciente
        Patient patient = patientService.findById(request.getPatientId())
                .orElseThrow(() ->
                        new RuntimeException("Paciente no encontrado"));

        Professional professional = professionalService.findById(request.getProfessionalId())
                .orElseThrow(() ->
                        new RuntimeException("Profesional no encontrado"));
        Scheduler scheduler = Optional.ofNullable(request.getSchedulerId())
                .flatMap(schedulerService::findById)
                .orElse(null);


        //instanciar una nueva cita
        Appointment appointment = new Appointment();

        //agregar estado inicial a la cita (patron state)
        appointment.initState();
        AppointmentState state = new CreatedAppointment();
        state.setContext(appointment);

        //configurar campos de la cita
        appointment.setPatient(patient);
        appointment.setProfessional(professional);
        appointment.setObservation(request.getObservation());
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setSchedulingDate(LocalDateTime.now());
        appointment.setScheduler(scheduler);

        //agendar la cita mediante el template method
        AppointmentScheduler scheduling =
                schedulingFactory.getScheduler(
                        request.getSchedulingType()
                );

        scheduling.schedule(appointment);

        //guardar cita en base de datos
        Appointment saved = appointmentService.save(appointment);
        logger.info("[FACADE] Cita id={} guardada exitosamente en BD.", saved.getIdAppointment());

        //crear evento para publicar en rabitMQ
        AppointmentCreatedEvent event = new AppointmentCreatedEvent(
                saved.getIdAppointment(),
                saved.getSchedulingDate(),
                saved.getAppointmentDate(),
                saved.getObservation(),
                saved.getScheduler()==null ?
                        null : saved.getScheduler().getId(),
                saved.getPatient().getId(),
                saved.getProfessional().getId()
        );
        eventPublisher.publish(event);

        return saved;
    }

    public AppointmentService getAppointmentService() {
        return appointmentService;
    }
}
