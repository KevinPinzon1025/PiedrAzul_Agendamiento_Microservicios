package co.unicauca.Entity.facade;

import co.unicauca.Entity.model.Appointment;
import co.unicauca.Entity.scheduling.ManualSchedule;
import co.unicauca.Entity.state.AppointmentState;
import co.unicauca.Entity.state.CreatedAppointment;
import co.unicauca.Service.AppointmentService;
import co.unicauca.infra.event.AppointmentCreatedEvent;        
import co.unicauca.infra.messaging.AppointmentEventPublisher;  
import org.slf4j.Logger;                                       
import org.slf4j.LoggerFactory;                                
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppointmentFacade {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentFacade.class);

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentEventPublisher eventPublisher; 

    public AppointmentFacade(AppointmentService appointmentService,
                             AppointmentEventPublisher eventPublisher) {
        this.appointmentService = appointmentService;
        this.eventPublisher     = eventPublisher;
    }

    public Appointment createAppointment(Appointment appointment) {

        appointment.initState();
        AppointmentState state = new CreatedAppointment();
        state.setContext(appointment);

        ManualSchedule scheduler = new ManualSchedule();
        scheduler.schedule(appointment);
      
        Appointment saved = appointmentService.save(appointment);
        logger.info("[FACADE] Cita id={} guardada exitosamente en BD.", saved.getIdAppointment());

       
        AppointmentCreatedEvent event = new AppointmentCreatedEvent(
                saved.getIdAppointment(),
                saved.getSchedulingDate(),
                saved.getAppointmenDate(),
                saved.getObservation(),
                saved.getScheduler().getId(),
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
